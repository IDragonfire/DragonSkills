package com.github.idragonfire.dragonskills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.idragonfire.dragonskills.api.DSystem;
import com.github.idragonfire.dragonskills.api.Skill;
import com.github.idragonfire.dragonskills.api.TimeEffect;
import com.github.idragonfire.dragonskills.command.CmdBind;
import com.github.idragonfire.dragonskills.command.CmdCd;
import com.github.idragonfire.dragonskills.command.CmdSkill;
import com.github.idragonfire.dragonskills.command.CmdSkillDesc;
import com.github.idragonfire.dragonskills.command.CmdSkills;
import com.github.idragonfire.dragonskills.command.CmdUnbind;
import com.github.idragonfire.dragonskills.command.CommandHandler;

public class DragonSkillsPlugin extends JavaPlugin {
    private Skills skills;
    private PlayerStorage players;
    private List<TimeEffect> effects;
    private CommandHandler cmds;

    @Override
    public void onEnable() {
        effects = new ArrayList<TimeEffect>();
        players = new PlayerStorage(this);
        skills = new Skills(this);

        initCommands();

        Bukkit.getPluginManager().registerEvents(players, this);
        Bukkit.getPluginManager()
                .registerEvents(new PlayerListener(this), this);
    }

    private void initCommands() {
        cmds = new CommandHandler();
        cmds.add(new CmdBind(this));
        cmds.add(new CmdCd(this));
        cmds.add(new CmdSkill(this));
        cmds.add(new CmdSkillDesc(this));
        cmds.add(new CmdSkills(this));
        cmds.add(new CmdUnbind(this));
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
        return cmds.onCommand(sender, command, label, args);
    }

    public PlayerStorage getPlayerStorage() {
        return players;

    }

    public Skills getSkills() {
        return skills;
    }

    // TODO: check if player exists
    public void cmdSkills(CommandSender sender) {
        for (Skill skill : skills.getSkills()) {
            sender.sendMessage(skill.getSkillName());
        }
    }

    public void cmdBindSkill(String skillName, CommandSender sender,
            String playername) {
        if (invalidSkill(skillName, sender)) {
            return;
        }
        Player player = Bukkit.getPlayer(playername);
        players.getDPlayer(player).addBind(player.getItemInHand().getType(),
                skillName);
        sender.sendMessage(skillName + " bind to "
                + player.getItemInHand().getType());
    }

    public void cmdUnbindMaterial(Material material, CommandSender sender,
            String playername) {
        Player player = Bukkit.getPlayer(playername);
        players.getDPlayer(player).removeBind(material);
        sender.sendMessage("unbind " + player.getItemInHand().getType());
    }

    public void cmdCd(String skillName, CommandSender sender, String playername) {
        if (invalidSkill(skillName, sender)) {
            return;
        }
        Player player = Bukkit.getPlayer(playername);
        int seconds = players.getDPlayer(player).getCooldown(skillName);
        if (seconds <= 0) {
            sender.sendMessage("no cd");
            return;
        }
        sender.sendMessage(DSystem.paramString("cd: $1", seconds));
    }

    public void cmdHelp(CommandSender sender) {
        sender
                .sendMessage("http://dev.bukkit.org/bukkit-plugins/dragonskills/");
    }

    public void cmdSkill(String skillName, CommandSender sender,
            String playername) {
        if (invalidSkill(skillName, sender)) {
            return;
        }
        Player player = Bukkit.getPlayer(playername);
        skills.useSkill(skillName, players.getDPlayer(player));
    }

    public void cmdSkillDesc(String skillName, CommandSender sender) {
        if (invalidSkill(skillName, sender)) {
            return;
        }
        Skill skill = skills.getSkill(skillName);
        if (skill == null) {
            sender.sendMessage("no skill found");
            return;
        }
        sender.sendMessage(skill.getDescription());
        return;
    }

    public boolean invalidSkill(String skillName, CommandSender sender) {
        if (!skills.hasSkill(skillName)) {
            sender.sendMessage("invalid skills");
            return true;
        }
        return false;
    }
}
