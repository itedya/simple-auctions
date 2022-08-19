package com.itedya.simpleauctions.commands;

import com.itedya.simpleauctions.SimpleAuctions;
import com.itedya.simpleauctions.daos.AuctionDao;
import com.itedya.simpleauctions.dtos.AuctionDto;
import com.itedya.simpleauctions.dtos.BidDto;
import com.itedya.simpleauctions.runnables.AnnounceBidRunnable;
import com.itedya.simpleauctions.utils.ChatUtil;
import com.itedya.simpleauctions.utils.ThreadUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BidAuctionSubCommand extends SubCommand {
    public BidAuctionSubCommand() {
        super("simpleauctions.bid");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        try {
            if (!(commandSender instanceof Player player)) {
                commandSender.sendMessage(ChatUtil.SERVER_ERROR);
                return true;
            }

            if (!player.hasPermission(permission)) {
                player.sendMessage(ChatUtil.NO_PERMISSION);
                return true;
            }

            AuctionDao auctionDao = AuctionDao.getInstance();
            AuctionDto auctionDto = auctionDao.getActive();

            if (auctionDto == null) {
                player.sendMessage(new ComponentBuilder()
                        .append(ChatUtil.PREFIX)
                        .append(" Aktualnie żaden przedmiot nie jest na licytacji").color(ChatColor.YELLOW)
                        .create());
                return true;
            }

            if (args.length == 0) {
                player.sendMessage(new ComponentBuilder()
                        .append(ChatUtil.PREFIX)
                        .append(" Musisz podać ").color(ChatColor.RED)
                        .append("swoją cenę za przedmiot").bold(true)
                        .append(" i musi ona być wyższa od aktualnej ceny licytowanego przedmiotu").bold(false).color(ChatColor.RED)
                        .create());
                return true;
            }

            int providedPrice;
            try {
                providedPrice = Integer.parseInt(args[0]);
            } catch (Exception e) {
                player.sendMessage(new ComponentBuilder()
                        .append(ChatUtil.PREFIX)
                        .append(" Nie podałeś poprawnej liczby!").color(ChatColor.RED)
                        .create());
                return true;
            }

            int currentPrice = auctionDto.getCurrentPrice();

            if (providedPrice <= currentPrice) {
                player.sendMessage(new ComponentBuilder()
                        .append(ChatUtil.PREFIX)
                        .append(" Twoja cena musi być większa od aktualnej ceny przedmiotu, która wynosi ").color(ChatColor.RED)
                        .append(currentPrice + "$").color(ChatColor.RED).bold(true)
                        .create());
                return true;
            }

            String playerUUID = player.getUniqueId().toString();

            if (auctionDto.sellerUUID.equals(playerUUID)) {
                player.sendMessage(new ComponentBuilder()
                        .append(ChatUtil.PREFIX)
                        .append(" Nie możesz licytować swojej aukcji").color(ChatColor.RED)
                        .create());
                return true;
            }

            // TODO: sprawdź czy ten check działa
            BidDto highestBid = auctionDto.getHighestBid();
            if (highestBid != null && highestBid.playerUUID.equals(playerUUID)) {
                player.sendMessage(new ComponentBuilder()
                        .append(ChatUtil.PREFIX)
                        .append(" Nie możesz licytować aukcji w której oferujesz już największą cenę").color(ChatColor.RED)
                        .create());
                return true;
            }

            Economy economy = SimpleAuctions.getEconomy();

            if (!economy.has(player, providedPrice)) {
                player.sendMessage(new ComponentBuilder()
                        .append(ChatUtil.PREFIX)
                        .append(" Nie masz tylu pieniędzy na koncie!").color(ChatColor.RED)
                        .create());
                return true;
            }

            BidDto bidDto = new BidDto();
            bidDto.playerUUID = playerUUID;
            bidDto.price = providedPrice;
            auctionDao.addBid(bidDto);
            auctionDao.addTTL(15);

            ThreadUtil.sync(new AnnounceBidRunnable(auctionDto));
        } catch (Exception e) {
            e.printStackTrace();
            commandSender.sendMessage(new ComponentBuilder()
                    .append(ChatUtil.PREFIX)
                    .append(" ")
                    .append(ChatUtil.SERVER_ERROR)
                    .create());
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return List.of("Twoja cena za przedmiot");
        }

        return new ArrayList<>();
    }
}
