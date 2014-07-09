package com.github.idragonfire.dragonskills.skills;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.DSystem;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TargetBlockSkill;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public class LeaveWall extends TargetBlockSkill {

    @SkillConfig
    private int wall_thickness = 5;
    @SkillConfig
    private int wall_height = 5;
    @SkillConfig
    private int wall_width = 18;

    public LeaveWall(DragonSkillsPlugin plugin/* Heroes plugin */) {
        super(plugin);
    }

    @Override
    public SkillResult use(Player player, Block targetBlock) {
        BlockFace[] faces = DUtils.getDirections(player);

        byte leaveValue = (byte) DUtils.nextInt(4);
        Block startLine = targetBlock;
        for (int z = 0; z < wall_thickness; z++) {
            transformBlock(player, startLine, leaveValue);
            Block leftBlockStart = startLine.getRelative(faces[DUtils.LEFT]);
            Block rightBlockStart = startLine.getRelative(faces[DUtils.RIGHT]);
            for (int x = 0; x < wall_width / 2 - 1; x++) {
                transformBlock(player, leftBlockStart, leaveValue);
                transformBlock(player, rightBlockStart, leaveValue);
                Block leftBlockUp = leftBlockStart.getRelative(BlockFace.UP);
                Block rightBlockUp = rightBlockStart.getRelative(BlockFace.UP);
                Block startBlockUp = startLine.getRelative(BlockFace.UP);
                for (int y = 0; y < wall_height - 1; y++) {
                    transformBlock(player, startBlockUp, leaveValue);
                    transformBlock(player, leftBlockUp, leaveValue);
                    transformBlock(player, rightBlockUp, leaveValue);
                    startBlockUp = startBlockUp.getRelative(BlockFace.UP);
                    leftBlockUp = leftBlockUp.getRelative(BlockFace.UP);
                    rightBlockUp = rightBlockUp.getRelative(BlockFace.UP);
                }
                leftBlockStart = leftBlockStart.getRelative(faces[DUtils.LEFT]);
                rightBlockStart = rightBlockStart
                        .getRelative(faces[DUtils.RIGHT]);
            }
            startLine = startLine.getRelative(faces[DUtils.FRONT]);
        }
        DSystem.log("Leave Wall has grown");
        return SkillResult.SUCESSFULL;
    }

    private void transformBlock(Player player, Block block, byte leaveValue) {
        if (DUtils.isAllowedGrassMaterial(block.getType())) {
            DUtils.transformBlock(player, block, Material.LEAVES.getId(),
                    leaveValue);
        }
    }

    @Override
    public String getDescription() {
        return DSystem.paramString("Spawn a $1x$2x$3 wall of Leaves",
                wall_height, wall_width, wall_thickness);
    }
}
