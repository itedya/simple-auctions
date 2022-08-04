package com.itedya.simpleauctions.prompts.create;

import com.itedya.simpleauctions.utils.ChatUtil;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HowMuchQuantityOfItemPrompt extends NumericPrompt {
    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull Number input) {
        context.setSessionData("quantity", input.intValue());

        return new ProvideStartCostPrompt();
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        return ChatUtil.p("&7Podaj &lilość przedmiotu &r&7który chcesz sprzedać");
    }
}
