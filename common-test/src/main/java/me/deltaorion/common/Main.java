package me.deltaorion.common;

import me.deltaorion.common.config.InvalidConfigurationException;
import me.deltaorion.common.locale.message.Message;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InvalidConfigurationException {
        Message message = Message.valueOf("{0}{0}");
        System.out.println(message.toString("Gamer","1"));
    }


}
