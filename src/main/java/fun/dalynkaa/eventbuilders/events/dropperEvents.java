package fun.dalynkaa.eventbuilders.events;

import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class dropperEvents implements Listener {
    public dropperEvents(EventBuilders eventBuilders){
        eventBuilders.getServer().getPluginManager().registerEvents(this, eventBuilders);

    }
    @EventHandler
    public void PlayerMoveEvent(EntityDamageEvent event){
        World world = Bukkit.getWorld("world");
        if(!(event.getEntity() instanceof Player)){
            return;
        }
        Player player = (Player) event.getEntity();
        if(player.getLocation().getBlockY() <= 180 && player.getLocation().getWorld().equals(world)){
            event.setCancelled(true);
            Location location = player.getLocation();
            int y = location.getBlockY()-1;
            Location block_down = new Location(world,location.getBlockX(),y,location.getBlockZ());
            if (block_down.getBlock().getType().equals(Material.OXIDIZED_COPPER)) {
                player.sendActionBar(Component.text("Чекпоинт ", TextColor.fromCSSHexString("#00b894")));
            }else if (block_down.getBlock().getType().equals(Material.BLUE_ICE)){
                player.sendActionBar(Component.text("Победа ", TextColor.fromCSSHexString("#00b894")));
                player.teleport(new Location(Bukkit.getWorld("world"),-14,184,-786));
                PlotPlayer.fromUUID(player.getUniqueId()).addDroperCount(1).save(false);

            } else {
                Location spawn = new Location(world,-14,184,-786);
                player.teleport(spawn);
                player.sendActionBar(Component.text("Неудача ", TextColor.fromCSSHexString("#d63031")));
            }
        }else {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void noGravity(EntityChangeBlockEvent event){
        if (event.getBlock().getLocation().getWorld() == Bukkit.getWorld("world")){
            event.setCancelled(true);
        }
    }
}
