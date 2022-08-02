package com.itedya.simpleauctions.prompts.create;

import com.itedya.simpleauctions.utils.ChatUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HowMuchQuantityOfItemPrompt extends NumericPrompt {
    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull Number input) {
        context.setSessionData("quantity", input.intValue());

        return new ConfirmAuctionCreationPrompt();
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        return ChatUtil.p("&7Podaj &lilość przedmiotu &r&7który chcesz sprzedać");
    }
}
