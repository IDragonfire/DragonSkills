package com.github.idragonfire.dragonskills.skills;

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
    private final Material[] ALLOWED_UP = new Material[] { Material.AIR,
            Material.LEAVES, Material.RED_ROSE, Material.YELLOW_FLOWER,
            Material.SUGAR_CANE, Material.LONG_GRASS, Material.VINE,
            Material.DEAD_BUSH, Material.SNOW };

    @SkillConfig
    private int wallLength = 7;
    @SkillConfig
    private int fireLifetime = 3;
    @SkillConfig
    private HashSet<Material> allowedMaterials;

    public Firewall(DragonSkillsPlugin plugin) {
        super(plugin);
        // setDescription("Create a wall of fire lasting $2-$3s. R:$1");
        // setUsage("/skill firewall");
        // setIdentifiers(new String[] { "skill firewall", "skill Firewall",
        // "skill fireWall", "skill FireWall" });
        // setTypes(new SkillType[] { SkillType.FIRE, SkillType.DAMAGING,
        // SkillType.SILENCABLE });
        allowedMaterials = new HashSet<Material>();
        for (int i = 0; i < ALLOWED_UP.length; i++) {
            allowedMaterials.add(ALLOWED_UP[i]);
        }
        setTargetBlockMaxDistance(32);
    }

    // @Override
    // public String getDescription(Hero hero) {
    // int distance = SkillConfigManager.getUseSetting(hero, this,
    // Setting.MAX_DISTANCE, 15, false)
    // + SkillConfigManager.getUseSetting(hero, this,
    // Setting.MAX_DISTANCE_INCREASE.node(), 0, false)
    // * hero.getSkillLevel(this);
    //
    // int minDuration = SkillConfigManager.getUseSetting(hero, this,
    // "min-duration", 5000, false) / 1000;
    // int maxDuration = SkillConfigManager.getUseSetting(hero, this,
    // "max-duration", 10000, false) / 1000;
    // StringBuffer sb = new StringBuffer(super.getDescription().replace("$1",
    // distance + "").replace("$2", minDuration + "").replace("$3",
    // maxDuration + ""));
    // double cdSec = SkillConfigManager.getUseSetting(hero, this,
    // Setting.COOLDOWN, 45000, false) / 1000.0D;
    // if (cdSec > 0.0D) {
    // sb.append(" CD:");
    // sb.append(Util.formatDouble(cdSec));
    // sb.append("s");
    // }
    // int mana = SkillConfigManager.getUseSetting(hero, this, Setting.MANA,
    // 30, false);
    // if (mana > 0) {
    // sb.append(" M:");
    // sb.append(mana);
    // }
    // return sb.toString();
    // }

    // TODO: works also on flat terrain
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
                try {
                    DUtils.transformBlock(player, left, Material.FIRE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            i++;
            if (i < store.length) {
                store[i] = transform(right);
                if (store[i] != null) {
                    try {
                        DUtils.transformBlock(player, right, Material.FIRE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            left = left.getRelative(faces[DUtils.LEFT]);
            right = right.getRelative(faces[DUtils.RIGHT]);
        }
        new FirewallEffect(plugin, fireLifetime * DUtils.TICKS, store)
                .startEffect();
        DSystem.log("cast firewall for $1 seconds", fireLifetime);
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

}