package fun.dalynkaa.eventbuilders.events;

import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.customEvents.regionEnterEvent;
import fun.dalynkaa.eventbuilders.customEvents.regionLeaveEvent;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import java.util.UUID;

public class playerRegionEnterLeave implements Listener {
    EventBuilders main;
    public playerRegionEnterLeave(EventBuilders eventBuilders){
        eventBuilders.getServer().getPluginManager().registerEvents(this, eventBuilders);
        this.main = eventBuilders;
    }
    @EventHandler
    public void PlayerMoveEvent(regionEnterEvent event){
        PlotPlayer plotPlayer = PlotPlayer.fromUUID(event.getPlayer().getUniqueId());
        if (plotPlayer.isVoter() || plotPlayer.isAdmin()){
            Plot plot = Plot.getPlotById(UUID.fromString(event.getProtectedRegion().getId()));
            Game game = Game.getGameById(plot.getGameId().toString());
            main.currentplot.put(plotPlayer.getUuid(),plot);
            if (plot.getPlotPlayer() == null){
                plotPlayer.getPlayer().sendActionBar("Не занято" + " - ("+plot.getPlotPosition()+"/"+game.getPlotCount()+")");
            }else {
                plotPlayer.getPlayer().sendActionBar(plot.getPlotPlayer().getName() + " - ("+plot.getPlotPosition()+"/"+game.getPlotCount()+")");
            }
        }
    }
    @EventHandler
    public void PlayerDecikleEvent(regionLeaveEvent event){
        PlotPlayer plotPlayer = PlotPlayer.fromUUID(event.getPlayer().getUniqueId());
        if (plotPlayer.isVoter() || plotPlayer.isAdmin()){
            main.currentplot.put(plotPlayer.getUuid(),null);
        }
    }
    @EventHandler
    public void playerSpreantEvent(PlayerToggleSprintEvent event){
        Player player = event.getPlayer();
        Boolean isSp = event.isSprinting();
        PlotPlayer plotPlayer = PlotPlayer.fromUUID(player.getUniqueId());
        if (plotPlayer.getCurrentLocation() == null && isSp){
            event.getPlayer().setFlySpeed(0.7f);
        }else {
            event.getPlayer().setFlySpeed(0.1f);
        }
    }
}
