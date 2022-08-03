package com.itedya.simpleauctions.prompts.create;

import com.itedya.simpleauctions.utils.ChatUtil;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProvideStartCostPrompt extends NumericPrompt {

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull Number number) {
        int startPrice = number.intValue();

        if (startPrice <= 0) {
            Conversable conversable = conversationContext.getForWhom();
            conversable.sendRawMessage(ChatUtil.p("&eMusisz podać liczbę większą od 0!"));
            return new ProvideStartCostPrompt();
        }

        conversationContext.setSessionData("startPrice", startPrice);

        return new ConfirmAuctionCreationPrompt();
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return ChatUtil.p("&7Wprowadź &lstartową cenę");
    }
}
