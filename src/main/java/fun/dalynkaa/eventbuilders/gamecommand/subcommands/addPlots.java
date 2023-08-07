package fun.dalynkaa.eventbuilders.gamecommand.subcommands;


import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.gamecommand.GameSubCommand;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Plot;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class createPlot extends GameSubCommand {
    public long start;
    @Override
    public String getName() {
        return "plot";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public void perform(Player player, String[] args) {
        PlotPlayer plotPlayer = PlotPlayer.fromUUID(player.getUniqueId());
        if (!plotPlayer.isAdmin()){
            return;
        }
        if (args.length <=1){
            Plot.addPlotToEnd();
            return;
        }
        int arg = Integer.parseInt(args[1]);
        start = System.currentTimeMillis();
        Bukkit.getScheduler().runTaskAsynchronously(EventBuilders.getInstance(),()->{
            for (int i = 0; i<=arg; i++){
                Plot.addPlotToEnd();
            }
            Bukkit.getScheduler().runTask(EventBuilders.getInstance(),this::SynkTask);
        });
    }


    public void SynkTask(){
        long end = System.currentTimeMillis();
        Integer res = (int) end - (int) start;
        Bukkit.broadcastMessage(String.valueOf(res) + " time");
        Bukkit.broadcastMessage(String.valueOf(EventBuilders.getInstance().plotHach.size())+" size");
        Plot.addPlotToEnd();
        for (Plot plot: Plot.getAllPlots(Game.getCurrentGame().getGameId())){
            plot.fill();
            plot.update();
        }

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
