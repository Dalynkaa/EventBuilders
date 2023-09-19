package fun.dalynkaa.eventbuilders.events;


import de.oliver.fancyholograms.api.FancyHologramsPlugin;
import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.utils.ControllItems;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameStage;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.Objects;

public class joinEvent implements Listener {
    public joinEvent(EventBuilders spBuildRevrited) {
        spBuildRevrited.getServer().getPluginManager().registerEvents(this,spBuildRevrited);

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        player.setFlySpeed(0.1f);
        PlotPlayer plotPlayer;
        plotPlayer = PlotPlayer.fromUUID(player.getUniqueId());
        Game game = Game.getCurrentGame();
        ControllItems controllItems = new ControllItems();
        controllItems.giveItems(plotPlayer, game);
        controllItems.spawn(plotPlayer, game);
        List<Plot> plots = Plot.getAllPlots(game.getGameId(), false);
        if (!plotPlayer.isInGame() && !plotPlayer.isVoter()&& !plotPlayer.isAdmin() && game.getGameStage().equals(GameStage.GAME)){
            Plot playerPlot = plots.remove(0);
            plotPlayer.setCurrentPlotId(playerPlot.getPlotId());
            playerPlot.setOwner(plotPlayer);
            plotPlayer.setInGame(true);
            playerPlot.update();
            plotPlayer.save(false);
            plotPlayer.getPlayer().teleport(playerPlot.getPlotCenter().getLocation());
        }
        FancyHologramsPlugin.get().getHologramManager().getHologram("Dalynkaa").get().refreshHologram(player);
        FancyHologramsPlugin.get().getHologramManager().getHologram("Донат").get().refreshHologram(player);
        FancyHologramsPlugin.get().getHologramManager().getHologram("puk").get().refreshHologram(player);
        FancyHologramsPlugin.get().getHologramManager().getHologram("fugagames").get().refreshHologram(player);
        FancyHologramsPlugin.get().getHologramManager().getHologram("skins").get().refreshHologram(player);
    }

}
