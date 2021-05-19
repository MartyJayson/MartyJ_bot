package com.example.martyj_bot.botapi.handlers.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.example.martyj_bot.botapi.BotState;
import com.example.martyj_bot.botapi.InputMessageHandler;
import com.example.martyj_bot.service.MainMenuService;
import com.example.martyj_bot.service.ReplyMessagesService;

@Component
public class CallBackHandler implements InputMessageHandler {
    private ReplyMessagesService messagesService;
    private MainMenuService mainMenuService;

    public CallBackHandler(ReplyMessagesService messagesService, MainMenuService mainMenuService) {
        this.messagesService = messagesService;
        this.mainMenuService = mainMenuService;
    }

    @Override
    public SendMessage handle(Message message) {

        return mainMenuService.getMainMenuMessage(message.getChatId(), messagesService.getReplyText("reply.callBackHandler"));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.CALL_BACK_MENU;
    }


}
