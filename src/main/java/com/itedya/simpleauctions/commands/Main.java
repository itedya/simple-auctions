package com.itedya.simpleauctions.commands;

import com.itedya.simpleauctions.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Main implements SubCommand {
    private final Map<String, SubCommand> executorMap;

    public Main() {
        this.executorMap = new HashMap<>();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatUtil.UNKNOWN_COMMAND);
        } else {
            String commandName = args[0];
            args = Arrays.copyOfRange(args, 1, args.length);

            SubCommand exec = executorMap.get(commandName);

            if (exec == null) {
                sender.sendMessage(ChatUtil.UNKNOWN_COMMAND);
                return true;
            }

            exec.onCommand(sender, command, label, args);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 0) return new ArrayList<>();
        else if (args.length == 1) {
            SubCommand exec = executorMap.get(args[0]);

            if (exec == null) return new ArrayList<>();

            args = Arrays.copyOfRange(args, 1, args.length);
            return exec.onTabComplete(sender, command, alias, args);
        }

        return new ArrayList<>();
    }
}
