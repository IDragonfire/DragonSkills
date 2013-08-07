package com.github.idragonfire.dragonskills.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;

public class CmdBind extends PlayerConsoleCommand {

    public CmdBind(DragonSkillsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onPlayerCommand(Player sender, Command command, String cmd,
            String label, String[] args) {
        if (args.length != 1) {
            // TODO: sms menu
            sender.sendMessage("Please enter a skill name");
            return;
        }
        plugin.cmdBindSkill(args[0], sender, sender.getName());
    }

    @Override
    public void onConsoleCommand(CommandSender sender, Command command,
            String cmd, String label, String[] args) {
        if (args.length != 2) {
            // TODO: sms menu
            sender
                    .sendMessage("console-usage: /skill [skill name] [player name]");
            return;
        }
        plugin.cmdBindSkill(args[0], sender, args[1]);
    }
}
