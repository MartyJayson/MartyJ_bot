package com.example.martyj_bot.botapi;

import com.example.martyj_bot.botapi.handlers.fillingprofile.UserProfileData;
import com.example.martyj_bot.botapi.handlers.menu.CompanyMenuHandler;
import com.example.martyj_bot.service.ReplyMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.example.martyj_bot.cache.UserDataCache;
import com.example.martyj_bot.service.MainMenuService;

@Component
@Slf4j
public class TelegramFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private MainMenuService mainMenuService;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache, MainMenuService mainMenuService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
        }


        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, userId: {}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMsg) {
            case "/start":
                botState = BotState.ASK_NAME;
                break;
            case "О компании":
                botState = BotState.ABOUT_MENU;
                break;
            case "О чат-ботах":
                botState = BotState.ABOUT_BOTS_MENU;
                break;
            case "Заказать обратный звонок":
                botState = BotState.CALL_BACK_MENU;
                break;
            case "Моя информация":
                botState = BotState.SHOW_USER_PROFILE;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        userDataCache.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final int userId = buttonQuery.getFrom().getId();
        BotApiMethod<?> callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "Воспользуйтесь главным меню");
        //choice of languages
        if (buttonQuery.getData().equals("buttonKZ")) {
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setLanguage("Казахский");
            userDataCache.saveUserProfileData(userId, userProfileData);
            callBackAnswer = new SendMessage(chatId, "Выбран казахский язык. Отправьте сообщение чтобы подтвердить.");
            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
        } else if (buttonQuery.getData().equals("buttonRU")) {
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setLanguage("Русский");
            userDataCache.saveUserProfileData(userId, userProfileData);
            callBackAnswer = new SendMessage(chatId, "Выбран русский язык. Отправьте сообщение чтобы подтвердить.");
            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
        }
        //About detail page
        else if (buttonQuery.getData().equals("aboutUs")) {
            String s = "Компания Qbots  занимается разработкой подсистемного программного обеспечения для мессенджера Telegram. Мы реализовали более 50 крупных проектов для 10 отраслей экономики как, государственные организации, национальные компании. \n Адрес: 050000, г. Алматы, ул. Байзакова 127, офис 4,БИН: 170540017769 \n Е-mail: qbots2020@gmail.com, www.qbots.kz \n Контакты: 8 778 349 97 94 Нурлан Биимбет";
            callBackAnswer = new SendMessage(chatId, s);
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        }
        else if(buttonQuery.getData().equals("menu")){
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        }
        else {
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        }

        return callBackAnswer;


    }

    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }


}
