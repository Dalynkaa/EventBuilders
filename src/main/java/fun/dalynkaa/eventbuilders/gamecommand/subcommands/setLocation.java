package fun.dalynkaa.eventbuilders.gamecommand.subcommands;

import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.gamecommand.GameSubCommand;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class setLocation extends GameSubCommand {

    @Override
    public String getName() {
        return "setLocation";
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
        PlotPlayer plotPlayer = PlotPlayer.fromUUID(player.getUniqueId());
        if (!plotPlayer.isAdmin()){
            return;
        }
        Component PREFIX = EventBuilders.getInstance().PREFIX;

        if (args[1].equals("спавн")){
            player.sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                    .append(Component.text("Локация спавна установленна ",TextColor.fromCSSHexString("#55efc4"))));
            EventBuilders.getInstance().config.setSpawnLocation(player.getLocation());
        } else if (args[1].equals("концовка")) {
            player.sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                    .append(Component.text("Локация концовки установленна ",TextColor.fromCSSHexString("#55efc4"))));
            EventBuilders.getInstance().config.setEndingLocation(player.getLocation());
        }else {
            player.sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                    .append(Component.text("Локация не найдена ",TextColor.fromCSSHexString("#ef0002"))));
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        if (args.length == 2){
            return Arrays.asList("спавн","концовка");
        }
        return null;
    }
}
