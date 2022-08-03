package com.itedya.simpleauctions.commands;

import com.itedya.simpleauctions.SimpleAuctions;
import com.itedya.simpleauctions.prompts.create.HowMuchQuantityOfItemPrompt;
import com.itedya.simpleauctions.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ExactMatchConversationCanceller;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateAuctionSubCommand extends SubCommand {
    public CreateAuctionSubCommand() {
        super("simpleauctions.create");
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

            ItemStack heldItem = player.getItemInHand();

            if (heldItem.getType().equals(Material.AIR)) {
                player.sendMessage(ChatUtil.p("&cMusisz trzymać przedmiot w ręce, aby wykonać tą komendę!"));
                return true;
            }

            Conversation conversation = new ConversationFactory(SimpleAuctions.getInstance())
                    .withFirstPrompt(new HowMuchQuantityOfItemPrompt())
                    .withConversationCanceller(new ExactMatchConversationCanceller("wyjdź"))
                    .withInitialSessionData(Map.of("item", heldItem.getType()))
                    .withLocalEcho(false)
                    .buildConversation(player);

            conversation.begin();
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
