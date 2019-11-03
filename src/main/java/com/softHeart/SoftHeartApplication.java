package com.softHeart;

import com.softHeart.telegram.SoftHeartBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class SoftHeartApplication {

    public static void main(String [] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new SoftHeartBot());
        } catch(TelegramApiRequestException e) {
            System.out.println("Error during bot registration");
            e.printStackTrace();
        }
    }

}
