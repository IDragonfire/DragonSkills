package api;

import org.bukkit.Material;
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
        Block b = player.getTargetBlock(null, targetBlockMaxDistance);
        if (b.getType() == Material.AIR) {
            DSystem.log("to far away");
            return SkillResult.FAIL;
        }
        return use(player, b);
    }

    public abstract SkillResult use(Player player, Block targetBlock);

}
