package com.github.idragonfire.dragonskills.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;

public class CmdSkill extends PlayerConsoleCommand {

    public CmdSkill(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onConsoleCommand(CommandSender sender, Command command,
            String cmd, String label, String[] args) {
        if (args.length != 2) {
            sender
                    .sendMessage("console-usage: /skill [skill name] [player name]");
        }
    }

    @Override
    public void onPlayerCommand(Player sender, Command command, String cmd,
            String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("skill name ist missing");
        }
        plugin.cmdSkill(args[0], sender, sender.getName());
    }
}
