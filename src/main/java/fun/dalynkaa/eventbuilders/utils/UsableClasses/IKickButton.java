package fun.dalynkaa.eventbuilders.utils.UsableClasses;

import org.bukkit.event.Event;

public interface IKickButton<T extends Event> {
    /**
     * Executes the event passed to it
     *
     * @param event Inventory action
     */
    void execute(final T event);

}