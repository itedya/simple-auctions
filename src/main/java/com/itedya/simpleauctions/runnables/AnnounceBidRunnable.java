package com.itedya.simpleauctions.runnables;

import com.itedya.simpleauctions.daos.AuctionDao;
import com.itedya.simpleauctions.dtos.AuctionDto;
import com.itedya.simpleauctions.dtos.BidDto;
import com.itedya.simpleauctions.utils.ChatUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class AnnounceBidRunnable extends BukkitRunnable {
    private AuctionDto auctionDto;

    public AnnounceBidRunnable(AuctionDto auctionDto) {
        this.auctionDto = auctionDto;
    }

    @Override
    public void run() {
        OfflinePlayer seller = Bukkit.getOfflinePlayer(UUID.fromString(auctionDto.sellerUUID));

        BidDto bidDto = AuctionDao.first().getHighestBid();

        int price = bidDto.price;
        OfflinePlayer bidder = Bukkit.getOfflinePlayer(UUID.fromString(bidDto.playerUUID));

        ComponentBuilder componentBuilder = new ComponentBuilder()
                .append(ChatUtil.PREFIX)
                .append(" cena przedmiotu ").color(ChatColor.GRAY)
                .append("x" + auctionDto.quantity).bold(true).color(ChatColor.GOLD)
                .append(" ")
                .append(new TranslatableComponent(auctionDto.material.getTranslationKey()))
                .append(" gracza ").bold(false).color(ChatColor.GRAY)
                .append(seller.getName()).bold(true).color(ChatColor.GOLD)
                .append(" została podbita przez gracza ").bold(false).color(ChatColor.GRAY)
                .append(bidder.getName()).bold(true).color(ChatColor.GOLD)
                .append(" do ").bold(false).color(ChatColor.GRAY)
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
    }
}
