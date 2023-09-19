package fun.dalynkaa.eventbuilders.utils.dataClasses.games;

import com.destroystokyo.paper.Title;
import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.utils.GameUtils;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.DonateSkin;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameStage;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameType;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.BuildPlot;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotVote;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BuildGame extends Game{
    public BuildGame(UUID gameId, GameType gameType, GameStage gameStage, Integer gameTime, Integer resourceTime, String thema, Integer plotSize, Integer plot_count, String shemaName, Boolean useSchema, Boolean currentGame) {
        super(gameId, gameType, gameStage, gameTime, resourceTime, thema, plotSize, plot_count, shemaName, useSchema, currentGame);
    }

    public BuildGame(UUID gameId, GameType gameType, GameStage gameStage, Integer gameTime, String thema, Integer plotSize, Integer plot_count, Boolean currentGame) {
        super(gameId, gameType, gameStage, gameTime, thema, plotSize, plot_count, currentGame);
    }

    public BuildGame(GameType gameType, GameStage gameStage, Integer gameTime, Integer resourceTime, String thema, Integer plotSize, Integer plot_count, String shemaName, Boolean useSchema, Boolean currentGame) {
        super(gameType, gameStage, gameTime, resourceTime, thema, plotSize, plot_count, shemaName, useSchema, currentGame);
    }

    public BuildGame(GameType gameType, GameStage gameStage, Integer gameTime, String thema, Integer plotSize, Integer plot_count, Boolean currentGame) {
        super(gameType, gameStage, gameTime, thema, plotSize, plot_count, currentGame);
    }

    public BuildGame(GameType gameType, GameStage gameStage, String thema) {
        super(gameType, gameStage, thema);
    }

    public void createWorldStage(Player player){
        setGameStage(GameStage.PREPARING_PLOT);
        createWorld();
        Component PREFIX = EventBuilders.getInstance().PREFIX;
        player.sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                .append(Component.text("Мир ",TextColor.fromCSSHexString("#55efc4")))
                .append(Component.text(getGameId().toString(),TextColor.fromCSSHexString("#00b894"))
                        .hoverEvent(HoverEvent.showText(Component.text("Тема: ",TextColor.fromCSSHexString("#a29bfe"))
                                .append(Component.text(getThema(), TextColor.fromCSSHexString("#6c5ce7"))))))
                .append(Component.text(" создан!", TextColor.fromCSSHexString("#55efc4")))
        );
        this.save(false);
        player.getInventory().close();
    }
    public void plotCreateStage(Player player, Integer count){
        setGameStage(GameStage.PREGENERATING_PLOT);

        this.save(false);
        Component PREFIX = EventBuilders.getInstance().PREFIX;
        player.sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                .append(Component.text("Начало создания плотов! ",TextColor.fromCSSHexString("#55efc4"))));
        Bukkit.getScheduler().runTaskAsynchronously(EventBuilders.getInstance(),()->{
            for (int i = 0; i<=count; i++){
                BuildPlot.addPlotToEnd();
                Component progress = Component.text("<= ",TextColor.fromCSSHexString("#e17055"))
                        .append(Component.text(String.valueOf(i), TextColor.fromCSSHexString("#55efc4")))
                        .append(Component.text("/",TextColor.fromCSSHexString("#2d3436")))
                        .append(Component.text(String.valueOf(count), TextColor.fromCSSHexString("#00b894")))
                        .append(Component.text(" =>", TextColor.fromCSSHexString("#e17055")));
                GameUtils.broatcastActionBar(progress);
            }
            Bukkit.getScheduler().runTask(EventBuilders.getInstance(),()-> synkTask(player,count));
        });
    }
    public void givePlotsStage(Player player){
        Component PREFIX = EventBuilders.getInstance().PREFIX;
        player.sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                .append(Component.text("Телепортирую людей на участки! ",TextColor.fromCSSHexString("#55efc4"))));
        Bukkit.getScheduler().runTaskAsynchronously(EventBuilders.getInstance(),()->{
            List<PlotPlayer> plotPlayers = PlotPlayer.getOnlinePlotPlayers(false);
            List<Plot> plots = Plot.getAllPlots(getGameId(), false);
            Integer iterator = 0;
            for (PlotPlayer plotPlayer: plotPlayers){
                if (plots.size() == 0){
                    player.sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                            .append(Component.text("Плоты закончились ",TextColor.fromCSSHexString("#55efc4"))));
                    return;
                }
                Plot plot = plots.remove(0);
                plot.setOwner(plotPlayer);
                plot.setBuild(false);
                plotPlayer.setCurrentPlotId(plot.getPlotId());
                plotPlayer.setInGame(true);
                plotPlayer.getPlayer().getInventory().setItem(8, new ItemStack(Material.AIR));
                if (!plotPlayer.getPlotSkin().equals(DonateSkin.DEFAULT)){
                    plot.setPos1(plot.getPos1().addX(-1).addZ(-1));
                    plot.setPos2(plot.getPos2().addX(1).addZ(1));
                    plot.setBottom(plotPlayer.getPlotSkin().getMaterials());
                    plot.setBorders(plotPlayer.getPlotSkin().getCornerDefault());
                }
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
                    plotPlayer.getPlayer().teleport(plotPlayer.getCurrentPlot().getPlotCenter().getLocation());
                }
                setGameStage(GameStage.WAITING_START);
                this.save(false);
            });
        });
    }
    public void startGameStage(Player player){
        setGameStage(GameStage.GAME);
        save(false);
        Component PREFIX = EventBuilders.getInstance().PREFIX;
        player.sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                .append(Component.text("Начинаю игру ",TextColor.fromCSSHexString("#55efc4"))));
        List<Plot> plots = Plot.getAllPlots(getGameId(), true);
        for (Plot plot: plots){
            plot.setBuild(true);
            if (plot.getPlotPlayer().getPlayer() != null) {
                Objects.requireNonNull(plot.getPlotPlayer()).getPlayer().setGameMode(GameMode.CREATIVE);
            }
        }
        Bukkit.getScheduler().runTaskAsynchronously(EventBuilders.getInstance(),()->{
            Bukkit.getScheduler().runTask(EventBuilders.getInstance(), ()->{
                Bukkit.getLogger().info("Начинаю таймер");
                GameUtils.startTimer( (game)->{
                    Bukkit.getBossBar(NamespacedKey.fromString("timer")).setTitle("Строительство закончено!");
                    for (Plot plot: plots){
                        plot.setBuild(false);
                        GameUtils.broatcastTitle(Title.builder().title("Строительтво окончено!").stay(100*20).subtitle("Ожидайте начала оценки!").build());
                        Bukkit.getScheduler().runTask(EventBuilders.getInstance(), ()->{Objects.requireNonNull(plot.getPlotPlayer()).getPlayer().setGameMode(GameMode.SPECTATOR);});
                    }
                    setGameStage(GameStage.WAIT_EVALUATION);
                    save(false);
                });
                BossBar bossBar = Bukkit.getBossBar(NamespacedKey.fromString("timer"));
                for (Player player1: Bukkit.getOnlinePlayers()){
                    bossBar.addPlayer(player1);
                }
            });
        });
    }
    public void startVoteStage(){
        setGameStage(GameStage.EVALUATION);
        this.save(false);
        EventBuilders.getInstance().config.resetTimer();
        Objects.requireNonNull(Bukkit.getBossBar(Objects.requireNonNull(NamespacedKey.fromString("timer")))).setVisible(false);
        if (EventBuilders.getInstance().timerId != null){
            Bukkit.getScheduler().cancelTask(EventBuilders.getInstance().timerId);
        }
        List<Plot> plots = Plot.getAllPlots(getGameId(), true);
        for (Plot plot: plots){
            plot.setBuild(false);
            GameUtils.broatcastTitle(Title.builder().title("Строительтво окончено!").stay(100*20).subtitle("Ожидайте начала оценки!").build());
            Bukkit.getScheduler().runTask(EventBuilders.getInstance(), ()->{Objects.requireNonNull(plot.getPlotPlayer()).getPlayer().setGameMode(GameMode.SPECTATOR);});
        }
        GameUtils.broatcastTitle(Title.builder().title("Начало оценки построек!").stay(1).build());
        for (PlotPlayer plotPlayer: PlotPlayer.getOnlinePlotVoters()){
            PlotVote.getVoteItems(plotPlayer, this);
        }
    }
    public void startFinishStage(){
        setGameStage(GameStage.ENDING);
        this.save(false);
        for (PlotPlayer plotVoter: PlotPlayer.getOnlinePlotVoters()){
            PlotVote.clearVoteItems(plotVoter);
            PlotVote.getEndGameItems(plotVoter, this);
            plotVoter.getPlayer().teleport(EventBuilders.getInstance().config.getEndingLocation());
        }
    }
    private void synkTask(Player player, Integer count){
        Bukkit.getScheduler().runTaskAsynchronously(EventBuilders.getInstance(),()->{
            Integer i = 0;
            Bukkit.getLogger().info("Creating plots");
            List<Plot> plots = Plot.getAllPlots(getGameId());
            for (Plot plot: plots){
                if (plot instanceof BuildPlot buildPlot){
                    buildPlot.fill(true,true, true);
                    buildPlot.setGenerated(true);
                    buildPlot.update();
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

        });
    }

    public void resumeGame(){
        if (getGameStage().equals(GameStage.GAME)){
            List<Plot> plots = Plot.getAllPlots(getGameId(), true);
            GameUtils.startTimer((game)->{
                BossBar bossBar = Bukkit.getBossBar(NamespacedKey.fromString("timer"));
                bossBar.setTitle("Строительство закончено!");
                for (Plot plot: plots){
                    plot.setBuild(false);
                    GameUtils.broatcastTitle(Title.builder().title("Строительтво окончено!").stay(100*20).subtitle("Ожидайте начала оценки!").build());
                    Bukkit.getScheduler().runTask(EventBuilders.getInstance(), ()->{Objects.requireNonNull(plot.getPlotPlayer()).getPlayer().setGameMode(GameMode.SPECTATOR);});
                }
                setGameStage(GameStage.WAIT_EVALUATION);
                save(false);
            });
        }
    }
    public boolean checkPlayerJoin(UUID uuid, Game game, PlotPlayer plotPlayer){
        if (game.getGameStage().getType()<=3){
            Bukkit.getLogger().info("1.2");
            return true;
        }
        if (plotPlayer.isInGame()){
            Bukkit.getLogger().info("1.3");
            return true;
        }
        if (plotPlayer.canJoin() == false){
            Bukkit.getLogger().info("1.5");
            return true;
        }
        List<Plot> plots = Plot.getAllPlots(game.getGameId(), false);
        if (Bukkit.getOnlinePlayers().size() < 100 && !Objects.requireNonNull(plots).isEmpty()&&EventBuilders.getInstance().newCanJoin&&game.getGameStage().equals(GameStage.GAME)&&plotPlayer.canJoin()){
            Bukkit.getLogger().info("1.4");
            return true;
        }
        return false;
    }
}
