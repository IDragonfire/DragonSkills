package com.github.idragonfire.dragonskills.skills;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.ActiveSkill;
import com.github.idragonfire.dragonskills.api.DSystem;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TickEffect;
import com.github.idragonfire.dragonskills.api.TimeEffect;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public class LightWave extends ActiveSkill {
	@SkillConfig
	private int distance = 16;

	public LightWave(DragonSkillsPlugin plugin) {
		super(plugin);
	}

	@Override
	public SkillResult use(Player player) {
		BlockFace[] faces = DUtils.getDirections(player);
		Block[] blocks = new Block[5];
		blocks[2] = player.getLocation().getBlock()
				.getRelative(faces[DUtils.FRONT]).getRelative(BlockFace.UP);
		blocks[1] = blocks[2].getRelative(faces[DUtils.LEFT]).getRelative(
				faces[DUtils.BACK]);
		blocks[0] = blocks[1].getRelative(faces[DUtils.LEFT]).getRelative(
				faces[DUtils.BACK]);
		blocks[3] = blocks[1].getRelative(faces[DUtils.RIGHT], 2);
		blocks[4] = blocks[0].getRelative(faces[DUtils.RIGHT], 4);
		new LightWaveEffect(plugin, player, blocks, faces[DUtils.FRONT],
				distance).start();
		return SkillResult.SUCESSFULL;
	}

	@Override
	public String getDescription() {
		return DSystem.paramString("send out a wave of light that flights $1 blocks", distance); 
	}

	public class LightWaveEffect extends TickEffect {
		private Block[] blocks;
		private BlockState[] states;
		private Player player;
		private BlockFace front;

		public LightWaveEffect(DragonSkillsPlugin plugin, Player player,
				Block[] blocks, BlockFace front, int distance) {
			super(plugin, 0, 1, distance);
			this.blocks = blocks;
			this.states = new BlockState[blocks.length];
			this.player = player;
			this.front = front;
		}

		@Override
		protected void effectTick() {
			restore();
			for (int i = 0; i < blocks.length; i++) {
				if (DUtils.canBreak(player, blocks[i])) {
					DUtils.addBlock(blocks[i]);
					states[i] = blocks[i].getState();
					blocks[i].setType(Material.GOLD_BLOCK);
					blocks[i] = blocks[i].getRelative(front);
				}
			}
		}
		
		@Override
		protected void endEffect() {
			restore();
		}

		public void restore() {
			for (int i = 0; i < states.length; i++) {
				if (states[i] != null
						&& states[i].getBlock().getType() == Material.GOLD_BLOCK) {
					DUtils.removeBlock(states[i].getBlock());
					states[i].update(true);
					states[i] = null;
				}
			}
		}
	}

}
