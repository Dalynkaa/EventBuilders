package fun.dalynkaa.eventbuilders.gamecommand.subcommands;

import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.gamecommand.GameSubCommand;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class unbanPlayer extends GameSubCommand {

    @Override
    public String getName() {
        return "unban";
    }

    @Override
    public String getDescription() {
        return "unban player";
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public void perform(Player player, String[] args) {
        PlotPlayer plotPlayer = PlotPlayer.fromUUID(player.getUniqueId());
        Component PREFIX = EventBuilders.getInstance().PREFIX;
        if (!plotPlayer.isAdmin()){
            return;
        }
        if (args.length <=1){
            return;
        }
        Bukkit.getLogger().info("0.5");
        PlotPlayer tpPlayer = PlotPlayer.fromUUID(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
        if (tpPlayer!=null){
            Bukkit.getLogger().info("1");
            tpPlayer.setHasBan(false);
            tpPlayer.setCanJoin(true);
            tpPlayer.save(false);
            plotPlayer.getPlayer().sendMessage(PREFIX.append(Component.text("Пользователь "+tpPlayer.getName()+" разбанен")));
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        if (args.length == 2){
            ArrayList<String> subcommandsArguments1 = new ArrayList<>();
            for (PlotPlayer user: PlotPlayer.getBannedPlotPlayers()){
                if (user.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                    subcommandsArguments1.add(user.getName());
            }
            return subcommandsArguments1;
        }
        return null;
    }
}
