package com.timerbot.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * Утилитарный класс для работы с временем
 */
public class TimeUtils {

    private static final Pattern TIME_PATTERN = Pattern.compile("^(\\d+)\\s*m\\s*$", Pattern.CASE_INSENSITIVE);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private TimeUtils() {
        // Приватный конструктор - класс утилитный
    }

    /**
     * Парсит строку времени формата "10m"
     */
    public static Integer parseMinutes(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return null;
        }

        var matcher = TIME_PATTERN.matcher(timeStr.trim());
        if (matcher.matches()) {
            int minutes = Integer.parseInt(matcher.group(1));
            if (minutes >= 1 && minutes <= 1440) {
                return minutes;
            }
        }
        return null;
    }

    /**
     * Форматирует время в формате HH:mm:ss
     */
    public static String formatTime(LocalDateTime dateTime) {
        return dateTime.format(TIME_FORMATTER);
    }

    /**
     * Форматирует дату и время
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * Рассчитывает оставшееся время в секундах
     */
    public static long calculateRemainingSeconds(LocalDateTime endTime) {
        return Math.max(0, java.time.Duration.between(LocalDateTime.now(), endTime).getSeconds());
    }

    /**
     * Проверяет, истекло ли время
     */
    public static boolean isTimeExpired(LocalDateTime endTime) {
        return LocalDateTime.now().isAfter(endTime);
    }

    /**
     * Создает LocalDateTime с добавлением минут
     */
    public static LocalDateTime addMinutesToNow(int minutes) {
        return LocalDateTime.now().plusMinutes(minutes);
    }
}