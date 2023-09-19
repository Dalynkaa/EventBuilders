package fun.dalynkaa.eventbuilders.gamecommand.subcommands;


import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.gamecommand.GameSubCommand;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.BuildPlot;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class addPlots extends GameSubCommand {
    public long start;
    Component PREFIX = EventBuilders.getInstance().PREFIX;
    @Override
    public String getName() {
        return "addPlots";
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
            BuildPlot.addPlotToEnd();
            return;
        }
        int arg;
        try {
             arg = Integer.parseInt(args[1]);
        }catch (NumberFormatException e){
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(EventBuilders.getInstance(),()->{
            for (int i = 0; i<=arg; i++){
                BuildPlot.addPlotToEnd();
            }
            Bukkit.getScheduler().runTask(EventBuilders.getInstance(),()->SynkTask(player, arg));
        });
    }


    public void SynkTask(Player player,int count){
        for (Plot plot: Plot.getAllPlots(Game.getCurrentGame().getGameId(), false, false)){
            if (plot instanceof BuildPlot buildPlot){
                buildPlot.fill(true, true, true);
                buildPlot.setGenerated(true);
                buildPlot.update();
            }
        }
        player.sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                .append(Component.text("Создано ",TextColor.fromCSSHexString("#55efc4")))
                .append(Component.text(String.valueOf(count),TextColor.fromCSSHexString("#00b894")))
                .append(Component.text(" плотов!", TextColor.fromCSSHexString("#55efc4"))));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        if (args.length == 2){
            return Arrays.asList("Количество плотов");
        }
        return null;
    }
}
