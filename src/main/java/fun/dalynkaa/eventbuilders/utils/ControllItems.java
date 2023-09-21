package fun.dalynkaa.eventbuilders.utils;

import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.guis.AdminGuiCore;
import fun.dalynkaa.eventbuilders.guis.PlayerMenu;
import fun.dalynkaa.eventbuilders.guis.PlotControllGui;
import fun.dalynkaa.eventbuilders.guis.VoterGui;
import fun.dalynkaa.eventbuilders.utils.UsableClasses.InventoryButton;
import fun.dalynkaa.eventbuilders.utils.UsableClasses.KickButton;
import fun.dalynkaa.eventbuilders.utils.dataClasses.*;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameStage;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameType;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.BuildGame;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.SkinPlot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ControllItems {
    ItemStack admin_button;
    ItemStack player_button;
    ItemStack voter_button;
    ItemStack skin_button;
    ItemStack skin_add_button;
    ItemStack skin_ban_button;
    public ControllItems(){
        List<UUID> misklickList = new ArrayList<>();
        Component PREFIX = EventBuilders.getInstance().PREFIX;
        this.admin_button = InventoryButton.fromItemsAdder(fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game.getCurrentGame().getGameType().getIcon())
                .setName(Component.text("Админ", TextColor.fromCSSHexString("#d63031"))
                        .append(Component.text("Меню",TextColor.fromCSSHexString("#ff7675"))))
                .setLore(Arrays.asList(Component.text("Открывает админское меню",TextColor.fromCSSHexString("#a29bfe"))))
                .build((event -> {
                    if (event.getPlayer().isSneaking()){
                        fun.dalynkaa.eventbuilders.utils.dataClasses.games.plots.Plot plot = EventBuilders.getInstance().currentplot.get(event.getPlayer().getUniqueId());
                        if (plot == null){
                            fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game game = fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game.getCurrentGame();
                            if (game.getGameStage().equals(GameStage.NO_GAME) || game.getGameStage().equals(GameStage.SKIN_NO_GAME)){
                                game.setGameType(game.getGameType().next());
                                event.getPlayer().sendActionBar(game.getGameType().getTranslated());
                                game.setGameStage(game.getGameType().getStart());
                                game.save(false);
                                event.getPlayer().sendMessage("Изменен режим игры!");
                                for (PlotPlayer plotPlayer: PlotPlayer.getOnlinePlotAdmin()){
                                    new ControllItems().giveItems(plotPlayer, game);
                                }
                            }
                            return;
                        }
                        PlotControllGui plotControllGui = new PlotControllGui(fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game.getCurrentGame(),plot);
                        plotControllGui.open(event.getPlayer());
                        return;
                    }
                    new AdminGuiCore(PlotPlayer.fromUUID(event.getPlayer().getUniqueId()));
                }), "admin_item");
        this.player_button = InventoryButton.fromItemsAdder("dalynkaa:player_button")
                .setName(Component.text("Меню", TextColor.fromCSSHexString("#00cec9"))
                        .append(Component.text("Игрока",TextColor.fromCSSHexString("#81ecec"))))
                .setLore(Arrays.asList(Component.text("Открывает меню игрока",TextColor.fromCSSHexString("#a29bfe"))))
                .build((event -> {
                    PlayerMenu playerMenu = new PlayerMenu(fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game.getCurrentGame());
                    playerMenu.open(event.getPlayer());
                }), "player_item");
        this.voter_button = InventoryButton.fromHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjVhNzk5N2I1MDlmM2FkNDBlZjUzYjhmMGU1MGU0MTU5ZjBmYWEzNjQzMDAyZjUyYzgzYWUyZmUxODViMTJhNiJ9fX0=")
                .setName(Component.text("Меню", TextColor.fromCSSHexString("#0984e3"))
                        .append(Component.text("Жюри",TextColor.fromCSSHexString("#74b9ff"))))
                .setLore(Arrays.asList(Component.text("Открывает меню Жюри",TextColor.fromCSSHexString("#a29bfe"))))
                .build((event -> {
                    VoterGui voterGui = new VoterGui(fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game.getCurrentGame());
                    voterGui.open(event.getPlayer());
                }), "voter_item");

        this.skin_button = KickButton.fromItemsAdder("dalynkaa:remove_player")
                .setName(Component.text("Играй на", TextColor.fromCSSHexString("#00cec9"))
                        .append(Component.text(" play.spworlds.ru",TextColor.fromCSSHexString("#81ecec"))))
                .setLore(Arrays.asList(Component.text("Кикает игрока из игры",TextColor.fromCSSHexString("#a29bfe"))))
                .build((event -> {
                    if (event.getEntity() instanceof Player player){
                        PlotPlayer plotPlayer = PlotPlayer.fromUUID(player.getUniqueId());
                        plotPlayer.setInGame(false);
                        plotPlayer.setCanJoin(false);
                        plotPlayer.getCurrentPlot().setOwner(null).update();
                        plotPlayer.save(false);
                        new ControllItems().spawn(plotPlayer, fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game.getCurrentGame());
                        Bukkit.broadcast(PREFIX.append(player.displayName().color(TextColor.fromCSSHexString("#d63031"))).append(Component.text(" кикнут из игры. Причина - ", TextColor.fromCSSHexString("#ff7675"))).append(Component.text("Играй на play.spworlds.ru", TextColor.fromCSSHexString("#d63031"))));
                    }
                }), "player_item");
        this.skin_add_button = KickButton.fromItemsAdder("dalynkaa:next_player")
                .setName(Component.text("Оставить на", TextColor.fromCSSHexString("#00cec9"))
                        .append(Component.text(" play.spworlds.ru",TextColor.fromCSSHexString("#81ecec"))))
                .setLore(Arrays.asList(Component.text("Перемещает на следуйщую стадию",TextColor.fromCSSHexString("#a29bfe"))))
                .build((event -> {
                    Game game = Game.getCurrentGame();
                    if (event.getEntity() instanceof Player player){
                        if (misklickList.contains(player.getUniqueId())){
                            return;
                        }
                        PlotPlayer plotPlayer = PlotPlayer.fromUUID(player.getUniqueId());
                        misklickList.add(player.getUniqueId());
                        plotPlayer.setStage(plotPlayer.getStage()+1);
                        plotPlayer.save(false);
                        Plot plot = SkinPlot.addPlotToEnd(plotPlayer.getStage());
                        if (plot instanceof SkinPlot skinPlot){
                            skinPlot.skinFill(true);
                            skinPlot.setOwner(plotPlayer);
                            skinPlot.setGenerated(true);
                            skinPlot.update();
                        }
                        if (plotPlayer.getCurrentPlot()!=null){
                            plotPlayer.getCurrentPlot().setOwner(null).update();
                        }
                        plotPlayer.setCurrentPlotId(plot.getPlotId());
                        plotPlayer.save(false);
                        player.teleport(plot.getPlotCenter().addX(-1).setY(game.getGameType().getY()-1+(plotPlayer.getStage())).getLocation().add(-0.5, 0, 0));
                        player.sendMessage(PREFIX.append(player.displayName().color(TextColor.fromCSSHexString("#55efc4"))).append(Component.text(" прошел в следуйщий этап", TextColor.fromCSSHexString("#00b894"))));
                        misklickList.remove(player.getUniqueId());
                    }

                }), "player_item");
        this.skin_ban_button = KickButton.fromItemsAdder("dalynkaa:remove_player")
                .setName(Component.text("Забанить на всех ", TextColor.fromCSSHexString("#00cec9"))
                        .append(Component.text(" ИВЕНТАХ СУКА",TextColor.fromCSSHexString("#81ecec"))))
                .setLore(Arrays.asList(Component.text("Запрещает участвовать на ивентах",TextColor.fromCSSHexString("#a29bfe"))))
                .build((event -> {
                    if (event.getEntity() instanceof Player player){
                        PlotPlayer plotPlayer = PlotPlayer.fromUUID(player.getUniqueId());
                        plotPlayer.setInGame(false);
                        plotPlayer.setCanJoin(false);
                        plotPlayer.setHasBan(true);
                        plotPlayer.getCurrentPlot().setOwner(null).update();
                        plotPlayer.save(false);
                        plotPlayer.getPlayer().kick(Component.text("Забанен!"));
                        Bukkit.broadcast(PREFIX.append(player.displayName().color(TextColor.fromCSSHexString("#d63031"))).append(Component.text(" ЗАБАНЕН на сервере. Причина - ", TextColor.fromCSSHexString("#ff7675"))).append(Component.text("Играй на play.spworlds.ru", TextColor.fromCSSHexString("#d63031"))));
                    }

                }), "player_item");
    }
    public void giveItems(PlotPlayer plotPlayer, fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game game){
        Player player = plotPlayer.getPlayer();
        player.getInventory().clear();
        if (plotPlayer.isAdmin()){
            player.getInventory().setItem(7,admin_button);
        }else if (plotPlayer.isVoter()){
            if (game.getGameStage().equals(GameStage.EVALUATION)){
                PlotVote.getVoteItems(plotPlayer, game);
            }
            player.getInventory().setItem(7,voter_button);
        }else {
            player.getInventory().setItem(7, new ItemStack(Material.AIR));
        }
        player.getInventory().setItem(8,player_button);
    }
    public void spawn(PlotPlayer plotPlayer, fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game game){
        Player player = plotPlayer.getPlayer();
        player.setFlySpeed(0.1f);
        player.teleport(EventBuilders.getInstance().config.getSpawnLocation());
        player.teleport(new Location(Bukkit.getWorld("world"),1000, 1000, 1000));
        player.teleport(EventBuilders.getInstance().config.getSpawnLocation());
        if (plotPlayer.isAdmin()){
            player.setGameMode(GameMode.CREATIVE);
        }else {
            player.setGameMode(GameMode.ADVENTURE);
        }
        if (game.getGameStage().equals(GameStage.GAME)){
            BossBar bossBar = Bukkit.getBossBar(NamespacedKey.fromString("timer"));
            bossBar.addPlayer(player);
        }else {
            BossBar bossBar = Bukkit.getBossBar(NamespacedKey.fromString("timer"));
            bossBar.removePlayer(player);
        }
    }
    public void giveSkinItems(PlotPlayer plotPlayer, Game game){
        Player player = plotPlayer.getPlayer();
        player.getInventory().clear();
        giveItems(plotPlayer, game);
        player.getInventory().setItem(0, skin_button);
        player.getInventory().setItem(1, skin_add_button);
        player.getInventory().setItem(2, skin_ban_button);
    }
}
