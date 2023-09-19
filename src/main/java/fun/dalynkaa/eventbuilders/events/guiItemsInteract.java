package fun.dalynkaa.eventbuilders.events;

import fun.dalynkaa.eventbuilders.EventBuilders;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class guiItemsInteract implements Listener {

    public guiItemsInteract(EventBuilders eventBuilders){
        eventBuilders.getServer().getPluginManager().registerEvents(this,eventBuilders);
    }

    @EventHandler
    public void itemDropEvent(PlayerDropItemEvent event){
        ItemMeta meta = event.getItemDrop().getItemStack().getItemMeta();
        if (meta == null){
            return;
        }
        if (meta.getPersistentDataContainer().has(NamespacedKey.fromString("gui_item"))){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void itemMoveEvent(InventoryMoveItemEvent event){
        ItemMeta meta = event.getItem().getItemMeta();
        if (meta == null){
            return;
        }
        if (meta.getPersistentDataContainer().has(NamespacedKey.fromString("gui_item"))){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void itemInteractEvent(PlayerInteractEvent event){
        if (event.getItem() == null){
            return;
        }
        if (event.getItem().getType().equals(Material.ENDER_PEARL)){
            event.setCancelled(true);
        }
        ItemMeta meta = event.getItem().getItemMeta();
        if (meta.getPersistentDataContainer().has(NamespacedKey.fromString("gui_item"))){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void itemClickEvent(InventoryClickEvent event){
        if (event.getCurrentItem() == null){
            return;
        }
        ItemMeta meta = event.getCurrentItem().getItemMeta();
        if (meta == null){
            return;
        }
        if (meta.getPersistentDataContainer().has(NamespacedKey.fromString("gui_item"))){
            event.setCancelled(true);
        }
    }
}
