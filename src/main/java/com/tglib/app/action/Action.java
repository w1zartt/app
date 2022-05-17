package com.tglib.app.action;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface Action {

    Object createResponseForMessage(Message message, String Command);
}
