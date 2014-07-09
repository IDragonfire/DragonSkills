package com.github.idragonfire.dragonskills.api;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;

public abstract class Skill {
    protected DragonSkillsPlugin plugin;

    public Skill(DragonSkillsPlugin plugin) {
        this.plugin = plugin;
    }

    public String getSkillName() {
        return this.getClass().getSimpleName();
    }

    public DragonSkillsPlugin getPlugin() {
        return plugin;
    }

    public abstract String getDescription();

    @Override
    public String toString() {
        return getSkillName();
    }
}
