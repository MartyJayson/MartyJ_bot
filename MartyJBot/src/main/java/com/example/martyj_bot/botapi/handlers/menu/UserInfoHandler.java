package com.example.martyj_bot.botapi.handlers.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.example.martyj_bot.botapi.BotState;
import com.example.martyj_bot.botapi.InputMessageHandler;
import com.example.martyj_bot.botapi.handlers.fillingprofile.UserProfileData;
import com.example.martyj_bot.cache.UserDataCache;

@Component
public class UserInfoHandler implements InputMessageHandler {
    private UserDataCache userDataCache;

    public UserInfoHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        final UserProfileData profileData = userDataCache.getUserProfileData(userId);

        userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        return new SendMessage(message.getChatId(), String.format("%s%n -------------------%nИмя: %s%nНомер: %s%n" +
                "Язык: %s%n", "Данные по вашей анкете", profileData.getName(), profileData.getNumber(), profileData.getLanguage()));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_USER_PROFILE;
    }
}
