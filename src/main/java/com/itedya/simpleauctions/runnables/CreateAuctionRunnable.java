package com.itedya.simpleauctions.runnables;

import com.itedya.simpleauctions.daos.AuctionDao;
import com.itedya.simpleauctions.dtos.AuctionDto;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CreateAuctionRunnable extends BukkitRunnable {
    private Player executor;
    private Material material;
    private int quantity;

    public CreateAuctionRunnable(Player player, Material material, int quantity) {
        this.executor = player;
        this.material = material;
        this.quantity = quantity;
    }

    @Override
    public void run() {
        try {
            AuctionDto auctionDto = AuctionDao.create(executor, material, quantity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
