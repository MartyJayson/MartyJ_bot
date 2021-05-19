package com.example.martyj_bot;


import lombok.Data;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Data
public class MartyJBot extends TelegramWebhookBot {
    /*
    private static final String botUserName = "@martyj_bot";
    private static final String botToken = "1810434406:AAF6O3f9LJxbXW-ctv0mxUnIsIZQArY5Gvo";
    */
    private String webHookPath;
    private String botUserName;
    private String botToken;

    private com.example.martyj_bot.botapi.TelegramFacade telegramFacade;


    public MartyJBot(DefaultBotOptions botOptions, com.example.martyj_bot.botapi.TelegramFacade telegramFacade) {
        super(botOptions);
        this.telegramFacade = telegramFacade;
    }
    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        final BotApiMethod<?> replyMessageToUser = telegramFacade.handleUpdate(update);

        return replyMessageToUser;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }
}
