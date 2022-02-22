package me.deltaorion.common.config.value;

import org.jetbrains.annotations.Nullable;

public class ConfigObjectValue implements ConfigValue {

    @Nullable private final Object value;

    public ConfigObjectValue(@Nullable Object value) {
        this.value = value;
    }

    @Override
    public Object asObject() {
        return value;
    }

    @Override
    public boolean isObject() {
        return value!=null;
    }

    @Override
    public String asString() {
        return value == null ? DEFAULT_STRING : value.toString();
    }

    @Override
    public boolean isString() {
        return value instanceof String;
    }

    @Override
    public int asInt() {
        return value instanceof Number ? ((Number) value).intValue() : DEFAULT_NUMBER;
    }

    @Override
    public boolean isInt() {
        return value instanceof Number;
    }

    @Override
    public boolean asBoolean() {
        return value instanceof Boolean ? (Boolean) value : DEFAULT_BOOLEAN;
    }

    @Override
    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    @Override
    public double asDouble() {
        return value instanceof Number ? ((Number) value).doubleValue() : DEFAULT_NUMBER;
    }

    @Override
    public boolean isDouble() {
        return value instanceof Number;
    }

    @Override
    public long asLong() {
        return value instanceof Number ? ((Number) value).longValue() : DEFAULT_NUMBER;
    }

    @Override
    public boolean isLong() {
        return value instanceof Number;
    }
}
