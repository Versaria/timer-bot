package com.timerbot;

import com.timerbot.handlers.MessageHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Основной класс Telegram бота
 */
public class TimerBot extends TelegramLongPollingBot {
    private static final Logger logger = Logger.getLogger(TimerBot.class.getName());

    private final String botToken;
    private final String botUsername;
    private final TimerManager timerManager;
    private final MessageHandler messageHandler;

    public TimerBot(Properties config) {
        super(config.getProperty("bot.token"));
        this.botToken = config.getProperty("bot.token");
        this.botUsername = config.getProperty("bot.username");
        this.timerManager = new TimerManager(this);
        this.messageHandler = new MessageHandler(timerManager);

        if (botToken == null || botToken.isEmpty() || botToken.equals("YOUR_BOT_TOKEN_HERE")) {
            throw new IllegalArgumentException("Bot token not configured properly");
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        try {
            var response = messageHandler.handleMessage(update.getMessage());
            if (response != null) {
                execute(response);
            }
        } catch (TelegramApiException e) {
            logger.log(Level.SEVERE, "Telegram API ошибка: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Общая ошибка обработки сообщения: " + e.getMessage(), e);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    /**
     * Корректное завершение работы бота
     */
    public void shutdown() {
        if (timerManager != null) {
            timerManager.shutdown();
        }
    }
}