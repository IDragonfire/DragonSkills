package com.github.idragonfire.dragonskills.api;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public abstract class TargetBlockSkill extends ActiveSkill {

    @SkillConfig
    protected int targetBlockMaxDistance = 16;

    protected HashSet<Byte> transparentBlocks = null;

    public TargetBlockSkill(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    public int getTargetBlockMaxDistance() {
        return targetBlockMaxDistance;
    }

    public void setTargetBlockMaxDistance(int targetBlockMaxDistance) {
        this.targetBlockMaxDistance = targetBlockMaxDistance;
    }

    public HashSet<Byte> getTransparentBlocks() {
        return transparentBlocks;
    }

    public void setTransparentBlocks(HashSet<Byte> transparentBlocks) {
        this.transparentBlocks = transparentBlocks;
    }

    @Override
    public SkillResult use(Player player) {
        Block b = player.getTargetBlock(transparentBlocks,
                targetBlockMaxDistance);
        if (b.getType() == Material.AIR) {
            DSystem.log("to far away");
            return SkillResult.FAIL;
        }
        return use(player, b);
    }

    public abstract SkillResult use(Player player, Block targetBlock);

}
