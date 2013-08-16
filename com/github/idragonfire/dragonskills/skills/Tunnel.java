package com.github.idragonfire.dragonskills.skills;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.DSystem;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TargetBlockSkill;
import com.github.idragonfire.dragonskills.api.TimeEffect;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public class Tunnel extends TargetBlockSkill {

    @SkillConfig
    public int length = 2;
    @SkillConfig
    public int tunnelLifeTime = 5;

    public Tunnel(DragonSkillsPlugin plugin) {
        super(plugin);
        setDefaultCooldown(tunnelLifeTime);
    }

    @Override
    public SkillResult use(Player player, Block targetBlock) {
        BlockFace[] faces = DUtils.getDirections(player);
        Block[] blocks = new Block[2 * length];
        for (int i = 0; i < length; i++) {
            blocks[i] = targetBlock;
            if (!DUtils.canBreak(player, blocks[i])) {
                return SkillResult.INVALID_TERRAIN;
            }
            blocks[i + length] = targetBlock.getRelative(BlockFace.UP);
            if (!DUtils.canBreak(player, blocks[i + length])) {
                return SkillResult.INVALID_TERRAIN;
            }
            targetBlock = targetBlock.getRelative(faces[DUtils.FRONT]);
        }
        new TunnelEffect(plugin, blocks, tunnelLifeTime * DUtils.TICKS)
                .startEffect();
        return SkillResult.SUCESSFULL;
    }

    @Override
    public String getDescription() {
        return DSystem.paramString("spawn a $1 long tunnel", length);
    }

    public class TunnelEffect extends TimeEffect {
        private Block[] blocks;
        private BlockState[] states;

        public TunnelEffect(DragonSkillsPlugin plugin, Block[] blocks,
                long duration) {
            super(plugin, duration);
            this.blocks = blocks;
            states = new BlockState[blocks.length];
        }

        @Override
        public void initTimeEffect() {
            for (int i = 0; i < blocks.length; i++) {
                states[i] = blocks[i].getState();
                blocks[i].setType(Material.AIR);
            }
        }

        @Override
        public void endTimeEffect() {
            for (int i = 0; i < states.length; i++) {
                states[i].update(true);
            }
        }

    }

}
