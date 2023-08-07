package com.otsosity.spbuildrevrited.utils.dataClasses;



import com.iridium.iridiumcolorapi.IridiumColorAPI;
import com.otsosity.spbuildrevrited.SpBuildRevrited;
import com.otsosity.spbuildrevrited.utils.GameUtils;
import com.otsosity.spbuildrevrited.utils.dataClasses.Enums.GameStage;
import com.otsosity.spbuildrevrited.utils.dataClasses.Enums.GameType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class Game {
    private UUID game_id;
    private GameType game_type;
    private GameStage game_stage;
    private Integer game_time;
    private Integer resource_time;
    private String thema;
    private Integer plot_size;
    private String shema_name;
    private Boolean use_schema;



    public Game(UUID gameId, GameType gameType,GameStage gameStage, Integer gameTime, Integer resourceTime, String thema, Integer plotSize, String shemaName, Boolean useSchema, Boolean schemaPlotSize) {
        this.game_id = gameId;
        this.game_type = gameType;
        this.game_stage = gameStage;
        this.game_time = gameTime;
        this.resource_time = resourceTime;
        this.thema = thema;
        this.plot_size = plotSize;
        this.shema_name = shemaName;
        this.use_schema = useSchema;
    }
    public Game(UUID gameId, GameType gameType, GameStage gameStage, Integer gameTime, String thema, Integer plotSize) {
        this.game_id = gameId;
        this.game_type = gameType;
        this.game_stage = gameStage;
        this.game_time = gameTime;
        this.resource_time = 0;
        this.thema = thema;
        this.plot_size = plotSize;
        this.shema_name = null;
        this.use_schema = false;
    }
    public Game(GameType gameType,GameStage gameStage, Integer gameTime, Integer resourceTime, String thema, Integer plotSize, String shemaName, Boolean useSchema, Boolean schemaPlotSize) {
        this.game_id = UUID.randomUUID();
        this.game_type = gameType;
        this.game_stage = gameStage;
        this.game_time = gameTime;
        this.resource_time = resourceTime;
        this.thema = thema;
        this.plot_size = plotSize;
        this.shema_name = shemaName;
        this.use_schema = useSchema;
    }
    public Game(GameType gameType, GameStage gameStage, Integer gameTime, String thema, Integer plotSize) {
        this.game_id = UUID.randomUUID();
        this.game_type = gameType;
        this.game_stage = gameStage;
        this.game_time = gameTime;
        this.resource_time = 0;
        this.thema = thema;
        this.plot_size = plotSize;
        this.shema_name = null;
        this.use_schema = false;
    }
    public Game(GameType gameType, GameStage gameStage, String thema) {
        this.game_id = UUID.randomUUID();
        this.game_type = gameType;
        this.game_stage = gameStage;
        this.game_time = 0;
        this.resource_time = 0;
        this.thema = thema;
        this.plot_size = 0;
        this.shema_name = null;
        this.use_schema = false;
    }

    public Game setGameId(UUID game_id) {
        this.game_id = game_id;
        return this;
    }

    public Game setGameType(GameType game_type) {
        this.game_type = game_type;
        return this;
    }

    public Game setGameStage(GameStage game_stage) {
        this.game_stage = game_stage;
        return this;
    }

    public Game setGameTime(Integer game_time) {
        this.game_time = game_time;
        return this;
    }

    public Game setResourceTime(Integer resource_time) {
        this.resource_time = resource_time;
        return this;
    }

    public Game setThema(String thema) {
        this.thema = thema;
        return this;
    }

    public Game setPlotSize(Integer plot_size) {
        this.plot_size = plot_size;
        return this;
    }

    public Game setShemaName(String shema_name) {
        this.shema_name = shema_name;
        return this;
    }

    public Game setUseSchema(Boolean use_schema) {
        this.use_schema = use_schema;
        return this;
    }
    public void createWorld(){
        WorldCreator wc = new WorldCreator(getGameId().toString());
        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.FLAT);
        wc.generateStructures(false);

        World world = wc.createWorld();
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setGameRule(GameRule.MOB_GRIEFING,false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING,false);
    }
    public void teleport(Player player){
        Location spawn = new Location(getWorld(), 0, -60, 0);
        player.teleport(spawn);
    }

    public UUID getGameId() {
        return game_id;
    }
    public GameType getGameType() {
        return game_type;
    }
    public String getGameTypeString() {
        return game_type.getTranslated();
    }

    public GameStage getGameStage() {
        return game_stage;
    }
    public String getGameStageString() {
        return game_stage.getTranslated();
    }

    public Integer getGameTime() {
        return game_time;
    }

    public Integer getResourceTime() {
        return resource_time;
    }

    public String getThema() {
        return thema;
    }

    public Integer getPlotSize() {
        return plot_size;
    }

    public String getShemaName() {
        return shema_name;
    }

    public Boolean getUseSchema() {
        return use_schema;
    }
    public World getWorld(){
        try {
            return Bukkit.getWorld(getGameId().toString());
        }catch (Exception e){
            return Bukkit.getWorld("world");
        }

    }



    public static Game getCurrentGame(){
        ResultSet resultSet = SpBuildRevrited.getInstance().db.getCurrentGame();
        if (resultSet == null){
            if (SpBuildRevrited.getInstance().currentGame == null){
                SpBuildRevrited.getInstance().currentGame = new Game(GameType.NORMAL,GameStage.NO_GAME,"Не задано");
            }
            return SpBuildRevrited.getInstance().currentGame;
        }
        try {
            UUID result_gameId = UUID.fromString(resultSet.getString("game_id"));
            GameType result_gameType = GameType.valueOf(resultSet.getString("game_type"));
            GameStage result_gameStage = GameStage.valueOf(resultSet.getString("game_stage"));
            Integer result_gameTime = resultSet.getInt("game_time");
            Integer result_resourceTime = resultSet.getInt("resource_time");
            String result_thema = resultSet.getString("thema");
            Integer result_plotSize = resultSet.getInt("plot_size");
            String result_schemaName = resultSet.getString("schema_name");
            Boolean result_useSchema = resultSet.getBoolean("use_schema");
            Boolean result_currentGame = resultSet.getBoolean("current_game");
            return new Game(result_gameId,result_gameType,result_gameStage,result_gameTime,result_resourceTime,result_thema,result_plotSize,result_schemaName,result_useSchema,result_currentGame);
        }catch (SQLException e){
            e.printStackTrace();
            if (SpBuildRevrited.getInstance().currentGame == null){
                SpBuildRevrited.getInstance().currentGame = new Game(GameType.NORMAL,GameStage.NO_GAME,"Не задано");
            }
            return SpBuildRevrited.getInstance().currentGame;
        }
    }
    public static Game getGameById(String id){
        try {
            ResultSet resultSet = SpBuildRevrited.getInstance().db.getGameByID(id);
            UUID result_gameId = UUID.fromString(resultSet.getString("game_id"));
            GameType result_gameType = GameType.valueOf(resultSet.getString("game_type"));
            GameStage result_gameStage = GameStage.valueOf(resultSet.getString("game_stage"));
            Integer result_gameTime = resultSet.getInt("game_time");
            Integer result_resourceTime = resultSet.getInt("resource_time");
            String result_thema = resultSet.getString("thema");
            Integer result_plotSize = resultSet.getInt("plot_size");
            String result_schemaName = resultSet.getString("schema_name");
            Boolean result_useSchema = resultSet.getBoolean("use_schema");
            Boolean result_currentGame = resultSet.getBoolean("current_game");
            return new Game(result_gameId,result_gameType,result_gameStage,result_gameTime,result_resourceTime,result_thema,result_plotSize,result_schemaName,result_useSchema,result_currentGame);
        }catch (SQLException e){
            e.printStackTrace();
            return new Game(GameType.NORMAL,GameStage.NO_GAME, "Не задано");
        }
    }
    public void save(Boolean create){
        Game game = Game.getCurrentGame();
        if (create){
            SpBuildRevrited.getInstance().db.setCurrentGame(game.game_id, false);
        }
        if (SpBuildRevrited.getInstance().db.getGameByID(getGameId().toString())==null){
            SpBuildRevrited.getInstance().db.InsertGameInToTableUsers(getGameId(),getGameType(),getGameStage(), getGameTime(), getResourceTime(), "Не выбрано", getPlotSize(),getShemaName(),getUseSchema());
            SpBuildRevrited.getInstance().currentGame = null;
            return;
        }
        SpBuildRevrited.getInstance().db.updateGameInToTableGames(this);
    }

    public void createWorldStage(Player player){
        this.game_stage = GameStage.PREPARING_PLOT;
        createWorld();
        Component PREFIX = SpBuildRevrited.getInstance().PREFIX;
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
        this.game_stage = GameStage.PREGENERATING_PLOT;
        Component PREFIX = SpBuildRevrited.getInstance().PREFIX;
        player.sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                .append(Component.text("Начало создания плотов! ",TextColor.fromCSSHexString("#55efc4"))));
        Bukkit.getScheduler().runTaskAsynchronously(SpBuildRevrited.getInstance(),()->{
            for (int i = 0; i<=count; i++){
                Plot.addPlotToEnd();
                Component progress = Component.text("<= ",TextColor.fromCSSHexString("#e17055"))
                        .append(Component.text(String.valueOf(i), TextColor.fromCSSHexString("#55efc4")))
                        .append(Component.text("/",TextColor.fromCSSHexString("#2d3436")))
                        .append(Component.text(String.valueOf(count), TextColor.fromCSSHexString("#00b894")))
                        .append(Component.text("=>", TextColor.fromCSSHexString("#e17055")));
                GameUtils.broatcastActionBar(progress);
            }
            Bukkit.getScheduler().runTask(SpBuildRevrited.getInstance(),()-> synkTask(player,count));
        });
    }
    private void synkTask(Player player, Integer count){
        Integer i = 0;
        for (Plot plot: Plot.getAllPlots(getGameId())){
            plot.fill(Material.MUD_BRICK_SLAB);
            Bukkit.getLogger().info(plot.getPlotId().toString());
            plot.update();
            Component progress = Component.text("<= ",TextColor.fromCSSHexString("#0984e3"))
                    .append(Component.text(String.valueOf(i), TextColor.fromCSSHexString("#81ecec")))
                    .append(Component.text("/",TextColor.fromCSSHexString("#2d3436")))
                    .append(Component.text(String.valueOf(count), TextColor.fromCSSHexString("#00cec9")))
                    .append(Component.text("=>", TextColor.fromCSSHexString("#0984e3")));
            GameUtils.broatcastActionBar(progress);
            i = i+1;
        }
        Component PREFIX = SpBuildRevrited.getInstance().PREFIX;
        player.sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                .append(Component.text("Создано ",TextColor.fromCSSHexString("#55efc4")))
                .append(Component.text(String.valueOf(count),TextColor.fromCSSHexString("#00b894")))
                .append(Component.text(" плотов!", TextColor.fromCSSHexString("#55efc4"))));

    }




    @Override
    public String toString() {
        return "Game{" +
                "game_id=" + game_id +
                ", game_type=" + game_type +
                ", game_stage=" + game_stage +
                ", game_time=" + game_time +
                ", resource_time=" + resource_time +
                ", thema='" + thema + '\'' +
                ", plot_size=" + plot_size +
                ", shema_name='" + shema_name + '\'' +
                ", use_schema=" + use_schema +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return game_id.equals(game.game_id) && game_type == game.game_type && game_stage == game.game_stage && game_time.equals(game.game_time) && Objects.equals(resource_time, game.resource_time) && getThema().equals(game.getThema()) && plot_size.equals(game.plot_size) && Objects.equals(shema_name, game.shema_name) && Objects.equals(use_schema, game.use_schema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game_id, game_type, game_stage, game_time, resource_time, getThema(), plot_size, shema_name, use_schema);
    }
}

