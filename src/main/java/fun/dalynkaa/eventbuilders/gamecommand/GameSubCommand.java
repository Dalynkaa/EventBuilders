package com.otsosity.spbuildrevrited.commands.gamecommand;

import org.bukkit.entity.Player;

import java.util.List;

public abstract class GameSubCommand {

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract void perform(Player player, String args[]);

    public abstract List<String> getSubcommandArguments(Player player, String args[]);

}
