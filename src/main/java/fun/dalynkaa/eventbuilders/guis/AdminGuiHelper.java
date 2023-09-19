package fun.dalynkaa.eventbuilders.guis;

import dev.lone.itemsadder.api.CustomStack;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.utils.ControllItems;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameStage;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.BuildGame;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.SkinGame;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class AdminGuiHelper {
    public AdminGuiHelper(){

    }

    public static void buildingGameType(Game game, PlotPlayer player, Gui gui){
        GuiItem stage_item = ItemBuilder.from(CustomStack.getInstance(game.getGameStage().getTexture()).getItemStack())
                .name(Component.text(game.getGameStage().getAction() , TextColor.fromCSSHexString("#ff7675")))
                .lore(Arrays.asList(
                        Component.text("Следуйщая cтадия - " , TextColor.fromCSSHexString("#e17055"))
                                .append(Component.text(game.getGameStage().next().getTranslated(), TextColor.fromCSSHexString("#fab1a0"))),
                        Component.text("Текущая стадия - ",TextColor.fromCSSHexString("#6c5ce7"))
                                .append(Component.text(game.getGameStageString(),TextColor.fromCSSHexString("#a29bfe")))
                )).asGuiItem((event -> {
                    if (game.getGameStage().equals(GameStage.NO_GAME)) {
                        new AnvilGui((Player) event.getWhoClicked(), AnvilGui.GuiType.THEMA_GUI);
                    }else if (game.getGameStage().equals(GameStage.PREPARING_PLOT)) {
                        new AnvilGui((Player) event.getWhoClicked(), AnvilGui.GuiType.PLOT_SIZE_GUI);
                    } else if (game.getGameStage().equals(GameStage.PREGENERATING_PLOT)) {
                        ((BuildGame) game).givePlotsStage((Player) event.getWhoClicked());
                        game.save(false);
                        event.getInventory().close();
                    } else if (game.getGameStage().equals(GameStage.WAITING_START)) {
                        new AnvilGui((Player) event.getWhoClicked(), AnvilGui.GuiType.GAME_TIME_GUI);
                    } else if (game.getGameStage().equals(GameStage.GAME) || game.getGameStage().equals(GameStage.WAIT_EVALUATION)) {
                        ((BuildGame) game).startVoteStage();
                        game.save(false);
                        event.getInventory().close();
                    } else if (game.getGameStage().equals(GameStage.EVALUATION)) {
                        ((BuildGame) game).startFinishStage();
                        game.save(false);
                        event.getInventory().close();
                    } else {
                        ((BuildGame) game).endGame((Player) event.getWhoClicked());
                        event.getInventory().close();
                    }
                }));
        GuiItem teleportToCurrent;
        if (!player.getPlayer().getWorld().equals(Bukkit.getWorld("world"))){
            teleportToCurrent = ItemBuilder.from(CustomStack.getInstance("dalynkaa:spawn_button").getItemStack())
                    .name(Component.text("Телепорт - " , TextColor.fromCSSHexString("#e17055"))
                            .append(Component.text("СПАВН", TextColor.fromCSSHexString("#fab1a0"))))
                    .lore(Arrays.asList(
                            Component.text("Телепортирует на спавн",TextColor.fromCSSHexString("#6c5ce7"))
                    )).asGuiItem((event -> {
                        Location spawn = EventBuilders.getInstance().config.getSpawnLocation();
                        event.getWhoClicked().teleport(spawn);
                        Player player1 = (Player) event.getWhoClicked();
                        player1.getInventory().clear();
                        ControllItems controllItems = new ControllItems();
                        controllItems.giveItems(PlotPlayer.fromUUID(player.getUuid()), Game.getCurrentGame());
                        event.getWhoClicked().setGameMode(GameMode.CREATIVE);
                        event.getInventory().close();
                    }));
        }else {
            teleportToCurrent = ItemBuilder.from(CustomStack.getInstance("dalynkaa:admin_teleport_button").getItemStack())
                    .name(Component.text("Телепорт - " , TextColor.fromCSSHexString("#e17055"))
                            .append(Component.text(game.getThema(), TextColor.fromCSSHexString("#fab1a0"))))
                    .lore(Arrays.asList(
                            Component.text("Телепортирует в текущий игровой мир",TextColor.fromCSSHexString("#6c5ce7"))
                    )).asGuiItem((event -> {
                        Location spawn = new Location(game.getWorld(), 0, 100, 0);
                        event.getWhoClicked().teleport(spawn);
                        event.getWhoClicked().setGameMode(GameMode.CREATIVE);
                        event.getInventory().close();
                    }));
        }
        GuiItem endCurrent = ItemBuilder.from(CustomStack.getInstance("dalynkaa:admin_cross_button").getItemStack())
                .name(Component.text("Завершить - " , TextColor.fromCSSHexString("#e17055"))
                        .append(Component.text(game.getThema(), TextColor.fromCSSHexString("#fab1a0"))))
                .lore(Arrays.asList(
                        Component.text("Завершает текущую игру",TextColor.fromCSSHexString("#6c5ce7"))
                )).asGuiItem((event -> {
                    game.endGame((Player) event.getWhoClicked());
                    event.getInventory().close();
                }));
        GuiItem worldListButton = ItemBuilder.from(CustomStack.getInstance("dalynkaa:admin_list_button").getItemStack())
                .name(Component.text("Список миров" , TextColor.fromCSSHexString("#e17055")))
                .lore(Arrays.asList(
                        Component.text("Открывет список миров где можно удалить или телепортироватся в мир",TextColor.fromCSSHexString("#6c5ce7"))
                )).asGuiItem((event -> {
                    WorldListGui worldListGui = new WorldListGui();
                    worldListGui.open(event.getWhoClicked());
                }));
        GuiItem settingsButton = ItemBuilder.from(CustomStack.getInstance("dalynkaa:admin_gear_button").getItemStack())
                .name(Component.text("Настройки" , TextColor.fromCSSHexString("#e17055")))
                .lore(Arrays.asList(
                        Component.text("Открывает настройки игры",TextColor.fromCSSHexString("#6c5ce7"))
                )).asGuiItem((event -> {
                    new SettingsGui(game, event.getWhoClicked());
                }));
        GuiItem addTimeButton = ItemBuilder.from(CustomStack.getInstance("dalynkaa:admin_watch_button").getItemStack())
                .name(Component.text("Добавить время" , TextColor.fromCSSHexString("#e17055")))
                .lore(Arrays.asList(
                        Component.text("Открывает меню где можно добавить время для строительства",TextColor.fromCSSHexString("#6c5ce7"))
                )).asGuiItem((event -> {
                    new AnvilGui((Player) event.getWhoClicked(), AnvilGui.GuiType.GAME_TIME_ADD_GUI);
                }));
        gui.setItem(10,stage_item);
        if (game.getGameStage().equals(GameStage.GAME)){
            gui.setItem(12,addTimeButton);
        }
        gui.setItem(6,teleportToCurrent);
        if (game.getGameStage()!=GameStage.NO_GAME){
            gui.setItem(8,endCurrent);
        }
        gui.setItem(24,worldListButton);
        gui.setItem(26, settingsButton);
    }

    public static void skinItems(Game game, PlotPlayer player, Gui gui){
        if (!(game instanceof SkinGame)){
            return;
        }
        SkinGame skinGame = (SkinGame) game;
        GuiItem stage_item = ItemBuilder.from(CustomStack.getInstance(skinGame.getGameStage().getTexture()).getItemStack())
                .name(Component.text(skinGame.getGameStage().getAction() , TextColor.fromCSSHexString("#ff7675")))
                .lore(Arrays.asList(
                        Component.text("Следуйщая cтадия - " , TextColor.fromCSSHexString("#e17055"))
                                .append(Component.text(skinGame.getGameStage().next().getTranslated(), TextColor.fromCSSHexString("#fab1a0"))),
                        Component.text("Текущая стадия - ",TextColor.fromCSSHexString("#6c5ce7"))
                                .append(Component.text(skinGame.getGameStageString(),TextColor.fromCSSHexString("#a29bfe")))
                )).asGuiItem((event -> {
                    if (skinGame.getGameStage().equals(GameStage.SKIN_NO_GAME)) {
                        skinGame.startSkinGame(player);
                        event.getInventory().close();
                    } else if (skinGame.getGameStage().equals(GameStage.SKIN_PREGENERATING_PLOT)) {
                        skinGame.startGameStage(player);
                        event.getInventory().close();
                    } else {
                        skinGame.endGame((Player) event.getWhoClicked());
                        event.getInventory().close();
                    }
                }));
        GuiItem teleportToCurrent;
        if (!player.getPlayer().getWorld().equals(Bukkit.getWorld("world"))){
            teleportToCurrent = ItemBuilder.from(CustomStack.getInstance("dalynkaa:spawn_button").getItemStack())
                    .name(Component.text("Телепорт - " , TextColor.fromCSSHexString("#e17055"))
                            .append(Component.text("СПАВН", TextColor.fromCSSHexString("#fab1a0"))))
                    .lore(Arrays.asList(
                            Component.text("Телепортирует на спавн",TextColor.fromCSSHexString("#6c5ce7"))
                    )).asGuiItem((event -> {
                        Location spawn = EventBuilders.getInstance().config.getSpawnLocation();
                        event.getWhoClicked().teleport(spawn);
                        Player player1 = (Player) event.getWhoClicked();
                        player1.getInventory().clear();
                        ControllItems controllItems = new ControllItems();
                        controllItems.giveItems(PlotPlayer.fromUUID(player.getUuid()), Game.getCurrentGame());
                        event.getWhoClicked().setGameMode(GameMode.CREATIVE);
                        event.getInventory().close();
                    }));
        }else {
            teleportToCurrent = ItemBuilder.from(CustomStack.getInstance("dalynkaa:admin_teleport_button").getItemStack())
                    .name(Component.text("Телепорт - " , TextColor.fromCSSHexString("#e17055"))
                            .append(Component.text(skinGame.getThema(), TextColor.fromCSSHexString("#fab1a0"))))
                    .lore(Arrays.asList(
                            Component.text("Телепортирует в текущий игровой мир",TextColor.fromCSSHexString("#6c5ce7"))
                    )).asGuiItem((event -> {
                        Location spawn = new Location(skinGame.getWorld(), 0, 100, 0);
                        event.getWhoClicked().teleport(spawn);
                        event.getWhoClicked().setGameMode(GameMode.CREATIVE);
                        event.getInventory().close();
                    }));
        }
        GuiItem endCurrent = ItemBuilder.from(CustomStack.getInstance("dalynkaa:admin_cross_button").getItemStack())
                .name(Component.text("Завершить - " , TextColor.fromCSSHexString("#e17055"))
                        .append(Component.text(skinGame.getThema(), TextColor.fromCSSHexString("#fab1a0"))))
                .lore(Arrays.asList(
                        Component.text("Завершает текущую игру",TextColor.fromCSSHexString("#6c5ce7"))
                )).asGuiItem((event -> {
                    skinGame.endGame((Player) event.getWhoClicked());
                    event.getInventory().close();
                }));
        GuiItem settingsButton = ItemBuilder.from(CustomStack.getInstance("dalynkaa:admin_gear_button").getItemStack())
                .name(Component.text("Настройки" , TextColor.fromCSSHexString("#e17055")))
                .lore(Arrays.asList(
                        Component.text("Открывает настройки игры",TextColor.fromCSSHexString("#6c5ce7"))
                )).asGuiItem((event -> {
                    new SettingsGui(skinGame, event.getWhoClicked());
                }));
        gui.setItem(10,stage_item);
        gui.setItem(6,teleportToCurrent);
        if (skinGame.getGameStage().equals(GameStage.SKIN_EVALUATION) || skinGame.getGameStage().equals(GameStage.SKIN_PREGENERATING_PLOT)){
            gui.setItem(8,endCurrent);
        }
        gui.setItem(26, settingsButton);
    }

}
