package me.deltaorion.extapi.animation;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Immutable
public class MinecraftFrame<T> {

    @Nullable private final T object;
    //time always converted down to millis
    private final long time;

    public MinecraftFrame(@Nullable T object, long time, TemporalUnit unit) {
        Preconditions.checkState(time >= 0);
        this.object = object;
        this.time = Duration.of(time,unit).toMillis();
    }

    public MinecraftFrame(@Nullable T object, long time) {
        this(object,time, ChronoUnit.MILLIS);
    }

    @Nullable
    public T getObject() {
        return object;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("Time",time)
                .add("Object",object)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof MinecraftFrame))
            return false;

        MinecraftFrame frame = (MinecraftFrame) o;
        return java.util.Objects.equals(frame.object,this.object) && this.time == frame.time;
    }
}
