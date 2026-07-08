package me.trungggg48.rtp;

import me.trungggg48.rtp.command.RTPCommand;
import me.trungggg48.rtp.listener.MenuListener;
import me.trungggg48.rtp.listener.MoveListener;
import me.trungggg48.rtp.service.CooldownService;
import me.trungggg48.rtp.service.RTPService;
import me.trungggg48.rtp.service.TeleportTaskService;
import org.bukkit.plugin.java.JavaPlugin;

public class MoulcchttaaRTP extends JavaPlugin {

    private static MoulcchttaaRTP instance;

    private CooldownService cooldownService;
    private TeleportTaskService teleportTaskService;
    private RTPService rtpService;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        this.cooldownService = new CooldownService();
        this.teleportTaskService = new TeleportTaskService(this);
        this.rtpService = new RTPService(this, cooldownService, teleportTaskService);

        RTPCommand rtpCommand = new RTPCommand(this, rtpService);

        if (getCommand("rtp") != null) {
            getCommand("rtp").setExecutor(rtpCommand);
            getCommand("rtp").setTabCompleter(rtpCommand);
        }

        getServer().getPluginManager().registerEvents(new MenuListener(this, rtpService), this);
        getServer().getPluginManager().registerEvents(new MoveListener(this, teleportTaskService), this);

        getLogger().info("MoulcchttaaRTP enabled.");
    }

    @Override
    public void onDisable() {
        if (teleportTaskService != null) {
            teleportTaskService.clearAll();
        }
    }

    public static MoulcchttaaRTP getInstance() {
        return instance;
    }

    public CooldownService getCooldownService() {
        return cooldownService;
    }

    public TeleportTaskService getTeleportTaskService() {
        return teleportTaskService;
    }

    public RTPService getRtpService() {
        return rtpService;
    }
}
