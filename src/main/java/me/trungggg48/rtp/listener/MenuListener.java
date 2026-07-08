package me.trungggg48.rtp.listener;

import me.trungggg48.rtp.MoulcchttaaRTP;
import me.trungggg48.rtp.model.RTPType;
import me.trungggg48.rtp.service.RTPService;
import me.trungggg48.rtp.util.ColorUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuListener implements Listener {

    private final MoulcchttaaRTP plugin;
    private final RTPService rtpService;

    public MenuListener(MoulcchttaaRTP plugin, RTPService rtpService) {
        this.plugin = plugin;
        this.rtpService = rtpService;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        String title = ColorUtil.color(plugin.getConfig().getString("messages.menu-title", "&8Chon the gioi RTP"));
        if (!event.getView().getTitle().equals(title)) return;

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;

        int slot = event.getRawSlot();

        if (slot == getSlot("menu.overworld")) {
            player.closeInventory();
            rtpService.startTeleport(player, RTPType.OVERWORLD);
            return;
        }

        if (slot == getSlot("menu.nether")) {
            player.closeInventory();
            rtpService.startTeleport(player, RTPType.NETHER);
            return;
        }

        if (slot == getSlot("menu.end")) {
            player.closeInventory();
            rtpService.startTeleport(player, RTPType.END);
        }
    }

    private int getSlot(String path) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection(path);
        return section == null ? -1 : section.getInt("slot", -1);
    }
}
