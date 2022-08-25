package com.itedya.simpleauctions.commands;

import com.itedya.simpleauctions.daos.AuctionDao;
import com.itedya.simpleauctions.dtos.AuctionDto;
import com.itedya.simpleauctions.utils.ChatUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ListAuctionsSubCommand extends SubCommand {
    public ListAuctionsSubCommand() {
        super("simpleauctions.list");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(ChatUtil.YOU_HAVE_TO_BE_IN_GAME);
                return true;
            }

            if (!player.hasPermission(permission)) {
                player.sendMessage(ChatUtil.NO_PERMISSION);
                return true;
            }

            List<AuctionDto> auctions = AuctionDao.getInstance().allInQueue();

            Map<String, String> usernameMap = new HashMap<>();

            player.sendMessage(ChatUtil.PREFIX);
            player.sendMessage(ChatColor.GRAY + "Licytacje w kolejce: ");
            if (auctions.size() == 0) {
                player.sendMessage(ChatColor.GRAY + "Nie ma żadnych licytacji w kolejce");
            }

            for (int i = 0; i < auctions.size(); i++) {
                AuctionDto auction = auctions.get(i);
                String username;

                if (usernameMap.containsKey(auction.sellerUUID)) {
                    username = usernameMap.get(auction.sellerUUID);
                } else {
                    Player seller = Bukkit.getPlayer(UUID.fromString(auction.sellerUUID));
                    usernameMap.put(auction.sellerUUID, seller.getName());
                    username = seller.getName();
                }

                TranslatableComponent translatableComponent = new TranslatableComponent();
                translatableComponent.setTranslate(auction.material.getTranslationKey());

                player.sendMessage(new ComponentBuilder()
                        .append((i + 1) + ".").color(ChatColor.YELLOW)
                        .append("x" + auction.quantity + " ").color(ChatColor.GOLD).bold(true)
                        .append(translatableComponent)
                        .append(" gracza ").color(ChatColor.GRAY).bold(false)
                        .append(username).bold(true).color(ChatColor.YELLOW)
                        .append(". Cena początkowa: ").color(ChatColor.GRAY).bold(false)
                        .append(auction.startingPrice + "$").color(ChatColor.YELLOW).bold(true)
                        .create());
            }
            player.sendMessage(ChatUtil.PREFIX);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
