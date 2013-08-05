package api;

import org.bukkit.Bukkit;

public class DSystem {
    public static void log(String msg) {
        Bukkit.getServer().broadcastMessage(msg);
    }
}
