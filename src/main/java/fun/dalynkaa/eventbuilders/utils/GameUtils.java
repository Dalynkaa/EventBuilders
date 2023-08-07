package com.otsosity.spbuildrevrited.utils;

import net.kyori.adventure.text.Component;
import com.destroystokyo.paper.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GameUtils {

    public static void broatcastActionBar(Component component){
        for (Player p: Bukkit.getOnlinePlayers()){
            p.sendActionBar(component);
        }
    }
    public static void broatcastTitle(Title title){
        for (Player p: Bukkit.getOnlinePlayers()){
            p.sendTitle(title);
        }
    }
}
