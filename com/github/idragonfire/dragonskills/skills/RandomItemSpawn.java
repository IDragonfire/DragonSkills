package com.github.idragonfire.dragonskills.skills;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.ActiveSkill;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public class RandomItemSpawn extends ActiveSkill {
	@SkillConfig
	private ArrayList<Material> spawnItems = new ArrayList<Material>(
			Arrays.asList(new Material[] { Material.WOOD_PICKAXE,
					Material.STONE_PICKAXE, Material.IRON_PICKAXE,
					Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE }));

	public RandomItemSpawn(DragonSkillsPlugin plugin) {
		super(plugin);
	}

	@Override
	public SkillResult use(Player player) {
		ItemStack is = new ItemStack(this.spawnItems.get(DUtils
				.nextInt(spawnItems.size())));
		player.getWorld().dropItemNaturally(player.getLocation(), is);
		return SkillResult.SUCESSFULL;
	}

	@Override
	public String getDescription() {
		return "Spawm a random item";
	}
}
