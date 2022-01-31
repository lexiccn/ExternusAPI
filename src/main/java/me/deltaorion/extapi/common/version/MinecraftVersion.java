package me.deltaorion.extapi.common.version;

import com.google.common.base.Preconditions;
import jdk.nashorn.internal.ir.annotations.Immutable;
import net.jcip.annotations.NotThreadSafe;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

/**
 * Represents a generic minecraft version. Currently minecraft versions are displayed as simple strings which is
 * easy to read however this is not very useful for programming as it is a common requirement to check the version of
 * the server or proxy when executing certain tasks.
 *
 * Versions are in the format 1.(major).(minor)-(snapshot)
 *
 * To obtain a minecraft version from a string use the {@link VersionFactory} class. This class is simply a data structure.
 */

@Immutable @NotThreadSafe
public class MinecraftVersion implements Comparable<MinecraftVersion> {

    private final int major;
    private final int minor;
    @Nullable private final String snapShot;

    public final static Comparator<MinecraftVersion> comparator = new Comparator<MinecraftVersion>() {
        @Override
        public int compare(MinecraftVersion o1, MinecraftVersion o2) {
            if(o1.major > o2.major) {
                return 1;
            } else if(o1.major < o2.major) {
                return -1;
            } else {
                return Integer.compare(o1.minor,o2.minor);
            }
        }
    };

    public MinecraftVersion(int major, int minor, @Nullable String snapShot) {

        Preconditions.checkState(major >= 0);
        Preconditions.checkState(minor >= 0);

        this.major = major;
        this.minor = minor;
        this.snapShot = snapShot;
    }

    public MinecraftVersion(int major, int minor) {
        this(major,minor,null);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof MinecraftVersion))
            return false;

        MinecraftVersion minecraftVersion = (MinecraftVersion) o;
        return this.major == minecraftVersion.major && this.minor == minecraftVersion.minor;
    }

    public boolean sameMajor(@NotNull MinecraftVersion version) {
        Validate.notNull(version);
        return this.major == version.major;
    }

    public int compareTo(@NotNull MinecraftVersion o2) {
        return MinecraftVersion.comparator.compare(this,o2);
    }

    @Override
    public String toString() {
        String stringify = "1." + major + "." + minor;

        if(this.snapShot != null) {
            return stringify + "-" + snapShot + "-SNAPSHOT";
        } else {
            return stringify;
        }
    }

    /**
     * Returns the minor version. If the version was 1.8.9-A2 Snapshot then this would return 8
     *
     * @return A numerical representation of the major version.
     */
    public int getMajor() {
        return major;
    }

    //returns the Minor version. If the version was 1.8.9-A2 Snapshot then this would return 9
    public int getMinor() {
        return minor;
    }

    @Nullable
    public String getSnapShot() {
        return snapShot;
    }

}
