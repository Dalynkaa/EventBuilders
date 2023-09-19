package fun.dalynkaa.eventbuilders.customEvents;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class regionEnterEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;
    private ProtectedRegion protectedRegion;
    private Player player;
    public regionEnterEvent(ProtectedRegion region, Player player){
        this.protectedRegion = region;
        this.player = player;
    }
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public ProtectedRegion getProtectedRegion() {
        return protectedRegion;
    }

    public regionEnterEvent setProtectedRegion(ProtectedRegion protectedRegion) {
        this.protectedRegion = protectedRegion;
        return this;
    }

    public Player getPlayer() {
        return player;
    }

    public regionEnterEvent setPlayer(Player player) {
        this.player = player;
        return this;
    }
}
