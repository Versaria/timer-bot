package com.timerbot.handlers;

import com.timerbot.TimerManager;
import com.timerbot.models.UserTimer;
import com.timerbot.utils.BotUtils;
import com.timerbot.utils.TimeUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Обработчик команд бота
 */
public class CommandHandler {

    private final TimerManager timerManager;

    public CommandHandler(TimerManager timerManager) {
        this.timerManager = timerManager;
    }

    public SendMessage handleStartCommand(Message message) {
        String welcomeText = "👋 Привет! Я бот для управления таймерами.\n\n" +
                "📋 Доступные команды:\n" +
                "/timer <время>m - установить таймер (например, /timer 5m)\n" +
                "/cancel - отменить активный таймер\n" +
                "/status - показать статус текущего таймера\n\n" +
                "Примеры:\n" +
                "/timer 10m - таймер на 10 минут\n" +
                "/timer 1m - таймер на 1 минуту";

        return BotUtils.createMessage(message.getChatId(), welcomeText);
    }

    public SendMessage handleTimerCommand(Message message, String[] args) {
        try {
            Long userId = message.getFrom().getId();
            Long chatId = message.getChatId();

            if (args.length < 2) {
                return BotUtils.createMessage(chatId,
                        "❌ Укажите время для таймера.\nПример: /timer 10m");
            }

            Integer minutes = timerManager.parseTimeInput(args[1]);
            if (minutes == null) {
                return BotUtils.createMessage(chatId,
                        "❌ Неверный формат времени.\n" +
                                "Используйте: /timer <число>m\n" +
                                "Пример: /timer 5m\n" +
                                "Диапазон: от 1 минуты до 24 часов");
            }

            boolean hadPreviousTimer = timerManager.cancelTimer(userId);
            boolean success = timerManager.startTimer(userId, chatId, minutes);

            if (!success) {
                return BotUtils.createMessage(chatId, "❌ Ошибка при запуске таймера.");
            }

            UserTimer timer = timerManager.getTimerInfo(userId);
            String previousText = hadPreviousTimer ? " (предыдущий таймер отменен)" : "";
            String timeLeft = timerManager.formatTimeLeft(timer);

            String response = String.format(
                    "✅ Таймер на %d минут установлен%s.%n" +
                            "⏳ Истекает в %s%n" +
                            "⌛ Осталось: %s",
                    minutes, previousText,
                    TimeUtils.formatTime(timer.getEndTime()),
                    timeLeft
            );

            return BotUtils.createMessage(chatId, response);

        } catch (Exception e) {
            System.err.println("Ошибка обработки команды /timer: " + e.getMessage());
            return BotUtils.createMessage(message.getChatId(),
                    "❌ Произошла ошибка при установке таймера. Попробуйте еще раз.");
        }
    }

    public SendMessage handleCancelCommand(Message message) {
        try {
            Long userId = message.getFrom().getId();
            Long chatId = message.getChatId();

            if (timerManager.cancelTimer(userId)) {
                return BotUtils.createMessage(chatId, "❌ Таймер отменен.");
            } else {
                return BotUtils.createMessage(chatId, "ℹ️ У вас нет активного таймера.");
            }
        } catch (Exception e) {
            System.err.println("Ошибка обработки команды /cancel: " + e.getMessage());
            return BotUtils.createMessage(message.getChatId(),
                    "❌ Произошла ошибка при отмене таймера.");
        }
    }

    public SendMessage handleStatusCommand(Message message) {
        try {
            Long userId = message.getFrom().getId();
            Long chatId = message.getChatId();

            UserTimer timer = timerManager.getTimerInfo(userId);

            if (timer != null && timer.isActive()) {
                String timeLeft = timerManager.formatTimeLeft(timer);
                String response = String.format(
                        "⏰ Активный таймер:%n" +
                                "🕐 Истекает в: %s%n" +
                                "⌛ Осталось: %s",
                        TimeUtils.formatTime(timer.getEndTime()),
                        timeLeft
                );
                return BotUtils.createMessage(chatId, response);
            } else {
                return BotUtils.createMessage(chatId, "ℹ️ Нет активного таймера.");
            }
        } catch (Exception e) {
            System.err.println("Ошибка обработки команды /status: " + e.getMessage());
            return BotUtils.createMessage(message.getChatId(),
                    "❌ Произошла ошибка при проверке статуса таймера.");
        }
    }

    public SendMessage handleHelpCommand(Message message) {
        String helpText = "📋 Доступные команды:%n%n" +
                "/timer <время>m - установить таймер%n" +
                "Примеры:%n" +
                "  /timer 5m  - на 5 минут%n" +
                "  /timer 1m  - на 1 минуту%n" +
                "  /timer 60m - на 1 час%n%n" +
                "/cancel - отменить активный таймер%n" +
                "/status - показать статус текущего таймера%n" +
                "/help   - показать эту справку%n%n" +
                "💡 Поддерживается от 1 минуты до 24 часов.";

        return BotUtils.createMessage(message.getChatId(), String.format(helpText));
    }

    public SendMessage handleUnknownCommand(Message message) {
        return BotUtils.createMessage(message.getChatId(),
                "🤖 Я бот для таймеров! Используйте /help для списка команд.");
    }
}