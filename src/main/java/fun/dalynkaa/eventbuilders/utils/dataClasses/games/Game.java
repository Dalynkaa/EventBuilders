package fun.dalynkaa.eventbuilders.utils.dataClasses.games;


import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.GlobalProtectedRegion;
import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.utils.ControllItems;
import fun.dalynkaa.eventbuilders.utils.GameUtils;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameStage;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameType;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotVote;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.ResultSet;
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
    private Integer plot_count;
    private String shema_name;
    private Boolean use_schema;
    private Boolean currentGame;
    private Boolean latest;

    public Game(UUID gameId, GameType gameType,GameStage gameStage, Integer gameTime, Integer resourceTime, String thema, Integer plotSize, Integer plot_count, String shemaName, Boolean useSchema, Boolean currentGame, Boolean latest) {
        this.game_id = gameId;
        this.game_type = gameType;
        this.game_stage = gameStage;
        this.game_time = gameTime;
        this.resource_time = resourceTime;
        this.thema = thema;
        this.plot_size = plotSize;
        this.plot_count = plot_count;
        this.shema_name = shemaName;
        this.use_schema = useSchema;
        this.currentGame = currentGame;
        this.latest = latest;
    }
    public Game(UUID gameId, GameType gameType, GameStage gameStage, Integer gameTime, String thema, Integer plotSize, Integer plot_count,Boolean currentGame , Boolean latest) {
        this.game_id = gameId;
        this.game_type = gameType;
        this.game_stage = gameStage;
        this.game_time = gameTime;
        this.resource_time = 0;
        this.thema = thema;
        this.plot_size = plotSize;
        this.plot_count = plot_count;
        this.shema_name = null;
        this.use_schema = false;
        this.currentGame = currentGame;
        this.latest = latest;
    }
    public Game(GameType gameType,GameStage gameStage, Integer gameTime, Integer resourceTime, String thema, Integer plotSize,Integer plot_count , String shemaName, Boolean useSchema, Boolean currentGame, Boolean latest) {
        this.game_id = UUID.randomUUID();
        this.game_type = gameType;
        this.game_stage = gameStage;
        this.game_time = gameTime;
        this.resource_time = resourceTime;
        this.thema = thema;
        this.plot_size = plotSize;
        this.plot_count = plot_count;
        this.shema_name = shemaName;
        this.use_schema = useSchema;
        this.currentGame = currentGame;
        this.latest = latest;
    }
    public Game(GameType gameType, GameStage gameStage, Integer gameTime, String thema, Integer plotSize, Integer plot_count, Boolean currentGame, Boolean latest) {
        this.game_id = UUID.randomUUID();
        this.game_type = gameType;
        this.game_stage = gameStage;
        this.game_time = gameTime;
        this.resource_time = 0;
        this.thema = thema;
        this.plot_size = plotSize;
        this.plot_count = plot_count;
        this.shema_name = null;
        this.use_schema = false;
        this.currentGame = currentGame;
        this.latest = latest;
    }
    public Game(GameType gameType, GameStage gameStage, String thema) {
        this.game_id = UUID.randomUUID();
        this.game_type = gameType;
        this.game_stage = gameStage;
        this.game_time = 0;
        this.resource_time = 0;
        this.thema = thema;
        this.plot_size = 0;
        this.plot_count = 0;
        this.shema_name = null;
        this.use_schema = false;
        this.currentGame = false;
        this.latest = false;
    }

    public Game setGameId(UUID game_id) {
        this.game_id = game_id;
        return this;
    }

    public Game setGameType(GameType game_type) {
        this.game_type = game_type;
        if (game_type == GameType.NORMAL){
            EventBuilders.getInstance().currentGame = new BuildGame(GameType.NORMAL, GameStage.NO_GAME, "Не задано");
        } else if (game_type.equals(GameType.SKINS)) {
            EventBuilders.getInstance().currentGame = new SkinGame(GameType.SKINS, GameStage.SKIN_NO_GAME, "Не задано");
        }
        return this;
    }

    public Boolean getLatest() {
        return latest;
    }

    public Game setLatest(Boolean latest) {
        if (latest) {
            if (Game.getLatestGame()!=null){
                Game.getLatestGame().setLatest(false).save(false);
            }
        }
        this.latest = latest;
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

    public Integer getPlotCount() {
        return plot_count;
    }

    public Game setPlotCount(Integer plot_count) {
        this.plot_count = plot_count;
        return this;
    }

    public void createWorld(){
        WorldCreator wc = new WorldCreator(getGameId().toString());
        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.FLAT);
        wc.generateStructures(false);
        if (getGameType().equals(GameType.NORMAL)){
            wc.generatorSettings("{\"layers\": [{\"block\": \"bedrock\", \"height\": 1}, {\"block\": \"dirt\", \"height\": 40}, {\"block\": \"grass_block\", \"height\": 1}], \"biome\":\"plains\"}");
        }else if (getGameType().equals(GameType.SKINS)){
            wc.generatorSettings("{\"layers\": [{\"block\": \"barrier\", \"height\": 2},{\"block\": \"barrier\", \"height\": 2}], \"biome\":\"plains\"}");
        }
        World world = wc.createWorld();
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setGameRule(GameRule.MOB_GRIEFING,false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING,false);
        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        GlobalProtectedRegion globalRegion = new GlobalProtectedRegion("__global__");
        RegionManager regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        globalRegion.setFlag(Flags.BUILD, StateFlag.State.DENY);
        globalRegion.setFlag(Flags.TNT, StateFlag.State.DENY);
        globalRegion.setFlag(Flags.POTION_SPLASH, StateFlag.State.DENY);
        globalRegion.setFlag(Flags.CHORUS_TELEPORT, StateFlag.State.DENY);
        globalRegion.setFlag(Flags.MOB_SPAWNING, StateFlag.State.DENY);
        globalRegion.setFlag(Flags.CREEPER_EXPLOSION, StateFlag.State.DENY);
        globalRegion.setFlag(Flags.LAVA_FLOW, StateFlag.State.DENY);
        globalRegion.setFlag(Flags.OTHER_EXPLOSION, StateFlag.State.DENY);
        globalRegion.setFlag(Flags.RESPAWN_ANCHORS, StateFlag.State.DENY);
        regions.addRegion(globalRegion);
        try {
            regions.save();
        }catch (StorageException e){
            e.printStackTrace();
        }
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

    public Game setCurrentGame(Boolean currentGame) {
        this.currentGame = currentGame;
        return this;
    }
    public Boolean isCurrentGame(){
        return this.currentGame;
    }


    public World getWorld(){
        try {
            return Bukkit.getWorld(getGameId().toString());
        }catch (Exception e){
            return Bukkit.getWorld("world");
        }

    }

    public static Game getCurrentGame(){
        ResultSet resultSet = EventBuilders.getInstance().db.getCurrentGame();
        if (resultSet == null){
            if (EventBuilders.getInstance().currentGame == null){
                EventBuilders.getInstance().currentGame = new BuildGame(GameType.NORMAL,GameStage.NO_GAME,"Не задано");
            }
            return EventBuilders.getInstance().currentGame;
        }
        try {
            UUID result_gameId = UUID.fromString(resultSet.getString("game_id"));
            GameType result_gameType = GameType.valueOf(resultSet.getString("game_type"));
            GameStage result_gameStage = GameStage.valueOf(resultSet.getString("game_stage"));
            Integer result_gameTime = resultSet.getInt("game_time");
            Integer result_resourceTime = resultSet.getInt("resource_time");
            String result_thema = resultSet.getString("thema");
            Integer result_plotSize = resultSet.getInt("plot_size");
            Integer result_plotCount = resultSet.getInt("plot_count");
            String result_schemaName = resultSet.getString("schema_name");
            Boolean result_useSchema = resultSet.getBoolean("use_schema");
            Boolean result_currentGame = resultSet.getBoolean("current_game");
            Boolean result_last = resultSet.getBoolean("latest");
            if (result_gameType.equals(GameType.SKINS)){
                EventBuilders.getInstance().currentGame = new SkinGame(result_gameId,result_gameType,result_gameStage,result_gameTime,result_resourceTime,result_thema,result_plotSize, result_plotCount,result_schemaName,result_useSchema,result_currentGame,result_last);
            }else if (result_gameType.equals(GameType.NORMAL)){
                EventBuilders.getInstance().currentGame = new BuildGame(result_gameId,result_gameType,result_gameStage,result_gameTime,result_resourceTime,result_thema,result_plotSize, result_plotCount,result_schemaName,result_useSchema,result_currentGame,result_last);
            }
            return EventBuilders.getInstance().currentGame;
        }catch (SQLException e){
            if (EventBuilders.getInstance().currentGame == null){
                EventBuilders.getInstance().currentGame = new Game(GameType.NORMAL,GameStage.NO_GAME,"Не задано");
            }
            return EventBuilders.getInstance().currentGame;
        }
    }
    public static Game getLatestGame(){
        ResultSet resultSet = EventBuilders.getInstance().db.getLatestGame();
        try {
            UUID result_gameId = UUID.fromString(resultSet.getString("game_id"));
            GameType result_gameType = GameType.valueOf(resultSet.getString("game_type"));
            GameStage result_gameStage = GameStage.valueOf(resultSet.getString("game_stage"));
            Integer result_gameTime = resultSet.getInt("game_time");
            Integer result_resourceTime = resultSet.getInt("resource_time");
            String result_thema = resultSet.getString("thema");
            Integer result_plotSize = resultSet.getInt("plot_size");
            Integer result_plotCount = resultSet.getInt("plot_count");
            String result_schemaName = resultSet.getString("schema_name");
            Boolean result_useSchema = resultSet.getBoolean("use_schema");
            Boolean result_currentGame = resultSet.getBoolean("current_game");
            Boolean result_last = resultSet.getBoolean("latest");
            Game result;
            if (result_gameType.equals(GameType.NORMAL)){
                result = EventBuilders.getInstance().currentGame = new BuildGame(result_gameId,result_gameType,result_gameStage,result_gameTime,result_resourceTime,result_thema,result_plotSize, result_plotCount,result_schemaName,result_useSchema,result_currentGame,result_last);
            }else {
                result = null;
            }
            return result;
        }catch (SQLException e){
            return null;
        }
    }
    public static Game getGameById(String id){
        try {
            ResultSet resultSet = EventBuilders.getInstance().db.getGameByID(id);
            UUID result_gameId = UUID.fromString(resultSet.getString("game_id"));
            GameType result_gameType = GameType.valueOf(resultSet.getString("game_type"));
            GameStage result_gameStage = GameStage.valueOf(resultSet.getString("game_stage"));
            Integer result_gameTime = resultSet.getInt("game_time");
            Integer result_resourceTime = resultSet.getInt("resource_time");
            String result_thema = resultSet.getString("thema");
            Integer result_plotSize = resultSet.getInt("plot_size");
            Integer result_plotCount = resultSet.getInt("plot_count");
            String result_schemaName = resultSet.getString("schema_name");
            Boolean result_useSchema = resultSet.getBoolean("use_schema");
            Boolean result_currentGame = resultSet.getBoolean("current_game");
            Boolean result_last = resultSet.getBoolean("latest");
            if (result_gameType.equals(GameType.SKINS)){
                return new SkinGame(result_gameId,result_gameType,result_gameStage,result_gameTime,result_resourceTime,result_thema,result_plotSize, result_plotCount,result_schemaName,result_useSchema,result_currentGame,result_last);
            }else if (result_gameType.equals(GameType.NORMAL)){
                return  new BuildGame(result_gameId,result_gameType,result_gameStage,result_gameTime,result_resourceTime,result_thema,result_plotSize, result_plotCount,result_schemaName,result_useSchema,result_currentGame,result_last);
            }else {
                return new Game(result_gameId,result_gameType,result_gameStage,result_gameTime,result_resourceTime,result_thema,result_plotSize, result_plotCount,result_schemaName,result_useSchema,result_currentGame, result_last);
            }
        }catch (SQLException e){
            e.printStackTrace();
            return new BuildGame(GameType.NORMAL,GameStage.NO_GAME, "Не задано");
        }
    }
    public void save(Boolean create){
        Game game = Game.getCurrentGame();
        if (create){
            EventBuilders.getInstance().db.setCurrentGame(game.game_id, false);
        }
        if (EventBuilders.getInstance().db.getGameByID(getGameId().toString())==null){
            EventBuilders.getInstance().db.InsertGameInToTableUsers(getGameId(),getGameType(),getGameStage(), getGameTime(), getResourceTime(), "Не выбрано", getPlotSize(),getShemaName(),getUseSchema());
            EventBuilders.getInstance().currentGame = null;
            return;
        }
        EventBuilders.getInstance().db.updateGameInToTableGames(this);
    }
    public void endGame(Player player){
        Component PREFIX = EventBuilders.getInstance().PREFIX;
        if (getGameStage() == GameStage.NO_GAME || getGameStage() == GameStage.SKIN_NO_GAME) {
            player.sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                    .append(Component.text("Завершить игру не возможно! ",TextColor.fromCSSHexString("#55efc4"))));
            return;
        }
        BossBar bossBar = Bukkit.getBossBar(NamespacedKey.fromString("timer"));
        bossBar.setVisible(false);
        EventBuilders.getInstance().config.resetTimer();
        if (EventBuilders.getInstance().timerId != null){
            Bukkit.getScheduler().cancelTask(EventBuilders.getInstance().timerId);
        }
        EventBuilders.getInstance().db.setCurrentGame(getGameId(), false);
        EventBuilders.getInstance().currentGame = new BuildGame(GameType.NORMAL,GameStage.NO_GAME,"Не задано");
        player.sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                .append(Component.text("Игра завершена ",TextColor.fromCSSHexString("#55efc4"))));
        for (PlotPlayer player1: PlotPlayer.getOnlinePlotPlayers(true)){
            if (player1.isInGame()){
                player1.setCurrentPlotId(null);
                player1.setInGame(false);
                player1.save(false);
            }
            if (player1.isVoter()){
                PlotVote.clearVoteItems(player1);
            }
            ControllItems controllItems = new ControllItems();
            controllItems.giveItems(player1, EventBuilders.getInstance().currentGame);
            controllItems.spawn(player1, EventBuilders.getInstance().currentGame);
        }
        GameUtils.ResetAllPlayers(this);
        if (game_type.equals(GameType.SKINS)){
            World world1 = Bukkit.getWorld(getGameId());
            if (world1==null){
                world1 = new WorldCreator(getGameId().toString()).createWorld();
            }
            if (world1 != null){
                Bukkit.getServer().unloadWorld(getWorld(),true);
                deleteWorld(getWorld().getWorldFolder());
            }
            EventBuilders.getInstance().db.deleteGameFromTable(getGameId().toString());
        }
    }
    private boolean deleteWorld(File path) {
        if(path.exists()) {
            File files[] = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
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

