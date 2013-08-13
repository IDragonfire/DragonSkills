package com.github.idragonfire.dragonskills.skills;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.DSystem;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TargetBlockSkill;
import com.github.idragonfire.dragonskills.utils.DUtils;

public class Squash extends TargetBlockSkill {

    public Squash(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public SkillResult use(Player player, Block targetBlock) {
        BlockFace[] faces = DUtils.getDirections(player);
        // Block left = targetBlock.getRelative(faces[DUtils.LEFT]);
        // Block right = targetBlock.getRelative(faces[DUtils.RIGHT]);
        // if (!isValid(left) || !isValid(right)) {
        // return SkillResult.FAIL;
        // }
        Block[] blocks = new Block[6 * 3];
        targetBlock = targetBlock.getRelative(BlockFace.UP);
        blocks[0] = targetBlock.getRelative(faces[DUtils.LEFT], 2);
        blocks[1] = blocks[0].getRelative(BlockFace.UP);
        blocks[2] = blocks[1].getRelative(BlockFace.UP);
        blocks[3] = targetBlock.getRelative(faces[DUtils.RIGHT], 3);
        blocks[4] = blocks[3].getRelative(BlockFace.UP);
        blocks[5] = blocks[4].getRelative(BlockFace.UP);
        for (int i = 0; i < 6; i++) {
            blocks[6 + i] = blocks[i].getRelative(faces[DUtils.FRONT]);
            blocks[12 + i] = blocks[i].getRelative(faces[DUtils.BACK]);
        }

        for (int i = 0; i < blocks.length; i++) {
            player.sendBlockChange(blocks[i].getLocation(), Material.GLASS
                    .getId(), (byte) 0);
        }
        new SquashEffect(plugin, blocks, faces);

        return SkillResult.SUCESSFULL;
    }

    public boolean isValid(Block b) {
        if (b.getRelative(BlockFace.UP).getType() == Material.AIR) {
            return false;
        }
        if (b.getRelative(BlockFace.UP, 2).getType() == Material.AIR) {
            return false;
        }
        return true;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public class SquashEffect implements Runnable {
        private DragonSkillsPlugin plugin;
        private int id;
        private int tickCount = 0;
        private Block[] blocks;
        private BlockFace[] faces;

        public SquashEffect(DragonSkillsPlugin plugin, Block[] blocks,
                BlockFace[] faces) {
            super();
            this.plugin = plugin;
            this.blocks = blocks;
            this.faces = faces;
            id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this,
                    0, 1);
        }

        @Override
        public void run() {
            if (tickCount > 2) {
                DSystem.log("to slow");
                // Bukkit.getScheduler().cancelTask(id);
                // blocks[0].setType(Material.DIRT);
                // blocks[2].setType(Material.DIRT);
            }
            if (tickCount == 2) {
                Bukkit.getScheduler().cancelTask(id);
                for (int i = 0; i < blocks.length; i++) {
                    blocks[i].setType(Material.DIRT);
                }
                // blocks[1].setType(Material.DIRT);
                // blocks[3].setType(Material.DIRT);
            }
            if (tickCount == 0) {
                blocks[1].setTypeIdAndData(Material.PISTON_BASE.getId(), DUtils
                        .getPistonData(faces[DUtils.RIGHT]), false);
                blocks[4].setTypeIdAndData(Material.PISTON_BASE.getId(), DUtils
                        .getPistonData(faces[DUtils.LEFT]), false);
                blocks[0].setType(Material.REDSTONE_BLOCK);
                blocks[3].setType(Material.REDSTONE_BLOCK);
            } else if (tickCount == 1) {
                blocks[0].setTypeIdAndData(Material.PISTON_BASE.getId(), DUtils
                        .getPistonData(faces[DUtils.RIGHT]), false);
                blocks[2].setTypeIdAndData(Material.PISTON_BASE.getId(), DUtils
                        .getPistonData(faces[DUtils.RIGHT]), false);
                blocks[3].setTypeIdAndData(Material.PISTON_BASE.getId(), DUtils
                        .getPistonData(faces[DUtils.LEFT]), false);
                blocks[5].setTypeIdAndData(Material.PISTON_BASE.getId(), DUtils
                        .getPistonData(faces[DUtils.LEFT]), false);
                blocks[1].setType(Material.REDSTONE_BLOCK);
                blocks[4].setType(Material.REDSTONE_BLOCK);
            }
            tickCount++;
        }
    }
}
