package fun.dalynkaa.eventbuilders.utils.dataClasses;

import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.guis.PlotListGui;
import fun.dalynkaa.eventbuilders.utils.UsableClasses.InventoryButton;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.VoteFilter;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.util.*;

public class PlotVote {
    private PlotPlayer voter;
    private PlotPlayer voted;
    private fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot plot;
    private fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game game;
    private Integer type;

    public PlotVote(PlotPlayer voter, PlotPlayer voted, fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot plot, fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game game, Integer type) {
        this.voter = voter;
        this.voted = voted;
        this.plot = plot;
        this.game = game;
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot getPlot() {
        return plot;
    }

    public PlotPlayer getVoted() {
        return voted;
    }

    public PlotPlayer getVoter() {
        return voter;
    }

    public PlotVote setVoter(PlotPlayer voter) {
        this.voter = voter;
        return this;
    }

    public PlotVote setVoted(PlotPlayer voted) {
        this.voted = voted;
        return this;
    }

    public PlotVote setPlot_id(fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot plot) {
        this.plot = plot;
        return this;
    }

    public PlotVote setType(Integer type) {
        this.type = type;
        return this;
    }

    public fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game getGame() {
        return game;
    }

    public PlotVote setGame(fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game game) {
        this.game = game;
        return this;
    }
    public void save(){
        EventBuilders.getInstance().db.incertVote(this);
    }

    public static void getVoteItems(PlotPlayer plotPlayer, fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game game){
        ItemStack dirt = InventoryButton.from(Material.DIRT)
                .setName(Component.text("1", TextColor.fromCSSHexString("#f0932b"))
                        .append(Component.text(" - бал",TextColor.fromCSSHexString("#ffbe76"))))
                .setLore(Arrays.asList(Component.text("Нажми что бы выдать 1 бал за постройку",TextColor.fromCSSHexString("#a29bfe"))))
                .build((event -> {
                    givePoint(plotPlayer, game, 1);
                }), "dirt");
        ItemStack iron = InventoryButton.from(Material.IRON_BLOCK)
                .setName(Component.text("2", TextColor.fromCSSHexString("#535c68"))
                        .append(Component.text(" - бала",TextColor.fromCSSHexString("#95afc0"))))
                .setLore(Arrays.asList(Component.text("Нажми что бы выдать 2 бала за постройку",TextColor.fromCSSHexString("#a29bfe"))))
                .build((event -> {
                    givePoint(plotPlayer, game, 2);
                }), "iron");
        ItemStack copper = InventoryButton.from(Material.COPPER_BLOCK)
                .setName(Component.text("3", TextColor.fromCSSHexString("#d63031"))
                        .append(Component.text(" - бала",TextColor.fromCSSHexString("#ff7675"))))
                .setLore(Arrays.asList(Component.text("Нажми что бы выдать 3 бала за постройку",TextColor.fromCSSHexString("#a29bfe"))))
                .build((event -> {
                    givePoint(plotPlayer, game, 3);
                }), "copper");
        ItemStack gold = InventoryButton.from(Material.GOLD_BLOCK)
                .setName(Component.text("4", TextColor.fromCSSHexString("#f9ca24"))
                        .append(Component.text(" - бала",TextColor.fromCSSHexString("#f6e58d"))))
                .setLore(Arrays.asList(Component.text("Нажми что бы выдать 4 бала за постройку",TextColor.fromCSSHexString("#a29bfe"))))
                .build((event -> {
                    givePoint(plotPlayer, game, 4);
                }), "gold");
        ItemStack diamond = InventoryButton.from(Material.DIAMOND_BLOCK)
                .setName(Component.text("5", TextColor.fromCSSHexString("#22a6b3"))
                        .append(Component.text(" - балов",TextColor.fromCSSHexString("#7ed6df"))))
                .setLore(Arrays.asList(Component.text("Нажми что бы выдать 5 балов за постройку",TextColor.fromCSSHexString("#a29bfe"))))
                .build((event -> {
                    givePoint(plotPlayer, game, 5);
                }), "diamond");
        ItemStack perl = InventoryButton.from(Material.ENDER_PEARL)
                .setName(Component.text("Список игроков", TextColor.fromCSSHexString("#22a6b3")))
                .setLore(Arrays.asList(Component.text("Открывает список игроков вместе с оценками",TextColor.fromCSSHexString("#a29bfe"))))
                .build((event -> {
                    PlotListGui voteListGui = new PlotListGui(VoteFilter.NORMAL, game, plotPlayer, PlotListGui.GuiType.NORMAL);
                    voteListGui.open(plotPlayer.getPlayer());
                }), "perl");
        plotPlayer.getPlayer().getInventory().setItem(0, dirt);
        plotPlayer.getPlayer().getInventory().setItem(1, iron);
        plotPlayer.getPlayer().getInventory().setItem(2, copper);
        plotPlayer.getPlayer().getInventory().setItem(3, gold);
        plotPlayer.getPlayer().getInventory().setItem(4, diamond);
        plotPlayer.getPlayer().getInventory().setItem(6, perl);
    }
    public static void getEndGameItems(PlotPlayer plotPlayer, fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game game){
        ItemStack perl = InventoryButton.from(Material.ENDER_PEARL)
                .setName(Component.text("Обратно", TextColor.fromCSSHexString("#22a6b3")))
                .setLore(Arrays.asList(Component.text("Возвращает к финальной локации",TextColor.fromCSSHexString("#a29bfe"))))
                .build((event -> {
                    Player player = event.getPlayer();
                    player.teleport(EventBuilders.getInstance().config.getEndingLocation());
                }), "back");
        plotPlayer.getPlayer().getInventory().setItem(6, perl);
    }
    public static void clearVoteItems(PlotPlayer plotPlayer){
        ItemStack stack = new ItemStack(Material.AIR);

        plotPlayer.getPlayer().getInventory().setItem(0, stack);
        plotPlayer.getPlayer().getInventory().setItem(1, stack);
        plotPlayer.getPlayer().getInventory().setItem(2, stack);
        plotPlayer.getPlayer().getInventory().setItem(3, stack);
        plotPlayer.getPlayer().getInventory().setItem(4, stack);
        plotPlayer.getPlayer().getInventory().setItem(6, stack);
    }
    public static void givePoint(PlotPlayer voter, fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game currentGame, Integer type){
        Component PREFIX = EventBuilders.getInstance().PREFIX;
        if (voter.getCurrentLocation() == null){
            voter.getPlayer().sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                    .append(Component.text("Что бы поствить оценку вы должны находится на участке игрока!",TextColor.fromCSSHexString("#55efc4"))));
            return;
        }
        Plot currentPlot = voter.getCurrentLocation();
        if (currentPlot.getPlotPlayer() == null){
            voter.getPlayer().sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                    .append(Component.text("Этот участок пустой!",TextColor.fromCSSHexString("#55efc4"))));
            return;
        }
        voter.getPlayer().sendActionBar(Component.text("Выдано - ", TextColor.fromCSSHexString("#00b894")).append(Component.text(type, TextColor.fromCSSHexString("#55efc4"))).append(Component.text(" баллов", TextColor.fromCSSHexString("#00b894"))));
        PlotVote plotVote = new PlotVote(voter, currentPlot.getPlotPlayer(), currentPlot, currentGame, type);
        plotVote.save();
        ArrayList<PlotVote> votes = PlotVote.getPlotVotes(currentGame, currentPlot);
        int summ = votes.stream().mapToInt(PlotVote::getType).sum();
        plotVote.getPlot().setPointSum(summ).update();
        if (votes.size() == PlotPlayer.getOnlinePlotVoters().size()){
            currentPlot.setBorders(currentPlot.getPlotPlayer().getPlotSkin().getCornerVoted());
        }
    }
    public static HashMap<UUID, ArrayList<PlotVote>> getGameVotes(Game game) {
        HashMap<UUID, ArrayList<PlotVote>> votes = new HashMap<>();
        ResultSet resultSet = EventBuilders.getInstance().db.getGameVote(game.getGameId());
        try {
            UUID voterResult, plotResult;
            int typeResult;
            Plot plot;

            while (resultSet.next()) {
                voterResult = UUID.fromString(resultSet.getString("voter"));
                plotResult = UUID.fromString(resultSet.getString("plot_id"));
                typeResult = resultSet.getInt("type");
                plot = Plot.getPlotById(plotResult);

                votes.computeIfAbsent(plotResult, k -> new ArrayList<>())
                        .add(new PlotVote(PlotPlayer.fromUUID(voterResult), plot.getPlotPlayer(), plot, game, typeResult));
            }
            return votes;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
    public static ArrayList<PlotVote> getPlotVotes(Game game, Plot plot){
        ArrayList<PlotVote> votes = new ArrayList<>();
        ResultSet resultSet = EventBuilders.getInstance().db.getPlotVote(game.getGameId(), plot.getPlotId());
        try {
            while (resultSet.next()){
                UUID voter_result = UUID.fromString(resultSet.getString("voter"));
                UUID plot_result = UUID.fromString(resultSet.getString("plot_id"));
                Integer type_result = resultSet.getInt("type");
                fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot plot1 = fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot.getPlotById(plot_result);
                votes.add(new PlotVote(PlotPlayer.fromUUID(voter_result),plot1.getPlotPlayer(), plot1, game, type_result));
            }
            return votes;
        }catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
    public static Boolean playerHasVoteOfPlot(PlotPlayer voter, Plot plot, UUID game_id){
        if (EventBuilders.getInstance().db.getVote(game_id,plot.getPlotId(),voter.getUuid()) == null){
            return false;
        }else {
            return true;
        }
    }
}
