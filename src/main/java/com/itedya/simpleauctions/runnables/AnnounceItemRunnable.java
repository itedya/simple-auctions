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
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class AnnounceItemRunnable extends BukkitRunnable {
    private AuctionDto dto;

    public AnnounceItemRunnable(AuctionDto dto) {
        this.dto = dto;
    }

    @Override
    public void run() {
        OfflinePlayer seller = Bukkit.getOfflinePlayer(UUID.fromString(dto.sellerUUID));

        int price = -1;

        BidDto highestBid = dto.getHighestBid();
        if (highestBid == null) {
            price = dto.startingPrice;
        } else {
            price = highestBid.price;
        }

        TextComponent bidComponent = new TextComponent("[LICYTUJ]");
        bidComponent.setColor(ChatColor.GREEN);
        bidComponent.setBold(true);
        bidComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/licytacje licytuj "));

        ComponentBuilder componentBuilder = new ComponentBuilder()
                .append(ChatUtil.PREFIX)
                .append(" licytacja przedmiotu ").color(ChatColor.GRAY)
                .append("x" + dto.quantity).bold(true).color(ChatColor.GOLD)
                .append(" ")
                .append(new TranslatableComponent(dto.material.getTranslationKey()))
                .append(" gracza ").bold(false).color(ChatColor.GRAY)
                .append(seller.getName()).bold(true).color(ChatColor.GOLD)
                .append(". ").bold(false).color(ChatColor.GRAY)
                .append("Cena ")
                .append(price + "$").bold(true).color(ChatColor.YELLOW)
                .append(" ")
                .append(bidComponent);


        var onlinePlayers = Bukkit.getOnlinePlayers();

        for (var player : onlinePlayers) {
            player.sendMessage(componentBuilder.create());
        }
    }
}
