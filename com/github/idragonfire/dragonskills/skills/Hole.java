package com.github.idragonfire.dragonskills.skills;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import api.ActiveSkill;
import api.DSystem;
import api.SkillResult;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;

public class Hole extends ActiveSkill {
    private final HashSet<Material> ALLOWED = new HashSet<Material>();
    private final Material[] ALLOWED_MATERIALS = new Material[] { Material.AIR,
            Material.WOOD, /* Material.BEDROCK, */Material.LEAVES,
            Material.BROWN_MUSHROOM, Material.CLAY, Material.COAL_ORE,
            Material.COBBLESTONE, Material.COBBLESTONE_STAIRS, Material.ICE,
            Material.IRON_ORE, Material.DIRT, Material.FENCE,
            Material.RED_MUSHROOM, Material.GLOWSTONE, Material.GRASS,
            Material.GRAVEL, Material.LAPIS_BLOCK, Material.LAPIS_ORE,
            Material.LOG, Material.MOSSY_COBBLESTONE, Material.NETHERRACK,
            Material.OBSIDIAN, Material.REDSTONE_ORE, Material.RED_ROSE,
            Material.YELLOW_FLOWER, Material.SAND, Material.SNOW,
            Material.SOUL_SAND, Material.STONE, Material.WATER,
            Material.SUGAR_CANE, Material.STATIONARY_WATER,
            Material.STATIONARY_LAVA, Material.LONG_GRASS, Material.VINE,
            Material.SANDSTONE, Material.PUMPKIN, Material.LAVA,
            Material.DEAD_BUSH };
    private final int HOLE_TIME = 10 * 1000;
    private final int DELAY_TO_SPAWN = 3 * 1000;
    private final String HOLE_TIME_NODE = "holetime";
    private final String DELAY_TO_SPAWN_NODE = "delaytospawn";
    private final int[] LEFT = new int[] { -1, 0, 0 };
    private final int[] RIGHT = new int[] { 1, 0, 0 };
    private final int[] BACK = new int[] { 0, 0, 1 };
    private final int[] FRONT = new int[] { 0, 0, -1 };
    private final int[][] MOVMENT = new int[][] { { 0, 0, 0 }, RIGHT, BACK,
            LEFT, LEFT, LEFT, FRONT, RIGHT, FRONT, RIGHT, FRONT, RIGHT, BACK,
            RIGHT, BACK, RIGHT, BACK, LEFT, BACK, LEFT, BACK, LEFT, FRONT, LEFT };
    private final int[][] STAIRS = new int[][] { { 3, -1, 2 }, { 0, 1, 1 },
            { -1, 1, 0 }, { 0, 1, 1 }, { -1, 1, 0 }, { -1, 1, 0 }, { -1, 1, 0 } };
    private final int[][] RING = new int[][] { FRONT, { -1, 0, -1 },
            { -1, 0, -1 }, FRONT, { 1, 0, -1 }, { 1, 0, -1 }, { 1, 0, -1 },
            RIGHT, { 1, 0, 1 }, { 1, 0, 1 }, { 1, 0, 1 }, BACK, BACK, BACK,
            { -1, 0, 1 }, { -1, 0, 1 }, LEFT, LEFT };
    private final Vector<HoleEffect> RESTORE = new Vector<HoleEffect>();

    public Hole(DragonSkillsPlugin plugin) {
        super(plugin);
        // super(plugin, "Hole");
        // setDescription("open the hole after $1 seconds, the mine is calling to you for $2 minutes");
        // setUsage("/skill hole");
        // setIdentifiers(new String[] { "skill hole", "skill Hole" });
        // setTypes(new SkillType[] { SkillType.EARTH, SkillType.COUNTER });
        for (int i = 0; i < ALLOWED_MATERIALS.length; i++) {
            ALLOWED.add(ALLOWED_MATERIALS[i]);
        }
        Bukkit.getServer().getPluginManager().registerEvents(
                new HoleRestore(plugin), plugin);
    }

    // @Override
    // public String getDescription(Hero hero) {
    // int holeTime = SkillConfigManager.getUseSetting(hero, this,
    // HOLE_TIME_NODE, HOLE_TIME, false)
    // / (60 * 1000);
    // int delayToSpawn = SkillConfigManager.getUseSetting(hero, this,
    // DELAY_TO_SPAWN_NODE, DELAY_TO_SPAWN, false) / 1000;
    // StringBuffer sb = new StringBuffer(super.getDescription().replace("$1",
    // delayToSpawn + "").replace("$2", holeTime + ""));
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
    // ConfigurationSection node = super.getDefaultConfig();
    // node.set(HOLE_TIME_NODE, Integer.valueOf(HOLE_TIME));
    // node.set(DELAY_TO_SPAWN_NODE, Integer.valueOf(DELAY_TO_SPAWN));
    // return node;
    // }

