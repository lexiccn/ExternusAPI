package me.deltaorion.extapi.common.plugin;

import me.deltaorion.extapi.common.entity.sender.BungeeSenderInfo;
import me.deltaorion.extapi.common.entity.sender.Sender;
import me.deltaorion.extapi.common.entity.sender.SimpleSender;
import me.deltaorion.extapi.common.logger.JavaPluginLogger;
import me.deltaorion.extapi.common.logger.PluginLogger;
import me.deltaorion.extapi.common.scheduler.BungeeSchedulerAdapter;
import me.deltaorion.extapi.common.scheduler.SchedulerAdapter;
import me.deltaorion.extapi.common.server.BungeeServer;
import me.deltaorion.extapi.common.server.EServer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;
import java.nio.file.Path;

public class BungeePlugin extends Plugin implements EPlugin {

    private final EServer eServer;
    private final PluginLogger logger;
    private final BungeeSchedulerAdapter schedulerAdapter;


    public BungeePlugin() {
        this.eServer = new BungeeServer(this.getProxy());
        this.schedulerAdapter = new BungeeSchedulerAdapter(this);
        this.logger = new JavaPluginLogger(getLogger());
    }

    @Override
    public EServer getEServer() {
        return eServer;
    }

    @Override
    public SchedulerAdapter getScheduler() {
        return schedulerAdapter;
    }

    @Override
    public Sender wrapSender(Object commandSender) {
        if(!(commandSender instanceof CommandSender))
            throw new IllegalArgumentException("Command Sender must be a net.md5 command sender");

        return new SimpleSender(new BungeeSenderInfo((CommandSender) commandSender,this));
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
    public void saveResource(String resourcePath, boolean replace) {
        if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResourceAsStream(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + resourcePath);
        }

        File outFile = new File(getDataDirectory().toFile(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(getDataDirectory().toFile(), resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                logger.warn( "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            logger.severe( "Could not save " + outFile.getName() + " to " + outFile, ex);
        }
    }

    @Override
    public PluginLogger getPluginLogger() {
        return this.logger;
    }
}
