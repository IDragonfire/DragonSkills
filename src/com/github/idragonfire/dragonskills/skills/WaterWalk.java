package com.github.idragonfire.dragonskills.skills;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.ActiveSkill;
import com.github.idragonfire.dragonskills.api.DSystem;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TimeEffect;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public class WaterWalk extends ActiveSkill {
    @SkillConfig
    private int duration = 10;

    public WaterWalk(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public SkillResult use(Player player) {
        new WaterWalkEffect(plugin, player, duration * DUtils.TICKS)
                .startEffect();
        return SkillResult.SUCESSFULL;
    }

    @Override
    public String getDescription() {
        return DSystem.paramString("You can walk over water for $1 seconds",
                duration);
    }

    public class WaterWalkEffect extends TimeEffect implements Listener {
        private Player player;
        private BlockState[] states;

        public WaterWalkEffect(DragonSkillsPlugin plugin, Player player,
                long duration) {
            super(plugin, duration);
            this.player = player;
            states = new BlockState[9];
        }

        @Override
        public void initTimeEffect() {
            Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onPlayerMoveEvent(PlayerMoveEvent event) {
            if (event.getPlayer() != player) {
                return;
            }
            Block[] blocks = DUtils.getHCube(event.getTo().getBlock()
                    .getRelative(BlockFace.DOWN), 3);
            restore();
            for (int i = 0; i < blocks.length; i++) {
                transformBlock(blocks[i], i);
            }
        }

        private void transformBlock(Block b, int index) {
            if (b.getType() != Material.WATER
                    && b.getType() != Material.STATIONARY_WATER) {
                return;
            }
            if (!DUtils.canBreak(player, b)) {
                return;
            }
            states[index] = b.getState();
            b.setType(Material.GLASS);
            return;

        }

        @Override
        public void endTimeEffect() {
            HandlerList.unregisterAll(this);
            restore();
        }

        private void restore() {
            for (int i = 0; i < states.length; i++) {
                if (states[i] != null
                        && states[i].getBlock().getType() == Material.GLASS) {
                    states[i].update(true);
                    states[i] = null;
                }
            }
        }
    }
}
