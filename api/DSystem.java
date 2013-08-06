package api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DSystem {
    public static ChatColor COLOR_VARIABLE = ChatColor.WHITE;
    public static ChatColor COLOR_TEXT = ChatColor.GRAY;

    public static void log(String msg, Object... args) {
        Bukkit.getServer().broadcastMessage(paramMsg(msg, args));
    }

    public static String paramMsg(String msg, Object... args) {
        msg = new StringBuilder().append(COLOR_TEXT).append(msg).toString();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                msg = msg.replace(new StringBuilder().append("$").append(i + 1)
                        .toString(), new StringBuilder().append(COLOR_VARIABLE)
                        .append(args[i].toString()).append(COLOR_TEXT)
                        .toString());
            }
        }
        return msg;
    }
}
