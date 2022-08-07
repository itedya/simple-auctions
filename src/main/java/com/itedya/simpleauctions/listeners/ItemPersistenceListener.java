package com.itedya.simpleauctions.listeners;

import com.itedya.simpleauctions.daos.ItemPersistenceDao;
import com.itedya.simpleauctions.dtos.ItemPersistenceDto;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemPersistenceListener implements Listener {
    @EventHandler()
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        List<ItemPersistenceDto> dtos = ItemPersistenceDao.getByUUID(player.getUniqueId().toString());

        dtos.forEach(dto -> {
            Inventory inventory = player.getInventory();
            inventory.addItem(new ItemStack(dto.material, dto.quantity));
        });
    }
}
