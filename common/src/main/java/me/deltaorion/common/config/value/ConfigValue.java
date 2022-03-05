package me.deltaorion.common.config.value;

import me.deltaorion.common.config.ConfigSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface ConfigValue {

    int DEFAULT_NUMBER = 0;

    String DEFAULT_STRING = null;

    boolean DEFAULT_BOOLEAN = false;

    /**
     * Returns an object representation of the config value. If the object can be represented as an integer or as a ConfigSection
     * then it should be represented as that and NOT as the original string.
     *
     * @return The object representation of the config value
     */
    @Nullable
    Object asObject();

    boolean isObject();

    @Nullable
    String asString();

    boolean isString();

    int asInt();

    boolean isInt();

    boolean asBoolean();

    boolean isBoolean();

    double asDouble();

    boolean isDouble();

    long asLong();

    boolean isLong();

    boolean isConfigSection();

    @Nullable
    ConfigSection asConfigSection();

    /**
     * Returns a list representation of the value. If the value cannot be represented as a list then this will
     * return an empty list.
     *
     * @return An empty list.
     */
    @Nullable
    List<?> asList();

    boolean isList();

    @NotNull
    List<String> asStringList();

    @NotNull
    List<Integer> asIntegerList();

    @NotNull
    List<Boolean> asBooleanList();

    @NotNull
    List<Double> asDoubleList();

    @NotNull
    List<Character> asCharacterList();

    @NotNull
    List<Byte> asByteList();

    @NotNull
    List<Long> asLongList();

    @NotNull
    List<Float> asFloatList();

    @NotNull
    List<Short> asShortList();

    @NotNull
    List<Map<?,?>> asMapList();
}
