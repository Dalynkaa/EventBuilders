package fun.dalynkaa.eventbuilders.guis;

import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.utils.GameUtils;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.BuildPlot;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SettingsGui {
    Gui gui;
    public SettingsGui(Game game, HumanEntity player){
        gui = Gui.gui()
                .title(Component.text("Настройки"))
                .rows(1)
                .disableAllInteractions().create();
        GuiItem allowJoin;
        Component PREFIX = EventBuilders.getInstance().PREFIX;
        if (!EventBuilders.getInstance().newCanJoin){
            allowJoin = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWVhOWU4ODVlOTNmOTY0ZTAwNzVhNzVlOWFlMjVjZGFiZGEyZmZhNWQxMmZlZWRmYWIwZjg4OWIzZWRiYmU2YiJ9fX0=")
                    .name(Component.text("Включить доп слоты" , TextColor.fromCSSHexString("#e17055")))
                    .lore(Arrays.asList(
                            Component.text("Дает возможность заходить новым людям",TextColor.fromCSSHexString("#6c5ce7"))
                            )).asGuiItem((event -> {
                        event.getInventory().close();
                        EventBuilders.getInstance().newCanJoin = true;
                        PlotPlayer plotPlayer = PlotPlayer.fromUUID(event.getWhoClicked().getUniqueId());
                        plotPlayer.getPlayer().sendMessage(PREFIX + "Вы включили доп слоты");
                    }));
        }else {
            allowJoin = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTk2MGQ2ZmZhZjQ0ZThhZmNiZGY4YjI5YTc3ZDg0Y2UyMmM3MWQwMGM2NGJmZDk5YWYzNDBhNjk1MzViZmQ3In19fQ==")
                    .name(Component.text("Выключить доп слоты" , TextColor.fromCSSHexString("#e17055")))
                    .lore(Arrays.asList(
                            Component.text("Дает возможность заходить новым людям",TextColor.fromCSSHexString("#6c5ce7"))
                            )).asGuiItem((event -> {
                        event.getInventory().close();
                        EventBuilders.getInstance().newCanJoin = false;
                        PlotPlayer plotPlayer = PlotPlayer.fromUUID(event.getWhoClicked().getUniqueId());
                        plotPlayer.getPlayer().sendMessage(PREFIX + "Вы выключили доп слоты");
                    }));
        }
        GuiItem canOut;
        if (!EventBuilders.getInstance().canOut){
            canOut = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWVhOWU4ODVlOTNmOTY0ZTAwNzVhNzVlOWFlMjVjZGFiZGEyZmZhNWQxMmZlZWRmYWIwZjg4OWIzZWRiYmU2YiJ9fX0=")
                    .name(Component.text("Включить выход с плота" , TextColor.fromCSSHexString("#e17055")))
                    .lore(Arrays.asList(
                            Component.text("Дает возможность выходить из плота",TextColor.fromCSSHexString("#6c5ce7"))
                    )).asGuiItem((event -> {
                        event.getInventory().close();
                        List<Plot> plots = Plot.getAllPlots(game.getGameId());
                        for (Plot plot: plots){
                            plot.getProtectedRegion().setFlag(Flags.EXIT, StateFlag.State.ALLOW);
                            plot.saveRegions();
                        }
                        EventBuilders.getInstance().canOut = true;

                    }));
        }else {
            canOut = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTk2MGQ2ZmZhZjQ0ZThhZmNiZGY4YjI5YTc3ZDg0Y2UyMmM3MWQwMGM2NGJmZDk5YWYzNDBhNjk1MzViZmQ3In19fQ==")
                    .name(Component.text("Выключить выход с плота" , TextColor.fromCSSHexString("#e17055")))
                    .lore(Arrays.asList(
                            Component.text("Дфает возможность выходить из плота",TextColor.fromCSSHexString("#6c5ce7"))
                    )).asGuiItem((event -> {
                        event.getInventory().close();
                        List<Plot> plots = Plot.getAllPlots(game.getGameId());
                        for (Plot plot: plots){
                            plot.getProtectedRegion().setFlag(Flags.EXIT, StateFlag.State.DENY);
                            plot.getProtectedRegion().setFlag(Flags.EXIT.getRegionGroupFlag(), RegionGroup.MEMBERS);
                            plot.saveRegions();
                        }
                        EventBuilders.getInstance().canOut = false;
                    }));
        }
        GuiItem kickAll = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDE3NmMxYWU3MTMwN2U4NTQ3MDA2OTI3ZTk3MWU1MzBkYzBjMzQ4YmRhYjZkYjc0YWJlYWQ3MDFhNDBiMWIwIn19fQ==")
                .name(Component.text("Кикнуть всех" , TextColor.fromCSSHexString("#e17055")))
                .lore(Arrays.asList(
                        Component.text("Кикает всех кроме админов",TextColor.fromCSSHexString("#6c5ce7"))
                        )).asGuiItem((event -> {
                    List<PlotPlayer> plotPlayers = PlotPlayer.getOnlinePlotPlayers(true);
                    for (PlotPlayer plotPlayer : plotPlayers){
                        if (plotPlayer.isAdmin()){
                            Bukkit.getLogger().info("Админ не кикнут");
                        } else if (plotPlayer.isVoter()) {
                            Bukkit.getLogger().info("Голосующий не кикнут");
                        }else {
                            plotPlayer.getPlayer().kick();
                        }
                    }
                }));
        GuiItem clearAllVoters = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWQ2Y2U5ZDE3ZGU5NjFmYTZkOGQzMzkyZmE5YTRmZTZiZDZhOTZhMzk0NjFjNWI0NjRlZjU1MmUyZjlhYzlmMiJ9fX0=")
                .name(Component.text("Очистить жюри" , TextColor.fromCSSHexString("#e17055")))
                .lore(Arrays.asList(
                        Component.text("Очищает список жюри",TextColor.fromCSSHexString("#6c5ce7"))
                )).asGuiItem((event -> {
                    List<PlotPlayer> plotPlayers = PlotPlayer.getAllPlayers();
                    for (PlotPlayer plotPlayer : plotPlayers){
                        if (plotPlayer.isAdmin()){
                            Bukkit.getLogger().info("Админ не очищен");
                        } else if (plotPlayer.isVoter()) {
                            Bukkit.getLogger().info("Голосующий очищен");
                            plotPlayer.setVoter(false);
                            plotPlayer.save(false);
                            ConsoleCommandSender commandSender = Bukkit.getServer().getConsoleSender();
                            String command = "lp user "+plotPlayer.getName()+" parent remove voter";
                            Bukkit.dispatchCommand(commandSender, command);
                        }
                    }
                }));
        GuiItem clearAll = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWQ2Y2U5ZDE3ZGU5NjFmYTZkOGQzMzkyZmE5YTRmZTZiZDZhOTZhMzk0NjFjNWI0NjRlZjU1MmUyZjlhYzlmMiJ9fX0=")
                .name(Component.text("Очистить всех" , TextColor.fromCSSHexString("#e17055")))
                .lore(Arrays.asList(
                        Component.text("Очищает список игроков",TextColor.fromCSSHexString("#6c5ce7"))
                )).asGuiItem((event -> {
                    GameUtils.ResetAllPlayers(game);
                }));
        gui.setItem(0, allowJoin);
        gui.setItem(1,clearAllVoters);
        gui.setItem(2, kickAll);
        gui.setItem(3, clearAll);
        gui.setItem(4,canOut);

        gui.open(player);
    }
}
