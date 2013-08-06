package com.github.idragonfire.dragonskills.utils;

import java.util.HashSet;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class DUtils {
    public static final int FRONT = 0;
    public static final int RIGHT = 1;
    public static final int BACK = 2;
    public static final int LEFT = 3;
    // public static final int[] LEFT = new int[] { -1, 0, 0 };
    // public static final int[] RIGHT = new int[] { 1, 0, 0 };
    // public static final int[] BACK = new int[] { 0, 0, 1 };
    // public static final int[] FRONT = new int[] { 0, 0, -1 };
    public static final int TICKS = 20;

    private static Random rand = new Random();

    public static int nextInt(int max) {
        return rand.nextInt(max);
    }

    public enum Direction {
        NORTH, EAST, SOUTH, WEST, INVALID;
    }

    public static BlockFace[] getDirections(Player player) {
        Direction direction = getDirection(player);
        BlockFace[] faces = new BlockFace[4];
        if (direction == Direction.NORTH) {
            faces[FRONT] = BlockFace.WEST;
            faces[RIGHT] = BlockFace.NORTH;
            faces[BACK] = BlockFace.EAST;
            faces[LEFT] = BlockFace.SOUTH;
            return faces;
        }
        if (direction == Direction.EAST) {
            faces[FRONT] = BlockFace.NORTH;
            faces[RIGHT] = BlockFace.EAST;
            faces[BACK] = BlockFace.SOUTH;
            faces[LEFT] = BlockFace.WEST;
            return faces;
        }
        if (direction == Direction.SOUTH) {
            faces[FRONT] = BlockFace.EAST;
            faces[RIGHT] = BlockFace.SOUTH;
            faces[BACK] = BlockFace.WEST;
            faces[LEFT] = BlockFace.NORTH;
            return faces;
        }
        if (direction == Direction.WEST) {
            faces[FRONT] = BlockFace.SOUTH;
            faces[RIGHT] = BlockFace.WEST;
            faces[BACK] = BlockFace.NORTH;
            faces[LEFT] = BlockFace.EAST;
            return faces;
        }

        return faces;
    }

    public static Direction getDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (45 <= rotation && rotation < 135) {
            return Direction.EAST;
        }
        if (135 <= rotation && rotation < 225) {
            return Direction.SOUTH;
        }
        if (225 <= rotation && rotation < 315) {
            return Direction.WEST;
        }
        // rotation < 45 && 315 <= rotation
        return Direction.NORTH;
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

    private static HashSet<Material> forbiddenMaterials = new HashSet<Material>();
    static {
        forbiddenMaterials.add(Material.CHEST);
        forbiddenMaterials.add(Material.ENDER_CHEST);
        forbiddenMaterials.add(Material.TRAPPED_CHEST);
        forbiddenMaterials.add(Material.DISPENSER);
        forbiddenMaterials.add(Material.DROPPER);
        forbiddenMaterials.add(Material.FURNACE);
        forbiddenMaterials.add(Material.PISTON_BASE);
        forbiddenMaterials.add(Material.PISTON_STICKY_BASE);
        forbiddenMaterials.add(Material.DIODE);
        forbiddenMaterials.add(Material.DAYLIGHT_DETECTOR);
        forbiddenMaterials.add(Material.REDSTONE_COMPARATOR);
        forbiddenMaterials.add(Material.RAILS);
        forbiddenMaterials.add(Material.DETECTOR_RAIL);
        forbiddenMaterials.add(Material.POWERED_RAIL);
        forbiddenMaterials.add(Material.BREWING_STAND);
        forbiddenMaterials.add(Material.JUKEBOX);
        forbiddenMaterials.add(Material.MOB_SPAWNER);
        forbiddenMaterials.add(Material.SIGN);
    }

    public static boolean isForbiddenToTransform(Player player, Block block) {
        if (forbiddenMaterials.contains(block.getType())) {
            return true;
        }
        return false;
    }

    public static boolean transformBlock(Player player, Block block, int type)
            throws Exception {
        return transformBlock(player, block, type, (byte) 0);
    }

    public static boolean transformBlock(Player player, Block block, int type,
            byte data) throws Exception {
        return transformBlock(player, block, type, data, true);
    }

    public static boolean transformBlock(Player player, Block block, int type,
            byte data, boolean applyPhysics) throws Exception {
        if (forbiddenMaterials.contains(block.getType())) {
            return false;
        }
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
        block.setTypeIdAndData(Material.LEAVES.getId(), data, applyPhysics);
        return true;
    }

    private Block getRelative(Block block, int[] pos) {
        return block.getRelative(pos[0], pos[1], pos[2]);
    }
}
