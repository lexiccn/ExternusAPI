package me.deltaorion.common.config.properties;

import me.deltaorion.common.config.ConfigSection;
import me.deltaorion.common.config.value.ConfigValue;
import org.jetbrains.annotations.NotNull;
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
            return Boolean.parseBoolean(value);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean isBoolean() {
        try {
            return Boolean.parseBoolean(value);
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

    @Override @Nullable
    public List<?> asList() {
        return null;
    }

    @Override
    public boolean isList() {
        return false;
    }

    @NotNull
    @Override
    public List<String> asStringList() {
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public List<Integer> asIntegerList() {
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public List<Boolean> asBooleanList() {
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public List<Double> asDoubleList() {
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public List<Character> asCharacterList() {
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public List<Byte> asByteList() {
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public List<Long> asLongList() {
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public List<Float> asFloatList() {
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public List<Short> asShortList() {
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public List<Map<?, ?>> asMapList() {
        return Collections.emptyList();
    }

}
