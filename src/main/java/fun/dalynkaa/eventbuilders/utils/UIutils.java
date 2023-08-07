package com.otsosity.spbuildrevrited.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UIutils {
    public UIutils(Player player){
        ItemStack head = HeadUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ4ODU0NWQ1N2M5ZWVkNTJjM2U1NDdlOTZjNDVkYWJiYjdjZjVmOThkNGM4ZmU2MWRjNmY2OWFiYTBhZWY5NiJ9fX0=");
        ItemMeta meta = head.getItemMeta();
        meta.displayName(Component.text("Админ меню", TextColor.fromCSSHexString("#d63031")));
        head.setItemMeta(meta);
        player.getInventory().addItem(head);

    }
}
