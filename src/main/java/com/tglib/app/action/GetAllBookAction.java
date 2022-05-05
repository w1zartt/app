package com.tglib.app.action;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component("/start_read")
public class GetAllBookAction implements Action {

    @Override
    public SendMessage createResponseForMessage(Message message) {
        return null;
    }
}
