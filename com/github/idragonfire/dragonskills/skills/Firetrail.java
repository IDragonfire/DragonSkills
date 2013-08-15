package com.github.idragonfire.dragonskills.skills;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

public class Firetrail extends ActiveSkill {
    @SkillConfig
    private int duration = 10;

    public Firetrail(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public SkillResult use(Player player) {
        new FireTrailEffect(plugin, player, duration * DUtils.TICKS)
                .startEffect();
        return SkillResult.SUCESSFULL;
    }

    @Override
    public String getDescription() {
        return DSystem
                .paramString(
                        "let a fire trail behind your for $1 seconds and make you fire immune",
                        duration);
    }

    public class FireTrailEffect extends TimeEffect implements Listener {
        private Player player;
        private Block lastBlock;

        public FireTrailEffect(DragonSkillsPlugin plugin, Player player,
                long duration) {
            super(plugin, duration);
            this.player = player;
        }

        @Override
        public void initTimeEffect() {
            Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.FIRE_RESISTANCE, (int) getDuration()
                            + DUtils.TICKS, 1));
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onPlayerMoveEvent(PlayerMoveEvent event) {
            if (event.getPlayer() != player) {
                return;
            }
            if (lastBlock != null
                    && !lastBlock.equals(event.getFrom().getBlock())) {
                if (lastBlock.getType() == Material.AIR
                        && DUtils.canBreak(player, lastBlock)) {
                    lastBlock.setType(Material.FIRE);
                }
            }
            lastBlock = event.getFrom().getBlock();
            player.setFireTicks(0);
        }

        @Override
        public void endTimeEffect() {
            HandlerList.unregisterAll(this);
            player.setFireTicks(0);
        }
    }

}
