package com.itedya.simpleauctions;

import com.itedya.simpleauctions.commands.Main;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleAuctions extends JavaPlugin {
    @Override
    public void onEnable() {
        try {
            PluginCommand command = this.getCommand("licytacje");
            assert command != null : "Command is null!";

            Main mainCommand = new Main();

            command.setExecutor(mainCommand);
            command.setTabCompleter(mainCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
    }
}
