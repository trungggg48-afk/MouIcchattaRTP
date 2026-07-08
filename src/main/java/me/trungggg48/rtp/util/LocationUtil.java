package me.trungggg48.rtp.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public final class LocationUtil {

    private static final Random RANDOM = new Random();

    private LocationUtil() {
    }

    public static Location findSafeLocation(World world, ConfigurationSection section, Set<Material> blockedMaterials) {
        if (world == null || section == null) {
            return null;
        }

        int minRadius = Math.max(0, section.getInt("min-radius", 500));
        int maxRadius = Math.max(minRadius + 1, section.getInt("max-radius", 3000));
        boolean allowWater = section.getBoolean("allow-water", false);
        boolean allowNetherRoof = section.getBoolean("allow-nether-roof", false);

        for (int tries = 0; tries < 100; tries++) {
            int x = randomCoord(minRadius, maxRadius);
            int z = randomCoord(minRadius, maxRadius);

            int highestY = world.getHighestBlockYAt(x, z);
            if (highestY <= world.getMinHeight()) {
                continue;
            }

            int y = highestY + 1;

            if (world.getEnvironment() == World.Environment.NETHER) {
                y = findNetherSafeY(world, x, z, allowNetherRoof);
                if (y == Integer.MIN_VALUE) {
                    continue;
                }
            } else if (world.getEnvironment() == World.Environment.THE_END) {
                y = highestY + 1;
            }

            Location loc = new Location(world, x + 0.5, y, z + 0.5);

            if (isSafe(loc, blockedMaterials, allowWater, allowNetherRoof)) {
                return loc;
            }
        }

        return null;
    }

    private static int randomCoord(int minRadius, int maxRadius) {
        int distance = minRadius + RANDOM.nextInt(Math.max(1, maxRadius - minRadius + 1));
        return RANDOM.nextBoolean() ? distance : -distance;
    }

    private static int findNetherSafeY(World world, int x, int z, boolean allowNetherRoof) {
        int min = Math.max(world.getMinHeight() + 1, 32);
        int max = Math.min(world.getMaxHeight() - 3, 120);

        for (int y = max; y >= min; y--) {
            if (!allowNetherRoof && y >= 120) {
                continue;
            }

            Block feet = world.getBlockAt(x, y, z);
            Block head = world.getBlockAt(x, y + 1, z);
            Block ground = world.getBlockAt(x, y - 1, z);

            if (feet.getType().isAir() && head.getType().isAir() && ground.getType().isSolid()) {
                return y;
            }
        }
        return Integer.MIN_VALUE;
    }

    public static boolean isSafe(Location loc, Set<Material> blockedMaterials, boolean allowWater, boolean allowNetherRoof) {
        World world = loc.getWorld();
        if (world == null) {
            return false;
        }

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        if (world.getEnvironment() == World.Environment.NETHER && !allowNetherRoof && y >= 120) {
            return false;
        }

        Block feet = world.getBlockAt(x, y, z);
        Block head = world.getBlockAt(x, y + 1, z);
        Block ground = world.getBlockAt(x, y - 1, z);

        if (!feet.getType().isAir() || !head.getType().isAir()) {
            return false;
        }

        if (!ground.getType().isSolid()) {
            return false;
        }

        Material groundType = ground.getType();
        if (blockedMaterials.contains(groundType)) {
            return false;
        }

        if (!allowWater && (groundType == Material.WATER || groundType == Material.KELP || groundType == Material.SEAGRASS)) {
            return false;
        }

        return true;
    }

    public static Set<Material> loadBlockedMaterials(List<String> names) {
        Set<Material> set = new HashSet<>();
        for (String name : names) {
            try {
                set.add(Material.valueOf(name.toUpperCase()));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return set;
    }
}
