package me.deltaorion.common.test.mock;

import com.google.common.collect.ImmutableList;
import me.deltaorion.common.locale.IChatColor;
import me.deltaorion.common.plugin.EPlugin;
import me.deltaorion.common.plugin.EServer;
import me.deltaorion.common.plugin.sender.Sender;
import me.deltaorion.common.plugin.sender.SenderFactory;
import me.deltaorion.common.plugin.sender.TestSender;
import me.deltaorion.common.plugin.version.MinecraftVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;

import static me.deltaorion.common.locale.IChatColor.COLOR_CHAR;

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
        initChatColor();
    }

    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + COLOR_CHAR + "[0-9A-FK-OR]");

    private void initChatColor() {

        if(IChatColor.isInitialised())
            return;
        for(IChatColor chatColor : IChatColor.values()) {
            chatColor.initialise(chatColor.getChar(), chatColor.isFormat(),chatColor.toString());
        }

        IChatColor.initialise((altColorChar, textToTranslate) -> {
            char[] b = textToTranslate.toCharArray();
            for (int i = 0; i < b.length - 1; i++) {
                if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                    b[i] = COLOR_CHAR;
                    b[i + 1] = Character.toLowerCase(b[i + 1]);
                }
            }
            return new String(b);
        }, s -> {
            if (s == null) {
                return null;
            }

            return STRIP_COLOR_PATTERN.matcher(s).replaceAll("");
        });
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
        return IChatColor.translateAlternateColorCodes('&',textToTranslate);
    }

    @Override
    public String getServerName() {
        return "Test Server";
    }

    @NotNull
    @Override
    public Sender wrapSender(@NotNull Object commandSender) {
        return new TestSender(String.valueOf(random.nextInt()), UUID.randomUUID(),random.nextBoolean(),random.nextBoolean(), Locale.ENGLISH);
    }

    @Override
    public void setSenderFactory(@NotNull SenderFactory factory) {

    }
}
