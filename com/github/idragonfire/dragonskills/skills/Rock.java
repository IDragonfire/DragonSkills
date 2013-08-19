package com.github.idragonfire.dragonskills.skills;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.DSystem;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TargetBlockSkill;
import com.github.idragonfire.dragonskills.api.TimeEffect;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public class Rock extends TargetBlockSkill {
	@SkillConfig
	private int rockLifetime = 8;

	public Rock(DragonSkillsPlugin plugin) {
		super(plugin);
	}

	@Override
	public SkillResult use(Player player, Block targetBlock) {
		List<Block> soulsand = DUtils.getHCyl(targetBlock, 3, 3, 1, true);
		targetBlock = targetBlock.getRelative(BlockFace.UP);
		soulsand.addAll(Arrays.asList(DUtils.getHCube(targetBlock, 3)));
		int lastIndex = soulsand.size() - 1;
		soulsand.add(soulsand.get(lastIndex).getRelative(BlockFace.UP));
		soulsand.add(soulsand.get(lastIndex - 2).getRelative(BlockFace.UP));
		soulsand.add(soulsand.get(lastIndex - (3 + 3))
				.getRelative(BlockFace.UP));
		soulsand.add(soulsand.get(lastIndex - (3 + 3 + 2)).getRelative(
				BlockFace.UP));
		for (Block b : soulsand) {
			if (!DUtils.canBreak(player, b)) {
				return SkillResult.INVALID_TERRAIN;
			}
		}

		Block[] dirt = new Block[3 * 4 + 1];
		dirt[0] = targetBlock.getRelative(BlockFace.UP, 4);
		Block tmp = targetBlock.getRelative(BlockFace.UP);
		dirt[1] = tmp.getRelative(BlockFace.NORTH);
		dirt[2] = tmp.getRelative(BlockFace.EAST);
		dirt[3] = tmp.getRelative(BlockFace.SOUTH);
		dirt[4] = tmp.getRelative(BlockFace.WEST);
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 4; j++) {
				dirt[1 + 4 + 4 * i + j] = dirt[j + 1].getRelative(BlockFace.UP,
						i + 1);
			}
		}
		for (Block b : dirt) {
			if (!DUtils.canBreak(player, b)) {
				return SkillResult.INVALID_TERRAIN;
			}
		}
		new RockEffect(plugin, soulsand, dirt, rockLifetime * DUtils.TICKS)
				.startEffect();
		return SkillResult.SUCESSFULL;
	}

	@Override
	public String getDescription() {
		return DSystem.paramString("spawn a rock for $1 seconds", rockLifetime);
	}

	public class RockEffect extends TimeEffect {
		private BlockState[] stateSoulSand;
		private BlockState[] stateDirt;
		private List<Block> soulsand;
		private Block[] dirt;

		public RockEffect(DragonSkillsPlugin plugin, List<Block> soulsand,
				Block[] dirt, long duration) {
			super(plugin, duration);
			this.soulsand = soulsand;
			this.dirt = dirt;
			stateSoulSand = new BlockState[soulsand.size()];
			stateDirt = new BlockState[dirt.length];
		}

		@Override
		public void initTimeEffect() {
			int i = 0;
			for (Block b : soulsand) {
				DUtils.addBlock(b);
				stateSoulSand[i] = b.getState();
				b.setType(Material.SOUL_SAND);
				i++;
			}
			for (int j = 0; j < dirt.length; j++) {
				DUtils.addBlock(dirt[j]);
				stateDirt[j] = dirt[j].getState();
				dirt[j].setType(Material.DIRT);
			}
		}

		@Override
		public void endTimeEffect() {
			for (int i = 0; i < stateSoulSand.length; i++) {
				DUtils.removeBlock(stateSoulSand[i].getBlock());
				if (stateSoulSand[i].getBlock().getType() == Material.SOUL_SAND) {
					stateSoulSand[i].update(true);
				}
			}
			for (int i = 0; i < stateDirt.length; i++) {
				DUtils.removeBlock(stateDirt[i].getBlock());
				if (stateDirt[i].getBlock().getType() == Material.DIRT) {
					stateDirt[i].update(true);
				}
			}
		}
	}
}
