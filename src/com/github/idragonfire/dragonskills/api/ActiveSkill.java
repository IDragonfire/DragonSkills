package com.github.idragonfire.dragonskills.api;

import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public abstract class ActiveSkill extends Skill {
    @SkillConfig
    public int cooldownInSeconds = 0;

    public ActiveSkill(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    public int getCooldown() {
        return cooldownInSeconds;
    }

    public void setDefaultCooldown(int defaultCooldownInSeconds) {
        cooldownInSeconds = defaultCooldownInSeconds;
    }

    public abstract SkillResult use(Player player);
}
