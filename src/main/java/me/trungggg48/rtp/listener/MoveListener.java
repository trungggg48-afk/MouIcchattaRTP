package me.trungggg48.rtp.listener;

import me.trungggg48.rtp.MoulcchttaaRTP;
import me.trungggg48.rtp.service.TeleportTaskService;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    private final MoulcchttaaRTP plugin;
    private final TeleportTaskService teleportTaskService;

    public MoveListener(MoulcchttaaRTP plugin, TeleportTaskService teleportTaskService) {
        this.plugin = plugin;
        this.teleportTaskService = teleportTaskService;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!plugin.getConfig().getBoolean("settings.cancel-on-move", true)) {
            return;
        }

        Player player = event.getPlayer();
        if (!teleportTaskService.isTeleporting(player)) {
            return;
        }

        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null) return;

        if (from.getBlockX() == to.getBlockX()
                && from.getBlockY() == to.getBlockY()
                && from.getBlockZ() == to.getBlockZ()) {
            return;
        }

        teleportTaskService.remove(player);
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("messages.teleport-cancelled", "&cRTP da bi huy vi ban di chuyen.")));
    }
}
