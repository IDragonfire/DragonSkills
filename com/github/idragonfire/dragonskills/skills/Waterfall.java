package com.github.idragonfire.dragonskills.skills;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.DSystem;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TargetBlockSkill;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public class Waterfall extends TargetBlockSkill {

    @SkillConfig
    private int waterHeight = 3;
    @SkillConfig
    private int waterLifetime = 8;

    public Waterfall(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public SkillResult use(Player player, Block targetBlock) {
        for (int i = 0; i < waterHeight; i++) {
            targetBlock = targetBlock.getRelative(BlockFace.UP);
        }
        if (targetBlock.getType() != Material.AIR) {
            DSystem.log("only allowed in the air");
            return SkillResult.FAIL;
        }
        final Block a = targetBlock;
        boolean sucessfull = DUtils.transformBlock(player, targetBlock,
                Material.WATER);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (a.getType() == Material.WATER
                        || a.getType() == Material.STATIONARY_WATER) {
                    a.setType(Material.AIR);
                }
            }
        }, waterLifetime * DUtils.TICKS);
        return sucessfull ? SkillResult.SUCESSFULL : SkillResult.FAIL;
    }

    @Override
    public String getDescription() {
        return DSystem.paramString(
                "spawn a waterfall $1 over the cursor for $2 seconds",
                waterHeight, waterLifetime);
    }
}
