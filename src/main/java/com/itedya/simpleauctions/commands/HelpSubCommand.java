package com.itedya.simpleauctions.commands;

import com.itedya.simpleauctions.SimpleAuctions;
import com.itedya.simpleauctions.daos.AuctionDao;
import com.itedya.simpleauctions.dtos.AuctionDto;
import com.itedya.simpleauctions.dtos.BidDto;
import com.itedya.simpleauctions.runnables.AnnounceBidRunnable;
import com.itedya.simpleauctions.utils.ChatUtil;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HelpSubCommand extends SubCommand {
    private List<String> lines = new ArrayList<>();

    public HelpSubCommand() {
        super("simpleauctions.help");

        lines.add(ChatUtil.PREFIX);
        lines.add(ChatUtil.p("&6/licytacje &3stworz &7- &eStwórz licytacje"));
        lines.add(ChatUtil.p("&6/licytacje &3licytuj &2<cena> &7- &eLicytuj licytacje"));
        lines.add(ChatUtil.PREFIX);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        try {
            if (!(commandSender instanceof Player player)) {
                commandSender.sendMessage(ChatUtil.YOU_HAVE_TO_BE_IN_GAME);
                return true;
            }

            if (!player.hasPermission(permission)) {
                player.sendMessage(ChatUtil.NO_PERMISSION);
                return true;
            }

            lines.forEach(player::sendMessage);
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
