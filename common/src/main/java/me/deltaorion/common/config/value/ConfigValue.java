package me.deltaorion.common.config.value;

import java.util.Collections;
import java.util.List;

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
}
