package com.github.idragonfire.dragonskills.skills;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import api.SkillResult;
import api.TargetBlockSkill;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.utils.DUtils;

public class Fireman extends TargetBlockSkill {

    public Fireman(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public SkillResult use(Player player, Block targetBlock) {
        Block[] blocks = new Block[9];
        blocks[0] = targetBlock;
        blocks[1] = targetBlock.getRelative(BlockFace.NORTH);
        blocks[2] = targetBlock.getRelative(BlockFace.EAST);
        blocks[3] = targetBlock.getRelative(BlockFace.SOUTH);
        blocks[4] = targetBlock.getRelative(BlockFace.WEST);
        blocks[5] = blocks[1].getRelative(BlockFace.EAST);
        blocks[6] = blocks[2].getRelative(BlockFace.SOUTH);
        blocks[7] = blocks[3].getRelative(BlockFace.WEST);
        blocks[8] = blocks[4].getRelative(BlockFace.NORTH);

        for (int i = 0; i < blocks.length; i++) {
            transform(player, blocks[i]);
            transform(player, blocks[i].getRelative(BlockFace.UP));
            transform(player, blocks[i].getRelative(BlockFace.DOWN));
        }

        return SkillResult.SUCESSFULL;
    }

    public void transform(Player player, Block b) {
        if (b.getType() == Material.FIRE) {
            try {
                DUtils.transformBlock(player, b, Material.AIR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // player.sendBlockChange(b.getLocation(), Material.GLASS, (byte) 0);
    }

}
