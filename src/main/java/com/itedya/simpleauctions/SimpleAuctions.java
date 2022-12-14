package com.itedya.simpleauctions;

import com.itedya.simpleauctions.commands.Main;
import com.itedya.simpleauctions.daos.AuctionDao;
import com.itedya.simpleauctions.daos.ItemPersistenceDao;
import com.itedya.simpleauctions.dtos.AuctionDto;
import com.itedya.simpleauctions.listeners.ItemPersistenceListener;
import com.itedya.simpleauctions.runnables.EndOfNotSoldAuctionRunnable;
import com.itedya.simpleauctions.utils.ThreadUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

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

    private static Economy econ = null;
    private static Integer savePersistenceAsyncThreadId = null;

    @Override
    public void onEnable() {
        instance = this;

        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        if (!setupEconomy()) {
            this.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        ItemPersistenceDao.read();
        savePersistenceAsyncThreadId = ThreadUtil.asyncRepeat(ItemPersistenceDao::save, 300);

        try {
            PluginCommand command = this.getCommand("licytacje");
            assert command != null : "Command is null!";

            Main.register();

            this.getServer().getPluginManager().registerEvents(new ItemPersistenceListener(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(savePersistenceAsyncThreadId);
        
        AuctionDao auctionDao = AuctionDao.getInstance();
        auctionDao.cancelTask();

        AuctionDto activeAuction = auctionDao.getActive();

        if (activeAuction != null) {
            ThreadUtil.sync(new EndOfNotSoldAuctionRunnable(activeAuction));
        }

        var queue = auctionDao.allInQueue();

        queue.forEach(dto -> {
            var runnable = new EndOfNotSoldAuctionRunnable(dto);
            runnable.run();
        });

        ItemPersistenceDao.save();
    }
}
