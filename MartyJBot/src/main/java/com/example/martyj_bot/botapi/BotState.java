package com.example.martyj_bot.botapi;

/**Возможные состояния бота */

public enum BotState {
    ASK_NAME,
    ASK_NUMBER,
    ASK_LANGUAGE,
    FILLING_PROFILE,
    PROFILE_FILLED,
    SHOW_MAIN_MENU,
    ABOUT_MENU,
    SHOW_USER_PROFILE,
    ABOUT_COMPANY_MENU,
    ABOUT_BOTS_MENU,
    CALL_BACK_MENU;
}
