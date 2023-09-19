package fun.dalynkaa.eventbuilders.utils.dataClasses;


import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.DonateSkin;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlotPlayer {
    private UUID uuid;
    private String name;
    private Boolean in_game;
    private Boolean voter;
    private Boolean admin;
    private Boolean canJoin;
    private Boolean hasBan;
    private Integer droper_count;
    private DonateSkin plotSkin;
    private UUID current_plot_id;
    private Integer stage;
    private Timestamp creation_date;


    public PlotPlayer(UUID uuid, String name, Boolean inGame, Boolean voter,Boolean admin,Boolean canJoin,Boolean hasBan, Integer droperCount,DonateSkin plotSkin, UUID current_plot_id, Integer stage, Timestamp creationDate) {
        this.uuid = uuid;
        this.name = name;
        this.in_game = inGame;
        this.voter = voter;
        this.admin = admin;
        this.canJoin = canJoin;
        this.hasBan = hasBan;
        this.droper_count = droperCount;
        this.plotSkin = plotSkin;
        this.current_plot_id = current_plot_id;
        this.stage = stage;
        this.creation_date = creationDate;
    }
    public PlotPlayer(UUID uuid, String name, Boolean inGame, Boolean voter, Boolean admin,Boolean canJoin,Boolean hasBan, Integer droperCount, DonateSkin plotSkin, Timestamp creationDate) {
        this.uuid = uuid;
        this.name = name;
        this.in_game = inGame;
        this.voter = voter;
        this.admin = admin;
        this.canJoin = canJoin;
        this.hasBan = hasBan;
        this.droper_count = droperCount;
        this.plotSkin = plotSkin;
        this.current_plot_id = null;
        this.stage = 1;
        this.creation_date = creationDate;
    }
    public PlotPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.in_game = false;
        this.voter = false;
        this.admin = false;
        this.canJoin = true;
        this.hasBan = false;
        this.droper_count = 0;
        this.plotSkin = null;
        this.current_plot_id = null;
        this.stage = 1;
        this.creation_date = null;
    }

    public PlotPlayer setUuid(UUID uuid) {
        this.uuid = uuid;
        save(false);
        return this;
    }

    public PlotPlayer setName(String name) {
        this.name = name;
        return this;
    }

    public PlotPlayer setInGame(Boolean in_game) {
        this.in_game = in_game;
        return this;
    }

    public PlotPlayer setVoter(Boolean voter) {
        this.voter = voter;
        return this;
    }

    public PlotPlayer setAdmin(Boolean admin) {
        this.admin = admin;
        return this;
    }

    public Boolean canJoin() {
        return canJoin;
    }

    public void setCanJoin(Boolean canJoin) {
        this.canJoin = canJoin;
    }

    public Boolean hasBan() {
        return hasBan;
    }

    public void setHasBan(Boolean hasBan) {
        this.hasBan = hasBan;
    }

    public PlotPlayer setDroperCount(Integer droper_count) {
        this.droper_count = droper_count;
        return this;
    }
    public PlotPlayer addDroperCount(Integer droper_count) {
        this.droper_count = this.getDroperCount()+droper_count;
        return this;
    }

    public PlotPlayer setCurrentPlotId(UUID current_plot) {
        this.current_plot_id = current_plot;
        return this;
    }

    public PlotPlayer setCreationDate(Timestamp creation_date) {
        this.creation_date = creation_date;
        return this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Boolean isInGame() {
        return in_game;
    }

    public Boolean isVoter() {
        return voter;
    }

    public Boolean isAdmin() {
        return admin;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public UUID getCurrentPlotId() {
        return current_plot_id;
    }
    public fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot getCurrentPlot() {
        if (current_plot_id==null){
            return null;
        }
        return fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot.getPlotById(getCurrentPlotId());
    }

    public DonateSkin getPlotSkin() {
        return plotSkin;
    }

    public PlotPlayer setPlotSkin(DonateSkin plotSkin) {
        this.plotSkin = plotSkin;
        return this;
    }

    public Integer getDroperCount() {
        return droper_count;
    }

    public Timestamp getCreationDate() {
        return creation_date;
    }
    public Player getPlayer(){
        Player player = Bukkit.getPlayer(this.uuid);
        return player;
    }
    public OfflinePlayer getOfflinePlayer(){
        OfflinePlayer player = Bukkit.getOfflinePlayer(this.uuid);
        return player;
    }

    public fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot getCurrentLocation(){
        if (!EventBuilders.getInstance().currentplot.containsKey(getUuid())){
            return null;
        }
        Plot plot = EventBuilders.getInstance().currentplot.get(getUuid());
        return plot;
    }

    public static List<PlotPlayer> getOnlinePlotPlayers(boolean all){
        List<PlotPlayer> plotPlayers = new ArrayList<>();
        for (Player player: Bukkit.getOnlinePlayers()){
            PlotPlayer plotPlayer = PlotPlayer.fromUUID(player.getUniqueId());
            if (!all){
                if (plotPlayer.voter || plotPlayer.admin){
                    Bukkit.getLogger().info("skip");
                }else {
                    plotPlayers.add(plotPlayer);
                }
            }else {
                plotPlayers.add(plotPlayer);
            }
        }
        return plotPlayers;
    }
    public static List<PlotPlayer> getInGamePlotPlayers(){
        List<PlotPlayer> plotPlayers = new ArrayList<>();
        ResultSet resultSet = EventBuilders.getInstance().db.getAllUsersInGame();
        try {
            while (resultSet.next()){
                String result_name = resultSet.getString("name");
                UUID playerUUID = UUID.fromString(resultSet.getString("uuid"));
                Boolean result_InGame = resultSet.getBoolean("in_game");
                Boolean result_Voter = resultSet.getBoolean("voter");
                Boolean result_admin = resultSet.getBoolean("admin");
                Boolean result_canJoin = resultSet.getBoolean("canJoin");
                Boolean result_hasBan = resultSet.getBoolean("hasBan");
                Integer result_droper_count = resultSet.getInt("droper_count");
                DonateSkin result_plotSkin = DonateSkin.valueOf(resultSet.getString("plotSkin"));
                String result_curent_plot = resultSet.getString("current_plot");
                Integer result_curent_stage = resultSet.getInt("plot_stage");
                Timestamp result_creation_date = resultSet.getTimestamp("creation_date");
                if (result_curent_plot == null) {
                    plotPlayers.add(new PlotPlayer(playerUUID, result_name, result_InGame, result_Voter, result_admin,result_canJoin,result_hasBan, result_droper_count,result_plotSkin , result_creation_date));
                }else {
                    plotPlayers.add(new PlotPlayer(playerUUID, result_name, result_InGame, result_Voter, result_admin,result_canJoin,result_hasBan, result_droper_count, result_plotSkin, UUID.fromString(result_curent_plot), result_curent_stage, result_creation_date));
                }
            }
            return plotPlayers;
        }catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
    public static List<PlotPlayer> getAllPlayers(){
        List<PlotPlayer> plotPlayers = new ArrayList<>();
        ResultSet resultSet = EventBuilders.getInstance().db.getAllUsers();
        try {
            while (resultSet.next()){
                String result_name = resultSet.getString("name");
                UUID playerUUID = UUID.fromString(resultSet.getString("uuid"));
                Boolean result_InGame = resultSet.getBoolean("in_game");
                Boolean result_Voter = resultSet.getBoolean("voter");
                Boolean result_admin = resultSet.getBoolean("admin");
                Boolean result_canJoin = resultSet.getBoolean("canJoin");
                Boolean result_hasBan = resultSet.getBoolean("hasBan");
                Integer result_droper_count = resultSet.getInt("droper_count");
                DonateSkin result_plotSkin = DonateSkin.valueOf(resultSet.getString("plotSkin"));
                String result_curent_plot = resultSet.getString("current_plot");
                Integer result_curent_stage = resultSet.getInt("plot_stage");
                Timestamp result_creation_date = resultSet.getTimestamp("creation_date");
                if (result_curent_plot == null) {
                    plotPlayers.add(new PlotPlayer(playerUUID, result_name, result_InGame, result_Voter, result_admin,result_canJoin,result_hasBan, result_droper_count, result_plotSkin, result_creation_date));
                }else {
                    plotPlayers.add(new PlotPlayer(playerUUID, result_name, result_InGame, result_Voter, result_admin, result_admin,result_canJoin, result_droper_count ,result_plotSkin ,UUID.fromString(result_curent_plot), result_curent_stage, result_creation_date));
                }
            }
            return plotPlayers;
        }catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
    public static List<PlotPlayer> getOnlinePlotVoters(){
        List<PlotPlayer> plotPlayers = new ArrayList<>();
        for (Player player: Bukkit.getOnlinePlayers()){
            PlotPlayer plotPlayer = PlotPlayer.fromUUID(player.getUniqueId());
            if (plotPlayer.voter){
                plotPlayers.add(plotPlayer);
            }
        }
        return plotPlayers;
    }
    public static List<PlotPlayer> getOnlinePlotAdmin(){
        List<PlotPlayer> plotPlayers = new ArrayList<>();
        for (Player player: Bukkit.getOnlinePlayers()){
            PlotPlayer plotPlayer = PlotPlayer.fromUUID(player.getUniqueId());
            if (plotPlayer.admin){
                plotPlayers.add(plotPlayer);
            }
        }
        return plotPlayers;
    }
    public static List<PlotPlayer> getBannedPlotPlayers(){
        List<PlotPlayer> plotPlayers = new ArrayList<>();
        for (PlotPlayer player: PlotPlayer.getAllPlayers()){
            if (player.hasBan){
                plotPlayers.add(player);
            }
        }
        return plotPlayers;
    }

    public static PlotPlayer fromUUID(UUID playerUUID) {
        ResultSet resultSet = EventBuilders.getInstance().db.getUserByUUID(playerUUID);
        if (resultSet == null){
            return null;
        }
        try {
            String result_name = resultSet.getString("name");
            Boolean result_InGame = resultSet.getBoolean("in_game");
            Boolean result_Voter = resultSet.getBoolean("voter");
            Boolean result_admin = resultSet.getBoolean("admin");
            Boolean result_canJoin = resultSet.getBoolean("canJoin");
            Boolean result_hasBan = resultSet.getBoolean("hasBan");
            Integer result_droper_count = resultSet.getInt("droper_count");
            DonateSkin result_plotSkin = DonateSkin.valueOf(resultSet.getString("plotSkin"));
            String result_curent_plot = resultSet.getString("current_plot");
            Integer result_curent_stage = resultSet.getInt("plot_stage");
            Timestamp result_creation_date = resultSet.getTimestamp("creation_date");
            if (result_curent_plot == null) {
                return new PlotPlayer(playerUUID, result_name, result_InGame, result_Voter, result_admin,result_canJoin,result_hasBan, result_droper_count, result_plotSkin, result_creation_date);
            }else {
                return new PlotPlayer(playerUUID, result_name, result_InGame, result_Voter, result_admin,result_canJoin,result_hasBan, result_droper_count, result_plotSkin,UUID.fromString(result_curent_plot), result_curent_stage, result_creation_date);
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void save(boolean update){
        if (update) {
            EventBuilders.getInstance().db.InsertUserInToTableUsers(getName(),getUuid().toString());
            ResultSet resultSet = EventBuilders.getInstance().db.getUserByUUID(getUuid());
            try {
                setName(resultSet.getString("name"));
                setInGame(resultSet.getBoolean("in_game"));
                setVoter(resultSet.getBoolean("voter"));
                setAdmin(resultSet.getBoolean("admin"));
                setDroperCount(resultSet.getInt("droper_count"));
                if (resultSet.getString("current_plot") == null) {
                    setCurrentPlotId(null);
                } else {
                    setCurrentPlotId(UUID.fromString(resultSet.getString("current_plot")));
                }
                setCreationDate(resultSet.getTimestamp("creation_date"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            EventBuilders.getInstance().db.updateUserInToTableUsers(this);
        }
    }
    public void save(){
        EventBuilders.getInstance().db.InsertUserInToTableUsers(getName(),getUuid().toString());
        ResultSet resultSet = EventBuilders.getInstance().db.getUserByUUID(getUuid());
        try {
            setName(resultSet.getString("name"));
            setInGame(resultSet.getBoolean("in_game"));
            setVoter(resultSet.getBoolean("voter"));
            setDroperCount(resultSet.getInt("droper_count"));
            if (resultSet.getString("current_plot") == null){
                setCurrentPlotId(null);
            }else {
                setCurrentPlotId(UUID.fromString(resultSet.getString("current_plot")));
            }
            setCreationDate(resultSet.getTimestamp("creation_date"));
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

}
