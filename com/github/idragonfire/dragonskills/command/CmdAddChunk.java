package com.github.idragonfire.dragonskills.command;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;

public class CmdAddChunk extends PlayerCommand {

	public CmdAddChunk(DragonSkillsPlugin plugin) {
		super(plugin);
	}

	@Override
	public void onPlayerCommand(Player sender, Command command, String cmd, String label, String[] args) {
		plugin.addChunk(sender.getLocation().getChunk(), sender);
	}
}
