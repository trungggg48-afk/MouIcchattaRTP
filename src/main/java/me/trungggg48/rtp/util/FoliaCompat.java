package me.trungggg48.rtp.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class FoliaCompat {

    private static final boolean FOLIA = classExists("io.papermc.paper.threadedregions.scheduler.EntityScheduler");

    private FoliaCompat() {
    }

    public static boolean isFolia() {
        return FOLIA;
    }

    private static boolean classExists(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    public static void runLater(Plugin plugin, Player player, Runnable task, long delayTicks) {
        if (isFolia()) {
            player.getScheduler().runDelayed(plugin, scheduledTask -> task.run(), null, delayTicks);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
        }
    }

    public static void teleport(Player player, Location location) {
        if (isFolia()) {
            player.teleportAsync(location);
        } else {
            player.teleport(location);
        }
    }
}
