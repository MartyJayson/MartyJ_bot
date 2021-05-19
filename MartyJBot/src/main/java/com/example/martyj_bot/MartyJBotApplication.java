package com.example.martyj_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;

@SpringBootApplication
public class MartyJBotApplication {

    public static void main(String[] args) {

        /*ApiContextInitializer.init();

        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

        MartyJBot martyJBot = new MartyJBot(botOptions);
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(martyJBot);
        }
        catch (Exception e){
            e.printStackTrace();
        }*/
        SpringApplication.run(MartyJBotApplication.class, args);
    }

}
