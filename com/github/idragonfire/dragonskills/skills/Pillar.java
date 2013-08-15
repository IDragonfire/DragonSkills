package com.github.idragonfire.dragonskills.skills;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.ActiveSkill;
import com.github.idragonfire.dragonskills.api.DSystem;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TickEffect;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public class Pillar extends ActiveSkill {

    @SkillConfig
    public int delayBeforeDespawn = 3;
    @SkillConfig
    public int height = 12;

    public Pillar(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public SkillResult use(Player player) {
        Block[] blocks = new Block[5];
        blocks[0] = player.getLocation().getBlock();
        blocks[1] = blocks[0].getRelative(BlockFace.NORTH);
        blocks[2] = blocks[0].getRelative(BlockFace.EAST);
        blocks[3] = blocks[0].getRelative(BlockFace.SOUTH);
        blocks[4] = blocks[0].getRelative(BlockFace.WEST);
        CreatePillar effect;
        try {
            effect = new CreatePillar(plugin, height, blocks,
                    delayBeforeDespawn, player);
            effect.start();
        } catch (Exception e) {
            DSystem.log("invalid terrain");
            return SkillResult.INVALID_TERRAIN;
        }
        return SkillResult.SUCESSFULL;
    }

    @Override
    public String getDescription() {
        return DSystem.paramString(
                "spawn a $1 height pillar under you for $2 seconds", height,
                delayBeforeDespawn);
    }

    public class CreatePillar extends TickEffect {
        private BlockState[] redstoneStates;
        private BlockState[] extractStates;
        private Block[] powerBlocks;
        private Block[] toMove;

        private int delayBeforeDespawn;

        public CreatePillar(DragonSkillsPlugin plugin, int height,
                Block[] toMove, int delayBeforeDespawn, Player player)
                throws Exception {
            super(plugin, 0, 2, (height - 2) * 2 + 1);
            this.toMove = toMove;
            this.delayBeforeDespawn = delayBeforeDespawn;
            redstoneStates = new BlockState[2 * toMove.length];
            extractStates = new BlockState[height * toMove.length];

            // find redstone power Blocka nd store it #1
            powerBlocks = new Block[toMove.length];
            for (int i = 0; i < toMove.length; i++) {
                powerBlocks[i] = toMove[i].getRelative(0, -2, 0);
                if (!DUtils.canBreak(player, powerBlocks[i])) {
                    throw new Exception("invalid terrain");
                }
                redstoneStates[i] = powerBlocks[i].getState();
            }
            // place pistons #2
            Block tmp = null;
            for (int i = 0; i < toMove.length; i++) {
                tmp = toMove[i].getRelative(BlockFace.DOWN);
                if (!DUtils.canBreak(player, tmp)) {
                    throw new Exception("invalid terrain");
                }
                redstoneStates[i + toMove.length] = tmp.getState();
            }
            for (int i = 0; i < toMove.length; i++) {
                tmp = toMove[i].getRelative(BlockFace.DOWN);
                tmp.setTypeIdAndData(Material.PISTON_BASE.getId(),
                        DUtils.getPistonData(BlockFace.UP), false);
            }
            // store targetBlocks;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < toMove.length; j++) {
                    extractStates[j + i * toMove.length] = toMove[j]
                            .getRelative(0, i, 0).getState();
                }
            }

            // Player p = Bukkit.getPlayer("IDragonfire");
            // for (int i = 0; i < redstoneStates.length; i++) {
            // p.sendBlockChange(redstoneStates[i].getLocation(),
            // Material.GLASS.getId(), (byte) 0);
            // redstoneStates[i].update(true);
            // }
        }

        @Override
        protected void effectTick() {
            if (tickCount % 2 == 0) {
                for (int i = 0; i < toMove.length; i++) {
                    toMove[i].setType(Material.DIRT);
                    powerBlocks[i].setType(Material.REDSTONE_BLOCK);
                }
            } else {
                for (int i = 0; i < powerBlocks.length; i++) {
                    powerBlocks[i].setType(Material.DIRT);
                }
            }
        }

        @Override
        protected void endEffect() {
            for (int i = 0; i < redstoneStates.length; i++) {
                DSystem.log("restore");
                redstoneStates[i].update(true);
            }
            RestoreTask restore = new RestoreTask(plugin, extractStates,
                    toMove.length, delayBeforeDespawn);
            restore.start();
        }
    }

    public class RestoreTask extends TickEffect {
        private int index;
        private BlockState[] blocks;
        private int amount;

        public RestoreTask(DragonSkillsPlugin plugin, BlockState[] blocks,
                int amount, int delayBeforeDespawn) {
            super(plugin, DUtils.TICKS * delayBeforeDespawn, 10, blocks.length
                    / amount);
            this.blocks = blocks;
            index = blocks.length - 1;
            this.amount = amount;
        }

        @Override
        protected void effectTick() {
            for (int i = 0; i < amount; i++) {
                blocks[index].update(true);
                index--;
            }
        }

    }
}
