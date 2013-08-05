package com.github.idragonfire.dragonskills;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import api.DSystem;
import api.Skill;

public class DragonSkillsPlugin extends JavaPlugin {
    private Skills skills;
    private PlayerStorage players;

    @Override
    public void onEnable() {
        players = new PlayerStorage();
        skills = new Skills(this);
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.LOWEST)
            public void onPlayerInteract(PlayerInteractEvent event) {
                players.getDPlayer(event.getPlayer()).onPlayerInteractEvent(
                        skills, event.getPlayer().getItemInHand().getType(),
                        event);

            }
        }, this);
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
            skills.useSkill(args[0], player);
        } else if (cmd.equals("bind")) {

        } else if (cmd.equals("skill_list")) {
            for (Skill skill : skills.getSkills()) {
                DSystem.log(skill.getSkillName());
            }
        }
        return true;
    }
}
