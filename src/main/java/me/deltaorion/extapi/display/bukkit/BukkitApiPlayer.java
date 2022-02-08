package me.deltaorion.extapi.display.bukkit;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.display.actionbar.ActionBarManager;
import me.deltaorion.extapi.display.bossbar.BossBar;
import me.deltaorion.extapi.display.scoreboard.EScoreboard;
import me.deltaorion.extapi.locale.message.Message;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class BukkitApiPlayer implements Sender {

    @NotNull private final BukkitPlugin plugin;
    @NotNull private final Sender sender;
    @NotNull private final Player player;
    @Nullable private EScoreboard scoreboard;
    @NotNull private final ActionBarManager actionBarManager;
    @Nullable private BossBar bossBar;

    public BukkitApiPlayer(@NotNull BukkitPlugin plugin, @NotNull Player player, APIPlayerSettings settings) {
        this.player = player;
        this.plugin = plugin;
        this.sender = plugin.wrapSender(player);
        this.scoreboard = null;
        this.actionBarManager = new ActionBarManager(plugin,this, settings.getFactory());
    }

    @Override
    public String getName() {
        return sender.getName();
    }

    @Override
    public UUID getUniqueId() {
        return sender.getUniqueId();
    }

    @Override
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }

    @Override
    public void sendMessage(int message) {
        sender.sendMessage(message);
    }

    @Override
    public void sendMessage(boolean message) {
        sender.sendMessage(message);
    }

    @Override
    public void sendMessage(Object message) {
        sender.sendMessage(message);
    }

    @Override
    public void sendMessage(double message) {
        sender.sendMessage(message);
    }

    @Override
    public void sendMessage(float message) {
        sender.sendMessage(message);
    }

    @Override
    public void sendMessage(Message message, Object... args) {
        sender.sendMessage(message,args);
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public void performCommand(@NotNull String commandLine) {
        sender.performCommand(commandLine);
    }

    @Override
    public boolean isConsole() {
        return sender.isConsole();
    }

    @Override
    public boolean isValid() {
        return sender.isValid();
    }

    @Override
    public boolean isOP() {
        return sender.isOP();
    }

    @Override
    public Locale getLocale() {
        return sender.getLocale();
    }

    public void setScoreboard(@Nullable EScoreboard scoreboard) {
        if(scoreboard==null) {
            player.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
        }
        this.scoreboard = scoreboard;
    }

    @Nullable
    public EScoreboard getScoreboard() {
        return this.scoreboard;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof BukkitApiPlayer))
            return false;

        return ((BukkitApiPlayer) o).getPlayer().getUniqueId().equals(this.player.getUniqueId());
    }

    @NotNull
    public ActionBarManager getActionBarManager() {
        return actionBarManager;
    }

    @Nullable
    public BossBar getBossBar() {
        return bossBar;
    }

    public void setBossBar(@Nullable BossBar bossBar) {
        if(Objects.equals(bossBar,this.bossBar))
            return;

        if(this.bossBar!=null) {
            this.bossBar.setVisible(false);
        }
        this.bossBar = bossBar;
    }
}
