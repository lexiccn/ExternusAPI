package me.deltaorion.common.config.properties;

import me.deltaorion.common.config.ConfigSection;
import me.deltaorion.common.config.value.ConfigValue;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PropertiesValue implements ConfigValue {

    @Nullable private final String value;

    public PropertiesValue(@Nullable String value) {
        this.value = value;
    }

    @Nullable @Override
    public Object asObject() {
        if(isDouble())
            return asDouble();

        if(isLong())
            return asLong();

        if(isInt())
            return asInt();

        if(isBoolean())
            return asBoolean();

        return value;
    }

    @Override
    public boolean isObject() {
        return value != null;
    }

    @Nullable @Override
    public String asString() {
        return value;
    }

    @Override
    public boolean isString() {
        return value != null;
    }

    @Override
    public int asInt() {
        if(value==null)
            return DEFAULT_NUMBER;

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return DEFAULT_NUMBER;
        }
    }

    @Override
    public boolean isInt() {
        if(value==null) {
            return false;
        }

        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean asBoolean() {
        try {
            return parseBoolean();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean isBoolean() {
        try {
            parseBoolean();
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public double asDouble() {
        if(value==null)
            return DEFAULT_NUMBER;

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return DEFAULT_NUMBER;
        }
    }

    @Override
    public boolean isDouble() {
        if(value==null)
            return false;

        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    @Override
    public long asLong() {
        if(value==null)
            return DEFAULT_NUMBER;

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return DEFAULT_NUMBER;
        }
    }

    @Override
    public boolean isLong() {
        if(value==null)
            return false;

        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean isConfigSection() {
        return false;
    }

    @Override
    public ConfigSection asConfigSection() {
        return null;
    }

    @Override
    public List<?> asList() {
        return Collections.emptyList();
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public List<String> asStringList() {
        return Collections.emptyList();
    }

    @Override
    public List<Integer> asIntegerList() {
        return Collections.emptyList();
    }

    @Override
    public List<Boolean> asBooleanList() {
        return Collections.emptyList();
    }

    @Override
    public List<Double> asDoubleList() {
        return Collections.emptyList();
    }

    @Override
    public List<Character> asCharacterList() {
        return Collections.emptyList();
    }

    @Override
    public List<Byte> asByteList() {
        return Collections.emptyList();
    }

    @Override
    public List<Long> asLongList() {
        return null;
    }

    @Override
    public List<Float> asFloatList() {
        return null;
    }

    @Override
    public List<Short> asShortList() {
        return null;
    }

    @Override
    public List<Map<?, ?>> asMapList() {
        return null;
    }

    private boolean parseBoolean() {
        if(value==null)
            throw new IllegalArgumentException("null");

        if(value.equalsIgnoreCase("true"))
            return true;

        if(value.equalsIgnoreCase("false"))
            return false;

        throw new IllegalArgumentException("Not a boolean");
    }
}
