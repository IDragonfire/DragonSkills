package com.github.idragonfire.dragonskills.skills;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import api.ActiveSkill;
import api.DSystem;
import api.SkillResult;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.DUtils.Direction;

public class DiaFinder extends ActiveSkill {
    private static final int FRONT = 0;
    private static final int RIGHT = 1;
    private static final int BACK = 2;
    private static final int LEFT = 3;
    private static final int MAX_DISTANCE = 100;
    private final Material[] ALLOWED_MATERIALS = new Material[] {
            Material.STONE, Material.DIAMOND_ORE, Material.GOLD_ORE,
            Material.IRON_ORE, Material.COAL_ORE, Material.REDSTONE_ORE,
            Material.LAPIS_ORE, Material.GRAVEL, Material.DIRT, Material.WOOD,
            Material.FENCE, Material.MOSSY_COBBLESTONE };
    private HashSet<Material> allowedMaterials;

    public DiaFinder(DragonSkillsPlugin plugin) {
        super(plugin);
        // super(plugin, "DiaFinder");
        // setDescription("Detect Diamond Ore up to $1 blocks in front of you.");
        // setUsage("/skill diafinder");
        // setIdentifiers(new String[] { "skill diafinder" });
        // setTypes(new SkillType[] { SkillType.ITEM });
        // this.allowedMaterials = new HashSet<Material>();
        // for (int i = 0; i < DiaFinder.this.ALLOWED_MATERIALS.length; i++) {
        // this.allowedMaterials.add(DiaFinder.this.ALLOWED_MATERIALS[i]);
        // }
    }

    // @Override
    // public String getDescription(Hero hero) {
    // int maxDistance = SkillConfigManager.getUseSetting(hero, this,
    // Setting.AMOUNT.node(), MAX_DISTANCE, false);
    // StringBuffer sb = new StringBuffer(super.getDescription().replace("$1",
    // "" + maxDistance));
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
    // node.set(Setting.COOLDOWN.node(), Double.valueOf(0.2D));
    // node.set(Setting.MANA.node(), Integer.valueOf(0));
    // node.set(Setting.RADIUS.node(), Integer.valueOf(MAX_DISTANCE));
    // return node;
    // }

    @SuppressWarnings("boxing")
    @Override
    public SkillResult use(Player player) {
        try {
            Block wTargetBlock = player.getLocation().getBlock();
            Direction direction = DUtils.getCardinalDirection(player);
            BlockFace[] vector = transformBlockFace(direction);

            boolean areaProtected = false;
            int diamonds = 0;
            ArrayList<Block> startBlocks = new ArrayList<Block>();
            startBlocks.add(wTargetBlock);
            startBlocks.add(wTargetBlock.getRelative(BlockFace.DOWN));
            startBlocks.add(wTargetBlock.getRelative(BlockFace.UP));
            startBlocks.add(wTargetBlock.getRelative(vector[LEFT]));
            startBlocks.add(wTargetBlock.getRelative(vector[RIGHT]));
            startBlocks.add(startBlocks.get(2).getRelative(BlockFace.UP));
            startBlocks.add(startBlocks.get(3).getRelative(BlockFace.UP));
            startBlocks.add(startBlocks.get(4).getRelative(BlockFace.UP));
            for (int i = 0; i < startBlocks.size(); i++) {
                Block tmpBlock = startBlocks.get(i).getRelative(vector[FRONT]);
                // if (isProtected(hero, tmpBlock)) {
                // areaProtected = true;
                // break;
                // }
                tmpBlock
                        .breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
                if (i == 1) {
                    for (int j = 0; j < 36; j++) {
                        tmpBlock.setType(Material.TORCH);
                    }

                }
            }
            // int maxDistance = SkillConfigManager.getUseSetting(hero, this,
            // Setting.AMOUNT.node(), MAX_DISTANCE, false);
            int maxDistance = 100;
            for (int i = 0; i < maxDistance; i++) {
                for (int j = 0; j < startBlocks.size(); j++) {
                    startBlocks.set(j, startBlocks.get(j).getRelative(
                            vector[FRONT]));
                    // hero.getPlayer().sendBlockChange(
                    // startBlocks.get(j).getLocation(), Material.GLASS,
                    // (byte) 0);

                    if (startBlocks.get(j).getType() == Material.DIAMOND_ORE) {
                        diamonds++;
                    }
                    // player.sendBlockChange(startBlocks.get(j).getLocation(),
                    // Material.GLASS, (byte) 0);
                }
            }
            if (areaProtected) {
                return SkillResult.FAIL;
            }
            if (diamonds > 0) {
                DSystem.log("Found : " + diamonds);
                // Messaging.send(player, "Found $1 Diamond Ore in the $2",
                // new Object[] { diamonds, direction });
            } else {
                DSystem.log("Found nothing");
                // Messaging.send(player,
                // "No Diamond Ore found within $1 blocks in the $2",
                // new Object[] { maxDistance, direction });
            }
        } catch (Exception e) {
            e.printStackTrace();
            return SkillResult.FAIL;
        }
        return SkillResult.SUCESSFULL;
    }

