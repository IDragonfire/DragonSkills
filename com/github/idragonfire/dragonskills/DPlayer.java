package com.github.idragonfire.dragonskills;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
            "yyyyMMddHHmmss");
    private HashMap<Material, String> bindList;
    private HashMap<String, Long> cooldowns;
    private Player bukkitPlayer;
    private File file;

    public DPlayer(PlayerStorage storage, Player bukkitPlayer) {
        this(storage.getPlayerFile(bukkitPlayer), bukkitPlayer);
    }

    public DPlayer(File storageFile, Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        file = storageFile;
        bindList = new HashMap<Material, String>();
        cooldowns = new HashMap<String, Long>();
    }

    public void addCooldown(String skillName, int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, seconds);
        cooldowns.put(skillName, Long.valueOf(FORMAT.format(cal.getTime())));
        save();
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
        skills.useSkill(bindList.get(mat), this);
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public void save() {
        FileConfiguration player = new YamlConfiguration();
        // save binds
        ConfigurationSection section = player.createSection("bind");
        for (Material material : bindList.keySet()) {
            section.set(material.toString(), bindList.get(material));
        }
        // save cooldowns
        Long current_time = Long.valueOf(FORMAT.format(new Date()));
        section = player.createSection("cooldown");
        for (String skillName : cooldowns.keySet()) {
            if (cooldowns.get(skillName) > current_time) {
                section.set(skillName, cooldowns.get(skillName));
            }
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
