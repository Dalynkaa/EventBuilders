package fun.dalynkaa.eventbuilders.utils.dataClasses;

import com.google.gson.Gson;
import com.sk89q.worldedit.math.BlockVector3;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

public class PlotLocation {
    private Integer x;
    private Integer y;
    private Integer z;
    private final String world;

    public PlotLocation(Integer x, Integer y, Integer z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public PlotLocation setX(Integer x) {
        this.x = x;
        return this;
    }
    public PlotLocation addX(Integer x1){
        this.x = this.x+x1;
        return this;
    }
    public PlotLocation addY(Integer y1){
        this.y = this.y+y1;
        return this;
    }
    public PlotLocation addZ(Integer z1){
        this.z = this.z+z1;
        return this;
    }


    public PlotLocation setY(Integer y) {
        this.y = y;
        return this;
    }

    public PlotLocation setZ(Integer z) {
        this.z = z;
        return this;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    public String getWorldName() {
        return world;
    }
    public World getWorld(){
        return Bukkit.getWorld(getWorldName());
    }
    public Location getLocation(){
        return new Location(getWorld(),getX(),getY(),getZ());
    }
    public BlockVector3 getBlockVector3(){
        return BlockVector3.at(getX(),getY(),getZ());
    }
    public PlotLocation next(Game game){
        this.x += game.getPlotSize()+4;
        return this;
    }
    public static PlotLocation fromLocation(Location location){
        return new PlotLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName());
    }
    public String toJson(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
    public static PlotLocation fromJson(String data){
        Gson gson = new Gson();
        PlotLocation json = gson.fromJson(data, PlotLocation.class);
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlotLocation that = (PlotLocation) o;
        return Objects.equals(getX(), that.getX()) && Objects.equals(getY(), that.getY()) && Objects.equals(getZ(), that.getZ()) && Objects.equals(getWorld(), that.getWorld());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getZ(), getWorld());
    }

    @Override
    public String toString() {
        return "PlotLocation{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", world='" + world + '\'' +
                '}';
    }
}
