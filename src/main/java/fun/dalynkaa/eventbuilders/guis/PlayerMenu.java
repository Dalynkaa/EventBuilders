package fun.dalynkaa.eventbuilders.guis;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameStage;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.VoteFilter;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class PlayerMenu {
    Gui gui;
    public PlayerMenu(Game game){
        gui = Gui.gui()
                .title(Component.text(""))
                .rows(3)
                .disableAllInteractions().create();
        GuiItem teleport = ItemBuilder.from(CustomStack.getInstance("dalynkaa:return_button").getItemStack())
                .name(Component.text("Телепорт на свой участок" , TextColor.fromCSSHexString("#e17055")))
                .lore(Arrays.asList(
                        Component.text("Телепортирует на свой участок если ты перезашел",TextColor.fromCSSHexString("#6c5ce7"))
                )).asGuiItem((event -> {
                    Component PREFIX = EventBuilders.getInstance().PREFIX;
                    PlotPlayer plotPlayer = PlotPlayer.fromUUID(event.getWhoClicked().getUniqueId());
                    if (plotPlayer.getCurrentPlotId() == null || !game.getGameStage().equals(GameStage.NO_GAME)){
                        plotPlayer.getPlayer().sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                                .append(Component.text("Ебать братуля ахуел? Тебе негде строить!",TextColor.fromCSSHexString("#55efc4"))));
                    }
                    if (plotPlayer.getCurrentPlot() != null){
                        plotPlayer.getPlayer().teleport(plotPlayer.getCurrentPlot().getPlotCenter().getLocation());
                        if (game.getGameStage().equals(GameStage.GAME)){
                            plotPlayer.getPlayer().setGameMode(GameMode.CREATIVE);
                        }
                        if (game.getGameStage().equals(GameStage.EVALUATION) || game.getGameStage().equals(GameStage.WAIT_EVALUATION)){
                            plotPlayer.getPlayer().setGameMode(GameMode.SPECTATOR);
                        }
                        plotPlayer.getPlayer().getInventory().setItem(8, new ItemStack(Material.AIR));
                    }
                    plotPlayer.getPlayer().sendMessage(PREFIX.append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                            .append(Component.text("Ебать братуля ты ты че вернулся?",TextColor.fromCSSHexString("#55efc4"))));
                }));
        GuiItem lastGame = ItemBuilder.from(CustomStack.getInstance("dalynkaa:previus_button").getItemStack())
                .name(Component.text("Результаты преведущей игры" , TextColor.fromCSSHexString("#e17055")))
                .lore(Arrays.asList(
                        Component.text("Отерывает список с результатами преведущей игры для телепортации",TextColor.fromCSSHexString("#6c5ce7"))
                )).asGuiItem((event -> {
                    LastGameGui lastGameGui = new LastGameGui(VoteFilter.NORMAL, PlotPlayer.fromUUID(event.getWhoClicked().getUniqueId()));
                    lastGameGui.open(event.getWhoClicked());
                }));
        GuiItem spawn = ItemBuilder.from(CustomStack.getInstance("dalynkaa:spawn_button").getItemStack())
                .name(Component.text("Телепорт на спавн" , TextColor.fromCSSHexString("#e17055")))
                .lore(Arrays.asList(
                        Component.text("Телепортирует на спавн",TextColor.fromCSSHexString("#6c5ce7"))
                )).asGuiItem((event -> {
                    event.getWhoClicked().teleport(EventBuilders.getInstance().config.getSpawnLocation());
                }));
        gui.setItem(10, teleport);
        gui.setItem(12,lastGame);
        gui.setItem(14, spawn);
    }
    public void open(HumanEntity entity){
        gui.open(entity);
        TexturedInventoryWrapper.setPlayerInventoryTexture((Player) entity,new FontImageWrapper("dalynkaa:player_gui"),null,0,-8);
    }
}
