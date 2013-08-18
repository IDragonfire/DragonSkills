package com.github.idragonfire.dragonskills.skills;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TargetBlockSkill;
import com.github.idragonfire.dragonskills.utils.DUtils;

public class WarpTo extends TargetBlockSkill {

    public WarpTo(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public SkillResult use(final Player player, Block targetBlock) {
        Location loc = DUtils.getLocationWithPlayerDelta(targetBlock
                .getRelative(BlockFace.UP));
        final Location finalLoc = loc;

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                finalLoc.setPitch(player.getLocation().getPitch());
                finalLoc.setYaw(player.getLocation().getYaw());
                player.teleport(finalLoc);
            }
        }, (long) (loc.distance(player.getLocation()) * 0.7));
        pullEntityToLocation(player, loc);
        return SkillResult.SUCESSFULL;
    }

    private void pullEntityToLocation(Entity e, Location loc) {
        Location entityLoc = e.getLocation();

        entityLoc.setY(entityLoc.getY() + 0.5D);
        e.teleport(entityLoc);

        double g = -0.08D;
        double d = loc.distance(entityLoc);
        double t = d;
        double v_x = (1.0D + 0.07000000000000001D * t)
                * (loc.getX() - entityLoc.getX()) / t;
        double v_y = (1.0D + 0.03D * t) * (loc.getY() - entityLoc.getY()) / t
                - 0.5D * g * t;
        double v_z = (1.0D + 0.07000000000000001D * t)
                * (loc.getZ() - entityLoc.getZ()) / t;

        Vector v = e.getVelocity();
        v.setX(v_x);
        v.setY(v_y);
        v.setZ(v_z);
        e.setVelocity(v);
    }

    @Override
    public String getDescription() {
        return "warp to a target block";
    }

}
