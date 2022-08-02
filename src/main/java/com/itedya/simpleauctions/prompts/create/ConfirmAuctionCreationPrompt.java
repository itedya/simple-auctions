package com.itedya.simpleauctions.prompts.create;

import com.itedya.simpleauctions.utils.ChatUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfirmAuctionCreationPrompt extends BooleanPrompt {
    @Override
    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {
        String[] accepted = {"tak", "Tak", "nie", "Nie"};
        return ArrayUtils.contains(accepted, input.toLowerCase());
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, boolean input) {
        Player player = (Player) context.getForWhom();

        if (!input) {
            player.sendMessage(ChatUtil.p("&e&lAnulowano &r&etworzenie aukcji przedmiotu"));
        } else {
            // TODO: make runnable
        }

        return null;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        Player player = (Player) context.getForWhom();

        BaseComponent[] itemQuantityComponents = new ComponentBuilder()
                .color(ChatColor.GRAY)
                .append("Ilość przedmiotu: ")
                .append((int) context.getSessionData("quantity") + "").color(ChatColor.YELLOW).bold(true)
                .create();

        Material item = (Material) context.getSessionData("item");
        assert item != null;

        TranslatableComponent itemComponent = new TranslatableComponent(item.getTranslationKey());

        BaseComponent[] itemNameComponents = new ComponentBuilder()
                .color(ChatColor.GRAY)
                .append("Nazwa przedmiotu: ")
                .append(itemComponent).color(ChatColor.YELLOW).bold(true)
                .create();

        player.sendMessage(itemNameComponents);
        player.sendMessage(itemQuantityComponents);

        return ChatUtil.p("&a&lCzy potwierdzasz? &r&7(Tak/tak/Nie/nie)");
    }
}
