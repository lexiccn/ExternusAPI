package me.deltaorion.extapi.common.plugin;

import me.deltaorion.extapi.common.entity.sender.BukkitSenderInfo;
import me.deltaorion.extapi.common.entity.sender.Sender;
import me.deltaorion.extapi.common.entity.sender.SimpleSender;
import me.deltaorion.extapi.common.logger.PluginLogger;
import me.deltaorion.extapi.common.scheduler.BukkitSchedulerAdapter;
import me.deltaorion.extapi.common.scheduler.SchedulerAdapter;
import me.deltaorion.extapi.common.server.BukkitServer;
import me.deltaorion.extapi.common.server.EServer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.nio.file.Path;

public class BukkitPlugin extends JavaPlugin implements EPlugin {

    private final EServer eServer;

    public BukkitPlugin() {
        this.eServer = new BukkitServer(getServer());
    }

    @Override
    public EServer getEServer() {
        return eServer;
    }

    @Override
    public SchedulerAdapter getScheduler() {
        return new BukkitSchedulerAdapter(this);
    }

    @Override
    public Sender wrapSender(Object commandSender) {
        if(!(commandSender instanceof CommandSender))
            throw new IllegalArgumentException("Must wrap a bukkit command sender");

        return new SimpleSender(new BukkitSenderInfo(this, (CommandSender) commandSender));
    }

    @Override
    public Path getDataDirectory() {
        return getDataFolder().toPath().toAbsolutePath();
    }

    @Override
    public InputStream getResourceStream(String path) {
        return getClass().getClassLoader().getResourceAsStream(path);
    }

    @Override
    public PluginLogger getPluginLogger() {
        return this.getPluginLogger();
    }
}
