package fun.dalynkaa.eventbuilders.guis;

import com.destroystokyo.paper.Title;
import com.iridium.iridiumcolorapi.IridiumColorAPI;

import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.BuildGame;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.BuildPlot;
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
        this.PREFIX = EventBuilders.getInstance().PREFIX;
        if (type.equals(GuiType.THEMA_GUI)) {
            new AnvilGUI.Builder()
                    .onClick((slot, stateSnapshot) -> {
                        if (!slot.equals(AnvilGUI.Slot.OUTPUT)){
                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        }
                        stateSnapshot.getPlayer().sendMessage(PREFIX
                                .append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                                .append(Component.text("Тема выбрана", TextColor.fromCSSHexString("#55efc4"))));
                        Game game = Game.getCurrentGame();
                        game.setThema(stateSnapshot.getText());
                        game.save(true);
                        if (game instanceof BuildGame) {
                            ((BuildGame) game).createWorldStage(stateSnapshot.getPlayer());
                        } else {
                            stateSnapshot.getPlayer().sendMessage("This game is not a BuildGame. Type: " + game.getClass().getSimpleName());
                        }
                        return Arrays.asList(AnvilGUI.ResponseAction.close());
                    })
                    .interactableSlots(AnvilGUI.Slot.INPUT_RIGHT)
                    .text("Не указано")
                    .itemLeft(new ItemStack(Material.PAPER))
                    .title("[Шаг 1] Тема ивента")
                    .plugin(EventBuilders.getInstance())
                    .open(p);
            return;
        }else if (type.equals(GuiType.PLOT_COUNT_GUI)){
            new AnvilGUI.Builder()
                    .onClick((slot, stateSnapshot) -> {
                        if (!slot.equals(AnvilGUI.Slot.OUTPUT)){
                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        }
                        stateSnapshot.getPlayer().sendMessage(PREFIX
                                        .append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                                .append(Component.text("Количество плотов установленно!", TextColor.fromCSSHexString("#55efc4"))));
                        Game game = Game.getCurrentGame();
                        try {
                            game.setPlotCount(Integer.parseInt(stateSnapshot.getText()));
                            game.save(false);
                            ((BuildGame) game).plotCreateStage(stateSnapshot.getPlayer(),Integer.parseInt(stateSnapshot.getText()));
                        }catch (NumberFormatException e){
                            stateSnapshot.getPlayer().sendMessage(PREFIX
                                            .append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                                    .append(Component.text("Нужны только цифры!!!", TextColor.fromCSSHexString("#ef4c00"))));
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("0"));
                        }
                        return Collections.singletonList(AnvilGUI.ResponseAction.close());
                    })
                    .interactableSlots(AnvilGUI.Slot.INPUT_RIGHT)
                    .text("0")
                    .itemLeft(new ItemStack(Material.PAPER))
                    .title("[Шаг 3] Количество плотов")
                    .plugin(EventBuilders.getInstance())
                    .open(p);
            return;
        }else if (type.equals(GuiType.PLOT_SIZE_GUI)){
            new AnvilGUI.Builder()
                    .onClick((slot, stateSnapshot) -> {
                        if (!slot.equals(AnvilGUI.Slot.OUTPUT)){
                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        }//called when the inventory output slot is clicked
                        stateSnapshot.getPlayer().sendMessage(PREFIX
                                        .append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                                .append(Component.text("Размер плотов установлено!", TextColor.fromCSSHexString("#55efc4"))));
                        Game game = Game.getCurrentGame();
                        try {
                            game.setPlotSize(Integer.parseInt(stateSnapshot.getText()));
                        }catch (NumberFormatException exception){
                            stateSnapshot.getPlayer().sendMessage(PREFIX
                                            .append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                                    .append(Component.text("Нужны только цифры!!!", TextColor.fromCSSHexString("#ef4c00"))));
                            return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText("0"));
                        }
                        game.save(false);
                        return Arrays.asList(AnvilGUI.ResponseAction.close(),AnvilGUI.ResponseAction.run(()->{
                            new AnvilGui(stateSnapshot.getPlayer(),GuiType.PLOT_COUNT_GUI);
                        }));
                    })
                    .interactableSlots(AnvilGUI.Slot.INPUT_RIGHT)
                    .text("0")
                    .itemLeft(new ItemStack(Material.PAPER))
                    .title("[Шаг 2] Размер плота")
                    .plugin(EventBuilders.getInstance())
                    .open(p);
            return;
        }else if (type.equals(GuiType.GAME_TIME_GUI)){
            new AnvilGUI.Builder()
                    .onClick((slot, stateSnapshot) -> {
                        if (!slot.equals(AnvilGUI.Slot.OUTPUT)){
                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        }//called when the inventory output slot is clicked
                        stateSnapshot.getPlayer().sendMessage(PREFIX
                                        .append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                                .append(Component.text("Время игры выбрано!", TextColor.fromCSSHexString("#55efc4"))));
                        Game game = Game.getCurrentGame();
                        try {
                            game.setGameTime(Integer.parseInt(stateSnapshot.getText())*60);
                            game.save(false);
                        }catch (NumberFormatException exception){
                            stateSnapshot.getPlayer().sendMessage(PREFIX
                                            .append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                                    .append(Component.text("Нужны только цифры!!!", TextColor.fromCSSHexString("#ef4c00"))));
                            return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText("0"));
                        }

                        return Arrays.asList(AnvilGUI.ResponseAction.close(),AnvilGUI.ResponseAction.run(()->{
                            ((BuildGame) game).startGameStage(stateSnapshot.getPlayer());
                            game.save(false);
                        }));
                    })
                    .interactableSlots(AnvilGUI.Slot.INPUT_RIGHT)
                    .text("0")
                    .itemLeft(new ItemStack(Material.CLOCK))
                    .title("Время на постройку(минуты)")
                    .plugin(EventBuilders.getInstance())
                    .open(p);
            return;
        }else if (type.equals(GuiType.GAME_TIME_ADD_GUI)){
            new AnvilGUI.Builder()
                    .onClick((slot, stateSnapshot) -> {
                        if (!slot.equals(AnvilGUI.Slot.OUTPUT)){
                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        }//called when the inventory output slot is clicked
                        stateSnapshot.getPlayer().sendMessage(PREFIX
                                .append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                                .append(Component.text("Время игры добавленно!", TextColor.fromCSSHexString("#55efc4"))));
                        Game game = Game.getCurrentGame();
                        try {
                            game.setGameTime(game.getGameTime()+Integer.parseInt(stateSnapshot.getText())*60);
                            for(PlotPlayer plotPlayer: PlotPlayer.getOnlinePlotPlayers(true)){
                                plotPlayer.getPlayer().sendTitle(Title.builder().title("Время на строительтво добавленно!").subtitle("Вы успеете достроить").build());
                            }
                            game.save(false);
                        }catch (NumberFormatException exception){
                            stateSnapshot.getPlayer().sendMessage(PREFIX
                                    .append(Component.text(" : ", TextColor.fromCSSHexString("#2d3436")))
                                    .append(Component.text("Нужны только цифры!!!", TextColor.fromCSSHexString("#ef4c00"))));
                            return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText("0"));
                        }

                        return Arrays.asList(AnvilGUI.ResponseAction.close(),AnvilGUI.ResponseAction.run(()->{
                            game.save(false);
                        }));
                    })
                    .interactableSlots(AnvilGUI.Slot.INPUT_RIGHT)
                    .text("0")
                    .itemLeft(new ItemStack(Material.CLOCK))
                    .title("Сколько времени добавить(минуты)")
                    .plugin(EventBuilders.getInstance())
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
        GAME_TIME_ADD_GUI,
        PLOT_SIZE_GUI,
        PLOT_COUNT_GUI
    }
}
