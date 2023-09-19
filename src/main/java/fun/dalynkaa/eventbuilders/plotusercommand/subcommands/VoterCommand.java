package fun.dalynkaa.eventbuilders.plotusercommand.subcommands;

import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.plotusercommand.SubCommand;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class VoterCommand extends SubCommand {


    @Override
    public String getName() {
        return "voter";
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
        if (args.length<2){
            return;
        }
        Component PREFIX = EventBuilders.getInstance().PREFIX;
        if (Objects.equals(args[2], "set") && args.length == 3){
            try {
                if (PlotPlayer.fromUUID(Bukkit.getPlayerUniqueId(args[0]))==null){
                    player.sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                            .append(Component.text("Игрок не найден ",TextColor.fromCSSHexString("#55efc4"))));
                    return;
                }
                PlotPlayer user = PlotPlayer.fromUUID(Bukkit.getPlayerUniqueId(args[0]));
                user.setVoter(true);
                user.save(false);
                ConsoleCommandSender commandSender = Bukkit.getServer().getConsoleSender();
                String command = "lp user "+user.getName()+" parent add voter";
                Bukkit.dispatchCommand(commandSender, command);
                player.sendMessage(Component.text("Игроку ", TextColor.fromCSSHexString("#a29bfe")).append(Component.text(user.getName()+" выдано право голосовать",TextColor.fromCSSHexString("#6c5ce7"))));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (Objects.equals(args[2], "unset") && args.length == 3){
            try {
                if (PlotPlayer.fromUUID(Bukkit.getPlayerUniqueId(args[0]))==null){
                    player.sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                            .append(Component.text("Игрок не найден ",TextColor.fromCSSHexString("#55efc4"))));
                    return;
                }
                PlotPlayer user = PlotPlayer.fromUUID(Bukkit.getPlayerUniqueId(args[0]));
                user.setVoter(false);
                user.save(false);
                ConsoleCommandSender commandSender = Bukkit.getServer().getConsoleSender();
                String command = "lp user "+user.getName()+" parent remove voter";
                Bukkit.dispatchCommand(commandSender, command);
                player.sendMessage(Component.text("Игроку ", TextColor.fromCSSHexString("#a29bfe")).append(Component.text(user.getName()+" убрано право голосовать",TextColor.fromCSSHexString("#6c5ce7"))));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (Objects.equals(args[2], "list") && args.length == 3){
            try {
                if (PlotPlayer.fromUUID(Bukkit.getPlayerUniqueId(args[0]))==null){
                    player.sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                            .append(Component.text("Игрок не найден ",TextColor.fromCSSHexString("#55efc4"))));
                    return;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        if (args.length == 3){
            return Arrays.asList("set","unset","list");
        }
        return null;
    }
}
