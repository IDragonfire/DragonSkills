package com.github.idragonfire.dragonskills.skills;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TargetBlockSkill;
import com.github.idragonfire.dragonskills.api.TickEffect;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public class Sandwall extends TargetBlockSkill {

    @SkillConfig
    public int width = 7;

    @SkillConfig
    public int height = 5;

    @SkillConfig
    public int sandwallLifetime = 4;

    public Sandwall(DragonSkillsPlugin plugin) {
        super(plugin);
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
        try {
            new SandwallEffect(plugin, player, blocks, height, sandwallLifetime)
                    .start();
        } catch (Exception e2) {
            return SkillResult.INVALID_TERRAIN;
        }

        return SkillResult.SUCESSFULL;
    }

    @Override
    public String getDescription() {
        return "A spawnwall pushed from the ground";
    }

    public class SandwallEffect extends TickEffect {
        private Block[] targetBlocks;
        protected Block[] powerBlocks;
        protected BlockState[] states;
        private int lifetime;

        public SandwallEffect(DragonSkillsPlugin plugin, Player player,
                Block[] targetBlocks, int height, int lifetime)
                throws Exception {
            super(plugin, 0, 1, height * 4 + 1);
            this.targetBlocks = targetBlocks;
            this.lifetime = lifetime;
            powerBlocks = new Block[targetBlocks.length];
            states = new BlockState[targetBlocks.length * (height + 3)];
            Block tmp;
            for (int i = 0; i < targetBlocks.length; i++) {
                // powerBlock
                tmp = targetBlocks[i].getRelative(0, -2, 0);
                if (!DUtils.canBreak(player, tmp)) {
                    throw new Exception("invalid terrain");
                }
                powerBlocks[i] = tmp;
                states[i] = tmp.getState();

                // piston
                tmp = targetBlocks[i].getRelative(0, -1, 0);
                if (!DUtils.canBreak(player, tmp)) {
                    throw new Exception("invalid terrain");
                }
                states[i + targetBlocks.length] = tmp.getState();

                // target
                tmp = targetBlocks[i];
                if (!DUtils.canBreak(player, tmp)) {
                    throw new Exception("invalid terrain");
                }
                states[i + targetBlocks.length * 2] = tmp.getState();

                // movearea
                for (int j = 0; j < height; j++) {
                    tmp = targetBlocks[i].getRelative(0, j + 1, 0);
                    if (!DUtils.canBreak(player, tmp)) {
                        throw new Exception("invalid terrain");
                    }
                    states[i + targetBlocks.length * (j + 3)] = tmp.getState();
                }
            }

            // place piston
            for (int i = 0; i < targetBlocks.length; i++) {
                targetBlocks[i].getRelative(BlockFace.DOWN).setTypeIdAndData(
                        Material.PISTON_BASE.getId(),
                        DUtils.getPistonData(BlockFace.UP), false);
            }

            // place air in move area
            for (int i = 0; i < targetBlocks.length; i++) {
                for (int j = 0; j < height; j++) {
                    targetBlocks[i].getRelative(0, j + 1, 0).setType(
                            Material.AIR);
                }
            }
        }

        @Override
        protected void effectTick() {

            if (tickCount % 2 == 0) {
                clearTargets();
                for (int i = 0; i < powerBlocks.length; i++) {
                    powerBlocks[i].setType(Material.DIRT);
                }
            } else {
                for (int i = 0; i < powerBlocks.length; i++) {
                    powerBlocks[i].setType(Material.REDSTONE_BLOCK);
                }
            }
        }

        private void clearTargets() {
            for (int i = 0; i < targetBlocks.length; i++) {
                targetBlocks[i].setType(Material.SAND);
            }
        }

        @Override
        protected void endEffect() {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                    new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < states.length; i++) {
                                states[i].update(true);
                            }
                        }
                    }, lifetime * DUtils.TICKS);
        }
    }

}
