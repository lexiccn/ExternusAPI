package me.deltaorion.common.plugin.version;

public interface VersionParser {

    public MinecraftVersion parse(String versionString) throws IllegalArgumentException;
}
