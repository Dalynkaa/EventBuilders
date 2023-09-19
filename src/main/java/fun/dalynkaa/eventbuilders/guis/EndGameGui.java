package fun.dalynkaa.eventbuilders.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.VoteFilter;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotVote;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.*;

public class EndGameGui {
    PaginatedGui gui;
    public EndGameGui(VoteFilter filter, Game game, PlotPlayer plotPlayer){
        gui = Gui.paginated()
                .title(Component.text("Список игроков"))
                .rows(6)
                .pageSize(36)
                .disableAllInteractions()
                .create();
        List<Plot> plots;
        HashMap<UUID,ArrayList<PlotVote>> plotListHashMap;
        plots = Plot.getAllPlots(game.getGameId(), true, filter, plotPlayer);
        plotListHashMap = PlotVote.getGameVotes(game);

        List<Plot> forSort = new ArrayList<>();

        for (Plot plot: plots){
            if (plotListHashMap.containsKey(plot.getPlotId())) {
                List<PlotVote> votes = plotListHashMap.get(plot.getPlotId());
                Integer summ = 0;
                for (PlotVote plotVote : votes) {
                    summ += plotVote.getType();
                }
                forSort.add(plot.setPointSum(summ));
            }
        }
        forSort.sort(Comparator.comparingDouble(Plot::getPointSum).reversed());

        for (Plot plot: forSort){
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("ПКМ - ", TextColor.fromCSSHexString("#a29bfe")).append(Component.text("телепорт", TextColor.fromCSSHexString("#6c5ce7"))));
            lore.add(Component.text("ЛКМ - ", TextColor.fromCSSHexString("#a29bfe")).append(Component.text("телепорт игрока сюда", TextColor.fromCSSHexString("#6c5ce7"))));
            if (plotListHashMap.containsKey(plot.getPlotId())){
                List<PlotVote> votes = plotListHashMap.get(plot.getPlotId());
                for (PlotVote plotVote: votes){
                    lore.add(Component.text(plotVote.getVoter().getName() + " - ", TextColor.fromCSSHexString("#a29bfe")).append(Component.text(plotVote.getType(), TextColor.fromCSSHexString("#6c5ce7"))));
                }
                lore.add(Component.text("Сумма - ", TextColor.fromCSSHexString("#fab1a0")).append(Component.text(plot.getPointSum(), TextColor.fromCSSHexString("#e17055"))));
            }
            GuiItem item = ItemBuilder.skull()
                    .owner(plot.getPlotPlayer().getOfflinePlayer())
                    .name(Component.text("Плот - ", TextColor.fromCSSHexString("#BA68C8"))
                            .append(Component.text(plot.getPlotPlayer().getOfflinePlayer().getName(), TextColor.fromCSSHexString("#8E24AA"))))
                    .lore(lore)
                    .asGuiItem((event -> {
                        if (event.isRightClick()){
                            Player player = (Player) event.getWhoClicked();
                            player.teleport(plot.getPlotCenter().getLocation());
                            player.setGameMode(GameMode.CREATIVE);
                            event.getInventory().close();
                        } else if (event.isLeftClick()) {
                            PlotPlayer player = plot.getPlotPlayer();
                            player.getPlayer().teleport(event.getWhoClicked().getLocation());
                        }
                    }));
            gui.addItem(item);
        }
        gui.setItem(Arrays.asList(36,37,38,39,40,41,42,43,44), ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).asGuiItem());
        gui.setItem(45, ItemBuilder
                .skull()
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWZhNGM4MjcxMDgzNzQ4MGRmNTc1Y2EwZDY0Y2VmMmZjZGFkYWVjZTcwOTFiNzA3NmI5MjNjNjdlNWY0ZTg0OSJ9fX0=")
                .name(Component.text("Преведущая страница"))
                .asGuiItem(event -> gui.previous()));
        gui.setItem(49, ItemBuilder
                .from(Material.PAPER)
                .name(Component.text("Фильтр"))
                .lore(Component.text("Текущий фильтр - ",TextColor.fromCSSHexString("#a29bfe")).append(Component.text(filter.getName(),TextColor.fromCSSHexString("#6c5ce7"))))
                .asGuiItem(event -> {
                    event.getInventory().close();
                    VoteListGui voteListGui = new VoteListGui(filter.next(), game, plotPlayer);
                    voteListGui.open(event.getWhoClicked());
                }));
        gui.setItem(53, ItemBuilder
                .skull()
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjM5NTExOWRkNTIwMWEyNDJiODZiNDg2NmQ2ZjA0NTQxYjAwYjkyZWJkZDU3Y2UyNzkxOWZiNWYxMDJhNmRkZCJ9fX0=")
                .name(Component.text("Следуйщая страница"))
                .asGuiItem(event -> gui.next()));

    }
    public void open(HumanEntity p){
        gui.open(p);
    }
}
