package com.otsosity.spbuildrevrited.utils.dataClasses;


import com.otsosity.spbuildrevrited.SpBuildRevrited;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class PlotPlayer {
    private UUID uuid;
    private String name;
    private Boolean in_game;
    private Boolean voter;
    private Integer droper_count;
    private UUID current_plot;
    private Timestamp creation_date;


    public PlotPlayer(UUID uuid, String name, Boolean inGame, Boolean voter, Integer droperCount, UUID currentPlot, Timestamp creationDate) {
        this.uuid = uuid;
        this.name = name;
        this.in_game = inGame;
        this.voter = voter;
        this.droper_count = droperCount;
        this.current_plot = currentPlot;
        this.creation_date = creationDate;
    }
    public PlotPlayer(UUID uuid, String name, Boolean inGame, Boolean voter, Integer droperCount, Timestamp creationDate) {
        this.uuid = uuid;
        this.name = name;
        this.in_game = inGame;
        this.voter = voter;
        this.droper_count = droperCount;
        this.current_plot = null;
        this.creation_date = creationDate;
    }
    public PlotPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.in_game = null;
        this.voter = null;
        this.droper_count = null;
        this.current_plot = null;
        this.creation_date = null;
    }

    public PlotPlayer setUuid(UUID uuid) {
        this.uuid = uuid;
        save(false);
        return this;
    }

    public PlotPlayer setName(String name) {
        this.name = name;
        save(false);
        return this;
    }

    public PlotPlayer setInGame(Boolean in_game) {
        this.in_game = in_game;
        save(false);
        return this;
    }

    public PlotPlayer setVoter(Boolean voter) {
        this.voter = voter;
        save(false);
        return this;
    }

    public PlotPlayer setDroperCount(Integer droper_count) {
        this.droper_count = droper_count;
        save(false);
        return this;
    }

    public PlotPlayer setCurrentPlot(UUID current_plot) {
        this.current_plot = current_plot;
        save(false);
        return this;
    }

    public PlotPlayer setCreationDate(Timestamp creation_date) {
        this.creation_date = creation_date;
        save(false);
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

    public UUID getCurrentPlotId() {
        return current_plot;
    }
    public Plot getCurrentPlot() {
        if (current_plot==null){
            return null;
        }
        return Plot.getPlotById(getCurrentPlotId());
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


    public static PlotPlayer fromUUID(UUID playerUUID) {
        ResultSet resultSet = SpBuildRevrited.getInstance().db.getUserByUUID(playerUUID);
        try {
            String result_name = resultSet.getString("name");
            Boolean result_InGame = resultSet.getBoolean("in_game");
            Boolean result_Voter = resultSet.getBoolean("voter");
            Integer result_droper_count = resultSet.getInt("droper_count");
            String result_curent_plot = resultSet.getString("current_plot");
            Timestamp result_creation_date = resultSet.getTimestamp("creation_date");
            if (result_curent_plot == null) {
                return new PlotPlayer(playerUUID, result_name, result_InGame, result_Voter, result_droper_count, result_creation_date);
            }else {
                return new PlotPlayer(playerUUID, result_name, result_InGame, result_Voter, result_droper_count,UUID.fromString(result_curent_plot), result_creation_date);
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
    public void save(boolean update){
        if (update) {
            SpBuildRevrited.getInstance().db.InsertUserInToTableUsers(getName(),getUuid().toString());
            ResultSet resultSet = SpBuildRevrited.getInstance().db.getUserByUUID(getUuid());
            try {
                setName(resultSet.getString("name"));
                setInGame(resultSet.getBoolean("in_game"));
                setVoter(resultSet.getBoolean("voter"));
                setDroperCount(resultSet.getInt("droper_count"));
                if (resultSet.getString("current_plot") == null) {
                    setCurrentPlot(null);
                } else {
                    setCurrentPlot(UUID.fromString(resultSet.getString("current_plot")));
                }
                setCreationDate(resultSet.getTimestamp("creation_date"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            SpBuildRevrited.getInstance().db.updateUserInToTableUsers(this);
        }
    }
    public void save(){
        SpBuildRevrited.getInstance().db.InsertUserInToTableUsers(getName(),getUuid().toString());
        ResultSet resultSet = SpBuildRevrited.getInstance().db.getUserByUUID(getUuid());
        try {
            setName(resultSet.getString("name"));
            setInGame(resultSet.getBoolean("in_game"));
            setVoter(resultSet.getBoolean("voter"));
            setDroperCount(resultSet.getInt("droper_count"));
            if (resultSet.getString("current_plot") == null){
                setCurrentPlot(null);
            }else {
                setCurrentPlot(UUID.fromString(resultSet.getString("current_plot")));
            }
            setCreationDate(resultSet.getTimestamp("creation_date"));
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

}
