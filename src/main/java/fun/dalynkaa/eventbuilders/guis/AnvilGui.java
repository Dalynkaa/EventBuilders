package com.otsosity.spbuildrevrited.guis;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import com.otsosity.spbuildrevrited.SpBuildRevrited;
import com.otsosity.spbuildrevrited.utils.dataClasses.Game;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;

public class AnvilGui {
    Component PREFIX;
    public AnvilGui(Player p, GuiType type){
        this.PREFIX = SpBuildRevrited.getInstance().PREFIX;
        if (type.equals(GuiType.THEMA_GUI)){
            new AnvilGUI.Builder()
                    .onComplete((completion) -> {                                    //called when the inventory output slot is clicked
                        completion.getPlayer().sendMessage(PREFIX
                                .append(Component.text("Тема выбрана", TextColor.fromCSSHexString("#55efc4"))));
                        return Arrays.asList(AnvilGUI.ResponseAction.close(),AnvilGUI.ResponseAction.run(()-> {
                            Game game = Game.getCurrentGame();
                            game.setThema(completion.getText());
                            game.save(true);
                            game.createWorldStage(completion.getPlayer());

                        }));
                    })
                    .interactableSlots(AnvilGUI.Slot.INPUT_RIGHT)
                    .text("Не указано")
                    .itemLeft(new ItemStack(Material.PAPER))
                    .title("[Шаг 1] Тема ивента")
                    .plugin(SpBuildRevrited.getInstance())
                    .open(p);
            return;
        }else if (type.equals(GuiType.PLOT_COUNT_GUI)){
            new AnvilGUI.Builder()
                    .onComplete((completion) -> {                                    //called when the inventory output slot is clicked
                        completion.getPlayer().sendMessage(PREFIX
                                .append(Component.text("Количество плотов установленно!", TextColor.fromCSSHexString("#55efc4"))));
                        Game game = Game.getCurrentGame();
                        try {
                            game.plotCreateStage(completion.getPlayer(),Integer.parseInt(completion.getText()));
                        }catch (NumberFormatException e){
                            completion.getPlayer().sendMessage(PREFIX
                                    .append(Component.text("Нужны только цифры!!!", TextColor.fromCSSHexString("#ef4c00"))));
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("0"));
                        }
                        return Collections.singletonList(AnvilGUI.ResponseAction.close());
                    })
                    .interactableSlots(AnvilGUI.Slot.INPUT_RIGHT)
                    .text("0")
                    .itemLeft(new ItemStack(Material.PAPER))
                    .title("[Шаг 3] Количество плотов")
                    .plugin(SpBuildRevrited.getInstance())
                    .open(p);
            return;
        }else if (type.equals(GuiType.PLOT_SIZE_GUI)){
            new AnvilGUI.Builder()
                    .onComplete((completion) -> {                                    //called when the inventory output slot is clicked
                        completion.getPlayer().sendMessage(PREFIX
                                .append(Component.text("Размер плотов установлено!", TextColor.fromCSSHexString("#55efc4"))));
                        Game game = Game.getCurrentGame();
                        try {
                            game.setPlotSize(Integer.parseInt(completion.getText()));
                        }catch (NumberFormatException exception){
                            completion.getPlayer().sendMessage(PREFIX
                                    .append(Component.text("Нужны только цифры!!!", TextColor.fromCSSHexString("#ef4c00"))));
                            return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText("0"));
                        }
                        game.save(false);
                        return Arrays.asList(AnvilGUI.ResponseAction.close(),AnvilGUI.ResponseAction.run(()->{
                            new AnvilGui(completion.getPlayer(),GuiType.PLOT_COUNT_GUI);
                        }));
                    })
                    .interactableSlots(AnvilGUI.Slot.INPUT_RIGHT)
                    .text("0")
                    .itemLeft(new ItemStack(Material.PAPER))
                    .title("[Шаг 2] Размер плота")
                    .plugin(SpBuildRevrited.getInstance())
                    .open(p);
            return;
        }else if (type.equals(GuiType.GAME_TIME_GUI)){
            new AnvilGUI.Builder()
                    .onComplete((completion) -> {                                    //called when the inventory output slot is clicked
                        completion.getPlayer().sendMessage(PREFIX
                                .append(Component.text("Время игры выбрано!", TextColor.fromCSSHexString("#55efc4"))));
                        return Arrays.asList(AnvilGUI.ResponseAction.close(),AnvilGUI.ResponseAction.run(()-> {
                            Game game = Game.getCurrentGame();
                            game.setThema(completion.getText());
                            game.save(false);
                            game.createWorldStage(completion.getPlayer());

                        }));
                    })
                    .interactableSlots(AnvilGUI.Slot.INPUT_RIGHT)
                    .text("0")
                    .itemLeft(new ItemStack(Material.CLOCK))
                    .title("Время на постройку")
                    .plugin(SpBuildRevrited.getInstance())
                    .open(p);
            return;
        }else if (type.equals(GuiType.RESOURCE_TIME_GUI)){
            new AnvilGUI.Builder()
                    .onComplete((completion) -> {                                    //called when the inventory output slot is clicked
                        completion.getPlayer().sendMessage(PREFIX
                                .append(Component.text("Время на сбор установлено", TextColor.fromCSSHexString("#55efc4"))));
                        return Arrays.asList(AnvilGUI.ResponseAction.close(),AnvilGUI.ResponseAction.run(()-> {
                            Game game = Game.getCurrentGame();
                            game.setThema(completion.getText());
                            game.save(false);
                            game.createWorldStage(completion.getPlayer());

                        }));
                    })
                    .interactableSlots(AnvilGUI.Slot.INPUT_RIGHT)
                    .text("0")
                    .itemLeft(new ItemStack(Material.CLOCK))
                    .title("Время на сбор ресурсов")
                    .plugin(SpBuildRevrited.getInstance())
                    .open(p);
            return;
        }
        return;
    }
    public @NotNull String format(String message){
        return ChatColor.translateAlternateColorCodes('&', IridiumColorAPI.process(message));
    }
    public enum GuiType{
        THEMA_GUI,
        GAME_TIME_GUI,
        RESOURCE_TIME_GUI,
        PLOT_SIZE_GUI,
        PLOT_COUNT_GUI
    }
}
