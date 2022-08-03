package com.itedya.simpleauctions;

import com.itedya.simpleauctions.commands.Main;
import com.itedya.simpleauctions.runnables.ScheduleAuctionAnnounciationRunnable;
import com.itedya.simpleauctions.utils.ThreadUtil;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public final class SimpleAuctions extends JavaPlugin {
    public SimpleAuctions() {
        super();
    }

    protected SimpleAuctions(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    private static SimpleAuctions instance;

    public static SimpleAuctions getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        try {
            PluginCommand command = this.getCommand("licytacje");
            assert command != null : "Command is null!";

            Main.register();

            ThreadUtil.sync(new ScheduleAuctionAnnounciationRunnable());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
    }
}
