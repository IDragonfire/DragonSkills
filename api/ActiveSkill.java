package api;

import org.bukkit.entity.Player;

public abstract class ActiveSkill extends Skill {
    public abstract SkillResult use(Player player);
}
