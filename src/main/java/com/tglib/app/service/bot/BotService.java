package com.tglib.app.service.bot;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class BotService {

    public SendMessage getStartScreen(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("This must be a start screen");
        return sendMessage;
    }
}
