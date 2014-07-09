package com.github.idragonfire.dragonskills.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;

public class CmdSkills extends DCommand {

    public CmdSkills(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String cmd,
            String label, String[] args) {
        plugin.cmdSkills(sender);
    }
}
