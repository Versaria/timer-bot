package com.timerbot.models;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledFuture;

/**
 * Модель для хранения информации о таймере пользователя
 */
public class UserTimer {
    private final Long userId;
    private final Long chatId;
    private final Integer minutes;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final ScheduledFuture<?> future;

    public UserTimer(Long userId, Long chatId, Integer minutes,
                     LocalDateTime startTime, LocalDateTime endTime,
                     ScheduledFuture<?> future) {
        this.userId = userId;
        this.chatId = chatId;
        this.minutes = minutes;
        this.startTime = startTime;
        this.endTime = endTime;
        this.future = future;
    }

    public Long getUserId() { return userId; }
    public Long getChatId() { return chatId; }
    public Integer getMinutes() { return minutes; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public ScheduledFuture<?> getFuture() { return future; }

    /**
     * Отменяет выполнение таймера
     */
    public void cancel() {
        if (future != null && !future.isDone() && !future.isCancelled()) {
            future.cancel(false);
        }
    }

    /**
     * Проверяет активен ли таймер
     */
    public boolean isActive() {
        return future != null && !future.isDone() && !future.isCancelled();
    }

    /**
     * Возвращает оставшееся время в секундах
     */
    public long getRemainingSeconds() {
        return Math.max(0, java.time.Duration.between(LocalDateTime.now(), endTime).getSeconds());
    }
}