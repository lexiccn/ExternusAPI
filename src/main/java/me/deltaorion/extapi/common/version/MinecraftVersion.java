package me.deltaorion.extapi.common.version;

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

public class MinecraftVersion {

    private final int major;
    private final int minor;
    private final String snapShot;

    public final static Comparator<MinecraftVersion> comparator = new Comparator<MinecraftVersion>() {
        @Override
        public int compare(MinecraftVersion o1, MinecraftVersion o2) {
            return Integer.compare(o1.major, o2.major);
        }
    };

    public MinecraftVersion(int major, int minor, String snapShot) {
        this.major = major;
        this.minor = minor;
        this.snapShot = snapShot;
    }

    public MinecraftVersion(int major, int minor) {
        this.major = major;
        this.minor = minor;
        this.snapShot = "";
    }

    public boolean equals(Object o) {
        if(!(o instanceof MinecraftVersion))
            return false;

        MinecraftVersion minecraftVersion = (MinecraftVersion) o;
        return this.major == minecraftVersion.major && this.minor == minecraftVersion.minor;
    }

    public boolean similar(Object o) {
        if(!(o instanceof MinecraftVersion))
            return false;

        MinecraftVersion minecraftVersion = (MinecraftVersion) o;

        return this.major == minecraftVersion.major;
    }

    public int compare(MinecraftVersion o2) {
        return MinecraftVersion.comparator.compare(this,o2);
    }

    public String toString() {
        String stringify = "1." + major + "." + minor;

        if(!this.snapShot.equals("")) {
            return stringify + "-" + snapShot + "-SNAPSHOT";
        } else {
            return stringify;
        }
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public String getSnapShot() {
        return snapShot;
    }
}
