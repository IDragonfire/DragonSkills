package api;

import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;

public abstract class ActiveSkill extends Skill {
    public ActiveSkill(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    public abstract SkillResult use(Player player);
}
