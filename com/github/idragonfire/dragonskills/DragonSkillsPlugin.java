package com.github.idragonfire.dragonskills;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import api.ActiveSkill;

import com.github.idragonfire.dragonskills.skills.LeaveWall;

public class DragonSkillsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        final ActiveSkill skill = new LeaveWall();
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.LOWEST)
            public void onPlayerInteract(PlayerInteractEvent event) {
                if (event.getPlayer().getItemInHand().getType() == Material.STICK) {
                    event.setCancelled(true);
                    skill.use(event.getPlayer());
                }
            }
        }, this);
    }
}
