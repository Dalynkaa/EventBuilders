package fun.dalynkaa.eventbuilders.utils;

import fun.dalynkaa.eventbuilders.EventBuilders;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ConfigUtils {
    EventBuilders main;
    public ConfigUtils(EventBuilders main){
        this.main = main;
    }
    public Integer getCurrentTime(){
        return main.getConfig().getInt("timers.current_time", 0);
    }
    public void setCurrenttime(Integer integer){
        main.getConfig().set("timers.current_time", integer);
        main.saveConfig();
    }
    public void resetTimer(){
        main.getConfig().set("timers.current_time", 0);
        main.saveConfig();
    }
    public void setSpawnLocation(Location spawn){
        main.getConfig().set("locations.spawn", spawn);
        main.saveConfig();
    }
    public Location getSpawnLocation(){
        return main.getConfig().getLocation("locations.spawn", new Location(Bukkit.getWorld("world"), 0, 100, 0));
    }
    public void setEndingLocation(Location location){
        main.getConfig().set("locations.ending", location);
        main.saveConfig();
    }
    public Location getEndingLocation(){
        return main.getConfig().getLocation("locations.ending", new Location(Bukkit.getWorld("world"), 0, 100, 0));
    }
}
