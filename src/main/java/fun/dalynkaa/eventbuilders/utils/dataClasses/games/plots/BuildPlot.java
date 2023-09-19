package fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.DonateSkin;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotLocation;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.UUID;

public class BuildPlot extends Plot{
    private boolean buildLatest;
    public BuildPlot(UUID gameId, Integer plotPosition, Integer stageId, PlotLocation pos1, PlotLocation pos2, PlotPlayer owner, Boolean isGenerated, Boolean latest) {
        super(gameId, plotPosition, stageId, pos1, pos2, owner, isGenerated, latest);
    }

    public BuildPlot(UUID plot_id, Integer plotPosition, Integer stageId, UUID gameId, PlotLocation pos1, PlotLocation pos2, PlotPlayer owner, Boolean isGenerated, Boolean latest) {
        super(plot_id, plotPosition, stageId, gameId, pos1, pos2, owner, isGenerated, latest);
    }
    public void fill(boolean fillCorners, boolean fillBottom, boolean fillWalls){
        if (fillBottom){
            setBottom(DonateSkin.DEFAULT.getMaterials());
        }
        if (fillCorners){
            setBorders(DonateSkin.DEFAULT.getCornerDefault());
        }
        if (fillWalls){
            setWalls(Material.STRUCTURE_VOID);
        }
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(getPos1().getWorld()));
        ProtectedCuboidRegion protectedCuboidRegion = new ProtectedCuboidRegion(getPlotId().toString(), getPos1().setY(-64).getBlockVector3(), getPos2().setY(255).getBlockVector3());
        protectedCuboidRegion.setFlag(Flags.ENTRY, StateFlag.State.DENY);
        protectedCuboidRegion.setFlag(Flags.TNT, StateFlag.State.DENY);
        protectedCuboidRegion.setFlag(Flags.EXP_DROPS, StateFlag.State.DENY);
        protectedCuboidRegion.setFlag(Flags.POTION_SPLASH, StateFlag.State.DENY);
        protectedCuboidRegion.setFlag(Flags.CHORUS_TELEPORT, StateFlag.State.DENY);
        protectedCuboidRegion.setFlag(Flags.MOB_SPAWNING, StateFlag.State.DENY);
        protectedCuboidRegion.setFlag(Flags.CREEPER_EXPLOSION, StateFlag.State.DENY);
        protectedCuboidRegion.setFlag(Flags.LAVA_FLOW, StateFlag.State.DENY);
        protectedCuboidRegion.setFlag(Flags.OTHER_EXPLOSION, StateFlag.State.DENY);
        protectedCuboidRegion.setFlag(Flags.RESPAWN_ANCHORS, StateFlag.State.DENY);
        protectedCuboidRegion.setFlag(Flags.EXIT, StateFlag.State.DENY);
        protectedCuboidRegion.setFlag(Flags.EXIT.getRegionGroupFlag(), RegionGroup.MEMBERS);
        regions.addRegion(protectedCuboidRegion);
        setGenerated(true);
    }
    public static void addPlotToEnd(){
        Integer Y = fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game.getCurrentGame().getGameType().getY();
        Plot latestPlot = Plot.getLatestPlot();
        fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game game = Game.getCurrentGame();
        Location pos1 = new Location(game.getWorld(),0,Y,0);
        Location pos2 = new Location(game.getWorld(),game.getPlotSize(),Y,game.getPlotSize());

        if (latestPlot == null){
            Plot plot = new Plot(game.getGameId(), 1, 1,PlotLocation.fromLocation(pos1),PlotLocation.fromLocation(pos2),null,false,true);
            plot.setLatest(true);
            EventBuilders.getInstance().plotHach.add(plot);
            plot.save();
            //plot.fill(Material.MUD_BRICK_SLAB);
            return;
        }
        Plot plot = new Plot(game.getGameId(), latestPlot.getPlotPosition()+1, 1, latestPlot.getPos1().setY(Y).next(game),latestPlot.getPos2().setY(Y).next(game),null,false,true);
        plot.setLatest(true);
        EventBuilders.getInstance().plotHach.add(plot);
        plot.save();
        //plot.fill(Material.MUD_BRICK_SLAB);
    }


}
