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
import com.github.idragonfire.dragonskills.utils.DUtils;

public class Earthquake extends TargetBlockSkill {

    public Earthquake(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public SkillResult use(Player player, Block targetBlock) {
        Block[] blocks = new Block[49];
        blocks[0] = targetBlock.getRelative(-3, 0, -3);
        for (int i = 1; i < 7; i++) {
            blocks[i] = blocks[i - 1].getRelative(1, 0, 0);
        }
        for (int i = 1; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                blocks[i * 7 + j] = blocks[j].getRelative(0, 0, i);
            }
        }
        EarthquakeEffect e = new EarthquakeEffect(blocks);
        e.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, e, 0, 1);
        // for (int i = 0; i < blocks.length; i++) {
        // player.sendBlockChange(blocks[i].getLocation(), Material.GLASS
        // .getId(), (byte) 0);
        // }
        return null;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public class EarthquakeEffect implements Runnable {
        private Block[] blocks;
        private BlockState[] states;
        private int index = 0;
        public int id;

        public EarthquakeEffect(Block[] blocks) {
            super();
            this.blocks = blocks;
            states = new BlockState[blocks.length * 4];
            for (int i = 0; i < blocks.length; i++) {
                states[i] = blocks[i].getState();
                states[i + blocks.length] = blocks[i].getRelative(BlockFace.UP)
                        .getState();
                states[i + blocks.length * 2] = blocks[i].getRelative(
                        BlockFace.DOWN).getState();
                states[i + blocks.length * 3] = blocks[i].getRelative(0, -2, 0)
                        .getState();
            }
            for (int i = 0; i < blocks.length; i++) {
                blocks[i].getRelative(0, -1, 0).setTypeIdAndData(
                        Material.PISTON_STICKY_BASE.getId(), (byte) 1, true);
                blocks[i].getRelative(BlockFace.UP).setType(Material.AIR);
            }

        }

        @Override
        public void run() {
            if (index > 3 * 20) {
                DSystem.log("to slow");
                return;
            }

            if (index == 3 * 20) {
                // set dirt to react piston to base
                Bukkit.getScheduler().cancelTask(id);
                for (int i = 0; i < blocks.length; i++) {
                    blocks[i].getRelative(0, -2, 0).setType(Material.DIRT);
                }
                DSystem.log("restore");
                for (int i = 0; i < states.length; i++) {
                    // states[i].getBlock().setType(Material.AIR);
                    states[i].update(true);
                }
                index++;
                return;
            }
            for (int i = 0; i < blocks.length; i++) {
                if (DUtils.nextInt(3) != 0) {
                    blocks[i].getRelative(0, -2, 0).setType(Material.DIRT);
                } else {
                    blocks[i].getRelative(0, -2, 0).setType(
                            Material.REDSTONE_BLOCK);
                }
            }
            index++;
        }
    }

}
