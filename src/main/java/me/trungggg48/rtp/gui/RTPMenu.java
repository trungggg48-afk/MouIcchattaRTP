package me.trungggg48.rtp.gui;

import me.trungggg48.rtp.MoulcchttaaRTP;
import me.trungggg48.rtp.model.RTPType;
import me.trungggg48.rtp.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class RTPMenu {

    private final MoulcchttaaRTP plugin;

    public RTPMenu(MoulcchttaaRTP plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        String title = ColorUtil.color(plugin.getConfig().getString("messages.menu-title", "&8Chon the gioi RTP"));
        Inventory inv = Bukkit.createInventory(null, 27, title);

        setItem(inv, RTPType.OVERWORLD, "menu.overworld");
        setItem(inv, RTPType.NETHER, "menu.nether");
        setItem(inv, RTPType.END, "menu.end");

        player.openInventory(inv);
    }

    private void setItem(Inventory inv, RTPType type, String path) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection(path);
        if (section == null) return;

        Material material;
        try {
            material = Material.valueOf(section.getString("material", "STONE").toUpperCase());
        } catch (IllegalArgumentException ex) {
            material = Material.STONE;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ColorUtil.color(section.getString("name", "&fRTP")));
            List<String> lore = section.getStringList("lore");
            if (!lore.isEmpty()) {
                meta.setLore(lore.stream().map(ColorUtil::color).toList());
            }
            item.setItemMeta(meta);
        }

        int slot = section.getInt("slot", 11);
        inv.setItem(slot, item);
    }
}
