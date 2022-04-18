package me.deltaorion.common.config.yaml;

import com.amihaiemil.eoyaml.Node;
import com.amihaiemil.eoyaml.Scalar;
import com.amihaiemil.eoyaml.YamlNode;
import com.amihaiemil.eoyaml.YamlSequence;
import me.deltaorion.common.config.ConfigSection;
import me.deltaorion.common.config.ConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EOYamlValue implements ConfigValue {

    @NotNull private final YamlNode node;

    public EOYamlValue(@NotNull YamlNode node) {
        this.node = node;
    }

    @Nullable @Override
    public Object asObject() {
        if(isNumber())
            return asNumber();

        if(isString())
            return asString();

        if(isConfigSection())
            return asConfigSection();

        if(isList())
            return asList();

        return null;
    }

    private boolean isNumber() {
        if(node.type() != Node.SCALAR)
            return false;

        String value = node.asScalar().value();
        try {
            Number num = NumberFormat.getInstance().parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private Number asNumber() {
        if(node.type() != Node.SCALAR)
            return DEFAULT_NUMBER;

        String value = node.asScalar().value();
        try {
            return NumberFormat.getInstance().parse(value);
        } catch (ParseException e) {
            return DEFAULT_NUMBER;
        }
    }

    @Override
    public boolean isObject() {
        return !node.isEmpty();
    }

    @Nullable @Override
    public String asString() {
        if(node.type() != Node.SCALAR)
            return DEFAULT_STRING;

        return node.asScalar().value();
    }

    @Override
    public boolean isString() {
        if(node.type() != Node.SCALAR)
            return false;

        return node.asScalar().type()!=null;
    }

    @Override
    public int asInt() {
        if(node.type()!=Node.SCALAR)
            return DEFAULT_NUMBER;

        String value = node.asScalar().value();
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

        if(node.type()!=Node.SCALAR)
            return false;

        String value = node.asScalar().value();
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
        if(node.type()!=Node.SCALAR)
            return DEFAULT_BOOLEAN;

        String value = node.asScalar().value();
        try {
            return Boolean.parseBoolean(value);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean isBoolean() {
        if(node.type()!=Node.SCALAR)
            return false;

        String value = node.asScalar().value();
        try {
            return Boolean.parseBoolean(value);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public double asDouble() {
        if(node.type()!=Node.SCALAR)
            return DEFAULT_NUMBER;

        String value = node.asScalar().value();
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
        if(node.type()!=Node.SCALAR)
            return false;

        String value = node.asScalar().value();
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
        if(node.type()!=Node.SCALAR)
            return DEFAULT_NUMBER;

        String value = node.asScalar().value();
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
        if(node.type()!=Node.SCALAR)
            return false;

        String value = node.asScalar().value();
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

    @Nullable
    @Override
    public ConfigSection asConfigSection() {
        return null;
    }

    @Nullable
    @Override
    public List<?> asList() {
        if(!isList())
            return null;

        if(isDoubleList())
            return asDoubleList();

        if(isLongList())
            return asLongList();

        if(isIntegerList())
            return asIntegerList();

        return asStringList();
    }

    @Override
    public boolean isList() {
        return node.type() == Node.SEQUENCE;
    }

    @NotNull
    @Override
    public List<String> asStringList() {
        if(node.type()!=Node.SEQUENCE)
            return Collections.emptyList();

        YamlSequence sequence = node.asSequence();
        List<String> list = new ArrayList<>();
        for(YamlNode node : sequence) {
            list.add(node.asScalar().value());
        }

        return list;
    }

    private boolean isIntegerList() {
        return asIntegerList().size()>0;
    }

    @NotNull @Override
    public List<Integer> asIntegerList() {
        if(node.type()!=Node.SEQUENCE)
            return Collections.emptyList();

        YamlSequence sequence = node.asSequence();
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<sequence.size();i++) {
            try {
                list.add(sequence.integer(i));
            } catch (NumberFormatException e) {
                return Collections.emptyList();
            }
        }
        return list;
    }

    private boolean isBooleanList() {
        return asBooleanList().size()>0;
    }

    @NotNull @Override
    public List<Boolean> asBooleanList() {
        if(node.type()!=Node.SEQUENCE)
            return Collections.emptyList();

        YamlSequence sequence = node.asSequence();
        List<Boolean> list = new ArrayList<>();
        for(YamlNode node : sequence) {
            list.add(Boolean.valueOf(node.asScalar().value()));
        }
        return list;
    }

    private boolean isDoubleList() {
        return asDoubleList().size()>0;
    }

    @NotNull @Override
    public List<Double> asDoubleList() {
        if(node.type()!=Node.SEQUENCE)
            return Collections.emptyList();

        YamlSequence sequence = node.asSequence();
        List<Double> list = new ArrayList<>();
        for(int i=0;i<sequence.size();i++) {
            try {
                list.add(sequence.doubleNumber(i));
            } catch (NumberFormatException e) {
                return Collections.emptyList();
            }
        }
        return list;
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

    private boolean isLongList() {
        return asLongList().size()>0;
    }

    @NotNull @Override
    public List<Long> asLongList() {
        if(node.type()!=Node.SEQUENCE)
            return Collections.emptyList();

        YamlSequence sequence = node.asSequence();
        List<Long> list = new ArrayList<>();
        for(int i=0;i<sequence.size();i++) {
            try {
                list.add(sequence.longNumber(i));
            } catch (NumberFormatException e) {
                return Collections.emptyList();
            }
        }
        return list;
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
