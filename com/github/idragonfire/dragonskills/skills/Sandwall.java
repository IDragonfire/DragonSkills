package com.github.idragonfire.dragonskills.skills;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.PistonEffect;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TargetBlockSkill;
import com.github.idragonfire.dragonskills.utils.DUtils;

public class Sandwall extends TargetBlockSkill {

    public int width = 7;

    public Sandwall(DragonSkillsPlugin plugin) {
        super(plugin);
        // TODO Auto-generated constructor stub
    }

    @Override
    public SkillResult use(Player player, Block targetBlock) {
        BlockFace[] faces = DUtils.getDirections(player);
        Block[] blocks = new Block[width];
        Block start = targetBlock.getRelative(faces[DUtils.LEFT], width / 2);
        for (int i = 0; i < width; i++) {
            blocks[i] = start;
            start = start.getRelative(faces[DUtils.RIGHT]);
        }
        SandwallEffect e = new SandwallEffect(plugin, player, blocks, 1, 20,
                false);
        return e.isValid() ? SkillResult.SUCESSFULL
                : SkillResult.INVALID_TERRAIN;
    }

    @Override
    public String getDescription() {
        return "A spawnwall pushed from the ground";
    }

    public class SandwallEffect extends PistonEffect {

        public SandwallEffect(DragonSkillsPlugin plugin, Player player,
                Block[] targetBlocks, int duration, int height, boolean sticky) {
            super(plugin, player, targetBlocks, duration, height, sticky);
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
