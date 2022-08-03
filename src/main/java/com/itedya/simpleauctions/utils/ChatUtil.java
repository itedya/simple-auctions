package com.itedya.simpleauctions.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class ChatUtil {

    public final static String SERVER_ERROR = ChatUtil.p("&cWystąpił błąd serwera!");
    public final static BaseComponent[] NO_PERMISSION = new ComponentBuilder()
            .color(ChatColor.RED)
            .append("Brak permisji!")
            .bold(true)
            .create();
    public final static String UNKNOWN_COMMAND = ChatUtil.p("&e&lPodaj komendę! Ich spis możesz zobaczyć pod /licytacje pomoc");
    public final static BaseComponent[] YOU_HAVE_TO_BE_IN_GAME = new ComponentBuilder()
            .color(ChatColor.RED)
            .append("Musisz być w grze, aby używać tej komendy!")
            .bold(true)
            .create();

    public static String p(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
