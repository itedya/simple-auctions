package com.itedya.simpleauctions.runnables;

import com.itedya.simpleauctions.daos.AuctionDao;
import com.itedya.simpleauctions.utils.ChatUtil;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class CreateAuctionRunnable extends BukkitRunnable {
    private final Player executor;
    private final Material material;
    private final int quantity;
    private final int startingPrice;

    public CreateAuctionRunnable(Player player, Material material, int quantity, int startingPrice) {
        this.executor = player;
        this.material = material;
        this.quantity = quantity;
        this.startingPrice = startingPrice;
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

            ItemStack itemStack = new ItemStack(material);
            itemStack.setAmount(quantity);

            inventory.removeItemAnySlot(itemStack);

            AuctionDao auctionDao = AuctionDao.getInstance();
            auctionDao.create(executor, material, quantity, startingPrice);

            int size = auctionDao.getQueueSize();

            String message = "&a&lDodano przedmiot na sprzedaż! ";

            if (size != 0) {
                message += "Jesteś %d w kolejce".formatted(size);
            }

            executor.sendMessage(new ComponentBuilder()
                    .append(ChatUtil.PREFIX)
                    .append(" ")
                    .append(ChatUtil.p(message))
                    .create());
        } catch (Exception e) {
            executor.sendMessage(new ComponentBuilder()
                    .append(ChatUtil.PREFIX)
                    .append(" ")
                    .append(ChatUtil.SERVER_ERROR)
                    .create());
            e.printStackTrace();
        }
    }
}
