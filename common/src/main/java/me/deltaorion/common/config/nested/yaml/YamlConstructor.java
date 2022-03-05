package me.deltaorion.common.config.nested.yaml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;

public class YamlConstructor extends SafeConstructor {

    @Nullable
    public Object construct(@NotNull Node node) {
        return constructObject(node);
    }

}
