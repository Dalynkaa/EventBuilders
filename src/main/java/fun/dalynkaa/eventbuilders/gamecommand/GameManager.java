package fun.dalynkaa.eventbuilders.gamecommand;


import fun.dalynkaa.eventbuilders.gamecommand.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameManager implements TabExecutor {

    private final ArrayList<GameSubCommand> subcommands = new ArrayList<>();

    public GameManager(){
        subcommands.add(new addPlots());
        subcommands.add(new setLocation());
        subcommands.add(new openEndGame());
        subcommands.add(new teleportToPlot());
        subcommands.add(new unbanPlayer());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (sender instanceof Player){
            Player p = (Player) sender;

            if (args.length > 0){
                for (int i = 0; i < getSubCommands().size(); i++){
                    if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())){
                        getSubCommands().get(i).perform(p, args);
                    }
                }
            }
        }
        return true;
    }

    public ArrayList<GameSubCommand> getSubCommands(){
        return subcommands;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1){ //prank <subcommand> <args>
            ArrayList<String> subcommandsArguments = new ArrayList<>();
            for (int i = 0; i < getSubCommands().size(); i++){
                subcommandsArguments.add(getSubCommands().get(i).getName());
            }

            return subcommandsArguments;
        }else if(args.length >= 2){
            for (int i = 0; i < getSubCommands().size(); i++){
                if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())){
                    return getSubCommands().get(i).getSubcommandArguments((Player) sender, args);
                }
            }
        }
        return null;
    }
}
