package fun.dalynkaa.eventbuilders.plotusercommand.subcommands;

import fun.dalynkaa.eventbuilders.plotusercommand.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class VoterCommand extends SubCommand {


    @Override
    public String getName() {
        return "donate";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public void perform(Player player, String[] args) {
        for (String a: args){
            Bukkit.getLogger().info(a);
        }
        if (args.length<3){
            return;
        }

        if (Objects.equals(args[2], "add") && args.length == 4){
            try {
                Integer count = Integer.parseInt(args[3]);
                if (MineUser.fromName(args[0])==null){
                    player.sendMessage(MiningSimulator.inst().format(MiningSimulator.inst().PREFIX+"игрок с таким ником не найден"));
                    return;
                }
                MineUser user = MineUser.fromName(args[0]);
                user.addDonate(count);
                player.sendMessage(Component.text("Игроку добавлено ", TextColor.fromCSSHexString("#a29bfe")).append(Component.text(count+" Доната",TextColor.fromCSSHexString("#6c5ce7"))));
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        if (Objects.equals(args[2], "set") && args.length == 4){
            try {
                Integer count = Integer.parseInt(args[3]);
                if (MineUser.fromName(args[0])==null){
                    player.sendMessage(MiningSimulator.inst().format(MiningSimulator.inst().PREFIX+"игрок с таким ником не найден"));
                    return;
                }
                MineUser user = MineUser.fromName(args[0]);
                user.setDonate(count);
                player.sendMessage(Component.text("Игроку установлено ", TextColor.fromCSSHexString("#a29bfe")).append(Component.text(count+" Доната",TextColor.fromCSSHexString("#6c5ce7"))));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (Objects.equals(args[2], "remove") && args.length == 4){
            try {
                Integer count = Integer.parseInt(args[3]);
                if (MineUser.fromName(args[0])==null){
                    player.sendMessage(MiningSimulator.inst().format(MiningSimulator.inst().PREFIX+"игрок с таким ником не найден"));
                    return;
                }
                MineUser user = MineUser.fromName(args[0]);
                if (!user.hasMonney(count)) {
                    player.sendMessage(MiningSimulator.inst().format(MiningSimulator.inst().PREFIX + "у игрока нету такого количества денег"));
                    return;
                }
                user.removeDonate(count);
                player.sendMessage(Component.text("У игрока убрано", TextColor.fromCSSHexString("#a29bfe")).append(Component.text(count+" Доната",TextColor.fromCSSHexString("#6c5ce7"))));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (Objects.equals(args[2], "get") && args.length == 3){
            try {
                if (MineUser.fromName(args[0])==null){
                    player.sendMessage(MiningSimulator.inst().format(MiningSimulator.inst().PREFIX+"игрок с таким ником не найден"));
                    return;
                }
                MineUser user = MineUser.fromName(args[0]);
                player.sendMessage(Component.text("У игрока ", TextColor.fromCSSHexString("#a29bfe")).append(Component.text(user.getDonate()+" Доната",TextColor.fromCSSHexString("#6c5ce7"))));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        if (args.length == 3){
            return Arrays.asList("add","set","get","remove");
        }
        if (Objects.equals(args[2], "add") && args.length == 4){
            return Arrays.asList("<количество>");
        }
        if (Objects.equals(args[2], "set") && args.length == 4){
            return Arrays.asList("<количество>");
        }
        if (Objects.equals(args[2], "remove") && args.length == 4){
            return Arrays.asList("<количество>");
        }
        if (Objects.equals(args[2], "get") && args.length == 4){
            return null;
        }
        return null;
    }
}
