package com.github.idragonfire.dragonskills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import api.DSystem;
import api.Skill;
import api.TimeEffect;

public class DragonSkillsPlugin extends JavaPlugin {
    private Skills skills;
    private PlayerStorage players;
    private List<TimeEffect> effects;

    @Override
    public void onEnable() {
        effects = new ArrayList<TimeEffect>();
        players = new PlayerStorage(this);
        skills = new Skills(this);
        Bukkit.getPluginManager().registerEvents(players, this);
        Bukkit.getPluginManager()
                .registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        // end all time effects
        for (TimeEffect effect : effects) {
            effect.endTimeEffect();
        }
    }

    public void addTimeEffect(TimeEffect newEffect) {
        effects.add(newEffect);
    }

    public void removeTimeEffect(TimeEffect oldEffect) {
        effects.remove(oldEffect);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }
        Player player = (Player) sender;
        String cmd = command.getName();
        if (cmd.equals("skill")) {
            if (args.length != 1) {
                sender.sendMessage("Skill name missing");
                return true;
            }
            skills.useSkill(args[0], players.getDPlayer(player));
        } else if (cmd.equals("bind")) {
            if (args.length != 1) {
                sender.sendMessage("Skill name missing");
                return true;
            }
            if (!skills.hasSkillUnchecked(args[0])) {
                sender.sendMessage("Skill not available");
                return true;
            }
            players.getDPlayer(player).addBind(
                    player.getItemInHand().getType(), args[0]);
            sender.sendMessage(args[0] + " bind to "
                    + player.getItemInHand().getType());
        } else if (cmd.equals("unbind")) {
            players.getDPlayer(player).removeBind(
                    player.getItemInHand().getType());
            sender.sendMessage("remove bind");
        } else if (cmd.equals("skill_list")) {
            for (Skill skill : skills.getSkills()) {
                DSystem.log(skill.getSkillName());
            }
        }
        return true;
    }

    public PlayerStorage getPlayerStorage() {
        return players;

    }

    public Skills getSkills() {
        return skills;
    }
}
