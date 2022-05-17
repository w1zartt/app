package com.tglib.app.action;

import java.io.File;
import java.nio.file.Files;
import lombok.SneakyThrows;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component("/download_book")
public class DownloadBook implements Action {

    @Override
    @SneakyThrows
    public Object createResponseForMessage(Message message, String Command) {
        CloseableHttpClient aDefault = HttpClients.createDefault();
        HttpUriRequest authorization = RequestBuilder
            .get("https://cloud-api.yandex.net/v1/disk/resources/download?path=tg_lib/conc.pdf")
            .addHeader("Authorization", "AQAAAAAhtlLwAAfm-iNBxUq-Fk4-spNYTUsB6p0").build();
        CloseableHttpResponse execute = aDefault.execute(authorization);
        byte[] bytes = execute.getEntity().getContent().readAllBytes();
        String s = new String(bytes);
        String https = s.substring(s.indexOf("https"), s.indexOf("\","));
        HttpGet request = new HttpGet(https);
        CloseableHttpResponse response = aDefault.execute(request);
        File outputFile = File.createTempFile("somebook", ".pdf");
        byte[] bytes2 = response.getEntity().getContent().readAllBytes();
        Files.write(outputFile.toPath(), bytes2);
        InputFile inputFile = new InputFile();
        inputFile.setMedia(outputFile);

        return SendDocument.builder()
            .chatId(message.getChatId().toString())
            .document(inputFile)
            .build();
    }
}
