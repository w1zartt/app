package com.tglib.app.action;

import com.tglib.app.entity.book.Book;
import com.tglib.app.service.book.BookService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component("/start_read")
@RequiredArgsConstructor
public class GetAllBookAction implements Action {

    private final BookService bookService;

    @Override
    public SendMessage createResponseForMessage(Message message) {
        Page<Book> bookList = bookService.getBookList(0, 4);
        List<List<InlineKeyboardButton>> rowList =
            bookList.get().map(this::createBookRow).collect(Collectors.toList());
        rowList.add(createLastRow(bookList));


        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return SendMessage.builder()
            .chatId(message.getChatId().toString())
            .replyMarkup(inlineKeyboardMarkup)
            .text("Some text").build();
    }

    private List<InlineKeyboardButton> createBookRow(Book book) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(String.format("%s \n %s", book.getAuthorRussian(), book.getNameRussian()));
        button.setCallbackData(String.format("/preview=%s", book.getId()));
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);
        return row;
    }

    private List<InlineKeyboardButton> createLastRow(Page<Book> page) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        if (page.hasPrevious()) {
            InlineKeyboardButton previous = new InlineKeyboardButton();
            previous.setText("<< Пред.");
            previous.setCallbackData("prev");
            row.add(previous);
        }
        if (page.hasNext()) {
            InlineKeyboardButton next = new InlineKeyboardButton();
            next.setText("След. >>");
            next.setCallbackData("next");
            row.add(next);
        }
        return row;
    }
}
