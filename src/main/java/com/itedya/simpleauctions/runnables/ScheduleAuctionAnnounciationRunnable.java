package com.itedya.simpleauctions.runnables;

import com.itedya.simpleauctions.utils.ThreadUtil;
import org.bukkit.scheduler.BukkitRunnable;

public class ScheduleAuctionAnnounciationRunnable extends BukkitRunnable {
    public static boolean announcing = false;

    @Override
    public void run() {
        ThreadUtil.syncRepeat(new AnnounceNewItemRunnable(), 20 * 2);
    }
}
