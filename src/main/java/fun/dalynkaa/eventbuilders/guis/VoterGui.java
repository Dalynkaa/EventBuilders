package fun.dalynkaa.eventbuilders.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;

import java.util.Arrays;

public class VoterGui {
    Gui gui;
    public VoterGui(Game game){
        gui = Gui.gui()
                .title(Component.text("Жюри Меню"))
                .rows(1)
                .disableAllInteractions().create();
        GuiItem creative = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTY3NTVhYzZjMDc4ZDAwODJmNjg3MTUzOWY4YzhlMDM3M2IwMDgyMTRjYWNkYjRjZGZmZmM4ODY2ZGYxZDJlNiJ9fX0=")
                .name(Component.text("ГейМод креатив" , TextColor.fromCSSHexString("#e17055")))
                .lore(Arrays.asList(
                        Component.text("Открывет список миров где можно удалить или телепортироватся в мир",TextColor.fromCSSHexString("#6c5ce7"))
                )).asGuiItem((event -> {
                    event.getInventory().close();
                    event.getWhoClicked().setGameMode(GameMode.CREATIVE);
                }));
        GuiItem spectator = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDRhMmViMmIxMDUzOWNlYzIwMmUxODY3ZjI2MWE2ODBkODcyOWFlNGFiYmE5NTdjZGZhMjQ1MzgxNTJmZGM3MSJ9fX0=")
                .name(Component.text("ГейМод Спектатор" , TextColor.fromCSSHexString("#e17055")))
                .lore(Arrays.asList(
                        Component.text("Открывет список миров где можно удалить или телепортироватся в мир",TextColor.fromCSSHexString("#6c5ce7"))
                )).asGuiItem((event -> {
                    event.getInventory().close();
                    event.getWhoClicked().setGameMode(GameMode.SPECTATOR);
                }));
        GuiItem survival = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzNmYjZhYjNmMTY1MjgzMjg1YzUxMzA1ZDUwNmUyNDE2ZjQ4MzFmZmZjY2RiYzRlZTMzNmE4MDQzYTQ4N2EwMSJ9fX0=")
                .name(Component.text("ГейМод  Выживание" , TextColor.fromCSSHexString("#e17055")))
                .lore(Arrays.asList(
                        Component.text("Открывет список миров где можно удалить или телепортироватся в мир",TextColor.fromCSSHexString("#6c5ce7"))
                )).asGuiItem((event -> {
                    event.getInventory().close();
                    event.getWhoClicked().setGameMode(GameMode.SURVIVAL);
                }));
        GuiItem teleportToCurrent = ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTM4MjYxODFjZTkwMTJiNjY1ODY1ZjNhYzAwNjYzMDdiNGQwMmRhMjgxNTQwMTA0ZTA0NjFmZmVmYTc0NTlmZCJ9fX0=")
                .name(Component.text("Телепорт - " , TextColor.fromCSSHexString("#e17055"))
                        .append(Component.text(game.getThema(), TextColor.fromCSSHexString("#fab1a0"))))
                .lore(Arrays.asList(
                        Component.text("Телепортирует в текущий игровой мир",TextColor.fromCSSHexString("#6c5ce7"))
                )).asGuiItem((event -> {
                    Location spawn = new Location(game.getWorld(), 0, -60, 0);
                    event.getWhoClicked().teleport(spawn);
                    event.getInventory().close();
                }));
        gui.setItem(0, creative);
        gui.setItem(1, spectator);
        gui.setItem(2, survival);
        gui.setItem(8, teleportToCurrent);
    }
    public void open(HumanEntity entity){
        gui.open(entity);
    }
}
