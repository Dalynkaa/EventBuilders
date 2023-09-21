package fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots;


import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.DonateSkin;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameType;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.VoteFilter;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotLocation;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotVote;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Plot {
    private UUID plot_id;
    private Integer plotPosition;
    private Integer stageId;
    private UUID game_id;
    private PlotLocation pos1;
    private PlotLocation pos2;
    private PlotPlayer owner;
    private Boolean isGenerated;
    private Boolean latest;
    private RegionManager regions;
    private Integer pointSum;

    public Plot(UUID gameId,Integer plotPosition, Integer stageId, PlotLocation pos1, PlotLocation pos2, PlotPlayer owner,Boolean isGenerated, Boolean latest, Integer pointSum) {
        this.plot_id = UUID.randomUUID();
        this.plotPosition = plotPosition;
        this.stageId = stageId;
        this.game_id = gameId;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.owner = owner;
        this.latest = latest;
        this.regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(getPos1().getWorld()));
        this.isGenerated = isGenerated;
        this.pointSum = pointSum;

    }
    public Plot(UUID plot_id, Integer plotPosition, Integer stageId,UUID gameId, PlotLocation pos1, PlotLocation pos2, PlotPlayer owner,Boolean isGenerated, Boolean latest, Integer pointSum) {
        this.plot_id = plot_id;
        this.plotPosition = plotPosition;
        this.stageId = stageId;
        this.game_id = gameId;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.owner = owner;
        this.isGenerated = isGenerated;
        this.latest = latest;
        this.pointSum = pointSum;
    }
    public Boolean isGenerated() {
        return isGenerated;
    }

    public Plot setGenerated(Boolean generated) {
        isGenerated = generated;
        update();
        return this;
    }

    public Integer getPlotPosition() {
        return plotPosition;
    }

    public Plot setPlotPosition(Integer plotPosition) {
        this.plotPosition = plotPosition;
        return this;
    }

    public UUID getPlotId() {
        return plot_id;
    }

    public Plot setPlotId(UUID plot_id) {
        this.plot_id = plot_id;
        return this;
    }
    public Plot setPointSum(Integer pointSum){
        this.pointSum = pointSum;
        return this;
    }
    public Integer getPointSum(){
        return this.pointSum;
    }

    public Plot setGameId(UUID game_id) {
        this.game_id = game_id;
        return this;
    }

    public Plot setPos1(PlotLocation pos1) {
        this.pos1 = pos1;
        return this;
    }

    public Plot setPos2(PlotLocation pos2) {
        this.pos2 = pos2;
        return this;
    }

    public Integer getStageId() {
        return stageId;
    }

    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }

    public Plot setOwner(PlotPlayer owner) {
        this.owner = owner;
        if (regions==null){
            this.regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(getPos1().getWorld()));
        }
        if (owner == null){
            getProtectedRegion().getMembers().removeAll();
            saveRegions();
            return this;
        }
        getProtectedRegion().getMembers().addPlayer(owner.getUuid());
        saveRegions();
        return this;
    }

    public Boolean getLatest() {
        return latest;
    }

    public Plot setLatest(Boolean latest) {
        if (latest) {
            if (Plot.getLatestPlot()!=null){
                Plot.getLatestPlot().setLatest(false).update();
            }
        }
        this.latest = latest;
        return this;
    }

    public Boolean isLatest() {
        return latest;
    }

    public PlotLocation getPos1() {
        return pos1;
    }

    public PlotLocation getPos2() {
        return pos2;
    }

    public PlotPlayer getPlotPlayer() {
        return owner;
    }

    public UUID getGameId() {
        return game_id;
    }

    public static Plot getPlotById(UUID id){
        try {
            ResultSet resultSet = EventBuilders.getInstance().db.getPlotByID(id);
            UUID result_gameId = UUID.fromString(resultSet.getString("game_id"));
            Integer result_position = resultSet.getInt("plotPosition");
            Integer result_stageId = resultSet.getInt("stageId");
            PlotLocation result_location1 = PlotLocation.fromJson(resultSet.getString("location1"));
            PlotLocation result_location2 = PlotLocation.fromJson(resultSet.getString("location2"));
            UUID result_owner = null;
            if (resultSet.getString("owner") != null){
                result_owner = UUID.fromString(resultSet.getString("owner"));
            }
            UUID result_plot_id = UUID.fromString(resultSet.getString("plot_id"));
            Boolean isGenerated1 = resultSet.getBoolean("isGenerated");
            Integer pointSum1 = resultSet.getInt("pointSum");
            Boolean result_latest = resultSet.getBoolean("latest");
            return new Plot(result_plot_id, result_position, result_stageId,result_gameId,result_location1,result_location2,PlotPlayer.fromUUID(result_owner),isGenerated1,result_latest, pointSum1);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    public PlotLocation getPlotCenter(){
        PlotLocation pos1 = this.pos1;
        PlotLocation pos2 = this.pos2;

        PlotLocation center = new PlotLocation(pos1.getX()+2, -21, pos1.getZ()+((pos2.getZ()-pos1.getZ())/2), game_id.toString());
        return center;
    }
    public static Plot getLatestPlot(){
        try {
            Plot lat = null;
            ResultSet resultSet = EventBuilders.getInstance().db.getLatestPlot(fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game.getCurrentGame().getGameId());
            UUID result_gameId = UUID.fromString(resultSet.getString("game_id"));
            Integer result_position = resultSet.getInt("plotPosition");
            Integer result_stageId = resultSet.getInt("stageId");
            PlotLocation result_location1 = PlotLocation.fromJson(resultSet.getString("location1"));
            PlotLocation result_location2 = PlotLocation.fromJson(resultSet.getString("location2"));
            UUID result_owner = null;
            if (resultSet.getString("owner") != null){
                result_owner = UUID.fromString(resultSet.getString("owner"));
            }
            UUID result_plot_id = UUID.fromString(resultSet.getString("plot_id"));
            Boolean isGenerated1 = resultSet.getBoolean("isGenerated");
            Integer pointSum1 = resultSet.getInt("pointSum");
            Boolean result_latest = resultSet.getBoolean("latest");
            if (Game.getCurrentGame().getGameType().equals(GameType.NORMAL)){
                lat = new BuildPlot(result_plot_id, result_position, result_stageId,result_gameId,result_location1,result_location2,PlotPlayer.fromUUID(result_owner),isGenerated1,result_latest, pointSum1);
            }else if (Game.getCurrentGame().getGameType().equals(GameType.SKINS)){
                lat = new SkinPlot(result_plot_id, result_position, result_stageId,result_gameId,result_location1,result_location2,PlotPlayer.fromUUID(result_owner),isGenerated1,result_latest, pointSum1);
            }
            return lat;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static Plot getLatestPlot(Integer playerStage){
        try {
            Plot lat = null;
            ResultSet resultSet = EventBuilders.getInstance().db.getLatestPlot(Game.getCurrentGame().getGameId(), playerStage);
            UUID result_gameId = UUID.fromString(resultSet.getString("game_id"));
            Integer result_position = resultSet.getInt("plotPosition");
            Integer result_stageId = resultSet.getInt("stageId");
            PlotLocation result_location1 = PlotLocation.fromJson(resultSet.getString("location1"));
            PlotLocation result_location2 = PlotLocation.fromJson(resultSet.getString("location2"));
            UUID result_owner = null;
            if (resultSet.getString("owner") != null){
                result_owner = UUID.fromString(resultSet.getString("owner"));
            }
            UUID result_plot_id = UUID.fromString(resultSet.getString("plot_id"));
            Boolean isGenerated1 = resultSet.getBoolean("isGenerated");
            Integer pointSum1 = resultSet.getInt("pointSum");
            Boolean result_latest = resultSet.getBoolean("latest");
            if (Game.getCurrentGame().getGameType().equals(GameType.NORMAL)){
                lat = new BuildPlot(result_plot_id, result_position, result_stageId,result_gameId,result_location1,result_location2,PlotPlayer.fromUUID(result_owner),isGenerated1,result_latest, pointSum1);
            }else if (Game.getCurrentGame().getGameType().equals(GameType.SKINS)){
                lat = new SkinPlot(result_plot_id, result_position, result_stageId,result_gameId,result_location1,result_location2,PlotPlayer.fromUUID(result_owner),isGenerated1,result_latest, pointSum1);
            }
            return lat;
        }catch (Exception e){
            return null;
        }
    }
    public ProtectedRegion getProtectedRegion(){
        if (regions==null){
            this.regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(getPos1().getWorld()));
        }
        return this.regions.getRegion(getPlotId().toString());
    }
    public void saveRegions(){
        if (regions==null){
            this.regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(getPos1().getWorld()));
        }
        try {
            this.regions.save();
        }catch (StorageException e){
            e.printStackTrace();
        }
    }
    public void setBuild(boolean build){
        if (regions==null){
            this.regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(getPos1().getWorld()));
        }
        if (build){
            getProtectedRegion().setFlag(Flags.BUILD, StateFlag.State.ALLOW);
            getProtectedRegion().setFlag(Flags.BUILD.getRegionGroupFlag(), RegionGroup.MEMBERS);
        }else {
            getProtectedRegion().setFlag(Flags.BUILD, StateFlag.State.DENY);
            getProtectedRegion().setFlag(Flags.BUILD.getRegionGroupFlag(), RegionGroup.ALL);
        }
        saveRegions();


    }
    public void update(){
        EventBuilders.getInstance().db.updatePlotInToTablePlots(this);
    }
    public void save(){
        EventBuilders.getInstance().db.InsertPlotInToTablePlots(getGameId(),getPlotPosition(), getStageId(),getPos1(),getPos2(),getPlotId(),isGenerated(),isLatest());
    }
    public void setBorders(Material material){
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(getPos1().getWorld()))) {
            //CuboidRegion region_walls = new CuboidRegion(BukkitAdapter.adapt(getPos1().getWorld()),BlockVector3.at(getPos1().getX()-1, fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game.getCurrentGame().getGameType().getY(), getPos1().getZ()-1), BlockVector3.at(getPos2().getX()+1, fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game.getCurrentGame().getGameType().getY(), getPos2().getZ()+1));
            CuboidRegion region_walls = new CuboidRegion(BukkitAdapter.adapt(getPos1().getWorld()),BlockVector3.at(getPos1().getX()-1, fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game.getCurrentGame().getGameType().getY(), getPos1().getZ()-1), BlockVector3.at(getPos2().getX()+1, fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game.getCurrentGame().getGameType().getY(), getPos2().getZ()+1));
            editSession.makeCuboidWalls(region_walls,BukkitAdapter.adapt(material.createBlockData()));
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public void setWalls(Material material){
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(getPos1().getWorld()))) {
            //CuboidRegion region_walls = new CuboidRegion(BukkitAdapter.adapt(getPos1().getWorld()),BlockVector3.at(getPos1().getX()-1, fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game.getCurrentGame().getGameType().getY(), getPos1().getZ()-1), BlockVector3.at(getPos2().getX()+1, fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game.getCurrentGame().getGameType().getY(), getPos2().getZ()+1));
            CuboidRegion region_walls = new CuboidRegion(BukkitAdapter.adapt(getPos1().getWorld()),BlockVector3.at(getPos1().getX()-1, Game.getCurrentGame().getGameType().getY(), getPos1().getZ()-1), BlockVector3.at(getPos2().getX()+1, 320, getPos2().getZ()+1));
            editSession.makeCuboidWalls(region_walls,BukkitAdapter.adapt(material.createBlockData()));
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public void setBottom(List<Material> material){
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(getPos1().getWorld()))) {
            CuboidRegion region = new CuboidRegion(BukkitAdapter.adapt(getPos1().getWorld()), BlockVector3.at(getPos1().getX(), getPos1().setY(Game.getCurrentGame().getGameType().getY()-1+(getStageId()-1)).getY(),getPos1().getZ()),BlockVector3.at(getPos2().getX(), getPos2().setY(Game.getCurrentGame().getGameType().getY()-1+(getStageId()-1)).getY(),getPos2().getZ()));
            RandomPattern pattern = new RandomPattern();
            pattern.add(BukkitAdapter.adapt(material.get(0).createBlockData()),0.25);
            pattern.add(BukkitAdapter.adapt(material.get(1).createBlockData()),0.25);
            pattern.add(BukkitAdapter.adapt(material.get(2).createBlockData()),0.25);
            pattern.add(BukkitAdapter.adapt(material.get(3).createBlockData()),0.25);
            editSession.setBlocks(region,pattern);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public void clearPlot(){
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(getPos1().getWorld()))) {
            CuboidRegion region = new CuboidRegion(BukkitAdapter.adapt(getPos1().getWorld()), getPos1().setY(-64).getBlockVector3(), getPos2().setY(256).getBlockVector3());
            region.getWorld().regenerate(region,editSession);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        setBottom(DonateSkin.DEFAULT.getMaterials());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plot plot = (Plot) o;
        return Objects.equals(plot_id, plot.plot_id) && Objects.equals(getGameId(), plot.getGameId()) && Objects.equals(getPos1(), plot.getPos1()) && Objects.equals(getPos2(), plot.getPos2()) && Objects.equals(getPlotPlayer(), plot.getPlotPlayer()) && Objects.equals(latest, plot.latest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plot_id, getGameId(), getPos1(), getPos2(), getPlotPlayer(), latest);
    }

    @Override
    public String toString() {
        return "Plot{" +
                "plot_id=" + plot_id +
                ", game_id=" + game_id +
                ", pos1=" + pos1 +
                ", pos2=" + pos2 +
                ", owner=" + owner +
                ", latest=" + latest +
                '}';
    }
    public static List<Plot> getAllPlots(UUID game_id){
        try {
            ResultSet resultSet = EventBuilders.getInstance().db.getAllPlotsByGame(game_id);
            return getPlots(resultSet);
        }catch (SQLException e){
            return null;
        }
    }
    public static List<Plot> getAllPlots(UUID game_id, Boolean claimed){
        try {
            ResultSet resultSet = EventBuilders.getInstance().db.getAllPlotsByGame(game_id, claimed);
            return getPlots(resultSet);
        }catch (SQLException e){
            return null;
        }
    }
    public static List<Plot> getAllPlots(UUID game_id, Boolean claimed, Boolean generated){
        try {
            ResultSet resultSet = EventBuilders.getInstance().db.getAllPlotsByGame(game_id, claimed, generated);
            return getPlots(resultSet);
        }catch (SQLException e){
            return null;
        }
    }

    @NotNull
    private static List<Plot> getPlots(ResultSet resultSet) throws SQLException {
        List<Plot> res = new ArrayList<>();
        while (resultSet.next()){
            Plot p = plotStartament(resultSet);
            res.add(p);
        }
        return res;
    }

    private static Plot plotStartament(ResultSet resultSet) throws SQLException {
        UUID result_gameId = UUID.fromString(resultSet.getString("game_id"));
        Integer result_position = resultSet.getInt("plotPosition");
        Integer result_stageId = resultSet.getInt("stageId");
        PlotLocation result_location1 = PlotLocation.fromJson(resultSet.getString("location1"));
        PlotLocation result_location2 = PlotLocation.fromJson(resultSet.getString("location2"));
        UUID result_owner = null;
        if (resultSet.getString("owner") != null){
            result_owner = UUID.fromString(resultSet.getString("owner"));
        }
        UUID result_plot_id = UUID.fromString(resultSet.getString("plot_id"));
        Boolean isGenerated1 = resultSet.getBoolean("isGenerated");
        Integer pointSum1 = resultSet.getInt("pointSum");
        Boolean result_latest = resultSet.getBoolean("latest");
        Plot p = null;
        if (Game.getCurrentGame().getGameType().equals(GameType.NORMAL)){
            p = new BuildPlot(result_plot_id, result_position, result_stageId,result_gameId,result_location1,result_location2, PlotPlayer.fromUUID(result_owner),isGenerated1,result_latest, pointSum1);
        }else if (Game.getCurrentGame().getGameType().equals(GameType.SKINS)){
            p = new SkinPlot(result_plot_id, result_position, result_stageId,result_gameId,result_location1,result_location2,PlotPlayer.fromUUID(result_owner),isGenerated1,result_latest, pointSum1);
        }
        return p;
    }

    public static List<Plot> getAllPlots(UUID game_id, Boolean claimed, VoteFilter filter, PlotPlayer plotPlayer){
        try {
            ResultSet resultSet = EventBuilders.getInstance().db.getAllPlotsByGame(game_id, claimed);
            List<Plot> res = new ArrayList<>();
            while (resultSet.next()){
                Plot p = plotStartament(resultSet);
                if (filter.equals(VoteFilter.NORMAL)){
                    res.add(p);
                } else if (filter.equals(VoteFilter.NO_VOTE)) {
                    if (!PlotVote.playerHasVoteOfPlot(plotPlayer, p, game_id)){
                        res.add(p);
                    }
                } else if (filter.equals(VoteFilter.VOTED)) {
                    if (PlotVote.playerHasVoteOfPlot(plotPlayer, p, game_id)){
                        res.add(p);
                    }
                }

            }
            return res;
        }catch (SQLException e){
            return null;
        }
    }
}
