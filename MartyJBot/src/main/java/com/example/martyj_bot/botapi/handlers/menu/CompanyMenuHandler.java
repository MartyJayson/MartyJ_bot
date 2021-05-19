package com.example.martyj_bot.botapi.handlers.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.example.martyj_bot.botapi.BotState;
import com.example.martyj_bot.botapi.InputMessageHandler;
import com.example.martyj_bot.service.MainMenuService;
import com.example.martyj_bot.service.ReplyMessagesService;

@Component
public class CompanyMenuHandler implements InputMessageHandler {
    private ReplyMessagesService messagesService;
    private MainMenuService mainMenuService;

    public CompanyMenuHandler(ReplyMessagesService messagesService, MainMenuService mainMenuService) {
        this.messagesService = messagesService;
        this.mainMenuService = mainMenuService;
    }

    @Override
    public SendMessage handle(Message message) {
        return mainMenuService.getMainMenuMessage(message.getChatId(), messagesService.getReplyText("reply.aboutCompanyMenu"));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ABOUT_COMPANY_MENU;
    }


}
