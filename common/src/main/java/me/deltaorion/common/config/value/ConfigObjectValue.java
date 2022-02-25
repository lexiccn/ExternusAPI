package me.deltaorion.common.config.value;

import me.deltaorion.common.config.ConfigSection;
import me.deltaorion.common.config.adapter.ConfigAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigObjectValue implements ConfigValue {

    @NotNull private final String path;
    @Nullable private final Object value;

    public ConfigObjectValue(@NotNull String path, @Nullable Object value) {
        this.path = path;
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

    @Override
    public boolean isConfigSection() {
        return value instanceof ConfigSection;
    }

    @Override
    public ConfigSection asConfigSection() {
        return value instanceof ConfigSection ? (ConfigSection) value : null;
    }

    @Override
    public List<?> asList() {
        return value instanceof List<?> ? (List<?>) value : null;
    }

    @Override
    public boolean isList() {
        return value instanceof List<?>;
    }

    @Override
    public List<String> asStringList() {
        List<?> list = asList();

        if (list == null) {
            return new ArrayList<String>(0);
        }

        List<String> result = new ArrayList<String>();

        for (Object object : list) {
            if ((object instanceof String) || (isPrimitiveWrapper(object))) {
                result.add(String.valueOf(object));
            }
        }

        return result;
    }

    protected boolean isPrimitiveWrapper(Object input) {
        return input instanceof Integer || input instanceof Boolean ||
                input instanceof Character || input instanceof Byte ||
                input instanceof Short || input instanceof Double ||
                input instanceof Long || input instanceof Float;
    }

    @Override
    public List<Integer> asIntegerList() {
        List<?> list = asList();

        if (list == null) {
            return new ArrayList<Integer>(0);
        }

        List<Integer> result = new ArrayList<Integer>();

        for (Object object : list) {
            if (object instanceof Integer) {
                result.add((Integer) object);
            } else if (object instanceof String) {
                try {
                    result.add(Integer.valueOf((String) object));
                } catch (Exception ex) {
                }
            } else if (object instanceof Character) {
                result.add((int) ((Character) object).charValue());
            } else if (object instanceof Number) {
                result.add(((Number) object).intValue());
            }
        }

        return result;
    }

    @Override
    public List<Boolean> asBooleanList() {
        List<?> list = asList();

        if (list == null) {
            return new ArrayList<Boolean>(0);
        }

        List<Boolean> result = new ArrayList<Boolean>();

        for (Object object : list) {
            if (object instanceof Boolean) {
                result.add((Boolean) object);
            } else if (object instanceof String) {
                if (Boolean.TRUE.toString().equals(object)) {
                    result.add(true);
                } else if (Boolean.FALSE.toString().equals(object)) {
                    result.add(false);
                }
            }
        }

        return result;
    }

    @Override
    public List<Double> asDoubleList() {
        List<?> list = asList();

        if (list == null) {
            return new ArrayList<Double>(0);
        }

        List<Double> result = new ArrayList<Double>();

        for (Object object : list) {
            if (object instanceof Double) {
                result.add((Double) object);
            } else if (object instanceof String) {
                try {
                    result.add(Double.valueOf((String) object));
                } catch (Exception ex) {
                }
            } else if (object instanceof Character) {
                result.add((double) ((Character) object).charValue());
            } else if (object instanceof Number) {
                result.add(((Number) object).doubleValue());
            }
        }

        return result;
    }

    @Override
    public List<Character> asCharacterList() {
        List<?> list = asList();

        if (list == null) {
            return new ArrayList<Character>(0);
        }

        List<Character> result = new ArrayList<Character>();

        for (Object object : list) {
            if (object instanceof Character) {
                result.add((Character) object);
            } else if (object instanceof String) {
                String str = (String) object;

                if (str.length() == 1) {
                    result.add(str.charAt(0));
                }
            } else if (object instanceof Number) {
                result.add((char) ((Number) object).intValue());
            }
        }

        return result;
    }

    @Override
    public List<Byte> asByteList() {
        List<?> list = asList();

        if (list == null) {
            return new ArrayList<Byte>(0);
        }

        List<Byte> result = new ArrayList<Byte>();

        for (Object object : list) {
            if (object instanceof Byte) {
                result.add((Byte) object);
            } else if (object instanceof String) {
                try {
                    result.add(Byte.valueOf((String) object));
                } catch (Exception ex) {
                }
            } else if (object instanceof Character) {
                result.add((byte) ((Character) object).charValue());
            } else if (object instanceof Number) {
                result.add(((Number) object).byteValue());
            }
        }

        return result;
    }

    @Override
    public List<Long> asLongList() {
        List<?> list = asList();

        if (list == null) {
            return new ArrayList<Long>(0);
        }

        List<Long> result = new ArrayList<Long>();

        for (Object object : list) {
            if (object instanceof Long) {
                result.add((Long) object);
            } else if (object instanceof String) {
                try {
                    result.add(Long.valueOf((String) object));
                } catch (Exception ex) {
                }
            } else if (object instanceof Character) {
                result.add((long) ((Character) object).charValue());
            } else if (object instanceof Number) {
                result.add(((Number) object).longValue());
            }
        }

        return result;
    }

    @Override
    public List<Float> asFloatList() {
        List<?> list = asList();

        if (list == null) {
            return new ArrayList<Float>(0);
        }

        List<Float> result = new ArrayList<Float>();

        for (Object object : list) {
            if (object instanceof Float) {
                result.add((Float) object);
            } else if (object instanceof String) {
                try {
                    result.add(Float.valueOf((String) object));
                } catch (Exception ex) {
                }
            } else if (object instanceof Character) {
                result.add((float) ((Character) object).charValue());
            } else if (object instanceof Number) {
                result.add(((Number) object).floatValue());
            }
        }

        return result;
    }

    @Override
    public List<Short> asShortList() {
        List<?> list = asList();

        if (list == null) {
            return new ArrayList<Short>(0);
        }

        List<Short> result = new ArrayList<Short>();

        for (Object object : list) {
            if (object instanceof Short) {
                result.add((Short) object);
            } else if (object instanceof String) {
                try {
                    result.add(Short.valueOf((String) object));
                } catch (Exception ex) {
                }
            } else if (object instanceof Character) {
                result.add((short) ((Character) object).charValue());
            } else if (object instanceof Number) {
                result.add(((Number) object).shortValue());
            }
        }

        return result;
    }

    @Override
    public List<Map<?, ?>> asMapList() {
        List<?> list = asList();
        List<Map<?, ?>> result = new ArrayList<Map<?, ?>>();

        if (list == null) {
            return result;
        }

        for (Object object : list) {
            if (object instanceof Map) {
                result.add((Map<?, ?>) object);
            }
        }

        return result;
    }
}
