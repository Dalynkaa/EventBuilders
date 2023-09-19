package fun.dalynkaa.eventbuilders;

import fun.dalynkaa.eventbuilders.events.*;
import fun.dalynkaa.eventbuilders.events.customListener.regionEvents;
import fun.dalynkaa.eventbuilders.gamecommand.GameManager;
import fun.dalynkaa.eventbuilders.plotusercommand.PlotUserManager;
import fun.dalynkaa.eventbuilders.utils.*;
import fun.dalynkaa.eventbuilders.utils.UsableClasses.InventoryButton;
import fun.dalynkaa.eventbuilders.utils.UsableClasses.KickButton;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameStage;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.BuildGame;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class EventBuilders extends JavaPlugin {

    public Component PREFIX = Component.text("[", TextColor.fromCSSHexString("#74b9ff"))
            .append(Component.text("SpBuild",TextColor.fromCSSHexString("#0984e3")))
            .append(Component.text("] ", TextColor.fromCSSHexString("#74b9ff")));
    private static EventBuilders instance;
    public GameManegerRevrite db;
    public Game currentGame;
    public ConfigUtils config;
    public HashMap<UUID,Plot> currentplot = new HashMap<UUID,Plot>();
    public HashMap<String, InventoryButton> inventoryMap = new HashMap<>();
    public HashMap<String, KickButton> kickMap = new HashMap<>();
    public ArrayList<Plot> plotHach = new ArrayList<>();
    public ArrayList<Plot> skinPlotHach = new ArrayList<>();
    public Integer timerId;
    public Boolean newCanJoin = false;
    public Boolean canOut = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        setInstance(this);
        saveDefaultConfig();
        this.config = new ConfigUtils(this);
        // register db
        try {
            db = new GameManegerRevrite(this);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        //Placeholder addon
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderApis(this).register();
        }
        getCommand("game").setExecutor(new GameManager());
        getCommand("user").setExecutor(new PlotUserManager());
        GameUtils.ResetAllPlayers(Game.getCurrentGame());
        loadWorlds();
        Game game = Game.getCurrentGame();
        if (game instanceof BuildGame buildGame){
            buildGame.resumeGame();
        }
        //events
        new joinEvent(this);
        new onInventoryClickEvent(this);
        new guiItemsInteract(this);
        new playerRegionEnterLeave(this);
        new dropperEvents(this);
        new UserPreloginEvent(this);
        getServer().getPluginManager().registerEvents(new regionEvents(this), this);
    }
    private static void setInstance(EventBuilders main){
        instance = main;
    }
    public static EventBuilders getInstance(){
        return instance;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public void loadWorlds(){
        for (Game s:db.getGameList()){
            if (s.isCurrentGame() || s.getGameStage().equals(GameStage.GAME)){
                World world = new WorldCreator(s.getGameId().toString()).createWorld();
                getLogger().info(world.getName()+" загружен");
            }

        }
    }
}
