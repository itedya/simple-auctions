package com.itedya.simpleauctions.runnables;

import com.itedya.simpleauctions.daos.AuctionDao;
import com.itedya.simpleauctions.dtos.AuctionDto;
import com.itedya.simpleauctions.utils.ChatUtil;
import com.itedya.simpleauctions.utils.ThreadUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class AnnounceNewItemRunnable extends BukkitRunnable {
    @Override
    public void run() {
        if (ScheduleAuctionAnnounciationRunnable.announcing) {
            return;
        }

        AuctionDto auctionDto = AuctionDao.first();
        if (auctionDto == null) return;

        ScheduleAuctionAnnounciationRunnable.announcing = true;

        Player seller = Bukkit.getPlayer(UUID.fromString(auctionDto.sellerUUID));
        if (seller == null) {
            ScheduleAuctionAnnounciationRunnable.announcing = false;
            return;
        }

        ComponentBuilder componentBuilder = new ComponentBuilder()
                .append(ChatUtil.PREFIX)
                .append(" licytacja przedmiotu ").color(ChatColor.GRAY)
                .append("x" + auctionDto.quantity).bold(true).color(ChatColor.GOLD)
                .append(" ")
                .append(new TranslatableComponent(auctionDto.material.getTranslationKey()))
                .append(" gracza ").bold(false).color(ChatColor.GRAY)
                .append(seller.getName()).bold(true).color(ChatColor.GOLD)
                .append(" rozpoczęła się. ").bold(false).color(ChatColor.GRAY)
                .append("Cena startowa ")
                .append(auctionDto.startingPrice + "$").bold(true).color(ChatColor.YELLOW)
                .append(" ");

        TextComponent bidComponent = new TextComponent("[LICYTUJ]");
        bidComponent.setColor(ChatColor.GREEN);
        bidComponent.setBold(true);
        bidComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/licytacje licytuj "));

        componentBuilder.append(bidComponent);

        var onlinePlayers = Bukkit.getOnlinePlayers();

        for (var player : onlinePlayers) {
            player.sendMessage(componentBuilder.create());
        }

        ThreadUtil.syncDelay(new AnnounceItemRunnable(1), 30 * 20);
    }
}
