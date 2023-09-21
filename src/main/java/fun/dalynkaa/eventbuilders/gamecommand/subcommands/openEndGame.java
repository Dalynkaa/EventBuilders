package fun.dalynkaa.eventbuilders.gamecommand.subcommands;

import fun.dalynkaa.eventbuilders.gamecommand.GameSubCommand;
import fun.dalynkaa.eventbuilders.guis.PlotListGui;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.VoteFilter;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class openEndGame extends GameSubCommand {
    @Override
    public String getName() {
        return "endGame";
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
        if (plotPlayer.isVoter() || plotPlayer.isAdmin()){
            new PlotListGui(VoteFilter.VOTED, Game.getCurrentGame(), plotPlayer, PlotListGui.GuiType.END_GAME).open(player.getPlayer());
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
