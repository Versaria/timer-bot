package com.timerbot.utils;

import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Утилитарный класс для валидации данных
 */
public class ValidationUtils {

    private ValidationUtils() {
        // Приватный конструктор - класс утилитный
    }

    /**
     * Проверяет валидность ID чата
     */
    public static boolean isValidChatId(Long chatId) {
        return chatId != null && chatId != 0;
    }

    /**
     * Проверяет валидность ID пользователя
     */
    public static boolean isValidUserId(Long userId) {
        return userId != null && userId != 0;
    }

    /**
     * Проверяет валидность количества минут
     */
    public static boolean isValidMinutes(Integer minutes) {
        return minutes != null && minutes >= 1 && minutes <= 1440;
    }

    /**
     * Проверяет, является ли текст командой
     */
    public static boolean isCommand(String text) {
        return text != null && text.trim().startsWith("/");
    }

    /**
     * Проверяет, не пустой ли текст
     */
    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }

    /**
     * Проверяет валидность сообщения
     */
    public static boolean isValidMessage(Message message) {
        return message != null &&
                message.hasText() &&
                isNotEmpty(message.getText()) &&
                isValidChatId(message.getChatId()) &&
                message.getFrom() != null &&
                isValidUserId(message.getFrom().getId());
    }

    /**
     * Проверяет, является ли сообщение невалидным (обратная логика для удобства)
     */
    public static boolean isInvalidMessage(Message message) {
        return !isValidMessage(message);
    }
}