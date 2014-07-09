package com.github.idragonfire.dragonskills.skills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.DSystem;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TargetBlockSkill;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public class Iceland extends TargetBlockSkill {
    @SkillConfig
    public int duration = 10;

    public Iceland(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public SkillResult use(Player player, Block targetBlock) {
        Block[] blocks = DUtils.getHCube(targetBlock, 15);
        for (int i = 0; i < blocks.length; i++) {
            if (!DUtils.canBreak(player, blocks[i])) {
                return SkillResult.INVALID_TERRAIN;
            }
        }
        final List<BlockState> states = new ArrayList<BlockState>();
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i].getType() != Material.AIR) {
                states.add(blocks[i].getState());
                blocks[i].setType(Material.ICE);
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                for (BlockState state : states) {
                    if (state.getBlock().getType() == Material.ICE) {
                        state.update(true);
                    }
                }
            }
        }, duration * DUtils.TICKS);

        return SkillResult.SUCESSFULL;
    }

    @Override
    public String getDescription() {
        return DSystem.paramString(
                "transform the land into ice for $1 seconds", duration);
    }
}
