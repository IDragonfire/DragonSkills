package com.github.idragonfire.dragonskills.skills;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TargetBlockSkill;
import com.github.idragonfire.dragonskills.utils.DUtils;

public class Lightning extends TargetBlockSkill {

	public Lightning(DragonSkillsPlugin plugin) {
		super(plugin);
	}

	@Override
	public SkillResult use(Player player, Block targetBlock) {
		if (!DUtils.canBreak(player, targetBlock)) {
			return SkillResult.INVALID_TERRAIN;
		}
		targetBlock.getWorld().strikeLightning(DUtils.getLocationWithPlayerDelta(targetBlock));
		return SkillResult.SUCESSFULL;
	}

	@Override
	public String getDescription() {
		return "ligtning striktes at target";
	}

}