    @Override
    public SkillResult use(Player player) {
        Block startBlock = player.getLocation().getBlock().getRelative(
                BlockFace.DOWN);
        if (!canPlaceHole(player, startBlock)) {
            DSystem.log("Cannot place mine here");
            return SkillResult.FAIL;
        }
        // final int holeTime = SkillConfigManager.getUseSetting(hero, this,
        // HOLE_TIME_NODE, HOLE_TIME, false);
        // final int delayToSpawn = SkillConfigManager.getUseSetting(hero, this,
        // DELAY_TO_SPAWN_NODE, DELAY_TO_SPAWN, false);

        final int holeTime = HOLE_TIME;
        final int delayToSpawn = DELAY_TO_SPAWN;
        final HoleEffect holeEffect = new HoleEffect(player, holeTime,
                startBlock);
        RESTORE.add(holeEffect);

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    try {
                        Thread.sleep(delayToSpawn);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    holeEffect.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        DSystem.log("The Mine call to you in $1 seconds");
        return SkillResult.SUCESSFULL;
    }

    public boolean canPlaceHole(Player player, Block start) {
        Block layer = start;
        Block tmp;
        for (int j = 0; j < 6; j++) {
            tmp = layer;
            for (int i = 0; i < MOVMENT.length; i++) {
                tmp = tmp.getRelative(MOVMENT[i][0], MOVMENT[i][1],
                        MOVMENT[i][2]);
                if (invalidBlock(player, tmp)) {
                    return false;
                }
            }
            layer = layer.getRelative(0, -1, 0);
        }
        tmp = layer.getRelative(0, 1, 0);
        for (int i = 0; i < STAIRS.length; i++) {
            tmp = tmp.getRelative(STAIRS[i][0], STAIRS[i][1], STAIRS[i][2]);

            Block tmp2 = tmp;
            for (int k = 0; k < 7 - i; k++) {
                tmp2 = tmp2.getRelative(0, 1, 0);
                if (invalidBlock(player, tmp2)) {
                    return false;
                }
            }
            if (invalidBlock(player, tmp)) {
                return false;
            }
        }
        for (int i = 0; i < RING.length; i++) {
            tmp = tmp.getRelative(RING[i][0], RING[i][1], RING[i][2]);
            if (invalidBlock(player, tmp)) {
                return false;
            }
            if (invalidBlock(player, tmp.getRelative(0, 1, 0))) {
                return false;
            }

        }
        tmp = start.getRelative(0, 1, 0);
        for (int i = 0; i < MOVMENT.length; i++) {
            tmp = tmp.getRelative(MOVMENT[i][0], MOVMENT[i][1], MOVMENT[i][2]);
            if (invalidBlock(player, tmp)) {
                return false;
            }
        }
        return true;
    }

    public boolean invalidBlock(Player player, Block block) {
        boolean allowed = ALLOWED.contains(block.getType());
        if (!allowed) {
            DSystem.log(player.getDisplayName() + " try to use Skill Hole on "
                    + block.getType());
        }
        return !allowed;
    }

    public class HoleEffect extends Thread {
        private Block block = null;
        private ArrayList<BlockState> store;
        private long duration;
        private Player player;

        public HoleEffect(Player player, long duration, Block startBlock) {
            block = startBlock;
            store = new ArrayList<BlockState>();
            this.player = player;
            this.duration = duration;
        }

        public void applyToHero(Player player) {
            // super.applyToHero(hero);
            DSystem.log("The Mine disapear in $1 seconds");
            Block tmp;
            for (int j = 0; j < 6; j++) {
                tmp = block;
                for (int i = 0; i < MOVMENT.length; i++) {
                    tmp = tmp.getRelative(MOVMENT[i][0], MOVMENT[i][1],
                            MOVMENT[i][2]);
                    store.add(tmp.getState());
                    if (tmp.getType() != Material.DIAMOND_ORE) {
                        tmp.setType(Material.AIR);
                    }
                }
                block = block.getRelative(0, -1, 0);
            }
            tmp = block.getRelative(0, 1, 0);
            for (int i = 0; i < STAIRS.length; i++) {
                tmp = tmp.getRelative(STAIRS[i][0], STAIRS[i][1], STAIRS[i][2]);
                Block tmp2 = tmp;
                for (int k = 0; k < 6 - i; k++) {
                    tmp2 = tmp2.getRelative(0, 1, 0);
                    store.add(tmp2.getState());
                    tmp2.setType(Material.AIR);

                }
                store.add(tmp.getState());
                tmp.setType(Material.BEDROCK);
            }
            for (int i = 0; i < RING.length; i++) {
                tmp = tmp.getRelative(RING[i][0], RING[i][1], RING[i][2]);
                store.add(tmp.getState());
                tmp.setType(Material.BEDROCK);
            }
        }

        public void removeFromHero(Player player) {
            DSystem.log("Mine has gone");
            restore();
            RESTORE.remove(this);
        }

        public void restore() {
            for (int j = store.size() - 1; j >= 0; j--) {
                store.get(j).update(true);
            }
            store.clear();
        }

        @Override
        public void run() {
            applyToHero(player);
            try {
                Thread.sleep(duration);
            } catch (Exception e) {
                e.printStackTrace();
            }
            removeFromHero(player);
        }
    }

    public class HoleRestore implements Listener {
        private Plugin pl;

        public HoleRestore(Plugin pl) {
            this.pl = pl;
        }

        @SuppressWarnings("synthetic-access")
        @EventHandler(priority = EventPriority.HIGHEST)
        public void onPluginDisable(PluginDisableEvent event) {
            if (event.getPlugin() == pl && RESTORE.size() > 0) {
                for (int i = 0; i < RESTORE.size(); i++) {
                    RESTORE.get(0).restore();
                }
            }
        }
    }
}