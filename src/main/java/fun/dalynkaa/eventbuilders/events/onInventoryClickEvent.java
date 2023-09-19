package fun.dalynkaa.eventbuilders.events;


import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.utils.UsableClasses.IInventoryButton;
import fun.dalynkaa.eventbuilders.utils.UsableClasses.IKickButton;
import fun.dalynkaa.eventbuilders.utils.UsableClasses.InventoryButton;
import fun.dalynkaa.eventbuilders.utils.UsableClasses.KickButton;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class onInventoryClickEvent implements Listener {
    EventBuilders main;
    public onInventoryClickEvent(EventBuilders spBuildRevrited){
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
        if (!container.has(NamespacedKey.fromString("click"))){
            return;
        }
        event.setCancelled(true);
        String item_uuid = container.get(NamespacedKey.fromString("item_id"), PersistentDataType.STRING);
        InventoryButton inventoryButton = main.inventoryMap.get(item_uuid);
        IInventoryButton<PlayerInteractEvent> action = inventoryButton.getAction();
        action.execute(event);
    }
    @EventHandler
    public void InteractEvent(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Player player){
            if (!PlotPlayer.fromUUID(player.getUniqueId()).isAdmin()){
                return;
            }
            if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)){
                return;
            }
            PersistentDataContainer container = player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
            if (!container.has(NamespacedKey.fromString("item_id"))){
                return;
            }
            if (!container.has(NamespacedKey.fromString("action"))){
                return;
            }
            event.setCancelled(true);
            String item_uuid = container.get(NamespacedKey.fromString("item_id"), PersistentDataType.STRING);
            KickButton inventoryButton = main.kickMap.get(item_uuid);
            IKickButton<EntityDamageByEntityEvent> action = inventoryButton.getAction();
            action.execute(event);
        }

    }


}
