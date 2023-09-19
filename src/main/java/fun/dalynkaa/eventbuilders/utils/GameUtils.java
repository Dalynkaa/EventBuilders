package fun.dalynkaa.eventbuilders.utils;

import com.destroystokyo.paper.Title;
import fun.dalynkaa.eventbuilders.EventBuilders;
import fun.dalynkaa.eventbuilders.utils.UsableClasses.ITimerEndAction;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameStage;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class GameUtils {

    public static void broatcastActionBar(Component component){
        for (Player p: Bukkit.getOnlinePlayers()){
            p.sendActionBar(component);
        }
    }
    public static void broatcastTitle(Title title){
        for (Player p: Bukkit.getOnlinePlayers()){
            p.sendTitle(title);
        }
    }
    public static void startTimer(ITimerEndAction<Game> action){
        EventBuilders main = EventBuilders.getInstance();
        Bukkit.createBossBar(NamespacedKey.fromString("timer"),"До окончания строительства - ", BarColor.GREEN, BarStyle.SOLID).setVisible(true);
        AtomicReference<Integer> currentTime = new AtomicReference<>(main.config.getCurrentTime());
        main.timerId = Bukkit.getScheduler().runTaskTimerAsynchronously(main, ()->{
            Game game = Game.getCurrentGame();
            currentTime.updateAndGet(v -> v + 1);
            main.config.setCurrenttime(currentTime.get());
            if (currentTime.get() >= game.getGameTime()){
                action.execute(Game.getCurrentGame());
                main.config.resetTimer();
                Bukkit.getScheduler().cancelTask(main.timerId);
                BossBar boss = Bukkit.getBossBar(NamespacedKey.fromString("timer"));
                boss.setTitle("Ожидание начала оценки!");
            }
            BossBar bossBar = Bukkit.getBossBar(NamespacedKey.fromString("timer"));
            bossBar.setTitle("До окончания строительства - " + getDurationString(game.getGameTime()-currentTime.get()));
            bossBar.setProgress(1 - getPercentage(game.getGameTime(), currentTime.get()));
        }, 0, 20).getTaskId();
    }
    public static void ResetAllPlayers(Game game){
        if (!game.getGameStage().equals(GameStage.NO_GAME)){
            return;
        }
        for(PlotPlayer player: Objects.requireNonNull(PlotPlayer.getAllPlayers())){
            player.setInGame(false);
            player.setCurrentPlotId(null);
            player.setCanJoin(true);
            player.save(false);
            player.setStage(1);
        }
    }
    public static double getPercentage(final double total, final double progress) {
        try {
            return (progress / (total / 100d))/100;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    public static String getDurationString(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
    }

    public static String twoDigitString(int number) {
        if (number == 0){
            return "00";
        }
        if (number / 10 == 0) {
            return "0" + number;
        }
        return String.valueOf(number);
    }
    public static void giveControllItems(){

    }
}
