package com.otsosity.spbuildrevrited.guis;

import com.otsosity.spbuildrevrited.utils.dataClasses.Enums.GameStage;
import com.otsosity.spbuildrevrited.utils.dataClasses.Game;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class AdminGuiCore {
    Gui gui;
    public AdminGuiCore(){
        gui = Gui.gui()
                .title(Component.text("Админское Меню"))
                .rows(1)
                .disableAllInteractions().create();

        GuiItem stage_item = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYThiNTBmY2U0MzhlNDJjMGIyODc4OGRmMjc3NzdlNTdjOTM5NmQzZmExOTVhMTIwZWM2MjM3Y2ZjNTA5MTk3NiJ9fX0=")
                .name(Component.text(Game.getCurrentGame().getGameStage().getAction() , TextColor.fromCSSHexString("#ff7675")))
                .lore(Arrays.asList(
                        Component.text("Следуйщая cтадия -" , TextColor.fromCSSHexString("#e17055"))
                                .append(Component.text(Game.getCurrentGame().getGameStage().next().getTranslated(), TextColor.fromCSSHexString("#fab1a0"))),
                        Component.text("Текущая стадия - ",TextColor.fromCSSHexString("#6c5ce7"))
                                .append(Component.text(Game.getCurrentGame().getGameStageString(),TextColor.fromCSSHexString("#a29bfe")))
                )).asGuiItem((event -> {
                    Game game = Game.getCurrentGame();
                        if (game.getGameStage().equals(GameStage.NO_GAME)) {
                            new AnvilGui((Player) event.getWhoClicked(), AnvilGui.GuiType.THEMA_GUI);
                        }else if (game.getGameStage().equals(GameStage.PREPARING_PLOT)) {
                            new AnvilGui((Player) event.getWhoClicked(), AnvilGui.GuiType.PLOT_SIZE_GUI);
                        }else {
                            event.getInventory().close();
                    }
                }));

        GuiItem teleportToCurrent = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTM4MjYxODFjZTkwMTJiNjY1ODY1ZjNhYzAwNjYzMDdiNGQwMmRhMjgxNTQwMTA0ZTA0NjFmZmVmYTc0NTlmZCJ9fX0=")
                .name(Component.text("Телепорт -" , TextColor.fromCSSHexString("#e17055"))
                        .append(Component.text(Game.getCurrentGame().getThema(), TextColor.fromCSSHexString("#fab1a0"))))
                .lore(Arrays.asList(
                        Component.text("Телепортирует в текущий игровой мир",TextColor.fromCSSHexString("#6c5ce7"))
                )).asGuiItem((event -> {
                    Game game = Game.getCurrentGame();
                    Location spawn = new Location(game.getWorld(), 0, -60, 0);
                    event.getWhoClicked().teleport(spawn);
                }));

        gui.setItem(1,stage_item);
        gui.setItem(7,teleportToCurrent);
    }
    public void openMenu(Player player){
        gui.open(player);
    }
}
