package com.github.idragonfire.dragonskills;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DragonSkillsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        final PlayerStorage players = new PlayerStorage();
        final Skills skills = new Skills(this);
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.LOWEST)
            public void onPlayerInteract(PlayerInteractEvent event) {
                players.getDPlayer(event.getPlayer()).onPlayerInteractEvent(
                        skills, event.getPlayer().getItemInHand().getType(),
                        event);

            }
        }, this);
    }
}
