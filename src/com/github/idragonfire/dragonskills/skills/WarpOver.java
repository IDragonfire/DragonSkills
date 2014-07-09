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

public class WarpOver extends TargetBlockSkill {

    @SkillConfig
    private int maxDistance = 3;
    @SkillConfig
    private int maxYDelta = 2;

    public WarpOver(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public SkillResult use(Player player, Block targetBlock) {
        BlockFace[] faces = DUtils.getDirections(player);
        Block tmp = null;
        for (int i = 0; i < maxDistance; i++) {
            targetBlock = targetBlock.getRelative(faces[DUtils.FRONT]);
            tmp = targetBlock;
            for (int j = 0; j < maxYDelta; j++) {
                if (DUtils.enoughForPlayer(tmp)) {
                    Location loc = DUtils.getLocationWithPlayerDelta(tmp);
                    loc.setPitch(player.getLocation().getPitch());
                    loc.setYaw(player.getLocation().getYaw());
                    player.teleport(loc);
                    return SkillResult.SUCESSFULL;
                }
                tmp = targetBlock.getRelative(BlockFace.UP);
            }
        }
        return SkillResult.FAIL;
    }

    @Override
    public String getDescription() {
        return DSystem.paramString(
                "try to teleport you over blocks, $1 width and max $2 up",
                maxDistance, maxYDelta);
    }
}
