package com.otsosity.spbuildrevrited.utils;


import com.otsosity.spbuildrevrited.SpBuildRevrited;
import com.otsosity.spbuildrevrited.utils.dataClasses.Game;
import com.otsosity.spbuildrevrited.utils.dataClasses.PlotPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholderApis extends PlaceholderExpansion {
    private final SpBuildRevrited plugin;
    public PlaceholderApis(SpBuildRevrited plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "spbuild";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Dalynkaa";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("getstagestring")){
            return Game.getCurrentGame().getGameStage().getTranslated();
        }

        if(params.equalsIgnoreCase("getthem")) {
            return Game.getCurrentGame().getThema();
        }
        if(params.equalsIgnoreCase("droper")) {
            return PlotPlayer.fromUUID(player.getUniqueId()).getDroperCount().toString();
        }
        if(params.equalsIgnoreCase("gameType")) {
            return Game.getCurrentGame().getGameType().getTranslated();
        }

        return null;
    }
}
