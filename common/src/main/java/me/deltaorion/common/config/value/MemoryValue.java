package me.deltaorion.common.config.value;

import me.deltaorion.common.config.MemoryConfig;
import org.jetbrains.annotations.NotNull;

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
}
