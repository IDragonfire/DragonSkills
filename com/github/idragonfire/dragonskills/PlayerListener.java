package com.github.idragonfire.dragonskills;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {
    private Skills skills;
    private PlayerStorage players;

    public PlayerListener(DragonSkillsPlugin plugin) {
        super();
        players = plugin.getPlayerStorage();
        skills = plugin.getSkills();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        players.getDPlayer(event.getPlayer()).onPlayerInteractEvent(skills,
                event.getPlayer().getItemInHand().getType(), event);

    }
}
