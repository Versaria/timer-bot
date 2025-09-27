package com.timerbot;

import com.timerbot.models.UserTimer;
import com.timerbot.utils.BotUtils; // ДОБАВЛЕН ИМПОРТ
import com.timerbot.utils.TimeUtils;
import com.timerbot.utils.ValidationUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Менеджер для управления таймерами пользователей
 */
public class TimerManager {
    private final Map<Long, UserTimer> userTimers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final TimerBot bot;

    public TimerManager(TimerBot bot) {
        this.bot = bot;
    }

    /**
     * Парсит входную строку времени
     */
    public Integer parseTimeInput(String timeStr) {
        return TimeUtils.parseMinutes(timeStr);
    }

    /**
     * Запускает таймер для пользователя
     */
    public boolean startTimer(Long userId, Long chatId, Integer minutes) {
        if (!ValidationUtils.isValidUserId(userId) ||
                !ValidationUtils.isValidChatId(chatId) ||
                !ValidationUtils.isValidMinutes(minutes)) {
            return false;
        }

        cancelTimer(userId);

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = TimeUtils.addMinutesToNow(minutes);

        var future = scheduler.schedule(() -> {
            try {
                sendTimerNotification(chatId, minutes);
                userTimers.remove(userId);
            } catch (Exception e) {
                System.err.println("Ошибка при отправке уведомления: " + e.getMessage());
                userTimers.remove(userId);
            }
        }, minutes, TimeUnit.MINUTES);

        UserTimer timer = new UserTimer(userId, chatId, minutes, startTime, endTime, future);
        userTimers.put(userId, timer);

        return true;
    }

    /**
     * Отменяет таймер пользователя
     */
    public boolean cancelTimer(Long userId) {
        UserTimer timer = userTimers.get(userId);
        if (timer != null && timer.isActive()) {
            timer.cancel();
            userTimers.remove(userId);
            return true;
        }
        return false;
    }

    /**
     * Проверяет наличие активного таймера
     */
    public boolean hasActiveTimer(Long userId) {
        UserTimer timer = userTimers.get(userId);
        return timer != null && timer.isActive();
    }

    /**
     * Возвращает информацию о таймере
     */
    public UserTimer getTimerInfo(Long userId) {
        return userTimers.get(userId);
    }

    /**
     * Форматирует оставшееся время
     */
    public String formatTimeLeft(UserTimer timer) {
        if (timer == null) return "0 секунд";
        long seconds = timer.getRemainingSeconds();
        return BotUtils.formatDuration(seconds);
    }

    /**
     * Отправляет уведомление о завершении таймера
     */
    private void sendTimerNotification(Long chatId, Integer minutes) {
        SendMessage message = BotUtils.createMessage(
                chatId,
                "⏰ Время вышло! Таймер на " + minutes + " минут завершен."
        );

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки уведомления: " + e.getMessage());
        }
    }

    /**
     * Останавливает менеджер таймеров
     */
    public void shutdown() {
        userTimers.values().forEach(UserTimer::cancel);
        userTimers.clear();

        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}