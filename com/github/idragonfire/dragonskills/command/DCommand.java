package com.github.idragonfire.dragonskills.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;

public abstract class DCommand {
    protected DragonSkillsPlugin plugin;

    public DCommand(DragonSkillsPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract void onCommand(CommandSender sender, Command command,
            String cmdName, String label, String[] args);
}
