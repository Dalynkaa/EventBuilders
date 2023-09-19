package fun.dalynkaa.eventbuilders.utils.dataClasses.games;

import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.utils.ControllItems;
import fun.dalynkaa.eventbuilders.utils.GameUtils;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameStage;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameType;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.SkinPlot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class SkinGame extends Game{
    public SkinGame(UUID gameId, GameType gameType, GameStage gameStage, Integer gameTime, Integer resourceTime, String thema, Integer plotSize, Integer plot_count, String shemaName, Boolean useSchema, Boolean currentGame) {
        super(gameId, gameType, gameStage, gameTime, resourceTime, thema, plotSize, plot_count, shemaName, useSchema, currentGame);
    }

    public SkinGame(UUID gameId, GameType gameType, GameStage gameStage, Integer gameTime, String thema, Integer plotSize, Integer plot_count, Boolean currentGame) {
        super(gameId, gameType, gameStage, gameTime, thema, plotSize, plot_count, currentGame);
    }

    public SkinGame(GameType gameType, GameStage gameStage, Integer gameTime, Integer resourceTime, String thema, Integer plotSize, Integer plot_count, String shemaName, Boolean useSchema, Boolean currentGame) {
        super(gameType, gameStage, gameTime, resourceTime, thema, plotSize, plot_count, shemaName, useSchema, currentGame);
    }

    public SkinGame(GameType gameType, GameStage gameStage, Integer gameTime, String thema, Integer plotSize, Integer plot_count, Boolean currentGame) {
        super(gameType, gameStage, gameTime, thema, plotSize, plot_count, currentGame);
    }

    public SkinGame(GameType gameType, GameStage gameStage, String thema) {
        super(gameType, gameStage, thema);
    }

    public void startSkinGame(PlotPlayer player){
        setGameStage(GameStage.SKIN_PREGENERATING_PLOT);
        save(false);
        createWorld();
        setThema("Оценка скинов");
        getWorld().getSpawnLocation().add(0,-1,0).getBlock().setType(Material.BEDROCK);
        player.getPlayer().teleport(getWorld().getSpawnLocation());
//        ControllItems controllItems = new ControllItems();
//        for (PlotPlayer admin: PlotPlayer.getOnlinePlotAdmin()){
//            controllItems.giveSkinItems(admin, Game.getCurrentGame());
//        }
        Bukkit.getScheduler().runTaskAsynchronously(EventBuilders.getInstance(),()->{
            for (int i = 0; i<=150; i++){
                SkinPlot.addPlotToEnd(1);
                Component progress = Component.text("<= ", TextColor.fromCSSHexString("#e17055"))
                        .append(Component.text(String.valueOf(i), TextColor.fromCSSHexString("#55efc4")))
                        .append(Component.text("/",TextColor.fromCSSHexString("#2d3436")))
                        .append(Component.text(String.valueOf(150), TextColor.fromCSSHexString("#00b894")))
                        .append(Component.text(" =>", TextColor.fromCSSHexString("#e17055")));
                GameUtils.broatcastActionBar(progress);
            }
            Bukkit.getScheduler().runTask(EventBuilders.getInstance(),()-> synkTask(player.getPlayer(),150));
        });
        save(false);
    }
    private void synkTask(Player player, Integer count){
        Bukkit.getScheduler().runTaskAsynchronously(EventBuilders.getInstance(),()->{
            Integer i = 0;
            Bukkit.getLogger().info("Creating plots");
            List<Plot> plots = Plot.getAllPlots(getGameId());
            Game.getCurrentGame().setPlotCount(150).save(false);
            for (Plot plot: plots){
                if (plot instanceof SkinPlot skinPlot){
                    skinPlot.skinFill(false);
                    skinPlot.setGenerated(true);
                    skinPlot.update();
                }
                Component progress = Component.text("<= ",TextColor.fromCSSHexString("#0984e3"))
                        .append(Component.text(String.valueOf(i), TextColor.fromCSSHexString("#81ecec")))
                        .append(Component.text("/",TextColor.fromCSSHexString("#2d3436")))
                        .append(Component.text(String.valueOf(count), TextColor.fromCSSHexString("#00cec9")))
                        .append(Component.text(" =>", TextColor.fromCSSHexString("#0984e3")));
                GameUtils.broatcastActionBar(progress);
                i = i+1;
            }
            Component PREFIX = EventBuilders.getInstance().PREFIX;
            player.sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                    .append(Component.text("Создано ",TextColor.fromCSSHexString("#55efc4")))
                    .append(Component.text(String.valueOf(count),TextColor.fromCSSHexString("#00b894")))
                    .append(Component.text(" плотов!", TextColor.fromCSSHexString("#55efc4"))));
            for (Plot plot: EventBuilders.getInstance().plotHach){
                plot.setLatest(false);
                plot.update();
            }
        });
    }
    public void startGameStage(PlotPlayer plotPlayer1){
        Player player = plotPlayer1.getPlayer();
        plotPlayer1.getPlayer().getInventory().close();
        new ControllItems().giveSkinItems(plotPlayer1, Game.getCurrentGame());
        setGameStage(GameStage.SKIN_EVALUATION);
        Component PREFIX = EventBuilders.getInstance().PREFIX;
        Bukkit.getScheduler().runTaskAsynchronously(EventBuilders.getInstance(),()->{
            List<PlotPlayer> plotPlayers = PlotPlayer.getOnlinePlotPlayers(false);
            List<Plot> plots1 = Plot.getAllPlots(getGameId(), false);
            Integer iterator = 0;
            for (PlotPlayer plotPlayer: plotPlayers){
                assert plots1 != null;
                if (plots1.isEmpty()){
                    player.sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                            .append(Component.text("Плоты закончились ",TextColor.fromCSSHexString("#55efc4"))));
                    return;
                }
                Plot plot = plots1.remove(0);
                plot.setOwner(plotPlayer);
                plot.setBuild(false);
                plotPlayer.setCurrentPlotId(plot.getPlotId());
                plotPlayer.setInGame(true);
                plotPlayer.getPlayer().getInventory().setItem(8, new ItemStack(Material.AIR));
                plot.update();
                plotPlayer.save(false);
                Component progress = Component.text("<= ",TextColor.fromCSSHexString("#e17055"))
                        .append(Component.text(String.valueOf(iterator), TextColor.fromCSSHexString("#55efc4")))
                        .append(Component.text("/",TextColor.fromCSSHexString("#2d3436")))
                        .append(Component.text(String.valueOf(plotPlayers.size()), TextColor.fromCSSHexString("#00b894")))
                        .append(Component.text(" =>", TextColor.fromCSSHexString("#e17055")));
                GameUtils.broatcastActionBar(progress);
                iterator +=1;
            }
            Bukkit.getScheduler().runTask(EventBuilders.getInstance(),()-> {
                for (PlotPlayer plotPlayer: plotPlayers){
                    plotPlayer.getPlayer().teleport(plotPlayer.getCurrentPlot().getPlotCenter().setY(-60).addX(-2).getLocation().add(0.5,0,0.5));
                    plotPlayer.getPlayer().setGameMode(GameMode.ADVENTURE);
                }
                this.save(false);
            });
        });
    }
    public boolean checkPlayerJoin(UUID uuid, Game game, PlotPlayer plotPlayer){
        if (game.getGameStage().equals(GameStage.SKIN_NO_GAME)){
            Bukkit.getLogger().info("2.2");
            return true;
        }
        return false;
    }

}
