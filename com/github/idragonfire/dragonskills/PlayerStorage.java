package com.github.idragonfire.dragonskills;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.github.idragonfire.dragonskills.skills.DiaFinder;
import com.github.idragonfire.dragonskills.skills.Hole;
import com.github.idragonfire.dragonskills.skills.LeaveWall;

public class PlayerStorage implements Listener {
    public HashMap<String, DPlayer> playerStorage;

    public PlayerStorage() {
        playerStorage = new HashMap<String, DPlayer>();
    }

    public DPlayer getDPlayer(Player player) {
        if (!playerStorage.containsKey(player.getName())) {
            return initDPlayerObject(player);
        }
        return playerStorage.get(player.getName());
    }

    private DPlayer initDPlayerObject(Player bukkitPlayer) {
        DPlayer player = new DPlayer(bukkitPlayer);
        player.bindList.put(Material.STICK, LeaveWall.class.getSimpleName());
        player.bindList.put(Material.DIAMOND, DiaFinder.class.getSimpleName());
        player.bindList.put(Material.WOOD_SPADE, Hole.class.getSimpleName());
        playerStorage.put(bukkitPlayer.getName(), player);
        return player;
    }
}
