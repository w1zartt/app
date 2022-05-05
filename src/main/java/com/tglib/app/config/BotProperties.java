package com.tglib.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "telegram.bot")
public class BotProperties {
    private String token;
    private String userName;
}
