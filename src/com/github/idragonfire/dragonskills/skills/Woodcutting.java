package com.github.idragonfire.dragonskills.skills;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.DSystem;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TargetBlockSkill;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public class Woodcutting extends TargetBlockSkill {
	@SkillConfig
	private int maxLogAmount = 64;
	@SkillConfig
	private int maxDistance = 4;

	private static BlockFace[] directions = new BlockFace[] { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH,
			BlockFace.WEST, BlockFace.UP };

	public Woodcutting(DragonSkillsPlugin plugin) {
		super(plugin);
	}

	@Override
	public SkillResult use(Player player, Block targetBlock) {
		int amount = 0;
		// first walk down
		amount = walkInDirection(player, BlockFace.DOWN, targetBlock, amount);
		// then walk up
		amount = walkInDirection(player, BlockFace.UP, targetBlock.getRelative(BlockFace.UP), amount);

		DSystem.log("fell $1 log", amount);
		return SkillResult.SUCESSFULL;
	}

	private boolean isTreeMaterial(Material mat) {

		return mat == Material.LOG;
	}

	private int walkInDirection(Player player, BlockFace direction, Block startBlock, int amount) {

		BlockBreakEvent event;
		while (amount < maxLogAmount && isTreeMaterial(startBlock.getType())) {
			// check if blocked
			event = new BlockBreakEvent(startBlock, player);
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				amount = breakLog(startBlock, player, amount);
			}
			startBlock = startBlock.getRelative(direction);
		}
		return amount;
	}

	public int breakLog(Block block, Player player, int amount) {
		block.breakNaturally(player.getItemInHand());
		amount++;
		if (amount >= maxLogAmount) {
			return amount;
		}
		List<Block> blocks = DUtils.getHCyl(block, maxDistance, maxDistance, 1, true);
		for (Block b : blocks) {
			if (!isTreeMaterial(b.getType()) || isNewTree(b)) {
				continue;
			}
			b.breakNaturally(player.getItemInHand());
			amount++;
			if (amount >= maxLogAmount) {
				return amount;
			}
		}
		return amount;
	}

	private boolean isNewTree(Block b) {
		if (b.getRelative(BlockFace.DOWN).getType() == Material.LOG) {
			return true;
		}
		for (int i = 0; i < directions.length; i++) {
			if (b.getRelative(directions[i]).getType() == Material.LEAVES) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String getDescription() {
		return DSystem.paramString("fell a tree, maximal $1 blocks", maxLogAmount);
	}

}
