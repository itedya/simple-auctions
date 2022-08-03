package com.itedya.simpleauctions.prompts.create;

import com.itedya.simpleauctions.runnables.CreateAuctionRunnable;
import com.itedya.simpleauctions.utils.ChatUtil;
import com.itedya.simpleauctions.utils.ThreadUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfirmAuctionCreationPrompt extends ValidatingPrompt {
    @Override
    protected boolean isInputValid(@NotNull ConversationContext context, @NotNull String input) {
        String[] accepted = {"tak", "Tak", "nie", "Nie"};
        return ArrayUtils.contains(accepted, input.toLowerCase());
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, String input) {
        if (input.equals("nie") || input.equals("Nie")) {
            context.getForWhom().sendRawMessage(ChatUtil.p("&e&lAnulowano &r&etworzenie aukcji przedmiotu"));
        } else {
            Player player = (Player) context.getForWhom();

            Material item = (Material) context.getSessionData("item");
            int quantity = (int) context.getSessionData("quantity");
            ThreadUtil.async(new CreateAuctionRunnable(player, item, quantity));
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
