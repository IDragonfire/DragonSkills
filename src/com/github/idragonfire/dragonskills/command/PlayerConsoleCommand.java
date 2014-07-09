package com.github.idragonfire.dragonskills.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;

public abstract class PlayerConsoleCommand extends PlayerCommand {

    public PlayerConsoleCommand(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String cmd,
            String label, String[] args) {
        if (!(sender instanceof Player)) {
            onConsoleCommand(sender, command, cmd, label, args);
            return;
        }
        onPlayerCommand((Player) sender, command, cmd, label, args);
    }

    public abstract void onConsoleCommand(CommandSender sender,
            Command command, String cmd, String label, String[] args);

}
