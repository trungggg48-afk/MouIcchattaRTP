package me.trungggg48.rtp.service;

import me.trungggg48.rtp.model.RTPType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TeleportTaskService {

    private final Plugin plugin;
    private final Map<UUID, PendingTeleport> pendingTeleports = new ConcurrentHashMap<>();

    public TeleportTaskService(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean isTeleporting(Player player) {
        return pendingTeleports.containsKey(player.getUniqueId());
    }

    public void addPending(Player player, RTPType type, Location origin) {
        pendingTeleports.put(player.getUniqueId(), new PendingTeleport(type, origin));
    }

    public PendingTeleport getPending(Player player) {
        return pendingTeleports.get(player.getUniqueId());
    }

    public void remove(Player player) {
        pendingTeleports.remove(player.getUniqueId());
    }

    public void clearAll() {
        pendingTeleports.clear();
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public static class PendingTeleport {
        private final RTPType type;
        private final Location origin;

        public PendingTeleport(RTPType type, Location origin) {
            this.type = type;
            this.origin = origin.clone();
        }

        public RTPType getType() {
            return type;
        }

        public Location getOrigin() {
            return origin.clone();
        }
    }
}
