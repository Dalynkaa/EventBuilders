package fun.dalynkaa.eventbuilders.events;

import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameType;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.BuildGame;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.SkinGame;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class UserPreloginEvent implements Listener {
    public UserPreloginEvent(EventBuilders eventBuilders){
        eventBuilders.getServer().getPluginManager().registerEvents(this,eventBuilders);

    }
    @EventHandler
    public void preLoginEvent(AsyncPlayerPreLoginEvent event){
        Game game = Game.getCurrentGame();
        PlotPlayer plotPlayer;
        if (!EventBuilders.getInstance().db.userHasAccount(event.getUniqueId().toString())){
            plotPlayer = new PlotPlayer(event.getUniqueId(),event.getPlayerProfile().getName());
            plotPlayer.save();
        }else {
            plotPlayer = PlotPlayer.fromUUID(event.getUniqueId());
        }
        if (plotPlayer.isVoter() || plotPlayer.isAdmin()){
            event.allow();
            return;
        }
        Bukkit.getLogger().info(plotPlayer.hasBan().toString());
        if (plotPlayer.hasBan()){
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, Component.text("Ты забанен!"));
            return;
        }
        if (plotPlayer.getOfflinePlayer().isWhitelisted()){
            event.allow();
            return;
        }
        if (game.getGameType().equals(GameType.NORMAL)){
            Bukkit.getLogger().info("1");
            if (game instanceof BuildGame buildGame){
                Bukkit.getLogger().info("1.1");
                if (buildGame.checkPlayerJoin(event.getUniqueId(), game, plotPlayer)){
                    Bukkit.getLogger().info("1.5");
                    event.allow();
                    return;
                }
            }

        } else if (game.getGameType().equals(GameType.SKINS)) {
            Bukkit.getLogger().info("2");
            if (game instanceof SkinGame skinGame) {
                Bukkit.getLogger().info("2.1");
                if (skinGame.checkPlayerJoin(event.getUniqueId(), game, plotPlayer)){
                    Bukkit.getLogger().info("2.3");
                    event.allow();
                    return;
                }
            }
        }
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, Component.text("Игра уже началась!"));
    }
}
