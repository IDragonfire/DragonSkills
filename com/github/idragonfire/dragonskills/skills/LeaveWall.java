package com.github.idragonfire.dragonskills.skills;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import api.DSystem;
import api.SkillResult;
import api.TargetBlockSkill;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
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
        /*
         * super(plugin, "LeaveWall"); setDescription("spawn a wand of Leaves in front of you"); setUsage("/skill leavewall"); setIdentifiers(new String[] { "skill leavewall" }); setTypes(new
         * SkillType[] { SkillType.EARTH }); for (int i = 0; i < ALLOWED_MATERIALS.length; i++) { allowedMaterials.add(ALLOWED_MATERIALS[i]); } try { Plugin towny =
         * Bukkit.getPluginManager().getPlugin("Towny"); useTowny = towny != null; } catch (Exception e) { e.printStackTrace(); useTowny = false; } try { Plugin worldGuardPlugin =
         * Bukkit.getPluginManager().getPlugin( "WorldGuard"); useWorldGuard = worldGuardPlugin != null; if (useWorldGuard) { worldGuard = (WorldGuardPlugin) worldGuardPlugin; } } catch (Exception e)
         * { e.printStackTrace(); useWorldGuard = false; }
         */
    }

    // @Override
    // public String getDescription(Hero hero) {
    // StringBuffer sb = new StringBuffer(super.getDescription());
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

    // @Override
    // public ConfigurationSection getDefaultConfig() {
    // return super.getDefaultConfig();
    // }

    @Override
    public SkillResult use(Player player, Block targetBlock) {
        BlockFace[] faces = DUtils.getDirections(player);

        byte leaveValue = (byte) DUtils.nextInt(4);
        DSystem.log("spawn wall");
        try {
            Block startLine = targetBlock;
            for (int z = 0; z < wall_thickness; z++) {
                transformBlock(player, startLine, leaveValue);
                Block leftBlockStart = startLine
                        .getRelative(faces[DUtils.LEFT]);
                Block rightBlockStart = startLine
                        .getRelative(faces[DUtils.RIGHT]);
                for (int x = 0; x < wall_width / 2 - 1; x++) {
                    transformBlock(player, leftBlockStart, leaveValue);
                    transformBlock(player, rightBlockStart, leaveValue);
                    Block leftBlockUp = leftBlockStart
                            .getRelative(BlockFace.UP);
                    Block rightBlockUp = rightBlockStart
                            .getRelative(BlockFace.UP);
                    Block startBlockUp = startLine.getRelative(BlockFace.UP);
                    for (int y = 0; y < wall_height - 1; y++) {
                        transformBlock(player, startBlockUp, leaveValue);
                        transformBlock(player, leftBlockUp, leaveValue);
                        transformBlock(player, rightBlockUp, leaveValue);
                        startBlockUp = startBlockUp.getRelative(BlockFace.UP);
                        leftBlockUp = leftBlockUp.getRelative(BlockFace.UP);
                        rightBlockUp = rightBlockUp.getRelative(BlockFace.UP);
                    }
                    leftBlockStart = leftBlockStart
                            .getRelative(faces[DUtils.LEFT]);
                    rightBlockStart = rightBlockStart
                            .getRelative(faces[DUtils.RIGHT]);
                }
                startLine = startLine.getRelative(faces[DUtils.FRONT]);
            }
        } catch (Exception e) {
            // no towny/worldguard permission
            return SkillResult.INVALID_TERRAIN;
        }
        // // Messaging.send(hero.getPlayer(), "Leave Wall has grown");
        return SkillResult.SUCESSFULL;
    }

    private void transformBlock(Player player, Block block, byte leaveValue)
            throws Exception {
        if (DUtils.isAllowedGrassMaterial(block.getType())) {
            DUtils.transformBlock(player, block, Material.GRASS.getId(),
                    leaveValue);
        }
    }

    private Block getRelative(Block block, int[] pos) {
        return block.getRelative(pos[0], pos[1], pos[2]);
    }
}
