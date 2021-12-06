package me.deltaorion.extapi.common.server;

import me.deltaorion.extapi.common.entity.sender.Sender;
import me.deltaorion.extapi.common.scheduler.SchedulerAdapter;
import me.deltaorion.extapi.common.version.MinecraftVersion;
import org.bukkit.Server;

import java.util.List;
import java.util.UUID;

public interface EServer {

    UUID CONSOLE_UUID = new UUID(0,0);

    String CONSOLE_NAME = "Console";

    public MinecraftVersion getVersion();

    public String getBrand();

    public List<UUID> getOnlinePlayers();

    public int getMaxPlayer();

    public boolean isPlayerOnline(UUID uuid);
}
