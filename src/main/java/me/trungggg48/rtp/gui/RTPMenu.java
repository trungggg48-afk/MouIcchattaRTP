package me.trungggg48.rtp.gui;

import me.trungggg48.rtp.MoulcchattaRTP;
import me.trungggg48.rtp.manager.RTPManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RTPMenu {

    private final MoulcchattaRTP plugin;
    private final RTPManager rtpManager;

    public RTPMenu(MoulcchattaRTP plugin, RTPManager rtpManager) {
        this.plugin = plugin;
        this.rtpManager = rtpManager;
    }

    public void open(Player player) {
        String title = color(plugin.getConfig().getString("menu.title", "&8Chọn thế giới RTP"));
        int size = plugin.getConfig().getInt("menu.size", 27);

        Inventory inv = Bukkit.createInventory(null, size, title);

        // fill glass / filler
        if (plugin.getConfig().getBoolean("menu.filler.enabled", true)) {
            ItemStack filler = createItem(
                    Material.valueOf(plugin.getConfig().getString("menu.filler.material", "BLACK_STAINED_GLASS_PANE")),
                    color(plugin.getConfig().getString("menu.filler.name", " ")),
                    plugin.getConfig().getStringList("menu.filler.lore")
            );

            for (int i = 0; i < size; i++) {
                inv.setItem(i, filler);
            }
        }

        ConfigurationSection worlds = plugin.getConfig().getConfigurationSection("menu.worlds");
        if (worlds != null) {
            for (String key : worlds.getKeys(false)) {
                ConfigurationSection sec = worlds.getConfigurationSection(key);
                if (sec == null) continue;

                String worldName = sec.getString("world");
                int slot = sec.getInt("slot");
                Material material = Material.valueOf(sec.getString("material", "GRASS_BLOCK"));
                String name = color(sec.getString("name", "&a" + key));
                List<String> lore = color(sec.getStringList("lore"));

                ItemStack item = createItem(material, name, lore);
                inv.setItem(slot, item);
            }
        }

        player.openInventory(inv);
    }

    public void handleClick(Player player, int slot) {
        ConfigurationSection worlds = plugin.getConfig().getConfigurationSection("menu.worlds");
        if (worlds == null) return;

        for (String key : worlds.getKeys(false)) {
            ConfigurationSection sec = worlds.getConfigurationSection(key);
            if (sec == null) continue;

            int configSlot = sec.getInt("slot");
            if (configSlot != slot) continue;

            String worldName = sec.getString("world");
            if (worldName == null || worldName.isEmpty()) {
                player.sendMessage(color("&cWorld chưa được cấu hình."));
                return;
            }

            player.closeInventory();
            rtpManager.startRTP(player, worldName);
            return;
        }
    }

    private ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }
        return item;
    }

    private String color(String s) {
        return s == null ? "" : s.replace("&", "§");
    }

    private List<String> color(List<String> list) {
        List<String> out = new ArrayList<>();
        for (String s : list) out.add(color(s));
        return out;
    }
}
