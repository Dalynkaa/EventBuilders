package com.otsosity.spbuildrevrited.events;

import com.otsosity.spbuildrevrited.SpBuildRevrited;
import com.otsosity.spbuildrevrited.utils.UsableClasses.IInventoryButton;
import com.otsosity.spbuildrevrited.utils.UsableClasses.InventoryButton;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class onInventoryClickEvent implements Listener {
    SpBuildRevrited main;
    public onInventoryClickEvent(SpBuildRevrited spBuildRevrited){
        spBuildRevrited.getServer().getPluginManager().registerEvents(this,spBuildRevrited);
        this.main = spBuildRevrited;

    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event){
        if (event.getMaterial().equals(Material.AIR)){
            return;
        }
        PersistentDataContainer container = event.getItem().getItemMeta().getPersistentDataContainer();
        if (!container.has(NamespacedKey.fromString("item_id"))){
            return;
        }
        event.setCancelled(true);
        UUID item_uuid = UUID.fromString(container.get(NamespacedKey.fromString("item_id"), PersistentDataType.STRING));
        InventoryButton inventoryButton = main.inventoryMap.get(item_uuid);
        IInventoryButton<PlayerInteractEvent> action = inventoryButton.getAction();
        action.execute(event);
    }


}
