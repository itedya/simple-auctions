package com.itedya.simpleauctions.runnables;

import com.itedya.simpleauctions.daos.AuctionDao;
import com.itedya.simpleauctions.dtos.AuctionDto;
import com.itedya.simpleauctions.utils.ChatUtil;
import com.itedya.simpleauctions.utils.ThreadUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class AnnounceEndSecondsOfAuctionRunnable extends BukkitRunnable {
    private final int sec;

    public AnnounceEndSecondsOfAuctionRunnable(int sec) {
        this.sec = sec;
    }

    @Override
    public void run() {
        ComponentBuilder componentBuilder = new ComponentBuilder()
                .append(ChatUtil.PREFIX)
                .append(" ")
                .append(sec + " ").color(ChatColor.YELLOW).bold(false);

        TextComponent bidComponent = new TextComponent("[LICYTUJ]");
        bidComponent.setColor(ChatColor.GREEN);
        bidComponent.setBold(true);
        bidComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/licytacje licytuj "));

        componentBuilder.append(bidComponent);

        var onlinePlayers = Bukkit.getOnlinePlayers();

        for (var player : onlinePlayers) {
            player.sendMessage(componentBuilder.create());
        }
    }
}
