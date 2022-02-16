package me.deltaorion.extapi.display;

import com.google.common.base.MoreObjects;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import me.deltaorion.extapi.locale.message.Message;
import org.jetbrains.annotations.NotNull;

public class SimpleDisplayLine implements DisplayLine {

    @NotNull private final Message message;
    @NotNull private String asDisplayed;
    @NotNull private final BukkitApiPlayer player;

    public SimpleDisplayLine(@NotNull BukkitApiPlayer player, @NotNull Message message, Object... args) {
        this.message = message;
        this.player = player;
        this.asDisplayed = render(args);
    }

    @NotNull
    private String render(Object... args) {
        return message.toString(player.getLocale(),args);
    }

    @NotNull
    @Override
    public synchronized String getAsDisplayed() {
        return asDisplayed;
    }

    @NotNull
    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public synchronized void setArgs(Object... args) {
        this.asDisplayed = render(args);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("Message",message)
                .add("as-displayed",asDisplayed)
                .add("locale",player.getLocale()).toString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof SimpleDisplayLine))
            return false;

        SimpleDisplayLine line = (SimpleDisplayLine) o;
        return line.message.equals(this.message) && line.asDisplayed.equals(this.asDisplayed) && line.player.equals(this.player);
    }
}
