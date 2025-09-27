package com.timerbot.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Утилитарный класс для работы с Telegram Bot API
 */
public class BotUtils {

    private BotUtils() {
        // Приватный конструктор
    }

    /**
     * Создает объект SendMessage с указанным текстом
     */
    public static SendMessage createMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        return message;
    }

    /**
     * Форматирует время в читаемый вид
     */
    public static String formatDuration(long seconds) {
        if (seconds <= 0) return "0 секунд";

        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(hours);
            if (hours == 1) {
                sb.append(" час ");
            } else if (hours <= 4) { // Упрощено условие
                sb.append(" часа ");
            } else {
                sb.append(" часов ");
            }
        }
        if (minutes > 0) {
            sb.append(minutes);
            if (minutes == 1) {
                sb.append(" минута ");
            } else if (minutes <= 4) { // Упрощено условие
                sb.append(" минуты ");
            } else {
                sb.append(" минут ");
            }
        }
        if (secs > 0 && hours == 0) { // Показываем секунды только если нет часов
            sb.append(secs);
            if (secs == 1) {
                sb.append(" секунда");
            } else if (secs <= 4) { // Упрощено условие
                sb.append(" секунды");
            } else {
                sb.append(" секунд");
            }
        }

        return sb.toString().trim();
    }

    /**
     * Проверяет валидность токена бота (может пригодиться для будущих улучшений)
     */
    public static boolean isValidToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        return token.matches("\\d+:[-a-zA-Z0-9_]+");
    }

    /**
     * Извлекает команду из текста сообщения
     */
    public static String extractCommand(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        String[] parts = text.trim().split("\\s+", 2);
        return parts[0].toLowerCase();
    }

    /**
     * Извлекает аргументы из текста сообщения (может пригодиться для будущих улучшений)
     */
    public static String extractArguments(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        String[] parts = text.trim().split("\\s+", 2);
        return parts.length > 1 ? parts[1] : "";
    }
}