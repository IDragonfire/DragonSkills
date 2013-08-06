package api;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public abstract class TargetBlockSkill extends ActiveSkill {

    @SkillConfig
    protected int targetBlockMaxDistance = 16;

    public TargetBlockSkill(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    public int getTargetBlockMaxDistance() {
        return targetBlockMaxDistance;
    }

    public void setTargetBlockMaxDistance(int targetBlockMaxDistance) {
        this.targetBlockMaxDistance = targetBlockMaxDistance;
    }

    @Override
    public SkillResult use(Player player) {
        return use(player, player.getTargetBlock(null, targetBlockMaxDistance));
    }

    public abstract SkillResult use(Player player, Block targetBlock);

}
