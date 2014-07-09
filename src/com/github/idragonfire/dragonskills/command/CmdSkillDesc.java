package com.github.idragonfire.dragonskills.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;

public class CmdSkillDesc extends DCommand {

    public CmdSkillDesc(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String cmd,
            String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Please enter a skill name");
            return;
        }
        plugin.cmdSkillDesc(args[0], sender);
    }
}
