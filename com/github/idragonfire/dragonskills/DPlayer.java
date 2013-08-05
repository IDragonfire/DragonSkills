package com.github.idragonfire.dragonskills;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class DPlayer {
    public HashMap<Material, String> bindList;
    private Player bukkitPlayer;
    private File file;

    public DPlayer(PlayerStorage storage, Player bukkitPlayer) {
        this(storage.getPlayerFile(bukkitPlayer), bukkitPlayer);
    }

    public DPlayer(File storageFile, Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        bindList = new HashMap<Material, String>();
        file = storageFile;
    }

    public boolean hasBind(Material material) {
        return bindList.containsKey(material);
    }

    public void addBind(Material material, String skillName) {
        bindList.put(material, skillName);
        save();
    }

    public void onPlayerInteractEvent(Skills skills, Material mat,
            PlayerInteractEvent event) {
        if (!hasBind(mat)) {
            return;
        }
        event.setCancelled(true);
        skills.useSkill(bindList.get(mat), getBukkitPlayer());
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public void save() {
        FileConfiguration player = new YamlConfiguration();
        player.set("bind", bindList);

        ConfigurationSection section = player.createSection("bind");
        for (Material material : bindList.keySet()) {
            section.set(material.toString(), bindList.get(material));
        }

        try {
            player.save(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static DPlayer load(File file, Player bukkitPlayer)
            throws FileNotFoundException, IOException,
            InvalidConfigurationException {
        DPlayer player = new DPlayer(file, bukkitPlayer);
        FileConfiguration playerData = new YamlConfiguration();
        playerData.load(file);
        ConfigurationSection section = playerData
                .getConfigurationSection("bind");
        Set<String> binds = section.getKeys(false);
        if ((binds != null) && (binds.size() > 0)) {
            for (String materialString : binds) {
                player.addBind(Material.valueOf(materialString), section
                        .getString(materialString));
            }
        }
        return player;
    }
}
