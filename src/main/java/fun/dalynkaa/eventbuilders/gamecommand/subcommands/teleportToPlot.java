package fun.dalynkaa.eventbuilders.gamecommand.subcommands;

import fun.dalynkaa.eventbuilders.gamecommand.GameSubCommand;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.BuildPlot;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class teleportToPlot extends GameSubCommand {

    @Override
    public String getName() {
        return "teleport";
    }

    @Override
    public String getDescription() {
        return "teleport to player current region";
    }

    @Override
    public String getSyntax() {
        return "/game teleport <player>";
    }

    @Override
    public void perform(Player player, String[] args) {
        PlotPlayer plotPlayer = PlotPlayer.fromUUID(player.getUniqueId());
        if (!plotPlayer.isAdmin()){
            return;
        }
        if (args.length <=1){
            return;
        }
        PlotPlayer tpPlayer = PlotPlayer.fromUUID(Bukkit.getPlayerUniqueId(args[1]));

        if (tpPlayer!=null){
            if (tpPlayer.getCurrentPlot()!=null){
                plotPlayer.getPlayer().teleport(tpPlayer.getCurrentPlot().getPlotCenter().getLocation());
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        if (args.length == 2){
            List<String> result = new ArrayList<>();
            for (PlotPlayer plotPlayer: PlotPlayer.getOnlinePlotPlayers(true)){
                result.add(plotPlayer.getName());
            }
            return result;
        }
        return null;
    }
}
