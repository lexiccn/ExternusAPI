package me.deltaorion.extapi.display.bossbar;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.common.base.Preconditions;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.protocol.WrapperPlayServerBoss;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class PacketBossBarRenderer implements BossBarRenderer {

    @NotNull private final BukkitPlugin plugin;
    @NotNull private final Player player;

    private final UUID uniqueId;

    private boolean visible;

    @NotNull private String toRender;
    private float progress;

    @NotNull private BarColor color;
    @NotNull private WrapperPlayServerBoss.BarStyle style;

    private boolean createFog;
    private boolean darkenSky;
    private boolean playMusic;

    public PacketBossBarRenderer(@NotNull BukkitPlugin plugin, @NotNull Player player) {
        this.plugin = plugin;
        this.player = player;

        this.uniqueId = UUID.randomUUID();

        style = WrapperPlayServerBoss.BarStyle.PROGRESS;
        color = BarColor.PINK;
        progress = 1f;
        toRender = "";
        visible = false;

        createFog = false;
        darkenSky = false;
        playMusic = false;
    }

    @Override
    public void setMessage(@NotNull String render) {
        Objects.requireNonNull(render);
        this.toRender = render;
        updatePacket(packet -> {
            packet.setAction(WrapperPlayServerBoss.Action.UPDATE_NAME);
            packet.setTitle(WrappedChatComponent.fromText(render));
        });
    }

    @Override
    public void setProgress(float progress) {
        Preconditions.checkState(progress >= 0 && progress<=1);
        this.progress = progress;
        updatePacket(packet -> {
            packet.setAction(WrapperPlayServerBoss.Action.UPDATE_PCT);
            packet.setHealth(progress);
        });
    }

    @Override
    public void setVisible(boolean visible) {
        if(this.visible == visible)
            return;

        this.visible = visible;

        if(visible) {
            sendCreatePacket();
        } else {
            sendRemovePacket();
        }
    }

    private void sendCreatePacket() {
        WrapperPlayServerBoss boss = new WrapperPlayServerBoss();
        boss.setTitle(WrappedChatComponent.fromText(toRender));
        boss.setUniqueId(uniqueId);
        boss.setAction(WrapperPlayServerBoss.Action.ADD);
        boss.setHealth(progress);
        boss.setColor(color);
        boss.setStyle(style);
        boss.setCreateFog(createFog);
        boss.setDarkenSky(darkenSky);
        boss.setPlayMusic(playMusic);
        boss.sendPacket(player);
    }

    private void sendRemovePacket() {
        WrapperPlayServerBoss boss = new WrapperPlayServerBoss();
        boss.setAction(WrapperPlayServerBoss.Action.REMOVE);
        boss.setUniqueId(uniqueId);
        boss.sendPacket(player);
    }

    @Override
    public void update() {
    }

    @Override
    public void setColor(@NotNull BarColor color) {
        if(this.color.equals(color))
            return;

        this.color = color;
        updateStyle();
    }

    private void updateStyle() {
        updatePacket(packet -> {
            packet.setAction(WrapperPlayServerBoss.Action.UPDATE_STYLE);
            packet.setStyle(style);
            packet.setColor(color);
        });
    }

    @Override
    public void setStyle(@NotNull WrapperPlayServerBoss.BarStyle style) {
        if(this.style.equals(style))
            return;

        this.style = style;
        updateStyle();
    }

    @Override
    public void setCreateFog(boolean createFog) {
        if(this.createFog == createFog)
            return;
        this.createFog = createFog;
        updateProperty();
    }

    @Override
    public void setDarkenSky(boolean darkenSky) {
        if(this.darkenSky == darkenSky)
            return;
        this.darkenSky = darkenSky;
        updateProperty();
    }

    @Override
    public void setPlayMusic(boolean playMusic) {
        if(this.playMusic == playMusic)
            return;
        this.playMusic = playMusic;
        updateProperty();
    }

    private void updateProperty() {
        updatePacket(packet -> {
            packet.setAction(WrapperPlayServerBoss.Action.UPDATE_PROPERTIES);
            packet.setPlayMusic(playMusic);
            packet.setDarkenSky(darkenSky);
            packet.setCreateFog(createFog);
        });
    }

    private void updatePacket(@NotNull Consumer<WrapperPlayServerBoss> bossConsumer) {
        WrapperPlayServerBoss update = new WrapperPlayServerBoss();
        update.setUniqueId(uniqueId);
        bossConsumer.accept(update);
        update.sendPacket(player);
    }
}
