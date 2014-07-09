package com.github.idragonfire.dragonskills.skills;

import java.util.Arrays;
import java.util.HashSet;

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

public class Firewall extends TargetBlockSkill {
    @SkillConfig
    private int wallLength = 7;
    @SkillConfig
    private int wallLifetime = 3;
    @SkillConfig
    private HashSet<Material> allowedMaterials = new HashSet<Material>(Arrays
            .asList(new Material[] { Material.AIR, Material.LEAVES,
                    Material.RED_ROSE, Material.YELLOW_FLOWER,
                    Material.SUGAR_CANE, Material.LONG_GRASS, Material.VINE,
                    Material.DEAD_BUSH, Material.SNOW }));

    public Firewall(DragonSkillsPlugin plugin) {
        super(plugin);
        setTargetBlockMaxDistance(32);
    }

    // TODO: works also on non flat terrain
    @Override
    public SkillResult use(Player player, Block targetBlock) {
        BlockFace[] faces = DUtils.getDirections(player);
        if (!allowedMaterials.contains(targetBlock.getType())) {
            targetBlock = targetBlock.getRelative(BlockFace.UP);
        }
        BlockState[] store = new BlockState[wallLength];
        Block left = targetBlock;
        Block right = targetBlock.getRelative(faces[DUtils.RIGHT]);
        for (int i = 0; i < store.length; i++) {
            store[i] = transform(left);
            if (store[i] != null) {
                DUtils.transformBlock(player, left, Material.FIRE);
            }
            i++;
            if (i < store.length) {
                store[i] = transform(right);
                if (store[i] != null) {
                    DUtils.transformBlock(player, right, Material.FIRE);
                }
            }
            left = left.getRelative(faces[DUtils.LEFT]);
            right = right.getRelative(faces[DUtils.RIGHT]);
        }
        new FirewallEffect(plugin, wallLifetime * DUtils.TICKS, store)
                .startEffect();
        DSystem.log("cast firewall for $1 seconds", wallLifetime);
        return SkillResult.SUCESSFULL;
    }

    public BlockState transform(Block b) {
        if (allowedMaterials.contains(b.getType())) {
            return b.getState();
        }
        return null;
    }

    public class FirewallEffect extends TimeEffect {
        private BlockState[] store;

        public FirewallEffect(DragonSkillsPlugin plugin, long duration,
                BlockState[] store) {
            super(plugin, duration);
            this.store = store;
        }

        @Override
        public void endTimeEffect() {
            for (int j = store.length - 1; j >= 0; j--) {
                if (store[j] != null) {
                    store[j].update(true);
                }
            }
        }

    }

    @Override
    public String getDescription() {
        return DSystem.paramString("Spawn a $1 width firewall for $2 seconds",
                wallLength, wallLifetime);
    }

}