package me.trungggg48.rtp.service;

import me.trungggg48.rtp.MoulcchttaaRTP;
import me.trungggg48.rtp.model.RTPType;
import me.trungggg48.rtp.util.ColorUtil;
import me.trungggg48.rtp.util.FoliaCompat;
import me.trungggg48.rtp.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Set;

public class RTPService {

    private final MoulcchttaaRTP plugin;
    private final CooldownService cooldownService;
    private final TeleportTaskService teleportTaskService;

    public RTPService(MoulcchttaaRTP plugin, CooldownService cooldownService, TeleportTaskService teleportTaskService) {
        this.plugin = plugin;
        this.cooldownService = cooldownService;
        this.teleportTaskService = teleportTaskService;
    }

    public void startTeleport(Player player, RTPType type) {
        if (teleportTaskService.isTeleporting(player)) {
            send(player, "messages.already-teleporting");
            return;
        }

        ConfigurationSection section = getSection(type);
        if (section == null || !section.getBoolean("enabled", true)) {
            send(player, "messages.world-not-found", "%world%", type.name().toLowerCase());
            return;
        }

        String permission = section.getString("permission", "");
        if (!permission.isEmpty() && !player.hasPermission(permission)) {
            send(player, "messages.no-permission");
            return;
        }

        if (cooldownService.isOnCooldown(player, type)) {
            long remain = cooldownService.getRemaining(player, type);
            send(player, "messages.cooldown", "%seconds%", String.valueOf(remain));
            return;
        }

        int delay = section.getInt("delay-seconds", 5);

        teleportTaskService.addPending(player, type, player.getLocation());
        send(player, "messages.teleport-start", "%seconds%", String.valueOf(delay));

        FoliaCompat.runLater(plugin, player, () -> executeTeleport(player, type), delay * 20L);
    }

    public void executeTeleport(Player player, RTPType type) {
        if (!player.isOnline()) {
            teleportTaskService.remove(player);
            return;
        }

        if (!teleportTaskService.isTeleporting(player)) {
            return;
        }

        ConfigurationSection section = getSection(type);
        if (section == null) {
            teleportTaskService.remove(player);
            return;
        }

        World world = plugin.getServer().getWorld(section.getString("world", "world"));
        if (world == null) {
            teleportTaskService.remove(player);
            send(player, "messages.world-not-found", "%world%", section.getString("world", "unknown"));
            return;
        }

        send(player, "messages.teleporting");

        Set<Material> blocked = LocationUtil.loadBlockedMaterials(
                plugin.getConfig().getStringList("settings.blocked-materials")
        );

        Location target = LocationUtil.findSafeLocation(world, section, blocked);
        if (target == null) {
            teleportTaskService.remove(player);
            send(player, "messages.unsafe-location");
            return;
        }

        FoliaCompat.teleport(player, target);

        int cooldown = section.getInt("cooldown-seconds", 60);
        cooldownService.setCooldown(player, type, cooldown);

        teleportTaskService.remove(player);

        String worldName = world.getName();
        send(player, "messages.teleported",
                "%world%", worldName,
                "%x%", String.valueOf(target.getBlockX()),
                "%y%", String.valueOf(target.getBlockY()),
                "%z%", String.valueOf(target.getBlockZ()));
    }

    public void cancelTeleport(Player player) {
        if (!teleportTaskService.isTeleporting(player)) {
            return;
        }
        teleportTaskService.remove(player);
        send(player, "messages.teleport-cancelled");
    }

    private ConfigurationSection getSection(RTPType type) {
        return plugin.getConfig().getConfigurationSection("rtp." + type.getPath());
    }

    private void send(Player player, String path, String... replacements) {
        String msg = plugin.getConfig().getString(path, "&cMissing message: " + path);
        for (int i = 0; i + 1 < replacements.length; i += 2) {
            msg = msg.replace(replacements[i], replacements[i + 1]);
        }
        player.sendMessage(ColorUtil.color(msg));
    }
}
