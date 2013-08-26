package com.github.idragonfire.dragonskills.skills;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.ActiveSkill;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TimeEffect;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public class WaterRing extends ActiveSkill implements Listener {
	@SkillConfig
	private int radius = 2;
	@SkillConfig
	private int height = 3;
	@SkillConfig
	private int waterRingLifetime = 3;

	protected Set<Block> waterBlocks;

	public WaterRing(DragonSkillsPlugin plugin) {
		super(plugin);
		waterBlocks = new HashSet<Block>();
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public SkillResult use(Player player) {
		List<Block> blocks = DUtils.getHCyl(player.getLocation().getBlock(), radius, radius, height, false);
		for (Block block : blocks) {
			if (block.getType() == Material.AIR && !DUtils.canBreak(player, block)) {
				return SkillResult.INVALID_TERRAIN;
			}
		}
		List<Block> transformedBlock = new ArrayList<Block>();
		for (Block block : blocks) {
			if (block.getType() == Material.AIR) {
				transformedBlock.add(block);
			}
		}
		new WaterRingEffect(plugin, transformedBlock, waterRingLifetime * DUtils.TICKS).startEffect();
		return SkillResult.SUCESSFULL;
	}

	@Override
	public String getDescription() {
		return "create a ring of water around you";
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockFromToEvent(BlockFromToEvent event) {
		if (!waterBlocks.contains(event.getBlock())) {
			return;
		}
		event.setCancelled(true);
	}

	public class WaterRingEffect extends TimeEffect {
		private List<Block> blocks;
		private BlockState[] states;

		public WaterRingEffect(DragonSkillsPlugin plugin, List<Block> blocks, long duration) {
			super(plugin, duration);
			this.blocks = blocks;
			states = new BlockState[blocks.size()];
		}

		@Override
		public void initTimeEffect() {
			int i = 0;
			for (Block b : blocks) {
				states[i] = b.getState();
				DUtils.addBlock(b);
				waterBlocks.add(b);
				b.setType(Material.WATER);
				i++;
			}
		}

		@Override
		public void endTimeEffect() {
			for (int i = 0; i < states.length; i++) {
				if (states[i].getBlock().getType() == Material.WATER
						|| states[i].getBlock().getType() == Material.STATIONARY_WATER) {
					states[i].update(true);
				}
				DUtils.removeBlock(states[i].getBlock());
				waterBlocks.remove(states[i].getBlock());
			}
		}
	}
}
