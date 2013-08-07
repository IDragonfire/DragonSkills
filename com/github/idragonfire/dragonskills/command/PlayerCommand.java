package com.github.idragonfire.dragonskills.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;

public abstract class PlayerCommand extends DCommand {

    public PlayerCommand(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String cmd,
            String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return;
        }
        onPlayerCommand((Player) sender, command, cmd, label, args);
    }

    public abstract void onPlayerCommand(Player sender, Command command,
            String cmd, String label, String[] args);
}
