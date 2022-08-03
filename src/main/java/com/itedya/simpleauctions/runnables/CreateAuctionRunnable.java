package com.itedya.simpleauctions.runnables;

import com.itedya.simpleauctions.daos.AuctionDao;
import com.itedya.simpleauctions.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class CreateAuctionRunnable extends BukkitRunnable {
    private final Player executor;
    private final Material material;
    private final int quantity;

    public CreateAuctionRunnable(Player player, Material material, int quantity) {
        this.executor = player;
        this.material = material;
        this.quantity = quantity;
    }

    @Override
    public void run() {
        try {
            Inventory inventory = executor.getInventory();

            boolean quantityCheck = inventory.containsAtLeast(new ItemStack(material), quantity);

            if (!quantityCheck) {
                executor.sendMessage(ChatUtil.p("&cNie masz tylu sztuk przedmiotu na sprzedaż!"));
                return;
            }

            AuctionDao.create(executor, material, quantity);

            int size = AuctionDao.getSize();

            String message = "&a&lDodano przedmiot na sprzedaż!";

            if (size != 1) {
                message += "Jesteś %d w kolejce".formatted(AuctionDao.getSize() - 1);
            }

            executor.sendMessage(ChatUtil.p(message));
        } catch (Exception e) {
            executor.sendMessage(ChatUtil.SERVER_ERROR);
            e.printStackTrace();
        }
    }
}