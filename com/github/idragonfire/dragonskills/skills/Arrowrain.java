package com.github.idragonfire.dragonskills.skills;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TargetBlockSkill;
import com.github.idragonfire.dragonskills.utils.DUtils;

public class Arrowrain extends TargetBlockSkill {

    public Arrowrain(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public SkillResult use(Player player, Block targetBlock) {
        Vector v = new Vector(0, -9, 0);
        for (int i = 0; i < 15; i++) {
            player.getWorld().spawnArrow(
                    targetBlock.getLocation().add(DUtils.nextInt(5) - 2, 8,
                            DUtils.nextInt(5) - 2), v,
                    DUtils.nextFloat() + DUtils.nextInt(5),
                    DUtils.nextFloat() + DUtils.nextInt(24));
        }
        return SkillResult.SUCESSFULL;
    }

    @Override
    public String getDescription() {
        return "arrow comes from the sky";
    }
}
