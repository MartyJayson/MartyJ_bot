package com.example.martyj_bot.botapi.handlers.fillingprofile;

import com.example.martyj_bot.botapi.handlers.fillingprofile.UserProfileData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import com.example.martyj_bot.botapi.BotState;
import com.example.martyj_bot.botapi.InputMessageHandler;
import com.example.martyj_bot.cache.UserDataCache;
import com.example.martyj_bot.service.ReplyMessagesService;

import java.util.ArrayList;
import java.util.List;

/**
 * Формирует анкету пользователя.
 */

@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;

    public FillingProfileHandler(UserDataCache userDataCache,
                                 ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_NAME);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();
        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);
        boolean callback = false;
        SendMessage replyToUser = null;

        if (botState.equals(BotState.CALL_BACK_MENU)) {
            callback =true;
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askName");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_NUMBER);
        }
        if (botState.equals(BotState.ASK_NAME)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askName");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_NUMBER);
        }

        if (botState.equals(BotState.ASK_NUMBER)) {
            profileData.setName(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askNumber");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_LANGUAGE);

        }

        if (botState.equals(BotState.ASK_LANGUAGE)) {
            profileData.setNumber(usersAnswer);
            if(!callback) {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askLanguage");
                replyToUser.setReplyMarkup(getLanguage());
            }
            else {
                replyToUser = messagesService.getReplyMessage(chatId,"reply.thanks");
                userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
            }
        }

        if (botState.equals(BotState.PROFILE_FILLED)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.profileFilled");
            replyToUser.setReplyMarkup(getMenu());
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        }

        userDataCache.saveUserProfileData(userId, profileData);

        return replyToUser;
    }

    private InlineKeyboardMarkup getLanguage() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton buttonKaz = new InlineKeyboardButton().setText("Казахский");
        InlineKeyboardButton buttonRus = new InlineKeyboardButton().setText("Русский");

        //Every button must have callBackData, or else not work !
        buttonKaz.setCallbackData("buttonKZ");
        buttonRus.setCallbackData("buttonRU");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonKaz);
        keyboardButtonsRow1.add(buttonRus);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
    private InlineKeyboardMarkup getMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton menu = new InlineKeyboardButton().setText("Главное меню");

        //Every button must have callBackData, or else not work !
        menu.setCallbackData("menu");

        List<InlineKeyboardButton> kbr = new ArrayList<>();
        kbr.add(menu);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(kbr);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }


}



