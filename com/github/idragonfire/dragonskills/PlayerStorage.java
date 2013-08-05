package com.github.idragonfire.dragonskills;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import api.DSystem;

import com.github.idragonfire.dragonskills.skills.DiaFinder;
import com.github.idragonfire.dragonskills.skills.Hole;
import com.github.idragonfire.dragonskills.skills.LeaveWall;

public class PlayerStorage implements Listener {
    public HashMap<String, DPlayer> playerStorage;
    private final File playerFolder;
    private final long SAVE_INTERVAL = 5 * 20;

    public PlayerStorage(JavaPlugin plugin) {
        playerStorage = new HashMap<String, DPlayer>();
        playerFolder = new File(plugin.getDataFolder(), "players");
        playerFolder.mkdirs();

        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin,
                new DPlayerSaveThread(), SAVE_INTERVAL, SAVE_INTERVAL);
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

    private void unloadPlayer(DPlayer player) {
        savePlayer(player);
        playerStorage.remove(player);
    }

    public void savePlayer(DPlayer player) {
        try {

            File playerFile = new File(playerFolder.getAbsolutePath()
                    + File.separator
                    + player.getBukkitPlayer().getName().toLowerCase() + ".yml");
            player.save(playerFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DPlayerSaveThread implements Runnable {
        public void run() {
            DSystem.log("save players");
            List<DPlayer> players = new ArrayList<DPlayer>(playerStorage
                    .values());
            for (DPlayer player : players) {
                savePlayer(player);
            }
        }
    }
}
