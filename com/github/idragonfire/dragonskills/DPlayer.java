package com.github.idragonfire.dragonskills;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class DPlayer {
    public HashMap<Material, String> bindList;
    private String name;
    private Player bukkitPlayer;

    public DPlayer(String playerName, Player bukkitPlayer) {
        name = playerName;
        this.bukkitPlayer = bukkitPlayer;
        bindList = new HashMap<Material, String>();
    }

    public boolean hasBind(Material material) {
        return bindList.containsKey(material);
    }

    public void onPlayerInteractEvent(Skills skills, Material mat,
            PlayerInteractEvent event) {
        if (!hasBind(mat)) {
            return;
        }
        event.setCancelled(true);
        skills.executeSkill(bindList.get(mat), bukkitPlayer);
    }
}
