package com.timerbot.handlers;

import com.timerbot.TimerManager;
import com.timerbot.utils.BotUtils;
import com.timerbot.utils.ValidationUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Обработчик входящих сообщений бота
 */
public class MessageHandler {
    private final CommandHandler commandHandler;

    public MessageHandler(TimerManager timerManager) {
        this.commandHandler = new CommandHandler(timerManager);
    }

    /**
     * Обрабатывает входящее сообщение и возвращает ответ
     */
    public SendMessage handleMessage(Message message) {
        if (ValidationUtils.isInvalidMessage(message)) {
            return null;
        }

        String text = message.getText().trim();
        String command = BotUtils.extractCommand(text);
        String[] parts = text.split("\\s+", 2);

        switch (command) {
            case "/start":
                return commandHandler.handleStartCommand(message);
            case "/timer":
                return commandHandler.handleTimerCommand(message, parts);
            case "/cancel":
                return commandHandler.handleCancelCommand(message);
            case "/status":
                return commandHandler.handleStatusCommand(message);
            case "/help":
                return commandHandler.handleHelpCommand(message);
            default:
                return commandHandler.handleUnknownCommand(message);
        }
    }
}