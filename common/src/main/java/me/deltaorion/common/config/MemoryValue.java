package me.deltaorion.common.config;

import me.deltaorion.common.config.ConfigSection;
import me.deltaorion.common.config.ConfigValue;
import me.deltaorion.common.config.MemoryConfig;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class MemoryValue implements ConfigValue {

    @NotNull private final ConfigValue value;
    @NotNull private final ConfigValue def;
    @NotNull private final MemoryConfig root;

    public MemoryValue(@NotNull ConfigValue value, @NotNull ConfigValue def, @NotNull MemoryConfig root) {
        this.value = value;
        this.def = def;
        this.root = root;
    }

    @Override
    public Object asObject() {
        if(value.isObject())
            return value.asObject();

        return def.asObject();
    }


    @Override
    public boolean isObject() {
        if(value.isObject())
            return true;

        if(root.options().copyDefaults())
            return def.isObject();

        return false;
    }

    @Override
    public String asString() {
        if(value.isString())
            return value.asString();

        return def.asString();
    }

    @Override
    public boolean isString() {
        if(value.isString())
            return true;

        if(root.options().copyDefaults())
            return def.isString();

        return false;
    }

    @Override
    public int asInt() {
        if(value.isInt())
            return value.asInt();

        return def.asInt();
    }

    @Override
    public boolean isInt() {
        if(value.isInt())
            return true;

        if(root.options().copyDefaults())
            return def.isInt();

        return false;
    }

    @Override
    public boolean asBoolean() {
        if(value.isBoolean())
            return value.asBoolean();

        return def.asBoolean();
    }

    @Override
    public boolean isBoolean() {
        if(value.isBoolean())
            return true;

        if(root.options().copyDefaults())
            return def.isBoolean();

        return false;
    }

    @Override
    public double asDouble() {
        if(value.isDouble())
            return value.asDouble();

        return def.asDouble();
    }

    @Override
    public boolean isDouble() {
        if(value.isDouble())
            return true;

        if(root.options().copyDefaults())
            return def.isDouble();

        return false;
    }

    @Override
    public long asLong() {
        if(value.isLong())
            return value.asLong();

        return def.asLong();
    }

    @Override
    public boolean isLong() {
        if(value.isLong())
            return true;

        if(root.options().copyDefaults())
            return def.isLong();

        return false;
    }

    @Override
    public boolean isConfigSection() {
        if(value.isConfigSection())
            return true;

        if(root.options().copyDefaults()) {
            return def.isConfigSection();
        }

        return false;
    }

    @Override
    public ConfigSection asConfigSection() {
        if(value.isConfigSection())
            return value.asConfigSection();

        return def.asConfigSection();
    }

    @NotNull
    @Override
    public List<?> asList() {
        if(value.isList())
            return value.asList();

        return def.asList();
    }

    @Override
    public boolean isList() {
        if(value.isList())
            return true;

        if(root.options().copyDefaults()) {
            return def.isList();
        }

        return false;
    }

    @NotNull
    @Override
    public List<String> asStringList() {
        if(value.isList())
            return value.asStringList();

        return def.asStringList();
    }

    @NotNull
    @Override
    public List<Integer> asIntegerList() {
        if(value.isList())
            return value.asIntegerList();

        return def.asIntegerList();
    }

    @NotNull
    @Override
    public List<Boolean> asBooleanList() {
        if(value.isList())
            return value.asBooleanList();

        return def.asBooleanList();
    }

    @NotNull
    @Override
    public List<Double> asDoubleList() {
        if(value.isList())
            return value.asDoubleList();

        return def.asDoubleList();
    }

    @NotNull
    @Override
    public List<Character> asCharacterList() {
        if(value.isList())
            return value.asCharacterList();

        return def.asCharacterList();
    }

    @NotNull
    @Override
    public List<Byte> asByteList() {
        if(value.isList())
            return value.asByteList();

        return def.asByteList();
    }

    @NotNull
    @Override
    public List<Long> asLongList() {
        if(value.isList())
            return value.asLongList();

        return def.asLongList();
    }

    @NotNull
    @Override
    public List<Float> asFloatList() {
        if(value.isList())
            return value.asFloatList();

        return def.asFloatList();
    }

    @NotNull
    @Override
    public List<Short> asShortList() {
        if(value.isList())
            return value.asShortList();

        return def.asShortList();
    }

    @NotNull
    @Override
    public List<Map<?, ?>> asMapList() {
        if(value.isList())
            return value.asMapList();

        return def.asMapList();
    }
}
