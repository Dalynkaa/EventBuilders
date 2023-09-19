package fun.dalynkaa.eventbuilders.guis.acceptGui;

import org.bukkit.event.Event;

public interface IAccept<T extends Event> {
    void execute(final T event);
}
