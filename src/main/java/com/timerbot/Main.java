package com.timerbot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Главный класс приложения
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final String[] CONFIG_PATHS = {
            "config/config.properties",
            "src/main/resources/config.properties",
            "config.properties"
    };
    private static final AtomicBoolean running = new AtomicBoolean(true);
    private static TimerBot bot;

    public static void main(String[] args) {
        logger.info("🚀 Запуск Timer Bot...");
        logger.info("Java version: " + System.getProperty("java.version"));
        logger.info("OS: " + System.getProperty("os.name"));

        try {
            Properties config = loadConfig();

            String token = config.getProperty("bot.token");
            if (token == null || token.equals("YOUR_BOT_TOKEN_HERE") || token.isEmpty()) {
                logger.severe("❌ ERROR: Установите токен бота в файле config.properties");
                logger.severe("💡 Получите токен у @BotFather в Telegram");
                logger.severe("📁 Проверьте файлы: " + String.join(", ", CONFIG_PATHS));
                System.exit(1);
            }

            setupShutdownHook();

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            bot = new TimerBot(config);

            try {
                botsApi.registerBot(bot);
                logger.info("✅ Timer Bot успешно запущен!");
                logger.info("🤖 Бот: @" + config.getProperty("bot.username", "unknown"));
                logger.info("⏰ Готов к работе. Нажмите Ctrl+C для остановки.");

                // Используем wait/notify вместо sleep в цикле
                synchronized (running) {
                    while (running.get()) {
                        try {
                            running.wait(60000); // Проверяем каждую минуту вместо постоянного опроса
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }

            } catch (TelegramApiException e) {
                handleTelegramError(e);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "❌ Неожиданная ошибка при запуске: " + e.getMessage(), e);
            System.exit(1);
        }
    }

    private static void setupShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            running.set(false);
            synchronized (running) {
                running.notifyAll();
            }
            logger.info("🛑 Получен сигнал завершения работы...");
            if (bot != null) {
                logger.info("⏳ Останавливаю бота...");
                bot.shutdown();
            }
            logger.info("✅ Timer Bot успешно остановлен.");
        }));
    }

    private static void handleTelegramError(TelegramApiException e) {
        if (e.getMessage().contains("Error removing old webhook")) {
            logger.warning("⚠️ Предупреждение: Ошибка при удалении вебхука, но бот продолжает работу...");
        } else if (e.getMessage().contains("Forbidden")) {
            logger.severe("❌ Ошибка: Неверный токен бота. Проверьте config.properties");
            System.exit(1);
        } else if (e.getMessage().contains("Unable to connect")) {
            logger.severe("❌ Ошибка: Нет подключения к интернету или Telegram заблокирован");
            logger.severe("💡 Попробуйте использовать VPN или прокси");
            System.exit(1);
        } else {
            logger.log(Level.SEVERE, "❌ Ошибка Telegram API: " + e.getMessage(), e);
            System.exit(1);
        }
    }

    private static Properties loadConfig() throws Exception {
        Properties props = new Properties();

        for (String configPath : CONFIG_PATHS) {
            try {
                Path path = Paths.get(configPath);
                if (Files.exists(path)) {
                    try (InputStream input = Files.newInputStream(path)) {
                        props.load(input);
                        logger.info("✅ Конфигурация загружена из: " + configPath);
                        return props;
                    }
                } else {
                    // Попробуем загрузить из classpath
                    try (InputStream input = Main.class.getClassLoader().getResourceAsStream(configPath)) {
                        if (input != null) {
                            props.load(input);
                            logger.info("✅ Конфигурация загружена из classpath: " + configPath);
                            return props;
                        }
                    }
                }
            } catch (Exception e) {
                logger.warning("⚠️ Не удалось загрузить конфиг из " + configPath + ": " + e.getMessage());
            }
        }

        throw new Exception("Не найден файл конфигурации. Проверьте пути: " + String.join(", ", CONFIG_PATHS));
    }
}