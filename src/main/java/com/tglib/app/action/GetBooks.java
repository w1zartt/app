package com.tglib.app.action;

import com.tglib.app.constant.Constants;
import com.tglib.app.entity.book.Book;
import com.tglib.app.service.book.BookService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component("/start_read")
@RequiredArgsConstructor
public class GetBooks implements Action {

    private final BookService bookService;

    @Override
    public SendMessage createResponseForMessage(Message message, String command) {
        String[] split = command.split(":");
        if (split.length == 1) {
            return getSendMessageWithPagination(message, 0);
        } else if (split.length == 2) {
            Integer page = Integer.parseInt(split[1]);
            return getSendMessageWithPagination(message, page);
        }
        return null;
    }

    public SendMessage getSendMessageWithPagination(Message message, Integer page) {

        Page<Book> bookList = bookService.getBookList(page, Constants.PAGINATION);
        String sendMessageText = createSendMessageText(bookList.getContent());
        List<List<InlineKeyboardButton>> buttons = createButtons(bookList.getContent());
        buttons.add(createLastRow(bookList));

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        return SendMessage.builder()
                          .chatId(message.getChatId().toString())
                          .replyMarkup(inlineKeyboardMarkup)
                          .text(sendMessageText).build();
    }

    private List<List<InlineKeyboardButton>> createButtons(List<Book> books) {
        List<List<InlineKeyboardButton>> result = new ArrayList<>();
        List<InlineKeyboardButton> currentRow = new ArrayList<>();
        for (int i = 0; i < books.size(); i++) {
            var button = InlineKeyboardButton.builder()
                                             .text("" + (i + 1))
                                             .callbackData(
                                                 "/get_book_preview:" + books.get(i).getId())
                                             .build();
            currentRow.add(button);
            if (currentRow.size() == 3) {
                result.add(currentRow);
                currentRow = new ArrayList<>();
            }
        }
        if (!currentRow.isEmpty()) {
            result.add(currentRow);
        }
        return result;
    }

    private String createSendMessageText(List<Book> bookList) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < bookList.size(); i++) {
            result.append(createLineFromBook(bookList.get(i), i + 1)).append("\n\n");
        }
        return result.toString().trim();
    }

    private String createLineFromBook(Book book, Integer number) {
        return String.format("%d. %s. %s", number, book.getAuthorRussian(),
                             book.getNameRussian());
    }

    private List<InlineKeyboardButton> createLastRow(Page<Book> page) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        if (page.hasPrevious()) {
            InlineKeyboardButton previous = new InlineKeyboardButton();
            previous.setText("<< Пред.");
            previous.setCallbackData("/start_read:" + (page.getNumber() - 1));
            row.add(previous);
        }
        if (page.hasNext()) {
            InlineKeyboardButton next = new InlineKeyboardButton();
            next.setText("След. >>");
            next.setCallbackData("/start_read:"+ (page.getNumber() + 1));
            row.add(next);
        }
        return row;
    }
}
