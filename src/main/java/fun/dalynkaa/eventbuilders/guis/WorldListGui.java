package fun.dalynkaa.eventbuilders.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.guis.acceptGui.AcceptGui;
import fun.dalynkaa.eventbuilders.utils.UsableClasses.InventoryButton;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.VoteFilter;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class WorldListGui {
    PaginatedGui gui;
    public WorldListGui(){
        gui = Gui.paginated()
                .title(Component.text("Список миров"))
                .rows(6)
                .pageSize(36)
                .disableAllInteractions()
                .create();
        List<Game> gameList = EventBuilders.getInstance().db.getGameList();
        for (Game game: gameList){
            GuiItem item = ItemBuilder.skull()
                    .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODc5ZTU0Y2JlODc4NjdkMTRiMmZiZGYzZjE4NzA4OTQzNTIwNDhkZmVjZDk2Mjg0NmRlYTg5M2IyMTU0Yzg1In19fQ==")
                    .name(Component.text("Тема - ", TextColor.fromCSSHexString("#BA68C8")).append(Component.text(game.getThema(), TextColor.fromCSSHexString("#8E24AA"))))
                    .lore(Arrays.asList(
                            Component.text("ПКМ - ", TextColor.fromCSSHexString("#d63031")).append(Component.text("Удалить мир", TextColor.fromCSSHexString("#ff7675"))),
                            Component.text("ЛКМ - ", TextColor.fromCSSHexString("#6c5ce7")).append(Component.text("Телепортироваться в мир", TextColor.fromCSSHexString("#a29bfe")))
                    ))
                    .asGuiItem(event -> {
                        if (event.isLeftClick()){
                            World world = new WorldCreator(game.getGameId().toString()).createWorld();
                            Bukkit.getLogger().info(world.getName()+" загружен");
                            Player player = (Player) event.getWhoClicked();
                            player.teleport(new Location(world, 0, 100, 0));
                            player.setGameMode(GameMode.CREATIVE);
                            ItemStack perl = InventoryButton.from(Material.ENDER_PEARL)
                                    .setName(Component.text("Список игроков", TextColor.fromCSSHexString("#22a6b3")))
                                    .setLore(Arrays.asList(Component.text("Открывает список игроков вместе с оценками",TextColor.fromCSSHexString("#a29bfe"))))
                                    .build((event1 -> {
                                        PlotListGui voteListGui = new PlotListGui(VoteFilter.NORMAL, game, PlotPlayer.fromUUID(player.getUniqueId()), PlotListGui.GuiType.NORMAL);
                                        voteListGui.open(player);
                                    }), "perl");
                            player.getInventory().setItem(6, perl);
                            event.getInventory().close();
                        } else if (event.isRightClick()) {
                            new AcceptGui("Удалить мир?", event.getWhoClicked(), event1->{
                                    World world1 = Bukkit.getWorld(game.getGameId());
                                    if (world1==null){
                                        world1=new WorldCreator(game.getGameId().toString()).createWorld();
                                    }
                                    if (world1 != null){
                                        Bukkit.getServer().unloadWorld(world1,true);
                                        deleteWorld(world1.getWorldFolder());
                                    }
                                    EventBuilders.getInstance().db.deleteGameFromTable(game.getGameId().toString());
                                    event1.getInventory().close();
                                    WorldListGui worldListGui = new WorldListGui();
                                    worldListGui.open(event1.getWhoClicked());
                            }, (event1 -> event1.getInventory().close()));

                        }
                    });
            gui.addItem(item);
        }

        gui.setItem(45, ItemBuilder
                .skull()
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWZhNGM4MjcxMDgzNzQ4MGRmNTc1Y2EwZDY0Y2VmMmZjZGFkYWVjZTcwOTFiNzA3NmI5MjNjNjdlNWY0ZTg0OSJ9fX0=")
                .name(Component.text("Преведущая страница"))
                .asGuiItem(event -> gui.previous()));

        gui.setItem(53, ItemBuilder
                .skull()
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjM5NTExOWRkNTIwMWEyNDJiODZiNDg2NmQ2ZjA0NTQxYjAwYjkyZWJkZDU3Y2UyNzkxOWZiNWYxMDJhNmRkZCJ9fX0=")
                .name(Component.text("Следуйщая страница"))
                .asGuiItem(event -> gui.next()));
    }
    public void open(HumanEntity player){
        gui.open(player);
    }
    public boolean deleteWorld(File path) {
        if(path.exists()) {
            File files[] = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
    }
}
