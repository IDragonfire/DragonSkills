package com.github.idragonfire.dragonskills;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.idragonfire.dragonskills.api.DSystem;
import com.github.idragonfire.dragonskills.api.Skill;
import com.github.idragonfire.dragonskills.api.TimeEffect;
import com.github.idragonfire.dragonskills.command.CmdAddChunk;
import com.github.idragonfire.dragonskills.command.CmdBind;
import com.github.idragonfire.dragonskills.command.CmdCd;
import com.github.idragonfire.dragonskills.command.CmdRemoveChunk;
import com.github.idragonfire.dragonskills.command.CmdSkill;
import com.github.idragonfire.dragonskills.command.CmdSkillDesc;
import com.github.idragonfire.dragonskills.command.CmdSkills;
import com.github.idragonfire.dragonskills.command.CmdUnbind;
import com.github.idragonfire.dragonskills.command.CommandHandler;
import com.github.idragonfire.dragonskills.utils.Metrics;
import com.github.idragonfire.dragonskills.utils.SkillLoader;
import com.google.common.base.Joiner;

public class DragonSkillsPlugin extends JavaPlugin {
	private Skills skills;
	private PlayerStorage players;
	private List<TimeEffect> effects;
	private CommandHandler cmds;
	private Regions region;

	// ScrollingMenuSign sms;

	@Override
	public void onEnable() {
		effects = new ArrayList<TimeEffect>();
		region = new Regions(this);
		players = new PlayerStorage(this);
		skills = new Skills(this);

		initCommands();

		Bukkit.getPluginManager().registerEvents(players, this);
		Bukkit.getPluginManager()
				.registerEvents(new PlayerListener(this), this);
		initMetrics();
		new SkillLoader(this);

		// test sms
		// Plugin p = Bukkit.getPluginManager().getPlugin("ScrollingMenuSign");
		// if (p != null && p instanceof ScrollingMenuSign) {
		// sms = (ScrollingMenuSign) p;
		// }
	}

	private void initCommands() {
		cmds = new CommandHandler();
		cmds.add(new CmdBind(this));
		cmds.add(new CmdCd(this));
		cmds.add(new CmdSkill(this));
		cmds.add(new CmdSkillDesc(this));
		cmds.add(new CmdSkills(this));
		cmds.add(new CmdUnbind(this));
		cmds.add(new CmdAddChunk(this));
		cmds.add(new CmdRemoveChunk(this));
	}

	@Override
	public void onDisable() {
		// end all time effects
		for (TimeEffect effect : effects) {
			effect.endTimeEffect();
		}
		region.save();
	}

	private void initMetrics() {
		Metrics metrics;
		try {
			metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			e.printStackTrace();
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
		// if (sms == null) {
		sender.sendMessage(Joiner.on(", ").skipNulls().join(skills.getSkills()));
		return;
		// }
		// SMSMenu menu = sms.getHandler().createMenu("Skills", "&1My Title",
		// sender.getName());
		// for (Skill s : skills.getSkills()) {
		// menu.addItem(s.getSkillName(), "/skill " + s.getSkillName(),
		// s.getSkillName());
		// }
		// menu.setAutosave(true);
		// menu.setAutosort(true);
		// SMSMapView view = null;
		// short id = Bukkit.getServer()
		// .createMap((World) Bukkit.getWorlds().get(0)).getId();
		// view = new SMSMapView("SKills view", menu);
		// view.register();
		// view.setMapId(id);
		// view.update(menu, SMSMenuAction.REPAINT);
		// view.setAutosave(true);
		// view.clearPlayerForView((Player) sender);
		// Player player = (Player) sender;
		// int slot = player.getInventory().first(Material.MAP);
		// if (slot == -1) {
		// sender.sendMessage("nooo");
		// return;
		// }
		// ItemStack map = player.getInventory().getItem(slot);
		// SMSMapView view = (SMSMapView) sms.getHandler().getViewManager()
		// .getView("SKills view");
		// map.setDurability(view.getMapView().getId());
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
		sender.sendMessage("http://dev.bukkit.org/bukkit-plugins/dragonskills/");
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

	public void addChunk(Chunk chunk, CommandSender sender) {
		region.addChunk(chunk);
		sender.sendMessage("chunk added");
	}

	public void removeChunk(Chunk chunk, CommandSender sender) {
		region.removeChunk(chunk);
		sender.sendMessage("chunk removed");
	}

	public boolean isInSkillFreeRegion(org.bukkit.Location location) {
		return region.isInSkillFreeRegion(location.getChunk());
	}
}
