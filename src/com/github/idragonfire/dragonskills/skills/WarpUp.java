package com.github.idragonfire.dragonskills.skills;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.DSystem;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TargetBlockSkill;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public class WarpUp extends TargetBlockSkill {

    @SkillConfig
    private int maxYDelta = 10;

    public WarpUp(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public SkillResult use(Player player, Block targetBlock) {
        for (int i = 0; i < maxYDelta; i++) {
            targetBlock = targetBlock.getRelative(BlockFace.UP);
            if (DUtils.enoughForPlayer(targetBlock)) {
                Location loc = DUtils.getLocationWithPlayerDelta(targetBlock);
                loc.setPitch(player.getLocation().getPitch());
                loc.setYaw(player.getLocation().getYaw());
                player.teleport(loc);
                return SkillResult.SUCESSFULL;
            }
        }
        return SkillResult.FAIL;
    }

    @Override
    public String getDescription() {
        return DSystem.paramString(
                "teleport you up to the nearest free space, max $1 blocks up",
                maxYDelta);
    }

}
