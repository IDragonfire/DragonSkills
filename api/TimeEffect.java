package api;

import org.bukkit.Bukkit;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.utils.DUtils;

public abstract class TimeEffect implements Runnable {
    private long duration;
    private DragonSkillsPlugin plugin;

    public TimeEffect(DragonSkillsPlugin plugin, long duration) {
        super();
        this.duration = duration;
        this.plugin = plugin;
    }

    public void startEffect(long delay) {
        if (delay <= 0) {
            startEffect();
        } else {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                    new Runnable() {

                        @Override
                        public void run() {
                            startEffect();
                        }
                    }, delay);
        }
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

    public int getDurationInSeconds() {
        return (int) (getDuration() / DUtils.TICKS);
    }

    public void initTimeEffect() {
        // maybe nothing
    }

    public abstract void endTimeEffect();
}
