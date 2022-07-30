package com.itedya.simpleauctions.utils;

import org.bukkit.ChatColor;

public class ChatUtil {
    public final static String UNKNOWN_COMMAND = ChatUtil.p("&e&lPodaj komendę! Ich spis możesz zobaczyć pod /licytacje pomoc");

    public static String p(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
