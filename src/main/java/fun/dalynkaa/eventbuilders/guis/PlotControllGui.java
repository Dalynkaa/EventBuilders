package fun.dalynkaa.eventbuilders.guis;

import com.destroystokyo.paper.Title;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.guis.acceptGui.AcceptGui;
import fun.dalynkaa.eventbuilders.utils.GameUtils;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.HumanEntity;

public class PlotControllGui {
    Gui gui;
    public PlotControllGui(Game game, Plot plot){
        Component text = Component.text("", TextColor.fromCSSHexString("#00b894"));
        Component PREFIX = EventBuilders.getInstance().PREFIX;
        if (plot.getPlotPlayer() == null){
            text.append(Component.text("Неизвестно!"));
        }else {
            text.append(Component.text(plot.getPlotPlayer().getName()));
        }
        gui = Gui.gui()
                .title(Component.text("Меню плота - ").append(text))
                .rows(1)
                .disableAllInteractions().create();

        GuiItem clear = ItemBuilder.skull()
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2YwYTI5ZDk0YTdjYTUxODcwYWQ5ZTA3YTJkZTFmZmIwNjYyZmQ4ZDhjOTk1N2MwNGI0ZmFiYWU1NjNjMGM0OCJ9fX0=")
                .name(Component.text("Очистить плот"))
                .lore(Component.text("Возвращает плот в исходное состояние"))
                .asGuiItem(inventoryClickEvent -> {
                    plot.clearPlot();
                });
        GuiItem removePlayer = ItemBuilder.skull()
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJjYmRjOWQ0YzU5MGVhYzI4NWE0NTQ0ZjJiMWUwNjhiZDI3ZmQ1MjE3M2FjOGQ3Njc5MDEzODIzY2JhYjk1YSJ9fX0=")
                .name(Component.text("Исключить игрока"))
                .lore(Component.text("Кикает игрока из игры запрещая заходить вновь в игру"))
                .asGuiItem(inventoryClickEvent -> {
                    if (plot.getPlotPlayer()==null){
                        return;
                    }
                    new AcceptGui("Кикнуть челвека с игры", inventoryClickEvent.getWhoClicked(),
                            event->{
                                event.getInventory().close();
                                PlotPlayer plotPlayer = plot.getPlotPlayer();
                                plotPlayer.setInGame(false);
                                plotPlayer.setCurrentPlotId(null);
                                plotPlayer.setCanJoin(false);
                                plot.setOwner(null);
                                plot.clearPlot();
                                plotPlayer.save(false);
                                plot.update();
                                if (plotPlayer.getPlayer() != null){
                                    plotPlayer.getPlayer().kick();
                                }
                                inventoryClickEvent.getWhoClicked().sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                                        .append(Component.text("Успешно",TextColor.fromCSSHexString("#55efc4"))));
                            },
                            event -> event.getInventory().close());

                });
        GuiItem banPlayer = ItemBuilder.skull()
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJjYmRjOWQ0YzU5MGVhYzI4NWE0NTQ0ZjJiMWUwNjhiZDI3ZmQ1MjE3M2FjOGQ3Njc5MDEzODIzY2JhYjk1YSJ9fX0=")
                .name(Component.text("Забанить игрока"))
                .lore(Component.text("Банит игрока запрещая заходить на совсем"))
                .asGuiItem(inventoryClickEvent -> {
                    if (plot.getPlotPlayer()==null){
                        return;
                    }
                    new AcceptGui("Забанить человека?", inventoryClickEvent.getWhoClicked(),
                            event->{
                                event.getInventory().close();
                                PlotPlayer plotPlayer = plot.getPlotPlayer();
                                plotPlayer.setInGame(false);
                                plotPlayer.setCurrentPlotId(null);
                                plotPlayer.setCanJoin(false);
                                plotPlayer.setHasBan(true);
                                plot.setOwner(null);
                                plot.clearPlot();
                                plotPlayer.save(false);
                                plot.update();
                                if (plotPlayer.getPlayer() != null){
                                    plotPlayer.getPlayer().kick();
                                }
                                inventoryClickEvent.getWhoClicked().sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                                        .append(Component.text("Успешно",TextColor.fromCSSHexString("#55efc4"))));
                                GameUtils.broatcastTitle(Title.builder().title("Игрок " +plotPlayer.getName()+" забанен!").build());
                            },
                            event -> event.getInventory().close());

                });
        gui.setItem(0, clear);
        gui.setItem(1,removePlayer);
        gui.setItem(2, banPlayer);
    }
    public void open(HumanEntity player){
        gui.open(player);
    }
}
