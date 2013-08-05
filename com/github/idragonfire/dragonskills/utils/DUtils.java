package com.github.idragonfire.dragonskills.utils;

import java.util.HashSet;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class DUtils {
    public static final int[] LEFT = new int[] { -1, 0, 0 };
    public static final int[] RIGHT = new int[] { 1, 0, 0 };
    public static final int[] BACK = new int[] { 0, 0, 1 };
    public static final int[] FRONT = new int[] { 0, 0, -1 };

    private static Random rand = new Random();

    public static int nextInt(int max) {
        return rand.nextInt(max);
    }

    public enum Direction {
        NORTH, EAST, SOUTH, WEST, INVALID;
    }

    public static Direction getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {

            return Direction.NORTH;
        } else if (22.5 <= rotation && rotation < 67.5) {
            // NE
            return Direction.INVALID;
        } else if (67.5 <= rotation && rotation < 112.5) {
            return Direction.EAST;
        } else if (112.5 <= rotation && rotation < 157.5) {
            // SE
            return Direction.INVALID;
        } else if (157.5 <= rotation && rotation < 202.5) {
            return Direction.SOUTH;
        } else if (202.5 <= rotation && rotation < 247.5) {
            // SW
            return Direction.INVALID;
        } else if (247.5 <= rotation && rotation < 292.5) {
            return Direction.WEST;
        } else if (292.5 <= rotation && rotation < 337.5) {
            // NW
            return Direction.INVALID;
        } else if (337.5 <= rotation && rotation < 360.0) {
            return Direction.NORTH;
        }
        return Direction.INVALID;
    }

    public static Vector getDirection(Player player) {
        try {
            return player.getLocation().getDirection();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static HashSet<Material> allowedGrassMaterials = new HashSet<Material>();

    static {
        allowedGrassMaterials.add(Material.AIR);
        allowedGrassMaterials.add(Material.GRASS);
        allowedGrassMaterials.add(Material.RED_ROSE);
        allowedGrassMaterials.add(Material.YELLOW_FLOWER);
        allowedGrassMaterials.add(Material.SNOW);
        allowedGrassMaterials.add(Material.LONG_GRASS);
        allowedGrassMaterials.add(Material.VINE);
        allowedGrassMaterials.add(Material.LEAVES);
    }

    public static boolean isAllowedGrassMaterial(Material toCheck) {
        return allowedGrassMaterials.contains(toCheck);
    }

    public static boolean transformBlock(Player player, Block block,
            byte leaveValue) throws Exception {
        // if (useTowny) {
        // boolean towny_allowed = PlayerCacheUtil.getCachePermission(hero
        // .getPlayer(), block.getLocation(), LEAVE_ID, (byte) 0,
        // TownyPermission.ActionType.BUILD);
        // if (!towny_allowed) {
        // Messaging.send(hero.getPlayer(),
        // "$1 Your are not allowed to build here",
        // new Object[] { "Towny:" });
        // System.out.println(hero.getPlayer().getDisplayName()
        // + " try to use Skill " + getName() + " in towny area "
        // + block.getLocation());
        // throw new Exception("Towny: no access");
        // }
        // }
        // if (useWorldGuard) {
        // boolean worldguard_allowed = worldGuard.canBuild(hero
        // .getPlayer(), block.getLocation());
        // if (!worldguard_allowed) {
        // Messaging.send(hero.getPlayer(),
        // "$1 Your are not allowed to build here",
        // new Object[] { "WorldGuard:" });
        // System.out.println(hero.getPlayer().getDisplayName()
        // + " try to use Skill " + getName()
        // + " in world guard area " + block.getLocation());
        // throw new Exception("WorldGuard: no access");
        // }
        // }
        if (DUtils.isAllowedGrassMaterial(block.getType())) {
            // if (this.r.nextInt(10) != 0) {
            // block.setTypeIdAndData(18, leaveValue, true);
            // }
            block.setTypeIdAndData(18, leaveValue, true);
            return true;
        }
        return false;
    }

    private Block getRelative(Block block, int[] pos) {
        return block.getRelative(pos[0], pos[1], pos[2]);
    }

    public static BlockFace getPlayerDirection(Player player) {
        Block wTargetBlock = player.getTargetBlock(null, 120);

        double wTargetX = wTargetBlock.getX();
        double wTargetZ = wTargetBlock.getZ();

        double wCenterX = player.getLocation().getX();
        double wCenterZ = player.getLocation().getZ();

        double wAngle = Math
                .atan((wTargetX - wCenterX) / (wCenterZ - wTargetZ)) * 57.295779513082323D;

        if ((wTargetX > wCenterX) && (wTargetZ > wCenterZ)) {
            wAngle = 90.0D + wAngle + 90.0D;
        } else if ((wTargetX < wCenterX) && (wTargetZ > wCenterZ)) {
            wAngle += 180.0D;
        } else if ((wTargetX < wCenterX) && (wTargetZ < wCenterZ)) {
            wAngle = 90.0D + wAngle + 270.0D;
        }

        BlockFace wDirection = null;
        if (wAngle < 45.0D) {
            // datamodel EAST, ingame NORTH
            wDirection = BlockFace.NORTH;
        } else if (wAngle < 135.0D) {
            // datamodel SOUTH, ingame EAST
            wDirection = BlockFace.EAST;
        } else if (wAngle < 225.0D) {
            // datamodel WEST, ingame SOUTH
            wDirection = BlockFace.SOUTH;
        } else if (wAngle < 315.0D) {
            // datamodel NORTH, ingame WEST
            wDirection = BlockFace.WEST;
        } else if (wAngle < 360.0D) {
            // datamodel EAST, ingame NORTH
            wDirection = BlockFace.NORTH;
        }

        return wDirection;
    }
}
