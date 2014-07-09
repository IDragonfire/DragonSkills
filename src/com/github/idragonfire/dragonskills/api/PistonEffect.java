package com.github.idragonfire.dragonskills.api;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.utils.DUtils;

public abstract class PistonEffect implements Runnable {
    protected int taskID;

    protected Block[] targetBlocks;
    protected int duration;
    protected int height;
    protected boolean sticky;
    protected Player player;

    protected Block[] powerBlocks;
    protected BlockState[] states;
    protected DragonSkillsPlugin plugin;
    protected int tickCount;

    protected boolean isValid;

    public PistonEffect(DragonSkillsPlugin plugin, Player player,
            Block[] targetBlocks, int duration, int height, boolean sticky) {
        this.targetBlocks = targetBlocks;
        this.duration = duration * 20;
        this.height = height;
        this.sticky = sticky;
        this.plugin = plugin;
        this.player = player;
        init();
        if (isValid) {
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,
                    this, 1, 1);
        }
    }

    public boolean isValid() {
        return isValid;
    }

    protected void init() {
        isValid = fetchStateOfBlocks();
        if (!isValid) {
            return;
        }
        clearMoveArea();
        clearTargets();
        placePistons();
        tickCount = 0;
    }

    protected void clearTargets() {
        for (int i = 0; i < targetBlocks.length; i++) {
            targetBlocks[i].setType(Material.SOUL_SAND);
        }
    }

    protected void placePistons() {
        Material mat = sticky ? Material.PISTON_STICKY_BASE
                : Material.PISTON_BASE;
        for (int i = 0; i < targetBlocks.length; i++) {
            targetBlocks[i].getRelative(BlockFace.DOWN).setTypeIdAndData(
                    mat.getId(), (byte) 1, false);
        }
    }

    protected void clearMoveArea() {
        for (int y = 0; y < height; y++) {
            for (int i = 0; i < targetBlocks.length; i++) {
                targetBlocks[i].getRelative(0, y + 1, 0).setType(Material.AIR);

                // player.sendBlockChange(
                // targetBlocks[i].getRelative(0, y + 1, 0).getLocation(),
                // Material.GLASS.getId(), (byte) 0);

            }
        }
    }

    protected boolean fetchStateOfBlocks() {
        // from bot to top, otherwise items, like flowers have problems
        states = new BlockState[targetBlocks.length * (height + 3)];
        Block tmp;
        for (int i = 0; i < targetBlocks.length; i++) {
            // powerBlock
            tmp = targetBlocks[i].getRelative(0, -2, 0);
            if (isInvalidBlock(tmp)) {
                return false;
            }
            states[i] = tmp.getState();
            // piston
            tmp = targetBlocks[i].getRelative(0, -1, 0);
            if (isInvalidBlock(tmp)) {
                return false;
            }
            states[i + targetBlocks.length] = tmp.getState();

            // target
            tmp = targetBlocks[i];
            if (isInvalidBlock(tmp)) {
                return false;
            }
            states[i + targetBlocks.length * 2] = tmp.getState();

            // movearea
            for (int j = 0; j < height; j++) {
                tmp = targetBlocks[i].getRelative(0, j + 1, 0);
                if (isInvalidBlock(tmp)) {
                    return false;
                }
                states[i + targetBlocks.length * (j + 3)] = tmp.getState();
            }

        }
        // for (int i = 0; i < states.length; i++) {
        // player.sendBlockChange(
        // states[i].getLocation(), Material.GLASS.getId(), (byte) 0);
        // }
        return true;
    }

    protected boolean isInvalidBlock(Block b) {
        if (DUtils.isForbiddenToTransform(null, b)) {
            return false;
        }
        BlockBreakEvent e = new BlockBreakEvent(b, player);
        Bukkit.getPluginManager().callEvent(e);
        return e.isCancelled();
    }

    @Override
    public void run() {
        if (duration < tickCount) {
            DSystem.log("Server to fast?");
            return;
        }
        if (duration == tickCount) {
            Bukkit.getScheduler().cancelTask(taskID);
            restore();
        } else {
            tickEvent();
        }
        tickCount++;
    }

    protected void restore() {
        restorePowerToPreventPistonBugs();
        restoreBlocks();
    }

    protected void restorePowerToPreventPistonBugs() {
        for (int i = 0; i < targetBlocks.length; i++) {
            targetBlocks[i].getRelative(0, -2, 0).setType(Material.DIRT);
        }
    }

    protected void restoreBlocks() {
        for (int i = 0; i < states.length; i++) {
            states[i].update(true);
        }
    }

    protected abstract void tickEvent();
}
