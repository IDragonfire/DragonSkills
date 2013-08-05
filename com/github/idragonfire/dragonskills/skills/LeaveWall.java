package com.github.idragonfire.dragonskills.skills;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import api.ActiveSkill;
import api.SkillResult;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.DUtils.Direction;

public class LeaveWall extends ActiveSkill {

    // private boolean useTowny;
    // private WorldGuardPlugin worldGuard;
    // private boolean useWorldGuard;

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
    public SkillResult use(Player player) {
        // Player player = hero.getPlayer();
        Block block = player.getLocation().getBlock().getRelative(
                BlockFace.DOWN);
        Direction direction = DUtils.getCardinalDirection(player);
        if (direction == Direction.INVALID) {
            // Messaging.send(player,
            // "Please face north, east, south or west");
            return SkillResult.FAIL;
        }

        block = block.getRelative(0, 1, 0);
        int[] front = DUtils.FRONT;
        int[] left = DUtils.LEFT;
        int[] right = DUtils.RIGHT;
        switch (direction) {
        case NORTH:
            front = DUtils.LEFT;
            left = DUtils.BACK;
            right = DUtils.FRONT;
            break;
        case SOUTH:
            front = DUtils.RIGHT;
            left = DUtils.FRONT;
            right = DUtils.BACK;

            break;
        case WEST:
            front = DUtils.BACK;
            left = DUtils.RIGHT;
            right = DUtils.LEFT;
            break;
        default:
            break;
        }
        byte leaveValue = (byte) DUtils.nextInt(4);
        // Messaging.send(hero.getPlayer(), "Spawn wall");
        Block start = getRelative(block, front);
        int thickness = 6;
        int height = 5;
        int width = 12;
        Block[] blocks = new Block[thickness * height * width * 4];
        int index = 0;
        try {
            for (int z = 0; z < thickness; z++) {
                Block leftBlockStart = start;
                Block rightBlockStart = start;
                for (int x = 0; x < width; x++) {
                    Block leftBlockUp = leftBlockStart;
                    Block leftBlockDown = leftBlockStart;
                    Block rightBlockUp = rightBlockStart;
                    Block rightBlockDown = rightBlockStart;
                    boolean left2 = true;
                    boolean right2 = true;
                    for (int y = 0; y < height; y++) {
                        if (DUtils.transformBlock(player, leftBlockUp,
                                leaveValue)) {
                            blocks[index] = leftBlockUp;
                            index++;
                        }
                        if (left2) {
                            left2 = DUtils.transformBlock(player,
                                    leftBlockDown, leaveValue);
                            if (left2) {
                                blocks[index] = leftBlockDown;
                                index++;
                            }
                        }
                        if (DUtils.transformBlock(player, rightBlockUp,
                                leaveValue)) {
                            blocks[index] = rightBlockUp;
                            index++;
                        }
                        if (right2) {
                            right2 = DUtils.transformBlock(player,
                                    rightBlockDown, leaveValue);
                            if (right2) {
                                blocks[index] = rightBlockDown;
                                index++;
                            }
                        }
                        leftBlockUp = leftBlockUp.getRelative(0, 1, 0);
                        leftBlockDown = leftBlockDown.getRelative(0, -1, 0);
                        rightBlockUp = rightBlockUp.getRelative(0, 1, 0);
                        rightBlockDown = rightBlockDown.getRelative(0, -1, 0);
                    }
                    leftBlockStart = getRelative(leftBlockStart, left);
                    rightBlockStart = getRelative(rightBlockStart, right);
                }
                start = getRelative(start, front);
            }
            for (int i = 0; i < blocks.length; i++) {
                if (blocks[i] != null && DUtils.nextInt(10) != 0) {
                    blocks[i].setTypeIdAndData(18, leaveValue, true);
                }
            }
        } catch (Exception e) {
            // no towny/worldguard permission
            return SkillResult.INVALID_TERRAIN;
        }
        // Messaging.send(hero.getPlayer(), "Leave Wall has grown");
        return SkillResult.SUCESSFULL;
    }

    private Block getRelative(Block block, int[] pos) {
        return block.getRelative(pos[0], pos[1], pos[2]);
    }
}
