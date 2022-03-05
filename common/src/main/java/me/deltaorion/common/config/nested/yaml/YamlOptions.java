package me.deltaorion.common.config.nested.yaml;

import com.google.common.base.Preconditions;

public class YamlOptions {

    private int indent = 2;
    private int width = 80;

    public int getIndent() {
        return indent;
    }

    public YamlOptions setIndent(int indent) {
        Preconditions.checkArgument(indent >= 2, "Indent must be at least 2 characters");
        Preconditions.checkArgument(indent <= 9, "Indent cannot be greater than 9 characters");
        this.indent = indent;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public YamlOptions setWidth(int width) {
        this.width = width;
        return this;

    }
}
