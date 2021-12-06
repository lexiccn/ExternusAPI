package me.deltaorion.extapi.common.version;

public interface VersionParser {

    public MinecraftVersion parse(String versionString) throws IllegalArgumentException;
}
