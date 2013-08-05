package api;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;

public abstract class Skill {
    protected DragonSkillsPlugin plugin;

    public Skill(DragonSkillsPlugin plugin) {
        this.plugin = plugin;
    }

    public String getSkillName() {
        return this.getClass().getSimpleName();
    }
}
