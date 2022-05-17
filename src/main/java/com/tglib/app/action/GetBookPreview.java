package com.tglib.app.action;

import com.tglib.app.entity.book.Book;
import com.tglib.app.service.book.BookService;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component("/get_book_preview")
@Slf4j
@RequiredArgsConstructor
public class GetBookPreview implements Action {

    private final BookService bookService;

    @Override
    public SendPhoto createResponseForMessage(Message message, String command) {
        String[] split = command.split(":");
        if (split.length <= 1) {
            throw new IllegalArgumentException("No book id");
        }
        Long bookId = Long.parseLong(split[1]);
        Book book = bookService.getBookById(bookId);
        InputFile inputFile = getBookImagePreview();
        String description = getBookDescription(book);
        InlineKeyboardMarkup downloadButton = getDownloadButton(book);
        return SendPhoto.builder()
            .photo(inputFile)
            .caption(description)
            .chatId(message.getChatId().toString())
            .replyMarkup(downloadButton)
            .build();
    }

    private InlineKeyboardMarkup getDownloadButton(Book book) {
        List<InlineKeyboardButton> list = new ArrayList<>();
        list.add(
            InlineKeyboardButton.builder()
                                .callbackData("/download_book:" + book.getStorageUrl())
                                .text("Download book")
                                .build()
        );
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(list);
        return InlineKeyboardMarkup.builder()
            .keyboard(buttons)
            .build();
    }

    private String getBookDescription(Book book) {
        return String.format("%s. %s \n %s. %s \n %s ", book.getAuthorEnglish(), book.getNameEnglish(),
                      book.getAuthorRussian(), book.getNameRussian(), book.getDescription());
    }

    private InputFile getBookImagePreview() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet("https://mosopora.ru/upload/000/u1/e2/21/c4fda177.jpeg");
            CloseableHttpResponse response = httpClient.execute(request);
            File outputFile = File.createTempFile("pic", "jpg");
            byte[] bytes = response.getEntity().getContent().readAllBytes();
            Files.write(outputFile.toPath(), bytes);
            InputFile inputFile = new InputFile();
            inputFile.setMedia(outputFile);

            return inputFile;
        } catch (Exception e) {
            log.info("Can't download file");
        }
        return null;
    }
}
