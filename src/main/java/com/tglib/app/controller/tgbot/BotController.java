package com.tglib.app.controller.tgbot;

import com.tglib.app.action.Action;
import com.tglib.app.service.bot.BotService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class BotController extends TelegramLongPollingBot {
    private final BotService botService;
    private final TelegramBotsApi telegramBotsApi;
    private final String userName;
    private final String token;
    private Map<String, Action> beans;

    public BotController(TelegramBotsApi telegramBotsApi,
                         BotService botService,
                         @Value("${telegram.bot.token}") String token,
                         @Value("${telegram.bot.userName}") String userName) {
        this.userName = userName;
        this.token = token;
        this.botService = botService;
        this.telegramBotsApi = telegramBotsApi;
    }

    /**
     * Method for start
     */
    @PostConstruct
    @SneakyThrows
    void startBot() {
        telegramBotsApi.registerBot(this);
    }

    @Autowired
    public void setBeans(Map<String, Action> map) {
        this.beans = map;
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Object response = null;
        if (update.hasMessage()) {
            Message received = update.getMessage();
            response = beans.get(received.getText()).createResponseForMessage(received, received.getText());
        } else if (update.hasCallbackQuery()) {
            var callbackQueryMessage = update.getCallbackQuery().getMessage();
            var callbackQueryCommand =  update.getCallbackQuery().getData();
            String text = callbackQueryCommand.substring(0, callbackQueryCommand.indexOf(":"));
            response = beans.get(text).createResponseForMessage(callbackQueryMessage,
                                                                callbackQueryCommand);
        }
        try {
            sendMessage(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(Object o) throws TelegramApiException {
        if (o instanceof SendPhoto) {
            execute((SendPhoto) o);
        } else if (o instanceof SendMessage) {
            execute((SendMessage) o);
        } else if (o instanceof SendDocument) {
            execute((SendDocument) o);
        }
    }
}
