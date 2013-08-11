package com.github.idragonfire.dragonskills.skills;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.PistonEffect;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TargetBlockSkill;

public class Sandwall extends TargetBlockSkill {

    public int width = 7;

    public Sandwall(DragonSkillsPlugin plugin) {
        super(plugin);
        // TODO Auto-generated constructor stub
    }

    @Override
    public SkillResult use(Player player, Block targetBlock) {
        Block[] blocks = new Block[width];
        Block start = targetBlock.getRelative(width / 2 * -1, 0, 0);
        for (int i = 0; i < width; i++) {
            blocks[i] = start;
            start = start.getRelative(1, 0, 0);
        }
        new SandwallEffect(plugin, blocks, 1, 20, false);
        return SkillResult.SUCESSFULL;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public class SandwallEffect extends PistonEffect {

        public SandwallEffect(DragonSkillsPlugin plugin, Block[] targetBlocks,
                int duration, int height, boolean sticky) {
            super(plugin, targetBlocks, duration, height, sticky);
        }

        @Override
        protected void tickEvent() {
            if (tickCount % 2 == 0) {
                clearTargets();
                for (int i = 0; i < targetBlocks.length; i++) {
                    targetBlocks[i].getRelative(0, -2, 0)
                            .setType(Material.DIRT);
                }
            } else {
                for (int i = 0; i < targetBlocks.length; i++) {
                    targetBlocks[i].getRelative(0, -2, 0).setType(
                            Material.REDSTONE_BLOCK);
                }
            }
        }

        @Override
        protected void clearTargets() {
            for (int i = 0; i < targetBlocks.length; i++) {
                targetBlocks[i].setType(Material.SAND);
            }
        }
    }

}
