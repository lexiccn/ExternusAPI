package me.deltaorion.extapi.animation;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * A frame represents something that needs to be rendered at a relative time.
 *
 * @param <T>
 */

@Immutable
public class MinecraftFrame<T> {

    @Nullable private final T object;
    //time always converted down to millis
    private final long time;

    /**
     * The object will be rendered by the {@link AnimationRenderer} at the specified time. The time should represent
     * the amount of time that needs to pass from the previous frame before it to play this time.
     *
     * For example if the time is 10 and the unit Milliseconds, then 10 milliseconds should pass from the previous
     * frame for this one to play.
     *
     * @param object The thing to be rendered
     * @param time The relative time in which to render the frame
     * @param unit The time unit relating to the time parameter
     */
    public MinecraftFrame(@Nullable T object, long time, TemporalUnit unit) {
        Preconditions.checkState(time >= 0);
        this.object = object;
        this.time = Duration.of(time,unit).toMillis();
    }

    /**
     * The object will be rendered by the {@link AnimationRenderer} at the specified time. The time should represent
     * the amount of time that needs to pass from the previous frame before it to play this time.
     *
     * For example if the time is 10 and the unit Milliseconds, then 10 milliseconds should pass from the previous
     * frame for this one to play.
     *
     * @param object The thing to be rendered
     * @param time The relative time in milliseconds to render this frame.
     */
    public MinecraftFrame(@Nullable T object, long time) {
        this(object,time, ChronoUnit.MILLIS);
    }

    /**
     *
     * @return The object to be rendered
     */
    @Nullable
    public T getObject() {
        return object;
    }

    /**
     *
     * @return The relative time, in milliseconds that needs to pass from the previous frame for this one to play.
     */
    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
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
