package com.github.idragonfire.dragonskills;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

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
        DPlayer player = null;
        try {
            player = DPlayer.load(getPlayerFile(bukkitPlayer), bukkitPlayer);
        } catch (Exception e) {
            e.printStackTrace();
            player = new DPlayer(this, bukkitPlayer);
        }
        playerStorage.put(bukkitPlayer.getName(), player);
        return player;
    }

    public void unloadPlayer(Player player) {
        unloadPlayer(getDPlayer(player));
    }

    private void unloadPlayer(DPlayer player) {
        player.save();
        playerStorage.remove(player.getBukkitPlayer().getName());
    }

    public File getPlayerFile(Player player) {
        return new File(playerFolder.getAbsolutePath() + File.separator
                + player.getName().toLowerCase() + ".yml");
    }

    /**
     * remove DPlayer from cache
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        unloadPlayer(event.getPlayer());
    }

    /**
     * load DPlayer into cache
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        getDPlayer(event.getPlayer());
    }

    private class DPlayerSaveThread implements Runnable {
        public void run() {
            List<DPlayer> players = new ArrayList<DPlayer>(playerStorage
                    .values());
            for (DPlayer player : players) {
                player.save();
            }
        }
    }
}
