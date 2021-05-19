package com.example.martyj_bot.botapi.handlers.menu;

import com.example.martyj_bot.botapi.BotState;
import com.example.martyj_bot.botapi.InputMessageHandler;
import com.example.martyj_bot.service.MainMenuService;
import com.example.martyj_bot.service.ReplyMessagesService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class AboutCompanyHandler implements InputMessageHandler {
    private ReplyMessagesService messagesService;
    private MainMenuService mainMenuService;

    public AboutCompanyHandler(ReplyMessagesService messagesService, MainMenuService mainMenuService) {
        this.messagesService = messagesService;
        this.mainMenuService = mainMenuService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);

    }
    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.about");
        replyToUser.setReplyMarkup(getInlineMessageButtons());
        return replyToUser;
    }
    @Override
    public BotState getHandlerName() {
        return BotState.ABOUT_MENU;
    }

    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton aboutUs = new InlineKeyboardButton().setText("О нас");
        InlineKeyboardButton news = new InlineKeyboardButton().setText("Новости компании");
        //Every button must have callBackData, or else not work !
        aboutUs.setCallbackData("aboutUs")  ;
        news.setCallbackData("news").setUrl("www.qbots.kz");

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(aboutUs);
        keyboardButtonsRow.add(news);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

}
