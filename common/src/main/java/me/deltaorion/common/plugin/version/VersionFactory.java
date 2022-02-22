package me.deltaorion.common.plugin.version;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class VersionFactory {

    private final static Pattern DASH_REGEX = Pattern.compile("-");
    private final static Pattern DOT_REGEX = Pattern.compile("\\.");
    private final static Pattern COLON_REGEX = Pattern.compile(":");

    /**
     * Parses a version String using all known parsers
     *
     * @param version The version string
     * @return the corresponding minecraft version
     */
    @Nullable
    public static MinecraftVersion parse(@NotNull String version) {
        final List<VersionParser> parsers = new ArrayList<>();
        parsers.add(BUKKIT_PARSER);
        parsers.add(BUNGEE_PARSER);

        for(VersionParser parser : parsers) {
            try {
                return parser.parse(version);
            } catch (IllegalArgumentException ignored) {

            }
        }

        return null;
    }

    private static final VersionParser BUKKIT_PARSER = new VersionParser() {

        private final int VERSION_POSITION = 0;
        private final int SNAP_POSITION = 1;
        private final int MINOR_POSITION = 2;
        private final int MAJOR_POSITION = 1;

        private final String ERR_MSG = "Invalid Version String. String should be in the format 1.(major).(minor)-(snapshot)";

        @Override
        public MinecraftVersion parse(@NotNull final String versionString) throws IllegalArgumentException {
            try {
                String[] snapSplit = DASH_REGEX.split(versionString);
                final String snapShot = snapSplit[SNAP_POSITION];
                final String version = snapSplit[VERSION_POSITION];
                String[] verSplit = DOT_REGEX.split(version);
                final int minor = Integer.parseInt(verSplit[MINOR_POSITION]);
                final int major = Integer.parseInt(verSplit[MAJOR_POSITION]);
                return new MinecraftVersion(major,minor,snapShot);
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                throw new IllegalArgumentException(ERR_MSG);
            }

        }
    };

    //git:BungeeCord-Bootstrap:1.18-R0.1-SNAPSHOT:425ee4e:1618
    private static final VersionParser BUNGEE_PARSER = new VersionParser() {

        private final int IMPORTANT_POSITION = 2;
        private final int MAJOR_POSITION = 0;
        private final int SNAP_POSITION = 1;

        private final String ERR_MSG = "Invalid Version String. String should be in the format git:BungeeCord-Bootstrap:(major)-(snapshot)-SNAPSHOT:425ee4e:1618";

        @Override
        public MinecraftVersion parse(final String versionString) throws IllegalArgumentException {
            try {
                String[] snapSplit = COLON_REGEX.split(versionString);
                final String important = snapSplit[IMPORTANT_POSITION];
                String[] impSplit = DASH_REGEX.split(important);
                String snapShot = impSplit[SNAP_POSITION];
                String ver = impSplit[MAJOR_POSITION];
                String[] verSplit = DOT_REGEX.split(ver);
                int major = Integer.parseInt(verSplit[1]);
                return new MinecraftVersion(major,0,snapShot);
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                throw new IllegalArgumentException(ERR_MSG);
            }

        }
    };

}