package me.deltaorion.extapi.display.scoreboard;

import com.google.common.base.Objects;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.deltaorion.extapi.locale.message.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Immutable
public class ScoreboardLine {
    @NotNull private final Message message;
    @Nullable private final String name;
    private final int line;

    public ScoreboardLine(@NotNull Message message, @Nullable String name, int line) {
        this.message = message;
        this.name = name;
        this.line = line;
    }

    @NotNull
    public Message getMessage() {
        return message;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public int getLine() {
        return line;
    }

    public String getTeamName() {
        return getTeamName(line);
    }

    public static String getTeamName(int line) {
        return line + "";
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("message",message)
                .add("index",line)
                .add("name",name).toString();
    }
}
