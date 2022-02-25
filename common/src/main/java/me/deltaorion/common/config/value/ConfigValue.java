package me.deltaorion.common.config.value;

import me.deltaorion.common.config.ConfigSection;

import java.util.List;
import java.util.Map;

public interface ConfigValue {

    int DEFAULT_NUMBER = 0;

    String DEFAULT_STRING = null;

    boolean DEFAULT_BOOLEAN = false;

    Object asObject();

    boolean isObject();

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

    ConfigSection asConfigSection();

    List<?> asList();

    boolean isList();

    List<String> asStringList();

    List<Integer> asIntegerList();

    List<Boolean> asBooleanList();

    List<Double> asDoubleList();

    List<Character> asCharacterList();

    List<Byte> asByteList();

    List<Long> asLongList();

    List<Float> asFloatList();

    List<Short> asShortList();

    List<Map<?,?>> asMapList();
}
