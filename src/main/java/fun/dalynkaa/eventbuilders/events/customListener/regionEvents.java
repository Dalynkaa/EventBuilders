package fun.dalynkaa.eventbuilders.events;

import com.destroystokyo.paper.Title;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.customEvents.regionEnterEvent;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Plot;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import com.sk89q.worldguard.bukkit.event.player.ProcessPlayerEvent;
import net.raidstone.wgevents.events.RegionLeftEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class regionEvents implements Listener {
    EventBuilders main;
    HashMap<UUID, String> players = new HashMap<>();
    public regionEvents(EventBuilders spBuildRevrited){
        this.main = spBuildRevrited;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(event.getPlayer().getWorld()));
        ProtectedRegion currentRegion;
        try {currentRegion = regionManager.getApplicableRegions(BlockVector3.at(player.getLocation().getX(),player.getLocation().getY(),player.getLocation().getZ())).getRegions().iterator().next();
        }catch (Exception ignored){currentRegion = null;}
        if (currentRegion != null) {
            if (isPlayerInRegion(player, currentRegion)) {
                if (players.containsKey(player.getUniqueId())){
                    if (players.get(player.getUniqueId()) != currentRegion.getId()){
                        players.put(player.getUniqueId(), currentRegion.getId());
                        regionEnterEvent enterEvent = new regionEnterEvent(currentRegion, player);
                        main.getServer().getPluginManager().callEvent(enterEvent);
                        player.sendMessage("You have entered the region: " + currentRegion.getId());
                    }
                }else {
                    players.put(player.getUniqueId(), currentRegion.getId());
                    regionEnterEvent enterEvent = new regionEnterEvent(currentRegion, player);
                    main.getServer().getPluginManager().callEvent(enterEvent);
                    player.sendMessage("You have entered the region: " + currentRegion.getId());
                }
            }
        } else {
            if (players.containsKey(player.getUniqueId())){
                players.remove(player.getUniqueId());
                player.sendMessage("You have left the region.");
                //logica
            }
        }
    }

    private boolean isPlayerInRegion(Player player, ProtectedRegion region) {
        return region.contains(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
    }
}
