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
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotLocation;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.UUID;

public class SkinPlot extends Plot{
    public SkinPlot(UUID gameId, Integer plotPosition, Integer stageId, PlotLocation pos1, PlotLocation pos2, PlotPlayer owner, Boolean isGenerated, Boolean latest) {
        super(gameId, plotPosition, stageId, pos1, pos2, owner, isGenerated, latest);
    }

    public SkinPlot(UUID plot_id, Integer plotPosition, Integer stageId, UUID gameId, PlotLocation pos1, PlotLocation pos2, PlotPlayer owner, Boolean isGenerated, Boolean latest) {
        super(plot_id, plotPosition, stageId, gameId, pos1, pos2, owner, isGenerated, latest);
    }
    public static Plot addPlotToEnd(Integer playerStage){
        Integer Y = -61;
        Plot latestPlot = Plot.getLatestPlot(playerStage);
        Game game = Game.getCurrentGame();
        Location pos1 = new Location(game.getWorld(),0,Y+(playerStage-1), (5 * playerStage));
        Location pos2 = new Location(game.getWorld(),game.getPlotSize(),Y+(playerStage-1),1+(5*playerStage));

        if (latestPlot == null){
            Plot plot = new SkinPlot(game.getGameId(), 1, playerStage,PlotLocation.fromLocation(pos1),PlotLocation.fromLocation(pos2),null,false,true);
            plot.setLatest(true);
            EventBuilders.getInstance().plotHach.add(plot);
            plot.save();
            //plot.fill(Material.MUD_BRICK_SLAB);
            return plot;
        }
        Plot plot = new SkinPlot(game.getGameId(), 1 ,playerStage , latestPlot.getPos1().setY(Y+(playerStage-1)).next(game),latestPlot.getPos2().setY(Y+(playerStage-1)).next(game),null,false,true);
        plot.setLatest(true);
        EventBuilders.getInstance().plotHach.add(plot);
        plot.save();
        //plot.fill(Material.MUD_BRICK_SLAB);
        return plot;
    }
    public void skinFill(boolean green){
        if (green){
            setBottom(Arrays.asList(Material.LIME_STAINED_GLASS, Material.LIME_STAINED_GLASS, Material.LIME_STAINED_GLASS, Material.LIME_STAINED_GLASS));
        } else{
            setBottom(Arrays.asList(Material.RED_STAINED_GLASS, Material.RED_STAINED_GLASS, Material.RED_STAINED_GLASS, Material.RED_STAINED_GLASS));
        }
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(getPos1().getWorld()));
        ProtectedCuboidRegion protectedCuboidRegion = new ProtectedCuboidRegion(getPlotId().toString(), getPos1().setY(-64).addX(0).addZ(1).getBlockVector3(), getPos2().setY(255).addZ(-1).getBlockVector3());
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
}
