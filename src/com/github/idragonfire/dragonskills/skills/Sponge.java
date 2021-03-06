package com.github.idragonfire.dragonskills.skills;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.DSystem;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TargetBlockSkill;
import com.github.idragonfire.dragonskills.api.TimeEffect;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public class Sponge extends TargetBlockSkill {
	@SkillConfig
	private int radius = 4;
	@SkillConfig
	private int spongeLifeTime = 5;

	private HashSet<Material> allowedMaterials = new HashSet<Material>(Arrays.asList(new Material[] { Material.AIR,
			Material.WATER, Material.STATIONARY_WATER }));

	public Sponge(DragonSkillsPlugin plugin) {
		super(plugin);
		setTargetBlockMaxDistance(8);
		HashSet<Byte> transparentIds = new HashSet<Byte>();
		for (Material material : allowedMaterials) {
			transparentIds.add((byte) material.getId());
		}
		setTransparentBlocks(transparentIds);
	}

	@Override
	public SkillResult use(Player player, Block targetBlock) {
		Block sponge = spawnSponge(player, targetBlock);
		if (sponge == null) {
			sponge = targetBlock.getRelative(BlockFace.UP);
			if (sponge == null) {
				DSystem.log("fail to cast sponge");
				return SkillResult.FAIL;
			}
		}
		List<Block> blocks = DUtils.sphere(targetBlock, radius, radius, radius, true);
		for (Block b : blocks) {
			if (!DUtils.canBreak(player, b)) {
				DSystem.log("no permission");
				return SkillResult.FAIL;
			}
		}
		SpongeEffect effect = new SpongeEffect(plugin, spongeLifeTime * DUtils.TICKS, new HashSet<Block>(blocks),
				sponge);
		DUtils.transformBlock(player, sponge, Material.SPONGE);
		effect.startEffect();
		for (Block block : blocks) {
			if (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER) {
				try {
					DUtils.transformBlock(player, block, Material.AIR);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return SkillResult.SUCESSFULL;
	}

	public Block spawnSponge(Player player, Block targetBlock) {
		if (allowedMaterials.contains(targetBlock.getType()) && !DUtils.isForbiddenToTransform(player, targetBlock)) {
			return targetBlock;
		}
		return null;
	}

	@Override
	public String getDescription() {
		return DSystem.paramString("Spawn a sponge for $1 seconds", spongeLifeTime);
	}

	public class SpongeEffect extends TimeEffect implements Listener {
		private Block sponge;
		private Set<Block> blocks;

		public SpongeEffect(DragonSkillsPlugin plugin, long duration, Set<Block> blocks, Block sponge) {
			super(plugin, duration);
			this.blocks = blocks;
			this.sponge = sponge;
		}

		@Override
		public void initTimeEffect() {
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		}

		@Override
		public void endTimeEffect() {
			HandlerList.unregisterAll(this);
			// TODO: unsafe
			sponge.setType(Material.AIR);
		}

		@EventHandler(ignoreCancelled = true)
		public void onBlockFromToEvent(BlockFromToEvent event) {
			if (blocks.contains(event.getBlock())) {
				event.setCancelled(true);
			}
		}

		@EventHandler(ignoreCancelled = true)
		public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
			if (blocks.contains(event.getBlockClicked())) {
				event.setCancelled(true);
			}
		}

		@EventHandler(ignoreCancelled = true)
		public void onBlockBreakEvent(BlockBreakEvent event) {
			if (sponge.equals(event.getBlock())) {
				event.setCancelled(true);
			}
		}
	}

}
