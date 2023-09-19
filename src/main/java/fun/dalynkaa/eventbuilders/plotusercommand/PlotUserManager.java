package fun.dalynkaa.eventbuilders.plotusercommand;


import fun.dalynkaa.eventbuilders.plotusercommand.subcommands.AdminCommand;
import fun.dalynkaa.eventbuilders.plotusercommand.subcommands.VoterCommand;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlotUserManager implements TabExecutor {

    private ArrayList<SubCommand> subcommands = new ArrayList<>();

    public PlotUserManager(){
        subcommands.add(new VoterCommand());
        subcommands.add(new AdminCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            PlotPlayer plotPlayer = PlotPlayer.fromUUID(((Player) sender).getUniqueId());
            if (!plotPlayer.isAdmin()){
                return false;
            }
            Player p = (Player) sender;
            if (args.length > 0){
                for (int i = 0; i < getSubCommands().size(); i++){
                    if (args[1].equalsIgnoreCase(getSubCommands().get(i).getName())){
                        getSubCommands().get(i).perform(p, args);
                    }
                }
            }

        }


        return true;
    }

    public ArrayList<SubCommand> getSubCommands(){
        return subcommands;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1){ //prank <subcommand> <args>
            ArrayList<String> subcommandsArguments1 = new ArrayList<>();
            for (PlotPlayer user: PlotPlayer.getAllPlayers()){
                if (user.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    subcommandsArguments1.add(user.getName());
            }
            return subcommandsArguments1;
        } else if (args.length == 2) {
            ArrayList<String> subcommandsArguments = new ArrayList<>();
            for (int i = 0; i < getSubCommands().size(); i++){
                subcommandsArguments.add(getSubCommands().get(i).getName());
            }
            return subcommandsArguments;
        } else if(args.length >= 3){
            for (int i = 0; i < getSubCommands().size(); i++){
                if (args[1].equalsIgnoreCase(getSubCommands().get(i).getName())){
                    return getSubCommands().get(i).getSubcommandArguments((Player) sender, args);
                }
            }
        }

        return null;
    }
}
