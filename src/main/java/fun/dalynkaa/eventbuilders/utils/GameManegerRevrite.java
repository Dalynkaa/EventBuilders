package fun.dalynkaa.eventbuilders.utils;


import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.utils.dataClasses.*;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameStage;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameType;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameManegerRevrite {
    EventBuilders main;
    private final String ip;
    private final Integer port;
    private final String user;
    private final String password;
    private final String database_name;
    private static Connection connection;
    public GameManegerRevrite(@NotNull EventBuilders main) throws SQLException, ClassNotFoundException {
        this.main = main;
        this.ip = main.getConfig().getString("mariadb.ip", "130.162.176.157");
        this.port = main.getConfig().getInt("mariadb.port", 3306);
        this.user = main.getConfig().getString("mariadb.user", "mine");
        this.password = main.getConfig().getString("mariadb.password", "mine1234");
        this.database_name = main.getConfig().getString("mariadb.database", "Sp_Build_base");

        Connection connection = ConntectToDb();
        Statement s = connection.createStatement();
        int game = s.executeUpdate("CREATE TABLE IF NOT EXISTS `game` (" +
                " `id` bigint(20) NOT NULL AUTO_INCREMENT," +
                " `game_id` varchar(40) NOT NULL," +
                " `game_type` enum('NORMAL','RESOURCES') NOT NULL," +
                " `game_stage` enum('NO_GAME','PREPARING_PLOT','PREGENERATING_PLOT','WAITING_START','GAME','WAIT_EVALUATION','EVALUATION','ENDING') NOT NULL," +
                " `game_time` int(11) NOT NULL," +
                " `resource_time` int(11) NOT NULL DEFAULT 0," +
                " `thema` varchar(40) NOT NULL," +
                " `plot_size` int(11) NOT NULL DEFAULT 5," +
                " `schema_name` varchar(40) DEFAULT NULL," +
                " `use_schema` tinyint(1) NOT NULL DEFAULT 0," +
                " `current_game` tinyint(1) NOT NULL DEFAULT 1," +
                " PRIMARY KEY (`id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci");
        int plots = s.executeUpdate("CREATE TABLE IF NOT EXISTS `plots` (" +
                " `id` bigint(20) NOT NULL AUTO_INCREMENT," +
                " `game_id` varchar(40) NOT NULL," +
                " `location1` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL CHECK (json_valid(`location1`))," +
                " `location2` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL CHECK (json_valid(`location2`))," +
                " `owner` varchar(40) DEFAULT NULL," +
                " `plot_id` varchar(40) NOT NULL," +
                " `isGenerated` tinyint(1) NOT NULL DEFAULT 0," +
                " `latest` tinyint(1) NOT NULL DEFAULT 0," +
                " PRIMARY KEY (`id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci");
        int user = s.executeUpdate("CREATE TABLE IF NOT EXISTS `user` (" +
                " `id` bigint(20) NOT NULL AUTO_INCREMENT," +
                " `name` varchar(40) NOT NULL," +
                " `uuid` varchar(40) NOT NULL," +
                " `droper_count` int(11) NOT NULL DEFAULT 0," +
                " `in_game` tinyint(1) NOT NULL DEFAULT 0," +
                " `voter` tinyint(1) NOT NULL DEFAULT 0," +
                " `current_plot` varchar(40) DEFAULT NULL," +
                " `creation_date` timestamp NOT NULL DEFAULT current_timestamp()," +
                " PRIMARY KEY (`id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci");
        int votes = s.executeUpdate("CREATE TABLE IF NOT EXISTS `votes` (" +
                " `id` bigint(11) NOT NULL AUTO_INCREMENT," +
                " `voter` varchar(40) NOT NULL," +
                " `user_id` varchar(40) NOT NULL," +
                " `plot_id` varchar(40) NOT NULL," +
                " `game_id` varchar(40) NOT NULL," +
                " `type` mediumint(9) NOT NULL," +
                " PRIMARY KEY (`id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci");
        connection.commit();
        connection.close();
    }

    public Connection ConntectToDb() throws SQLException, ClassNotFoundException {
        String jdbcDriver = "org.mariadb.jdbc.Driver";
        Class.forName(jdbcDriver);
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:mariadb://" + this.ip + ":" + this.port + "/" + this.database_name + "?user=" + this.user + "&password=" + this.password);
        }
        return connection;
    }
    public void InsertUserInToTableUsers(String name, String uuid) {
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("INSERT INTO `user` (`name`, `uuid`) VALUES (?, ?)");
            s.setString(1, name);
            s.setString(2, uuid);
            int affectedRows = s.executeUpdate();
            c.commit();
            
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public void InsertPlotInToTablePlots(UUID gameId,Integer position, Integer stageId, PlotLocation location1, PlotLocation location2, UUID plotId, Boolean isGenerated, Boolean latest) {
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("INSERT INTO `plots`(`plotPosition`, `stageId`, `game_id`, `location1`, `location2`, `plot_id`,`isGenerated`, `latest`) VALUES (?,?,?,?,?,?,?,?)");
            s.setInt(1, position);
            s.setInt(2, stageId);
            s.setString(3, gameId.toString());
            s.setString(4, location1.toJson());
            s.setString(5, location2.toJson());
            s.setString(6, plotId.toString());
            s.setBoolean(7, isGenerated);
            s.setBoolean(8, latest);
            int affectedRows = s.executeUpdate();
            c.commit();
             
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public void updatePlotInToTablePlots(Plot plot) {
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("UPDATE `plots` SET `plotPosition`=?,`stageId`=?, `game_id`=?,`location1`=?,`location2`=?,`owner`=?,`latest`=?,`isGenerated`=? WHERE `plot_id` = ?");
            s.setInt(1, plot.getPlotPosition());
            s.setInt(2, plot.getStageId());
            s.setString(3, plot.getGameId().toString());
            s.setString(4, plot.getPos1().toJson());
            s.setString(5, plot.getPos2().toJson());
            if (plot.getPlotPlayer()!=null){
                s.setString(6, plot.getPlotPlayer().getUuid().toString());
            }else {
                s.setString(6, null);
            }
            s.setBoolean(7, plot.isGenerated());
            s.setBoolean(8, plot.isLatest());
            s.setString(9, plot.getPlotId().toString());
            int affectedRows = s.executeUpdate();
            c.commit();
             
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public void updateUserInToTableUsers(PlotPlayer player) {
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("UPDATE `user` SET `name`=?,`uuid`=?,`droper_count`=?,`plotSkin`=?,`in_game`=?,`voter`=?,`admin`=?,`canJoin`=?,`hasBan`=?,`current_plot`=?,`plot_stage`=?,`creation_date`=? WHERE `uuid` = ?");
            s.setString(1, player.getName());
            s.setString(2, player.getUuid().toString());
            s.setInt(3, player.getDroperCount());
            s.setInt(4, player.getPlotSkin().getId());
            s.setBoolean(5, player.isInGame());
            s.setBoolean(6, player.isVoter());
            s.setBoolean(7, player.isAdmin());
            s.setBoolean(8, player.canJoin());
            s.setBoolean(9, player.hasBan());
            if (player.getCurrentPlot() != null){
                s.setString(10, player.getCurrentPlot().getPlotId().toString());
            }else {
                s.setNull(10, Types.VARCHAR);
            }
            s.setInt(11, player.getStage());
            s.setTimestamp(12, player.getCreationDate());
            s.setString(13, player.getUuid().toString());
            s.executeUpdate();
            c.commit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public void InsertGameInToTableUsers(UUID game_id, GameType gameType, GameStage gameStage, Integer gameTime, Integer resourceTime, String thema, Integer plotSize, String schema_name, Boolean useSchema) {
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("INSERT INTO `game` (`id`, `game_id`, `game_type`, `game_stage`, `game_time`, `resource_time`, `thema`, `plot_size`, `schema_name`, `use_schema`, `current_game`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)");
            s.setString(1, game_id.toString());
            s.setInt(2, gameType.getIntType());
            s.setInt(3, gameStage.getType());
            s.setInt(4, gameTime);
            s.setInt(5, resourceTime);
            s.setString(6, thema);
            s.setInt(7, plotSize);
            s.setString(8, schema_name);
            s.setBoolean(9, useSchema);
            int affectedRows = s.executeUpdate();
            c.commit();
             
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public void deleteGameFromTable(String game_id){
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("DELETE FROM `game` WHERE `game_id` = ?");
            s.setString(1, game_id);
            s.executeUpdate();
            c.commit();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public void updateGameInToTableGames(fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game game) {
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement(
                    "UPDATE `game` SET `game_type`=?,`game_stage`=?,`game_time`=?,`resource_time`=?,`thema`=?,`plot_size`=?,`plot_count`=?,`schema_name`=?,`use_schema`=? WHERE `game_id` = ?"
            );
            s.setInt(1,game.getGameType().getIntType());
            s.setInt(2,game.getGameStage().getType());
            s.setInt(3,game.getGameTime());
            s.setInt(4,game.getResourceTime());
            s.setString(5,game.getThema());
            s.setInt(6,game.getPlotSize());
            s.setInt(7,game.getPlotCount());
            s.setString(8,game.getShemaName());
            s.setBoolean(9,game.getUseSchema());
            s.setString(10,game.getGameId().toString());
            int affectedRows = s.executeUpdate();
            c.commit();
             
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public boolean userHasAccount(String uuid) {
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("SELECT count(*) FROM `user` WHERE `uuid` = ?");
            s.setString(1, uuid);
            ResultSet result = s.executeQuery();
            result.next();
            int count = result.getInt(1);
            c.commit();
             
            return (count > 0);
        } catch (Exception ignored) {
            return false;
        }
    }
    public ResultSet getUserByUUID(UUID uuid){
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("SELECT * FROM `user` WHERE `uuid` = ?");
            s.setString(1, uuid.toString());
            ResultSet result = s.executeQuery();
            result.next();
            c.commit();
             
            return result;
        }catch (Exception ignored) {
            return null;
        }
    }
    public ResultSet getAllUsersInGame(){
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("SELECT * FROM `user` WHERE `in_game` = 1 OR `current_plot` IS NOT NULL");
            ResultSet result = s.executeQuery();
            result.next();
            c.commit();
            return result;
        }catch (Exception ignored) {
            return null;
        }
    }
    public ResultSet getAllUsers(){
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("SELECT * FROM `user`");
            ResultSet result = s.executeQuery();
            result.next();
            c.commit();
            return result;
        }catch (Exception ignored) {
            return null;
        }
    }

    public ResultSet getPlotByID(UUID id){
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("SELECT * FROM `plots` WHERE `plot_id` = ?");
            s.setString(1, id.toString());
            ResultSet result = s.executeQuery();
            result.next();
            c.commit();
             
            return result;
        }catch (Exception ignored) {
            return null;
        }
    }
    public ResultSet getAllPlotsByGame(UUID id){
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("SELECT * FROM `plots` WHERE `game_id` = ?");
            s.setString(1, id.toString());
            ResultSet result = s.executeQuery();
            c.commit();
             
            return result;
        }catch (Exception ignored) {
            return null;
        }
    }
    public ResultSet getAllPlotsByGame(UUID id, Boolean claimed){
        try {
            Connection c = ConntectToDb();
            PreparedStatement s;
            if (claimed){
                s = c.prepareStatement("SELECT * FROM `plots` WHERE `game_id` = ? AND `owner` IS NOT NULL");
            }else {
                s = c.prepareStatement("SELECT * FROM `plots` WHERE `game_id` = ? AND `owner` IS NULL");
            }
            s.setString(1, id.toString());
            ResultSet result = s.executeQuery();
            c.commit();
             
            return result;
        }catch (Exception ignored) {
            return null;
        }
    }
    public ResultSet getAllPlotsByGame(UUID id, Boolean claimed, Boolean generated){
        try {
            Connection c = ConntectToDb();
            PreparedStatement s;
            String arg = "";
            if (generated){
                arg = " AND `isGenerated` = 1";
            }else {
                arg = " AND `isGenerated` = 0";
            }
            if (claimed){
                s = c.prepareStatement("SELECT * FROM `plots` WHERE `game_id` = ? AND `owner` IS NOT NULL"+arg);
            }else {
                s = c.prepareStatement("SELECT * FROM `plots` WHERE `game_id` = ? AND `owner` IS NULL"+arg);
            }
            s.setString(1, id.toString());
            ResultSet result = s.executeQuery();
            c.commit();

            return result;
        }catch (Exception ignored) {
            return null;
        }
    }
    public ResultSet getLatestPlot(UUID gameId){
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("SELECT * FROM `plots` WHERE `latest` = 1 AND `game_id` = ?");
            s.setString(1,gameId.toString());
            ResultSet result = s.executeQuery();
            result.next();
            c.commit();
             
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ResultSet getLatestPlot(UUID gameId, Integer stageId){
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("SELECT * FROM `plots` WHERE `latest` = 1 AND `game_id` = ? AND `stageId` = ?");
            s.setString(1,gameId.toString());
            s.setInt(2, stageId);
            ResultSet result = s.executeQuery();
            result.next();
            c.commit();

            return result;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ResultSet getGameByID(String id){
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("SELECT * FROM `game` WHERE `game_id` = ?");
            s.setString(1, id);
            ResultSet result = s.executeQuery();
            Boolean hasData = result.next();
            if (hasData){
                c.commit();
                 
                return result;
            }else {
                 
                return null;
            }
        }catch (Exception ignored) {
            return null;
        }
    }
    public ResultSet getVote(UUID game_id, UUID plot_id, UUID voter_id){
        try {

            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("SELECT * FROM `votes` WHERE `game_id` = ? AND `plot_id` = ? AND `voter` = ?");
            s.setString(1, game_id.toString());
            s.setString(2, plot_id.toString());
            s.setString(3, voter_id.toString());
            ResultSet result = s.executeQuery();
            boolean hasData = result.next();
            if (hasData){
                return result;
            }else {
                return null;
            }
        }catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
    public ResultSet getGameVote(UUID game_id){
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("SELECT * FROM `votes` WHERE `game_id` = ?");
            s.setString(1, game_id.toString());
            ResultSet result = s.executeQuery();
            return result;
        }catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
    public ResultSet getPlotVote(UUID game_id, UUID plot_id){
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("SELECT * FROM `votes` WHERE `game_id` = ? AND `plot_id` = ?");
            s.setString(1, game_id.toString());
            s.setString(2, plot_id.toString());
            ResultSet result = s.executeQuery();
            return result;
        }catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
    public void incertVote(PlotVote plotVote){
        try {
            Connection c = ConntectToDb();
            PreparedStatement s;
            ResultSet resultSet = getVote(plotVote.getGame().getGameId(), plotVote.getPlot().getPlotId(), plotVote.getVoter().getUuid());
            if (resultSet == null){
                s = c.prepareStatement("INSERT INTO `votes` (`voter`, `user_id`, `plot_id`, `game_id`, `type`) VALUES (?, ?, ?, ?, ?)");
                s.setString(1, plotVote.getVoter().getUuid().toString());
                s.setString(2, plotVote.getVoted().getUuid().toString());
                s.setString(3, plotVote.getPlot().getPlotId().toString());
                s.setString(4, plotVote.getGame().getGameId().toString());
                s.setInt(5, plotVote.getType());
            }else {
                s = c.prepareStatement(
                        "UPDATE `votes` SET `type`=? WHERE `game_id` = ? AND `plot_id` = ? AND `voter` = ?"
                );
                s.setInt(1, plotVote.getType());
                s.setString(2, plotVote.getGame().getGameId().toString());
                s.setString(3, plotVote.getPlot().getPlotId().toString());
                s.setString(4, plotVote.getVoter().getUuid().toString());
            }
            s.executeUpdate();
        }catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
    public ResultSet getCurrentGame(){
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("SELECT * FROM `game` WHERE `current_game` = 1");
            ResultSet result = s.executeQuery();
            Boolean hasData = result.next();
            if (hasData){
                c.commit();
                 
                return result;
            }else {
                 
                return null;
            }
        }catch (Exception ignored) {
            return null;
        }
    }
    public List<fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game> getGameList(){
        try {
            List<fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game> res = new ArrayList<>();
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("SELECT `game_id` FROM `game`");
            ResultSet result = s.executeQuery();
            while (result.next()){
                res.add(Game.getGameById(result.getString("game_id")));
            }
            c.commit();
             
            return res;
        }catch (Exception ignored) {
            return null;
        }
    }
    public void setCurrentGame(UUID id, Boolean bool){
        try {
            Connection c = ConntectToDb();
            PreparedStatement s = c.prepareStatement("UPDATE `game` SET `current_game`= ? WHERE `game_id` = ?");
            s.setString(2, id.toString());
            s.setBoolean(1,bool);
            s.executeQuery();
            c.commit();
             
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