    // @SuppressWarnings("unqualified-field-access")
    // private boolean isProtected(Hero hero, Block block) {
    // boolean allowed = true;
    // if (!allowedMaterials.contains(block.getType())) {
    // Messaging.send(hero.getPlayer(), "Not possible to cast into "
    // + block.getType());
    // return true;
    // }
    // if (useTowny) {
    // allowed = PlayerCacheUtil.getCachePermission(hero.getPlayer(),
    // block.getLocation(), new Integer(54), (byte) 0,
    // TownyPermission.ActionType.BUILD);
    // if (!allowed) {
    // Messaging.send(hero.getPlayer(),
    // "$1 Your are not allowed to build here",
    // new Object[] { "Towny:" });
    // System.out.println(hero.getPlayer().getDisplayName()
    // + " try to use Skill " + getName() + " in towny area "
    // + block.getLocation());
    // return true;
    // }
    // }
    // if (useWorldGuard) {
    // allowed = worldGuard
    // .canBuild(hero.getPlayer(), block.getLocation());
    // if (!allowed) {
    // Messaging.send(hero.getPlayer(),
    // "$1 Your are not allowed to build here",
    // new Object[] { "WorldGuard:" });
    // System.out.println(hero.getPlayer().getDisplayName()
    // + " try to use Skill " + getName()
    // + " in world guard area " + block.getLocation());
    // return true;
    // }
    // }
    // return !allowed;
    // }

    private static BlockFace[] transformBlockFace(DUtils.Direction direction) {
        BlockFace[] vectors = new BlockFace[4];
        switch (direction) {
        case NORTH:
            vectors[FRONT] = BlockFace.WEST;
            vectors[RIGHT] = BlockFace.NORTH;
            vectors[BACK] = BlockFace.EAST;
            vectors[LEFT] = BlockFace.SOUTH;
            return vectors;
        case EAST:
            vectors[FRONT] = BlockFace.NORTH;
            vectors[RIGHT] = BlockFace.EAST;
            vectors[BACK] = BlockFace.SOUTH;
            vectors[LEFT] = BlockFace.WEST;
            return vectors;
        case SOUTH:
            vectors[FRONT] = BlockFace.EAST;
            vectors[RIGHT] = BlockFace.SOUTH;
            vectors[BACK] = BlockFace.WEST;
            vectors[LEFT] = BlockFace.NORTH;
            return vectors;
        case WEST:
            vectors[FRONT] = BlockFace.SOUTH;
            vectors[RIGHT] = BlockFace.WEST;
            vectors[BACK] = BlockFace.NORTH;
            vectors[LEFT] = BlockFace.EAST;

            return vectors;
        default:
            return null;
        }
    }
}