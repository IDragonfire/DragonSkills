package com.github.idragonfire.dragonskills.command;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;

public class CmdUnbind extends PlayerCommand {

    public CmdUnbind(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onPlayerCommand(Player sender, Command command, String cmd,
            String label, String[] args) {
        plugin.cmdUnbindMaterial(sender.getItemInHand().getType(), sender,
                sender.getName());
    }
}
