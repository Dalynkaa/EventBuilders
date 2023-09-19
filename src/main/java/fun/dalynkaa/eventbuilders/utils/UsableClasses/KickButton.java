package fun.dalynkaa.eventbuilders.utils.UsableClasses;



import dev.lone.itemsadder.api.CustomStack;
import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.utils.HeadUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class KickButton {
    private ItemStack itemStack;
    private Component name;
    private List<Component> lore;
    private IKickButton<EntityDamageByEntityEvent> action;
    public KickButton(ItemStack item){
        this.action = null;
        this.name = null;
        this.lore = null;
        this.itemStack = item;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public KickButton setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public Component getName() {
        return name;
    }

    public KickButton setName(Component name) {
        this.name = name;
        return this;
    }

    public List<Component> getLore() {
        return lore;
    }

    public KickButton setLore(List<Component> lore) {
        this.lore = lore;
        return this;
    }

    public IKickButton<EntityDamageByEntityEvent> getAction() {
        return action;
    }

    public static KickButton from(ItemStack stack){
        return new KickButton(stack);
    }
    public static KickButton from(Material material){
        ItemStack stack = new ItemStack(material);
        return new KickButton(stack);
    }
    public static KickButton fromHead(String url){
        ItemStack stack = HeadUtils.getCustomHead(url);
        return new KickButton(stack);
    }
    public static KickButton fromItemsAdder(String id){
        CustomStack customStack = CustomStack.getInstance(id);

        return new KickButton(customStack.getItemStack());

    }
    public ItemStack build(IKickButton<EntityDamageByEntityEvent> action, String id){
        this.action = action;
        String uuid = UUID.randomUUID().toString();
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.displayName(this.name);
        meta.lore(lore);
        meta.getPersistentDataContainer().set(NamespacedKey.fromString("item_id"), PersistentDataType.STRING, uuid);
        meta.getPersistentDataContainer().set(NamespacedKey.fromString("gui_item"), PersistentDataType.BOOLEAN, true);
        meta.getPersistentDataContainer().set(NamespacedKey.fromString("action"), PersistentDataType.BOOLEAN, true);
        this.itemStack.setItemMeta(meta);
        EventBuilders.getInstance().kickMap.put(uuid,this);
        return this.itemStack;
    }
}
