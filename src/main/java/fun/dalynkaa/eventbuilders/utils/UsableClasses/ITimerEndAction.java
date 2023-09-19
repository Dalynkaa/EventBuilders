package fun.dalynkaa.eventbuilders.utils.UsableClasses;

import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;

public interface ITimerEndAction<T extends Game> {
    void execute(final T player);
}
