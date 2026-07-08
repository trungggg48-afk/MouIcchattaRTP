package me.trungggg48.rtp.command;

import me.trungggg48.rtp.MoulcchttaaRTP;
import me.trungggg48.rtp.gui.RTPMenu;
import me.trungggg48.rtp.service.RTPService;
import me.trungggg48.rtp.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RTPCommand implements CommandExecutor, TabCompleter {

    private final MoulcchttaaRTP plugin;
    private final RTPService rtpService;
    private final RTPMenu menu;

    public RTPCommand(MoulcchttaaRTP plugin, RTPService rtpService) {
        this.plugin = plugin;
        this.rtpService = rtpService;
        this.menu = new RTPMenu(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("moulcchttaa.rtp.reload")) {
                sender.sendMessage(ColorUtil.color(plugin.getConfig().getString("messages.no-permission", "&cBan khong co quyen.")));
                return true;
            }

            plugin.reloadConfig();
            sender.sendMessage(ColorUtil.color(plugin.getConfig().getString("messages.reloaded", "&aDa reload config RTP.")));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorUtil.color(plugin.getConfig().getString("messages.player-only", "&cLenh nay chi dung cho nguoi choi.")));
            return true;
        }

        if (!player.hasPermission("moulcchttaa.rtp.use")) {
            player.sendMessage(ColorUtil.color(plugin.getConfig().getString("messages.no-permission", "&cBan khong co quyen.")));
            return true;
        }

        menu.open(player);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1 && sender.hasPermission("moulcchttaa.rtp.reload")) {
            if ("reload".startsWith(args[0].toLowerCase())) {
                list.add("reload");
            }
        }
        return list;
    }
}
