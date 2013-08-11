package com.github.idragonfire.dragonskills.api;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;

public abstract class PistonEffect implements Runnable {
    protected int taskID;

    protected Block[] targetBlocks;
    protected int duration;
    protected int height;
    protected boolean sticky;

    protected Block[] powerBlocks;
    protected BlockState[] states;
    protected DragonSkillsPlugin plugin;
    protected int tickCount;

    public PistonEffect(DragonSkillsPlugin plugin, Block[] targetBlocks,
            int duration, int height, boolean sticky) {
        this.targetBlocks = targetBlocks;
        this.duration = duration * 20;
        this.height = height;
        this.sticky = sticky;
        this.plugin = plugin;
        init();
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this,
                0, 1);
    }

    protected void init() {
        fetchStateOfBlocks();
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
            }
        }
    }

    protected void fetchStateOfBlocks() {
        // from bot to top, otherwise items, like flowers have problems
        states = new BlockState[targetBlocks.length * (height + 3)];
        for (int i = 0; i < targetBlocks.length; i++) {
            // powerBlock
            states[i] = targetBlocks[i].getRelative(0, -2, 0).getState();

            // piston
            states[i + targetBlocks.length] = targetBlocks[i].getRelative(0,
                    -1, 0).getState();

            // target
            states[i + targetBlocks.length * 2] = targetBlocks[i].getState();

            // movearea
            for (int j = 0; j < height; j++) {
                states[i + targetBlocks.length * (j + 3)] = targetBlocks[i]
                        .getRelative(0, j + 1, 0).getState();
            }

        }
        // for (int i = 0; i < states.length; i++) {
        // Bukkit.getPlayer("IDragonfire").sendBlockChange(
        // states[i].getLocation(), Material.GLASS.getId(), (byte) 0);
        // }
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
