package fun.dalynkaa.eventbuilders.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class HeadUtils {
    public static ItemStack getCustomHead(String url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (url.isEmpty()) {
            return head;
        }

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(),"Ddsf");

        profile.setProperty(new ProfileProperty("textures", url));


        headMeta.setPlayerProfile(profile);
        head.setItemMeta(headMeta);
        return head;
    }
}
