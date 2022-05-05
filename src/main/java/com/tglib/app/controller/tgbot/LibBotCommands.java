package com.tglib.app.controller.tgbot;

public enum LibBotCommands {
    START_READ("/start_read");


    private final String value;
    private  LibBotCommands(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
