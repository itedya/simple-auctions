package com.itedya.simpleauctions.runnables;

import com.itedya.simpleauctions.dtos.AuctionDto;
import com.itedya.simpleauctions.dtos.BidDto;
import com.itedya.simpleauctions.utils.ChatUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class EndOfSoldAuctionRunnable extends BukkitRunnable {
    private AuctionDto auctionDto;

    public EndOfSoldAuctionRunnable(AuctionDto auctionDto) {
        this.auctionDto = auctionDto;
    }

    @Override
    public void run() {
        BidDto bidDto = auctionDto.getHighestBid();

        OfflinePlayer seller = Bukkit.getOfflinePlayer(UUID.fromString(auctionDto.sellerUUID));
        OfflinePlayer buyer = Bukkit.getOfflinePlayer(UUID.fromString(bidDto.playerUUID));

        if (buyer.isOnline()) {
            Player onlineBuyer = Bukkit.getPlayer(buyer.getName());
            Inventory inventory = onlineBuyer.getInventory();

            ItemStack itemStack = new ItemStack(auctionDto.material);
            itemStack.setAmount(auctionDto.quantity);

            inventory.addItem(itemStack);
        } else {
            // TODO: zrob cos z tym
        }

        ComponentBuilder componentBuilder = new ComponentBuilder()
                .append(ChatUtil.PREFIX)
                .append(" licytacja przedmiotu ").color(ChatColor.GRAY)
                .append("x" + auctionDto.quantity).bold(true).color(ChatColor.GOLD)
                .append(" ")
                .append(new TranslatableComponent(auctionDto.material.getTranslationKey()))
                .append(" gracza ").bold(false).color(ChatColor.GRAY)
                .append(seller.getName()).bold(true).color(ChatColor.GOLD)
                .append(" skończyła się").bold(false).color(ChatColor.RED)
                .append(". Wygrał ").color(ChatColor.GRAY)
                .append(buyer.getName() + " ").bold(true).color(ChatColor.GOLD)
                .append(bidDto.price + "$").color(ChatColor.YELLOW);

        var onlinePlayers = Bukkit.getOnlinePlayers();

        for (var player : onlinePlayers) {
            player.sendMessage(componentBuilder.create());
        }

        // TODO: dodaj saldo do konta sellerowi
    }
}