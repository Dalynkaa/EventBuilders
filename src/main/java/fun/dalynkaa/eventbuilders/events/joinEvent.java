package com.otsosity.spbuildrevrited.events;


import com.otsosity.spbuildrevrited.SpBuildRevrited;
import com.otsosity.spbuildrevrited.guis.AdminGuiCore;
import com.otsosity.spbuildrevrited.utils.UsableClasses.InventoryButton;
import com.otsosity.spbuildrevrited.utils.dataClasses.Game;
import com.otsosity.spbuildrevrited.utils.dataClasses.PlotPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class joinEvent implements Listener {
    ItemStack admin_button;
    ItemStack player_button;
    ItemStack voter_button;
    public joinEvent(SpBuildRevrited spBuildRevrited) {
        spBuildRevrited.getServer().getPluginManager().registerEvents(this,spBuildRevrited);
        this.admin_button = InventoryButton.fromHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ4ODU0NWQ1N2M5ZWVkNTJjM2U1NDdlOTZjNDVkYWJiYjdjZjVmOThkNGM4ZmU2MWRjNmY2OWFiYTBhZWY5NiJ9fX0=")
                .setName(Component.text("Админ", TextColor.fromCSSHexString("#d63031"))
                        .append(Component.text("Меню",TextColor.fromCSSHexString("#ff7675"))))
                .setLore(Arrays.asList(Component.text("Открывает админское меню",TextColor.fromCSSHexString("#a29bfe"))))
                .build((event -> {
                    AdminGuiCore adminGuiCore = new AdminGuiCore();
                    adminGuiCore.openMenu(event.getPlayer());
                    event.getPlayer().sendMessage("Админское меню");
                }));
        this.player_button = InventoryButton.fromHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzBjOWEzYTRmZmJlZTYxZDFlZTFjM2E1MzMzNTViZGE5Y2RjMzc3ZTA3YjBmZjhiYzYxOGQzOTc3YjdmODZjYyJ9fX0=")
                .setName(Component.text("Меню", TextColor.fromCSSHexString("#00cec9"))
                        .append(Component.text("Игрока",TextColor.fromCSSHexString("#81ecec"))))
                .setLore(Arrays.asList(Component.text("Открывает меню игрока",TextColor.fromCSSHexString("#a29bfe"))))
                .build((event -> {
                    event.getPlayer().sendMessage("Меню игрока");
                }));
        this.voter_button = InventoryButton.fromHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjVhNzk5N2I1MDlmM2FkNDBlZjUzYjhmMGU1MGU0MTU5ZjBmYWEzNjQzMDAyZjUyYzgzYWUyZmUxODViMTJhNiJ9fX0=")
                .setName(Component.text("Меню", TextColor.fromCSSHexString("#0984e3"))
                        .append(Component.text("Голосования",TextColor.fromCSSHexString("#74b9ff"))))
                .setLore(Arrays.asList(Component.text("Открывает меню голосуещего",TextColor.fromCSSHexString("#a29bfe"))))
                .build((event -> {
                    event.getPlayer().sendMessage("Меню голосуещего");
                }));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if (!SpBuildRevrited.getInstance().db.userHasAccount(player.getUniqueId().toString())){
            PlotPlayer plotPlayer = new PlotPlayer(player.getUniqueId(),player.getDisplayName());
            plotPlayer.save();
        }
        PlotPlayer plotPlayer = PlotPlayer.fromUUID(player.getUniqueId());
        if (plotPlayer.isVoter()){
            player.getInventory().setItem(7,voter_button);
        }
        if (player.hasPermission("spbuild.admin")){
            player.getInventory().setItem(7,admin_button);
        }
        player.getInventory().setItem(8,player_button);
        Game.getCurrentGame();
    }

}
