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

public class Pillar extends TargetBlockSkill {

    public Pillar(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public SkillResult use(Player player, Block targetBlock) {
        // for (int i = 0; i < 16; i++) {
        // targetBlock = targetBlock.getRelative(BlockFace.UP);
        // targetBlock.setTypeIdAndData(Material.PISTON_BASE.getId(),
        // (byte) i, true);
        // }
        final BlockState[] states = new BlockState[3];
        final Block under = targetBlock.getRelative(BlockFace.DOWN)
                .getRelative(BlockFace.DOWN);
        final Block red = under.getRelative(BlockFace.DOWN);

        states[0] = targetBlock.getState();
        states[1] = under.getState();
        states[2] = red.getState();
        under.setTypeIdAndData(Material.PISTON_BASE.getId(), (byte) 1, false);
        // under.getRelative(BlockFace.DOWN).setType(Material.REDSTONE_BLOCK);

        // under.setType(Material.PISTON_BASE);
        Piston p = new Piston(targetBlock.getRelative(BlockFace.DOWN), under,
                red, states);
        final int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,
                p, 0, 1);
        p.id = id;

        // PistonBaseMaterial material = (PistonBaseMaterial) under.getType()
        // .getNewData((byte) 0);
        // material.setPowered(true);
        // DSystem.log(under.getType());
        return SkillResult.SUCESSFULL;
    }

    @Override
    public String getDescription() {
        return "earth pillar";
    }

    public class Piston implements Runnable {

        public int id = -1;
        private int index = 0;

        private Block a;
        private Block b;
        private Block c;
        private BlockState[] s;
        private int z = 0;

        public Piston(Block targetBlock, Block under, Block red,
                BlockState[] states) {
            a = targetBlock;
            b = under;
            c = red;
            s = states;
        }

        public void run() {
            if (id < 0) {
                return;
            }
            if (index > 30 * 2) {
                for (int i = 0; i < s.length; i++) {
                    s[i].update(false);
                }
                Bukkit.getScheduler().cancelTask(id);
                DSystem.log("canceld");
                return;
            }
            if (index % 2 == 0) {
                a.setType(Material.DIRT);
                c.setType(Material.REDSTONE_BLOCK);
            } else {
                c.setType(Material.DIRT);
            }
            // if (z == 0) {
            // a.setType(Material.DIRT);
            // } else if (z == 1) {
            // c.setType(Material.REDSTONE_BLOCK);
            //
            // } else {
            // c.setType(Material.DIRT);
            // }
            // z++;
            // if (z > 2) {
            // z = 0;
            // }
            index++;
        }
    }
}
