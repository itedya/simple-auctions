package com.itedya.simpleauctions.runnables;

import com.itedya.simpleauctions.daos.ItemPersistenceDao;
import com.itedya.simpleauctions.dtos.AuctionDto;
import com.itedya.simpleauctions.dtos.ItemPersistenceDto;
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

public class EndOfNotSoldAuctionRunnable extends BukkitRunnable {
    private AuctionDto auctionDto;

    public EndOfNotSoldAuctionRunnable(AuctionDto auctionDto) {
        this.auctionDto = auctionDto;
    }

    @Override
    public void run() {
        OfflinePlayer seller = Bukkit.getOfflinePlayer(UUID.fromString(auctionDto.sellerUUID));

        if (seller.isOnline()) {
            Player onlineSeller = Bukkit.getPlayer(seller.getName());
            Inventory inventory = onlineSeller.getInventory();

            ItemStack itemStack = new ItemStack(auctionDto.material);
            itemStack.setAmount(auctionDto.quantity);

            inventory.addItem(itemStack);
        } else {
            ItemPersistenceDto dto = new ItemPersistenceDto();
            dto.material = auctionDto.material;
            dto.quantity = auctionDto.quantity;

            ItemPersistenceDao.addItem(auctionDto.sellerUUID, dto);
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
                .append(". Nikt nie wygrał").color(ChatColor.GRAY);

        var onlinePlayers = Bukkit.getOnlinePlayers();

        for (var player : onlinePlayers) {
            player.sendMessage(componentBuilder.create());
        }
    }
}
