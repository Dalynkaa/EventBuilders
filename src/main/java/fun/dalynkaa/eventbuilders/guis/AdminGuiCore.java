package fun.dalynkaa.eventbuilders.guis;


import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameStage;
import fun.dalynkaa.eventbuilders.utils.dataClasses.Enums.GameType;
import fun.dalynkaa.eventbuilders.utils.dataClasses.games.Game;
import fun.dalynkaa.eventbuilders.utils.dataClasses.PlotPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class AdminGuiCore {
    Gui gui;
    public AdminGuiCore(PlotPlayer player){
        gui = Gui.gui()
                .title(Component.text(""))
                .rows(3)
                .disableAllInteractions().create();
        Game game = Game.getCurrentGame();
        if (game.getGameType().equals(GameType.NORMAL)){
            AdminGuiHelper.buildingGameType(game,player,gui);
        }else {
            AdminGuiHelper.skinItems(game,player,gui);
        }
        gui.open(player.getPlayer());
        if (game.getGameStage().equals(GameStage.NO_GAME)){
            TexturedInventoryWrapper.setPlayerInventoryTexture(player.getPlayer(),new FontImageWrapper("dalynkaa:admin_gui_1"),null,0,-8);
        } else if (game.getGameStage().equals(GameStage.GAME)) {
            TexturedInventoryWrapper.setPlayerInventoryTexture(player.getPlayer(),new FontImageWrapper("dalynkaa:admin_gui_2"),null,0,-8);
        }else {
            TexturedInventoryWrapper.setPlayerInventoryTexture(player.getPlayer(),new FontImageWrapper("dalynkaa:admin_gui_3"),null,0,-8);
        }
    }
}
