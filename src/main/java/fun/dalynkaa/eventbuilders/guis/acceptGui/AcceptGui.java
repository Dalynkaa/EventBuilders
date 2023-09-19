package fun.dalynkaa.eventbuilders.guis.acceptGui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class AcceptGui {
    public @NotNull Gui gui;
    List<Integer> decicle = Arrays.asList(0,1,2,3);
    List<Integer> accept = Arrays.asList(5,6,7,8);
    public AcceptGui(String action,HumanEntity p , @NotNull IAccept<InventoryClickEvent> acceptation , @NotNull IAccept<InventoryClickEvent> deselection){
        gui = Gui.gui()
                .title(Component.text(""))
                .rows(1).disableItemPlace().disableItemDrop().disableItemSwap().disableItemTake()
                .create();
        GuiItem acceptitem = setItem(Component.text("Применить", TextColor.fromCSSHexString("#00b894")),Arrays.asList(Component.text("Нажми что бы подтвердить действие",TextColor.fromCSSHexString("#55efc4"))), ItemType.ACCEPT).asGuiItem(acceptation::execute);
        GuiItem decikleitem = setItem(Component.text("Отменить", TextColor.fromCSSHexString("#d63031")),Arrays.asList(Component.text("Нажми что бы отменить действие",TextColor.fromCSSHexString("#ff7675"))),ItemType.DECIKLE).asGuiItem(deselection::execute);
        GuiItem infoitem = setItem(Component.text("Действие - "+action, TextColor.fromCSSHexString("#6c5ce7")),Arrays.asList(Component.text("Этот интерфейс согдан для подтверждения действия",TextColor.fromCSSHexString("#a29bfe"))),ItemType.MIDDLE).asGuiItem(event -> {

        });
        for (int i=0;i<=8;i++){
            if (accept.contains(i)){
                gui.setItem(i,acceptitem);
            }
            if (4==i){
                gui.setItem(i,infoitem);
            }
            if (decicle.contains(i)){
                gui.setItem(i,decikleitem);
            }
        }
        gui.open(p);
    }

    public ItemBuilder setItem(Component name, List<Component> lore, ItemType itemType){
        ItemStack item;
        if (itemType.equals(ItemType.ACCEPT)){
            item = new ItemStack(Material.LIME_WOOL);
        } else if (itemType.equals(ItemType.DECIKLE)) {
            item = new ItemStack(Material.RED_WOOL);
        } else {
            item = new ItemStack(Material.WHITE_WOOL);
        }
        return ItemBuilder.from(item).name(name).lore(lore).glow(true);
    }

    public void open(HumanEntity p){
        gui.open(p);
    }
    public enum ItemType{
        ACCEPT,
        DECIKLE,
        MIDDLE;
    }
}
