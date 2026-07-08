package me.trungggg48.rtp.service;

import me.trungggg48.rtp.model.RTPType;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownService {

    private final Map<UUID, Map<RTPType, Long>> cooldowns = new ConcurrentHashMap<>();

    public boolean isOnCooldown(Player player, RTPType type) {
        return getRemaining(player, type) > 0;
    }

    public long getRemaining(Player player, RTPType type) {
        Map<RTPType, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) {
            return 0;
        }

        Long expireAt = playerCooldowns.get(type);
        if (expireAt == null) {
            return 0;
        }

        long remainingMillis = expireAt - System.currentTimeMillis();
        return Math.max(0, (long) Math.ceil(remainingMillis / 1000.0));
    }

    public void setCooldown(Player player, RTPType type, int seconds) {
        cooldowns.computeIfAbsent(player.getUniqueId(), uuid -> new EnumMap<>(RTPType.class))
                .put(type, System.currentTimeMillis() + (seconds * 1000L));
    }
}
