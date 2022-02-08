package me.deltaorion.extapi.display.actionbar;

import com.google.common.base.Objects;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.deltaorion.extapi.locale.message.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

//data structure that stores info about an action bar
@Immutable
public class ActionBar {

    @Nullable private final String name;
    @NotNull private final Message message;
    @NotNull private final Duration duration;

    public ActionBar(@NotNull Message message, @NotNull Duration duration, @Nullable String name) {
        this.message = message;
        this.duration = duration;
        this.name = name;
    }

    public ActionBar(@NotNull Message message, @NotNull Duration duration) {
        this(message,duration,null);
    }

    public ActionBar(@NotNull String message, @NotNull Duration duration) {
        this(Message.valueOf(message),duration);
    }

    @NotNull
    public Message getMessage() {
        return message;
    }

    public long getTime() {
        return duration.toMillis();
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("Message",message)
                .add("Duration (ms)",getTime()).toString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ActionBar))
            return false;

        ActionBar bar = (ActionBar) o;
        return bar.message.equals(this.message) && bar.getTime() == this.getTime();
    }
}
