package com.github.idragonfire.dragonskills.command;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;

public class CmdRemoveChunk extends PlayerCommand {

	public CmdRemoveChunk(DragonSkillsPlugin plugin) {
		super(plugin);
	}

	@Override
	public void onPlayerCommand(Player sender, Command command, String cmd, String label, String[] args) {
		plugin.removeChunk(sender.getLocation().getChunk(), sender);
	}

}
