package com.github.idragonfire.dragonskills.skills;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.PistonEffect;
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

        EarthquakeEffect e = new EarthquakeEffect(plugin, player, blocks, 3, 1,
                true);

        // EarthquakeEffect e = new EarthquakeEffect(blocks);
        // e.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, e, 0, 1);
        // for (int i = 0; i < blocks.length; i++) {
        // player.sendBlockChange(blocks[i].getLocation(), Material.GLASS
        // .getId(), (byte) 0);
        // }
        return e.isValid() ? SkillResult.SUCESSFULL
                : SkillResult.INVALID_TERRAIN;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public class EarthquakeEffect extends PistonEffect {

        public EarthquakeEffect(DragonSkillsPlugin plugin, Player player,
                Block[] targetBlocks, int duration, int height, boolean sticky) {
            super(plugin, player, targetBlocks, duration, height, sticky);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void tickEvent() {
            for (int i = 0; i < targetBlocks.length; i++) {
                if (DUtils.nextInt(3) != 0) {
                    targetBlocks[i].getRelative(0, -2, 0)
                            .setType(Material.DIRT);
                } else {
                    targetBlocks[i].getRelative(0, -2, 0).setType(
                            Material.REDSTONE_BLOCK);
                }
            }
        }
    }

}
