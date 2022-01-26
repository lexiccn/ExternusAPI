package me.deltaorion.extapi.locale.message;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.Locale;

@Immutable
public class SimpleComponent implements MessageComponent {

    private final String component;

    public SimpleComponent(Object value) {
        this.component = String.valueOf(value);
    }

    public SimpleComponent(String value) {
        this.component = value;
    }

    public SimpleComponent(int value) {
        this.component = String.valueOf(value);
    }

    public SimpleComponent(boolean value) {
        this.component = String.valueOf(value);
    }

    public SimpleComponent(float value) {
        this.component = String.valueOf(value);
    }

    public SimpleComponent(double value) {
        this.component = String.valueOf(value);
    }

    public SimpleComponent(char value) {
        this.component = String.valueOf(value);
    }

    @Override
    public String toString(Locale locale) {
        return component;
    }
}
