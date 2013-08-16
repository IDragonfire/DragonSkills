package com.github.idragonfire.dragonskills.skills;

import java.util.ArrayList;
import java.util.List;

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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.ActiveSkill;
import com.github.idragonfire.dragonskills.api.DSystem;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.api.TimeEffect;
import com.github.idragonfire.dragonskills.utils.DUtils;
import com.github.idragonfire.dragonskills.utils.SkillConfig;

public class Sandtrail extends ActiveSkill {
    @SkillConfig
    private int duration = 10;

    public Sandtrail(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public SkillResult use(Player player) {
        new SandTrailEffect(plugin, player, duration * DUtils.TICKS)
                .startEffect();
        return SkillResult.SUCESSFULL;
    }

    @Override
    public String getDescription() {
        return DSystem
                .paramString(
                        "let a trail of soulsand for $1 seconds behind you and make you faster",
                        duration);
    }

    public class SandTrailEffect extends TimeEffect implements Listener {
        private Player player;
        private Block lastBlock;
        private List<BlockState> states;

        public SandTrailEffect(DragonSkillsPlugin plugin, Player player,
                long duration) {
            super(plugin, duration);
            this.player = player;
            states = new ArrayList<BlockState>();
        }

        @Override
        public void initTimeEffect() {
            Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                    (int) getDuration(), 1));
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onPlayerMoveEvent(PlayerMoveEvent event) {
            if (event.getPlayer() != player) {
                return;
            }
            if (lastBlock != null
                    && !lastBlock.equals(event.getFrom().getBlock())) {
                Block tmp = lastBlock.getRelative(BlockFace.DOWN);
                if (lastBlock.getRelative(BlockFace.DOWN).getType() != Material.AIR
                        && DUtils.canBreak(player, tmp)) {
                    states.add(tmp.getState());
                    tmp.setType(Material.SOUL_SAND);
                }
            }
            lastBlock = event.getFrom().getBlock();
            player.setFireTicks(0);
        }

        @Override
        public void endTimeEffect() {
            HandlerList.unregisterAll(this);
            for (BlockState state : states) {
                if (state.getBlock().getType() == Material.SOUL_SAND) {
                    state.update(true);
                }
            }
        }
    }

}
