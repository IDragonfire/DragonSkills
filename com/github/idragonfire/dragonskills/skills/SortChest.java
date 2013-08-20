package com.github.idragonfire.dragonskills.skills;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TargetBlockSkill;

public class SortChest extends TargetBlockSkill {

	public SortChest(DragonSkillsPlugin plugin) {
		super(plugin);
	}

	@Override
	public SkillResult use(Player player, Block targetBlock) {
		if (targetBlock.getType() != Material.CHEST && targetBlock.getType() != Material.TRAPPED_CHEST) {
			return SkillResult.INVALID_TERRAIN;
		}
		InventoryHolder block = (InventoryHolder) targetBlock.getState();
		Inventory inventory = block.getInventory();

		// check permission
		InventoryOpenEvent event = new InventoryOpenEvent(player.openInventory(inventory));
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return SkillResult.INVALID_TERRAIN;
		}

		List<ItemStack> content = Arrays.asList(inventory.getContents());
		inventory.clear();
		Collections.sort(content, new Comparator<ItemStack>() {

			@Override
			public int compare(ItemStack o1, ItemStack o2) {
				if (o2 == null) {
					return -1;
				}
				if (o1 == null) {
					return 1;
				}
				int compare = o1.getTypeId() - o2.getTypeId();
				if (compare == 0) {
					compare = o2.getAmount() - o1.getAmount();
				}
				return compare;
			}
		});
		// add item stack after stack to combine stacks
		for (int i = 0; i < content.size(); i++) {
			if (content.get(i) != null) {
				inventory.addItem(content.get(i));
			}
		}
		// inventory.setContents(content.toArray(new ItemStack[0]));
		return SkillResult.SUCESSFULL;
	}

	@Override
	public String getDescription() {
		return "sort a chest";
	}

}
