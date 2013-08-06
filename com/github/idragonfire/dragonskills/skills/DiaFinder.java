package com.github.idragonfire.dragonskills.skills;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import api.DSystem;
import api.SkillResult;
import api.TargetBlockSkill;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public class DiaFinder extends TargetBlockSkill {
    private final Material[] ALLOWED_MATERIALS = new Material[] {
            Material.STONE, Material.DIAMOND_ORE, Material.GOLD_ORE,
            Material.IRON_ORE, Material.COAL_ORE, Material.REDSTONE_ORE,
            Material.LAPIS_ORE, Material.GRAVEL, Material.DIRT, Material.WOOD,
            Material.FENCE, Material.MOSSY_COBBLESTONE };

    @SkillConfig
    private int checkDistance = 100;
    @SkillConfig
    private HashSet<Material> allowedMaterialsForTunnelDestruction;

    public DiaFinder(DragonSkillsPlugin plugin) {
        super(plugin);
        // super(plugin, "DiaFinder");
        // setDescription("Detect Diamond Ore up to $1 blocks in front of you.");
        // setUsage("/skill diafinder");
        // setIdentifiers(new String[] { "skill diafinder" });
        // setTypes(new SkillType[] { SkillType.ITEM });
        allowedMaterialsForTunnelDestruction = new HashSet<Material>();
        for (int i = 0; i < ALLOWED_MATERIALS.length; i++) {
            allowedMaterialsForTunnelDestruction.add(ALLOWED_MATERIALS[i]);
        }
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

    @Override
    public SkillResult use(final Player player, final Block targetBlock) {
        BlockFace[] faces = DUtils.getDirections(player);
        int diamonds = 0;
        ArrayList<Block> startBlocks = new ArrayList<Block>();
        startBlocks.add(targetBlock);
        startBlocks.add(targetBlock.getRelative(BlockFace.DOWN));
        startBlocks.add(targetBlock.getRelative(BlockFace.UP));
        startBlocks.add(targetBlock.getRelative(faces[DUtils.LEFT]));
        startBlocks.add(targetBlock.getRelative(faces[DUtils.RIGHT]));
        startBlocks.add(startBlocks.get(2).getRelative(BlockFace.UP));
        startBlocks.add(startBlocks.get(3).getRelative(BlockFace.UP));
        startBlocks.add(startBlocks.get(4).getRelative(BlockFace.UP));
        for (int i = 0; i < startBlocks.size(); i++) {
            if (isProtected(player, startBlocks.get(i))) {

                continue;
            }
            startBlocks.get(i).breakNaturally(
                    new ItemStack(Material.DIAMOND_PICKAXE));
            if (i == 1) {
                startBlocks.get(i).setType(Material.TORCH);
            }
        }
        for (int i = 0; i < checkDistance; i++) {
            for (int j = 0; j < startBlocks.size(); j++) {
                startBlocks.set(j, startBlocks.get(j).getRelative(
                        faces[DUtils.FRONT]));
                if (startBlocks.get(j).getType() == Material.DIAMOND_ORE) {
                    diamonds++;
                }
                // player.sendBlockChange(startBlocks.get(j).getLocation(),
                // Material.GLASS, (byte) 0);
            }
        }
        if (diamonds > 0) {
            DSystem.log("Found $1 diamonds", diamonds);
        } else {
            DSystem.log("Found $1", "nothing");
        }
        return SkillResult.SUCESSFULL;
    }

    private boolean isProtected(Player player, Block block) {
        return !allowedMaterialsForTunnelDestruction.contains(block.getType());
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
    }
}