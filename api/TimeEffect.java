package api;

import org.bukkit.Bukkit;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;

public abstract class TimeEffect implements Runnable {
    private long duration;
    private DragonSkillsPlugin plugin;

    public TimeEffect(DragonSkillsPlugin plugin, long duration) {
        super();
        this.duration = duration;
        this.plugin = plugin;
    }

    public void startEffect() {
        initTimeEffect();
        plugin.addTimeEffect(this);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, duration);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public void run() {
        plugin.removeTimeEffect(this);
        endTimeEffect();
    }

    public long getDuration() {
        return duration;
    }

    public abstract void initTimeEffect();

    public abstract void endTimeEffect();
}
