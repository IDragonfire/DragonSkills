package com.github.idragonfire.dragonskills.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;

public class CmdCd extends PlayerConsoleCommand {

    public CmdCd(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onConsoleCommand(CommandSender sender, Command command,
            String cmd, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("console-usage: /cd [skill name] [player name]");
            return;
        }
        plugin.cmdCd(args[0], sender, args[1]);
    }

    @Override
    public void onPlayerCommand(Player sender, Command command, String cmd,
            String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Please enter a skill name");
            return;
        }
        plugin.cmdCd(args[0], sender, sender.getName());
    }

}
