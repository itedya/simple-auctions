package com.itedya.simpleauctions.runnables;

import com.itedya.simpleauctions.daos.AuctionDao;
import com.itedya.simpleauctions.dtos.AuctionDto;
import com.itedya.simpleauctions.dtos.BidDto;
import com.itedya.simpleauctions.utils.ChatUtil;
import com.itedya.simpleauctions.utils.ThreadUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class AnnounceItemRunnable extends BukkitRunnable {
    private int nthAlert = 0;

    public AnnounceItemRunnable(int nthAlert) {
        this.nthAlert = nthAlert;
    }

    @Override
    public void run() {
        AuctionDto auctionDto = AuctionDao.first();
        if (auctionDto == null) {
            ScheduleAuctionAnnounciationRunnable.announcing = false;
            return;
        }

        Player seller = Bukkit.getPlayer(UUID.fromString(auctionDto.sellerUUID));
        if (seller == null) {
            ScheduleAuctionAnnounciationRunnable.announcing = false;
            return;
        }

        int price = -1;

        BidDto highestBid = auctionDto.getHighestBid();
        if (highestBid == null) {
            price = auctionDto.startingPrice;
        } else {
            price = highestBid.price;
        }

        ComponentBuilder componentBuilder = new ComponentBuilder()
                .append(ChatUtil.PREFIX)
                .append(" licytacja przedmiotu ").color(ChatColor.GRAY)
                .append("x" + auctionDto.quantity).bold(true).color(ChatColor.GOLD)
                .append(" ")
                .append(new TranslatableComponent(auctionDto.material.getTranslationKey()))
                .append(" gracza ").bold(false).color(ChatColor.GRAY)
                .append(seller.getName()).bold(true).color(ChatColor.GOLD)
                .append(". ").bold(false).color(ChatColor.GRAY)
                .append("Cena ")
                .append(price + "$").bold(true).color(ChatColor.YELLOW)
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

        if (nthAlert >= 2) {
            ThreadUtil.syncDelay(new AnnounceEndSecondsOfAuctionRunnable(5), 25 * 20);
            return;
        }

        ThreadUtil.syncDelay(new AnnounceItemRunnable(nthAlert + 1), 30 * 20);
    }
}
