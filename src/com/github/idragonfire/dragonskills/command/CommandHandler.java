package com.github.idragonfire.dragonskills.command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandHandler {
    private Map<String, DCommand> cmds;

    public CommandHandler() {
        cmds = new HashMap<String, DCommand>();
    }

    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
        String name = command.getName();
        DCommand cmd = cmds.get(name.toLowerCase());
        if (cmd == null) {
            return false;
        }
        cmd.onCommand(sender, command, name, label, args);
        return true;
    }

    public void add(DCommand newCmd) {
        cmds.put(newCmd.getClass().getSimpleName().substring(3).toLowerCase(),
                newCmd);
    }
}
