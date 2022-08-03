package com.itedya.simpleauctions.utils;

import com.itedya.simpleauctions.SimpleAuctions;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class ThreadUtil {
    public static int asyncRepeat(Runnable runnable, int period) {
        SimpleAuctions plugin = SimpleAuctions.getInstance();

        return Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, runnable, 0, period);
    }

    public static BukkitTask async(Runnable runnable) {
        SimpleAuctions plugin = SimpleAuctions.getInstance();

        return Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static int sync(Runnable runnable) {
        SimpleAuctions plugin = SimpleAuctions.getInstance();

        return Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, runnable);
    }

    public static int syncDelay(Runnable runnable, int delay) {
        SimpleAuctions plugin = SimpleAuctions.getInstance();

        return Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, runnable, delay);
    }
}