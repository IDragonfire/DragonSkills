package com.github.idragonfire.dragonskills.api;

import org.bukkit.Bukkit;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.utils.DUtils;

public abstract class TickEffect implements Runnable {
    protected int tickCount = 0;
    private int taskId;

    protected int durationInEffectTick;
    protected DragonSkillsPlugin plugin;
    protected long delayInTicks;
    protected long repeatInTicks;

    public TickEffect(DragonSkillsPlugin plugin, long delayInTicks,
            long repeatInTicks, int durationInEffectTick) {
        this.durationInEffectTick = durationInEffectTick;
        this.plugin = plugin;
        this.delayInTicks = delayInTicks;
        this.repeatInTicks = repeatInTicks;
    }

    public TickEffect(DragonSkillsPlugin plugin, long delayInSeconds,
            int durationInSeconds) {
        this(plugin, delayInSeconds * DUtils.TICKS, DUtils.TICKS,
                durationInSeconds);
    }

    public void start() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this,
                delayInTicks, repeatInTicks);
    }

    @Override
    public void run() {
        if (tickCount > durationInEffectTick) {
            DSystem.log("overload");
            return;
        }
        if (tickCount == durationInEffectTick) {
            Bukkit.getScheduler().cancelTask(taskId);
            endEffect();
        } else {
            effectTick();
        }
        tickCount++;
    }

    protected abstract void effectTick();

    protected void endEffect() {
        // nothing
    }
}
