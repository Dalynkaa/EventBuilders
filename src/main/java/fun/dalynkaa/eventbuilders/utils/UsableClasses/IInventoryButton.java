package com.otsosity.spbuildrevrited.utils.UsableClasses;


import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public interface IInventoryButton<T extends Event> {
    /**
     * Executes the event passed to it
     *
     * @param event Inventory action
     */
    void execute(final T event);

}
