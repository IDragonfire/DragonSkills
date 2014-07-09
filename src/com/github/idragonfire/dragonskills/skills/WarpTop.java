package com.github.idragonfire.dragonskills.skills;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TargetBlockSkill;
import com.github.idragonfire.dragonskills.utils.DUtils;

public class WarpTop extends TargetBlockSkill {

    public WarpTop(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public SkillResult use(final Player player, Block targetBlock) {
        Location loc = DUtils.getLocationWithPlayerDelta(player.getWorld()
                .getHighestBlockAt(targetBlock.getLocation()));
        loc.setPitch(player.getLocation().getPitch());
        loc.setYaw(player.getLocation().getYaw());
        player.teleport(loc);
        return SkillResult.SUCESSFULL;
    }

    @Override
    public String getDescription() {
        return "warp you to the highest block that you target";
    }
}
