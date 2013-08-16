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

public class WebTrap extends TargetBlockSkill {

    @SkillConfig
    public int webLifeTime = 5;

    public WebTrap(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public SkillResult use(Player player, Block targetBlock) {
        List<Block> blocks = DUtils.sphere(targetBlock, 2, 3, 2, true);
        for (Block b : blocks) {
            if (!DUtils.canBreak(player, b)) {
                return SkillResult.INVALID_TERRAIN;
            }
        }
        final List<BlockState> states = new ArrayList<BlockState>();
        for (Block b : blocks) {
            if (b.getType() == Material.AIR) {
                states.add(b.getState());
                b.setType(Material.WEB);
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                for (BlockState state : states) {
                    if (state.getBlock().getType() == Material.WEB) {
                        state.update(true);
                    }
                }
            }
        }, webLifeTime * DUtils.TICKS);
        return SkillResult.SUCESSFULL;
    }

    @Override
    public String getDescription() {
        return DSystem.paramString("spawn a web for $1", webLifeTime);
    }

}
