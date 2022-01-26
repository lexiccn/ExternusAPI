package me.deltaorion.extapi.test;

import com.google.common.collect.ImmutableList;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.sender.TestSender;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.common.version.MinecraftVersion;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TestServer implements EServer {

    private final MinecraftVersion version = new MinecraftVersion(8,8);
    private final String SERVER_BRAND = "Test";
    private final int MAX_PLAYERS = 20;
    private final List<Sender> onlineSenders;
    private final Random random;
    private final List<Locale> locales = ImmutableList.of(Locale.ENGLISH,Locale.FRANCE,new Locale("en","PT"));
    private final Sender CONSOLE = new TestSender(CONSOLE_NAME,CONSOLE_UUID,true,true,Locale.ENGLISH);
    private final HashMap<String,EPlugin> plugins;

    public TestServer() {
        this.random = new Random();
        this.onlineSenders = new ArrayList<>();
        this.plugins = new HashMap<>();
        initSenders();
    }

    private void initSenders() {
        for(int i=0;i<MAX_PLAYERS;i++) {
            String[] perms = new String[i];
            for(int j=0;j<i;j++) {
                perms[j] = String.valueOf(j);
            }
            onlineSenders.add(new TestSender("Gamer"+i,UUID.randomUUID(),false,false,locales.get(random.nextInt(locales.size())),perms));
        }
    }

    @Override
    public MinecraftVersion getServerVersion() {
        return version;
    }

    @Override
    public String getServerBrand() {
        return SERVER_BRAND;
    }

    @Override
    public List<UUID> getOnlinePlayers() {
        List<UUID> onlinePlayers = new ArrayList<>();
        for(Sender sender : onlineSenders) {
            onlinePlayers.add(sender.getUniqueId());
        }
        return Collections.unmodifiableList(onlinePlayers);
    }

    @Override
    public List<Sender> getOnlineSenders() {
        return Collections.unmodifiableList(onlineSenders);
    }

    @Override
    public Sender getConsoleSender() {
        return CONSOLE;
    }

    @Override
    public int getMaxPlayer() {
        return MAX_PLAYERS;
    }

    @Override
    public boolean isPlayerOnline(@NotNull UUID uuid) {
        return getOnlinePlayers().contains(uuid);
    }

    @Nullable
    @Override
    public EPlugin getPlugin(@NotNull String name) {
        return this.plugins.get(name.toLowerCase(Locale.ROOT));
    }

    public void addPlugin(@NotNull String name, @NotNull EPlugin plugin) {
        this.plugins.put(name.toLowerCase(Locale.ROOT),plugin);
    }

    @Nullable
    @Override
    public Object getPluginObject(@NotNull String name) {
        return plugins.get(name);
    }

    @Override
    public boolean isPluginEnabled(@NotNull String name) {
        EPlugin plugin = plugins.get(name);
        if(plugin==null)
            return false;

        return plugin.isPluginEnabled();
    }

    @Override
    public String translateColorCodes(@NotNull String textToTranslate) {
        return ChatColor.translateAlternateColorCodes('&',textToTranslate);
    }
}
