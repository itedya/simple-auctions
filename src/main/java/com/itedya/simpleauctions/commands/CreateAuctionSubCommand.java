package com.itedya.simpleauctions.commands;

import com.itedya.simpleauctions.SimpleAuctions;
import com.itedya.simpleauctions.prompts.create.HowMuchQuantityOfItemPrompt;
import com.itedya.simpleauctions.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ExactMatchConversationCanceller;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CreateAuctionSubCommand implements SubCommand {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(ChatUtil.YOU_HAVE_TO_BE_IN_GAME);
                return true;
            }

            if (!player.hasPermission("simpleauctions.create")) {
                player.sendMessage(ChatUtil.NO_PERMISSION);
                return true;
            }

            Conversation conversation = new ConversationFactory(SimpleAuctions.getInstance())
                    .withFirstPrompt(new HowMuchQuantityOfItemPrompt())
                    .withConversationCanceller(new ExactMatchConversationCanceller("wyjdź"))
                    .buildConversation(player);

            conversation.begin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
