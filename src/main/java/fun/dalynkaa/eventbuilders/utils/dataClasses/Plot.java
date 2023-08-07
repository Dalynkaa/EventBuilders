package com.otsosity.spbuildrevrited.utils.dataClasses;


import com.google.gson.Gson;
import com.otsosity.spbuildrevrited.SpBuildRevrited;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class Plot {
    private UUID plot_id;
    private UUID game_id;
    private PlotLocation pos1;
    private PlotLocation pos2;
    private UUID owner;
    private Boolean isGenerated;
    private Boolean latest;

    public Plot(UUID gameId, PlotLocation pos1, PlotLocation pos2, UUID owner,Boolean isGenerated, Boolean latest) {
        this.plot_id = UUID.randomUUID();
        this.game_id = gameId;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.owner = owner;
        if (latest){
            Plot latestPlot = Plot.getLatestPlot();
            if (latestPlot != null){
                latestPlot.latest = false;
                if (SpBuildRevrited.getInstance().plotHach.size() == 0){
                    SpBuildRevrited.getInstance().plotHach.add(latestPlot);
                }else {
                    SpBuildRevrited.getInstance().plotHach.set(SpBuildRevrited.getInstance().plotHach.size()-1,latestPlot);
                }
                latestPlot.update();
            }
            this.latest = true;
        }else {
            this.latest = false;
        }
        this.isGenerated = isGenerated;

    }
    public Plot(UUID plot_id,UUID gameId, PlotLocation pos1, PlotLocation pos2, UUID owner,Boolean isGenerated, Boolean latest) {
        this.plot_id = plot_id;
        this.game_id = gameId;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.owner = owner;
        this.isGenerated = isGenerated;
        this.latest = latest;
    }

    public Boolean isGenerated() {
        return isGenerated;
    }

    public Plot setGenerated(Boolean generated) {
        isGenerated = generated;
        return this;
    }

    public UUID getPlotId() {
        return plot_id;
    }

    public Plot setPlotId(UUID plot_id) {
        this.plot_id = plot_id;
        return this;
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

    public Plot setOwner(UUID owner) {
        this.owner = owner;
        getProtectedRegion().getMembers().addPlayer(owner);
        return this;
    }

    public Boolean getLatest() {
        return latest;
    }

    public Plot setLatest(Boolean latest) {
        if (latest) {
            Plot.getLatestPlot().setLatest(false).update();
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

    public UUID getOwner() {
        return owner;
    }

    public UUID getGameId() {
        return game_id;
    }

    public static Plot getPlotById(UUID id){
        try {
            ResultSet resultSet = SpBuildRevrited.getInstance().db.getPlotByID(id);
            UUID result_gameId = UUID.fromString(resultSet.getString("game_id"));
            PlotLocation result_location1 = PlotLocation.fromJson(resultSet.getString("location1"));
            PlotLocation result_location2 = PlotLocation.fromJson(resultSet.getString("location2"));
            UUID result_owner = null;
            if (resultSet.getString("owner") != null){
                result_owner = UUID.fromString(resultSet.getString("owner"));
            }
            UUID result_plot_id = UUID.fromString(resultSet.getString("plot_id"));
            Boolean isGenerated1 = resultSet.getBoolean("isGenerated");
            Boolean result_latest = resultSet.getBoolean("latest");
            return new Plot(result_plot_id,result_gameId,result_location1,result_location2,result_owner,isGenerated1,result_latest);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    public static Plot getLatestPlot(){
        try {
            Plot lat;
            ResultSet resultSet = SpBuildRevrited.getInstance().db.getLatestPlot(Game.getCurrentGame().getGameId());
            UUID result_gameId = UUID.fromString(resultSet.getString("game_id"));
            PlotLocation result_location1 = PlotLocation.fromJson(resultSet.getString("location1"));
            PlotLocation result_location2 = PlotLocation.fromJson(resultSet.getString("location2"));
            UUID result_owner = null;
            if (resultSet.getString("owner") != null){
                result_owner = UUID.fromString(resultSet.getString("owner"));
            }
            UUID result_plot_id = UUID.fromString(resultSet.getString("plot_id"));
            Boolean isGenerated1 = resultSet.getBoolean("isGenerated");
            Boolean result_latest = resultSet.getBoolean("latest");
            lat = new Plot(result_plot_id,result_gameId,result_location1,result_location2,result_owner,isGenerated1,result_latest);
            return lat;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public ProtectedRegion getProtectedRegion(){
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(getPos1().getWorld()));
        return regions.getRegion(getPlotId().toString());
    }
    public void update(){
        SpBuildRevrited.getInstance().db.updatePlotInToTablePlots(this);
    }
    public void save(){
        SpBuildRevrited.getInstance().db.InsertPlotInToTablePlots(getGameId(),getPos1(),getPos2(),getPlotId(),isGenerated(),isLatest());
    }
    public void fill(Material material){
        Bukkit.getScheduler().runTaskAsynchronously(SpBuildRevrited.getInstance(),()->{
            try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(getPos1().getWorld()))) {
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionManager regions = container.get(BukkitAdapter.adapt(getPos1().getWorld()));
                CuboidRegion region = new CuboidRegion(BukkitAdapter.adapt(getPos1().getWorld()), BlockVector3.at(getPos1().getX(), getPos1().getY()-1,getPos1().getZ()),BlockVector3.at(getPos2().getX(), getPos2().getY()-1,getPos2().getZ()));
                RandomPattern pattern = new RandomPattern();
                pattern.add(BukkitAdapter.adapt(Material.GREEN_CONCRETE_POWDER.createBlockData()),0.25);
                pattern.add(BukkitAdapter.adapt(Material.GREEN_CONCRETE.createBlockData()),0.25);
                pattern.add(BukkitAdapter.adapt(Material.GRASS_BLOCK.createBlockData()),0.25);
                pattern.add(BukkitAdapter.adapt(Material.MOSS_BLOCK.createBlockData()),0.25);
                editSession.setBlocks(region,pattern);
                boolean hasSchem;
                hasSchem = Game.getCurrentGame().getUseSchema();
                if (hasSchem){
                    try {
                        String currentSchema = Game.getCurrentGame().getShemaName();
                        File myfile = new File(SpBuildRevrited.getInstance().getDataFolder().getAbsolutePath() + "/schems/"+currentSchema);
                        ClipboardFormat format = ClipboardFormats.findByFile(myfile);
                        ClipboardReader reader = format.getReader(new FileInputStream(myfile));
                        Clipboard clipboard = reader.read();
                        int x = (getPos1().getX()-((getPos2().getX()- getPos1().getX())/2))+2;
                        int y = getPos2().getY()+clipboard.getRegion().getHeight()/2;
                        int z = (getPos1().getZ()+((getPos2().getZ()- getPos1().getZ())/2))-1;
                        Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                                .to(BlockVector3.at(-x, y, z)).ignoreAirBlocks(true).build();
                        Operations.complete(operation);
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                }
                CuboidRegion region_walls = new CuboidRegion(BukkitAdapter.adapt(getPos1().getWorld()),BlockVector3.at(getPos1().getX(), getPos2().getY(), getPos1().getZ()),BlockVector3.at(getPos2().getX(), getPos2().getY(), getPos2().getZ()));
                editSession.makeCuboidWalls(region_walls,BukkitAdapter.adapt(material.createBlockData()));
                ProtectedCuboidRegion protectedCuboidRegion = new ProtectedCuboidRegion(getPlotId().toString(), getPos1().setY(-64).getBlockVector3(), getPos2().setY(255).getBlockVector3());
                regions.addRegion(protectedCuboidRegion);
                setGenerated(true);
            } catch (MaxChangedBlocksException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }
    public static void addPlotToEnd(){
        Plot latestPlot = Plot.getLatestPlot();
        Game game = Game.getCurrentGame();
        Location pos1 = new Location(game.getWorld(),0,-60,0);
        Location pos2 = new Location(game.getWorld(),game.getPlotSize(),-60,game.getPlotSize());

        if (latestPlot == null){
            Plot plot = new Plot(game.getGameId(),PlotLocation.fromLocation(pos1),PlotLocation.fromLocation(pos2),null,false,true);
            SpBuildRevrited.getInstance().plotHach.add(plot);
            plot.save();
            //plot.fill(Material.MUD_BRICK_SLAB);
            return;
        }
        Plot plot = new Plot(game.getGameId(),latestPlot.pos1.setY(-60).next(game),latestPlot.pos2.setY(-60).next(game),null,false,true);
        SpBuildRevrited.getInstance().plotHach.add(plot);
        plot.save();
        //plot.fill(Material.MUD_BRICK_SLAB);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plot plot = (Plot) o;
        return Objects.equals(plot_id, plot.plot_id) && Objects.equals(getGameId(), plot.getGameId()) && Objects.equals(getPos1(), plot.getPos1()) && Objects.equals(getPos2(), plot.getPos2()) && Objects.equals(getOwner(), plot.getOwner()) && Objects.equals(latest, plot.latest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plot_id, getGameId(), getPos1(), getPos2(), getOwner(), latest);
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
            List<Plot> res = new ArrayList<>();
            ResultSet resultSet = SpBuildRevrited.getInstance().db.getPlotByID(Game.getCurrentGame().getGameId());
            while (resultSet.next()){
                UUID result_gameId = UUID.fromString(resultSet.getString("game_id"));
                PlotLocation result_location1 = PlotLocation.fromJson(resultSet.getString("location1"));
                PlotLocation result_location2 = PlotLocation.fromJson(resultSet.getString("location2"));
                UUID result_owner = null;
                if (resultSet.getString("owner") != null){
                    result_owner = UUID.fromString(resultSet.getString("owner"));
                }
                UUID result_plot_id = UUID.fromString(resultSet.getString("plot_id"));
                Boolean isGenerated1 = resultSet.getBoolean("isGenerated");
                Boolean result_latest = resultSet.getBoolean("latest");
                Plot p = new Plot(result_plot_id,result_gameId,result_location1,result_location2,result_owner,isGenerated1,result_latest);
                res.add(p);
            }
            return res;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
