package com.github.idragonfire.dragonskills.skills;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.DSystem;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TargetBlockSkill;
import com.github.idragonfire.dragonskills.api.TickEffect;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public class SidePush extends TargetBlockSkill {

    private BlockFace[] facePush = new BlockFace[] { BlockFace.NORTH,
            BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
    private BlockFace[] facePower = new BlockFace[] { BlockFace.DOWN,
            BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH,
            BlockFace.WEST };

    @SkillConfig
    private int blockLifeTime = 3;

    public SidePush(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public SkillResult use(Player player, Block targetBlock) {
        SidePushOptions options = preparePush(targetBlock);
        if (options == null) {
            DSystem.log("not the right place");
            return SkillResult.FAIL;
        }
        new SidePushEffect(plugin, options.restore, options.pistonBlock,
                options.side, options.powerBlock, blockLifeTime).start();
        return SkillResult.SUCESSFULL;
    }

    public SidePushOptions preparePush(Block targetBlock) {
        BlockState[] restore = new BlockState[4];
        // pushed block
        restore[2] = targetBlock.getState();

        BlockFace side = null;
        // only one side must be air
        for (int i = 0; i < facePush.length; i++) {
            if (targetBlock.getRelative(facePush[i]).getType() == Material.AIR) {
                if (side != null) {
                    return null;
                }
                side = facePush[i];
            }
        }
        // move area
        restore[3] = targetBlock.getRelative(side).getState();
        // piston block
        restore[1] = targetBlock.getRelative(side.getOppositeFace()).getState();
        // find power place
        for (int i = 0; i < facePower.length; i++) {
            if (side == facePower[i]) {
                continue;
            }
            if (restore[1].getBlock().getRelative(facePower[i]).getType() != Material.AIR) {
                // power block
                restore[0] = restore[1].getBlock().getRelative(facePower[i])
                        .getState();
                break;
            }
        }
        if (restore[0] == null) {
            return null;
        }

        return new SidePushOptions(side, restore[1].getBlock(),
                restore[0].getBlock(), restore);
    }

    @Override
    public String getDescription() {
        return DSystem.paramString("push a block out of a wall for $1 seconds",
                blockLifeTime);
    }

    public class SidePushOptions {
        public BlockFace side;
        public Block pistonBlock;
        public Block powerBlock;
        public BlockState[] restore;

        public SidePushOptions(BlockFace side, Block pistonBlock,
                Block powerBlock, BlockState[] restore) {
            super();
            this.side = side;
            this.pistonBlock = pistonBlock;
            this.powerBlock = powerBlock;
            this.restore = restore;
        }
    }

    public class SidePushEffect extends TickEffect {
        private BlockState[] restore;
        private Block piston;
        private BlockFace side;
        private Block power;
        private int delayInSeconds;

        public SidePushEffect(DragonSkillsPlugin plugin, BlockState[] restore,
                Block piston, BlockFace side, Block power, int delayInSeconds) {
            super(plugin, 0, 2, 2);
            this.restore = restore;
            this.piston = piston;
            this.side = side;
            this.power = power;
            this.delayInSeconds = delayInSeconds;
        }

        @Override
        protected void effectTick() {
            if (tickCount == 0) {
                restore[3].getBlock().setType(Material.AIR);
                piston.setTypeIdAndData(Material.PISTON_BASE.getId(),
                        DUtils.getPistonData(side), false);
                power.setType(Material.REDSTONE_BLOCK);
            } else if (tickCount == 1) {
                piston.setType(Material.DIRT);
                power.setType(Material.DIRT);
            }
        }

        @Override
        protected void endEffect() {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                    new Runnable() {

                        @Override
                        public void run() {
                            for (int i = 0; i < restore.length; i++) {
                                restore[i].update(true);
                            }
                        }
                    }, delayInSeconds * DUtils.TICKS);
        }
    }

}
