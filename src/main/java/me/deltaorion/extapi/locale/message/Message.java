package me.deltaorion.extapi.locale.message;

import me.deltaorion.extapi.common.server.EServer;
import net.jcip.annotations.GuardedBy;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class Message {

    public final static String PLACEHOLDER = "%s";
    private final List<MessageComponent> composition;
    @Nullable @GuardedBy("this") private Object[] defaultArgs;

    private Message(MessageComponent... components) {
        List<MessageComponent> temp = new ArrayList<>(Arrays.asList(components));
        this.composition = Collections.unmodifiableList(temp);
    }

    private Message(Builder builder) {
        this.composition = Collections.unmodifiableList(builder.components);
        defaultArgs = new Object[builder.defArgs.size()];
        for(int i=0;i<builder.defArgs.size();i++) {
            defaultArgs[i] = builder.defArgs.get(i);
        }
    }

    public static Message valueOf(String value) {
        return new Message(new SimpleComponent(value));
    }

    public static Message valueOf(int value) {
        return new Message(new SimpleComponent(value));
    }

    public static Message valueOf(boolean value) {
        return new Message(new SimpleComponent(value));
    }

    public static Message valueOf(double value) {
        return new Message(new SimpleComponent(value));
    }

    public static Message valueOf(float value) {
        return new Message(new SimpleComponent(value));
    }

    public static Message valueOf(Object value) {
        return new Message(new SimpleComponent(value));
    }

    public static Message valueOf(char value) {
        return new Message(new SimpleComponent(value));
    }

    public static Message valueOfTranslatable(String location) {
        return new Message(new TranslatableComponent(location));
    }

    public static Message valueOfBuilder(Consumer<Builder> consumer) {
        Builder builder = new Builder();
        consumer.accept(builder);
        return new Message(builder);
    }

    @Override
    public String toString() {
        return toString(EServer.DEFAULT_LOCALE);
    }

    public String toString(Object... args) {
        return toString(EServer.DEFAULT_LOCALE,args);
    }

    public String toString(Locale locale, Object... args) {
        StringBuilder builder = new StringBuilder();
        for(MessageComponent component : composition) {
            builder.append(component.toString(locale));
        }

        return ChatColor.translateAlternateColorCodes(
                '&',substitutePlaceHolders(builder.toString(),args));
    }

    public synchronized void setDefaults(@Nullable Object... defaultArgs) {
        this.defaultArgs = defaultArgs;
    }

    private String substitutePlaceHolders(String rendered, Object... args) {
        StringBuilder fin = new StringBuilder();
        int i = 0;
        int objCount = 0;
        Object[] defArgs;
        synchronized (this) {
            defArgs = this.defaultArgs;
        }

        while(i<rendered.length()-PLACEHOLDER.length()+1) {
            boolean placeholder = isPlaceHolder(rendered,PLACEHOLDER,i);

            Object arg = null;
            if(placeholder) {
                arg = getArg(objCount,args,defArgs);
                objCount++;
            }

            if(arg==null) {
                fin.append(rendered.charAt(i));
                i++;
            } else {
                fin.append(arg);
                i+=PLACEHOLDER.length();
            }
        }

        while(i<rendered.length()) {
            fin.append(rendered.charAt(i));
            i++;
        }
        return fin.toString();
    }

    private Object getArg(int objCount, Object[] args, Object[] defaultArgs) {
        if (objCount < args.length) {
            return args[objCount];
        } else {
            if (defaultArgs != null) {
                if (objCount < defaultArgs.length) {
                    return defaultArgs[objCount];
                }
            }
        }
        return null;
    }

    private boolean isPlaceHolder(String rendered, String placeholder, int i) {
        for(int j=0;j<placeholder.length();j++) {
            char placeHolder = placeholder.charAt(j);
            char actual = rendered.charAt(i+j);
            if (actual != placeHolder) {
                return false;
            }
        }
        return true;
    }



    public static class Builder {
        private final List<MessageComponent> components;
        private final List<Object> defArgs;

        public Builder() {
            this.components = new ArrayList<>();
            defArgs = new ArrayList<>();
        }

        public Builder appendTranslatable(String location) {
            this.components.add(new TranslatableComponent(location));
            return this;
        }

        public Builder style(ChatColor chatColor) {
            this.components.add(new SimpleComponent(chatColor));
            this.components.add(new SimpleComponent(""));
            return this;
        }

        public Builder style(net.md_5.bungee.api.ChatColor chatColor) {
            this.components.add(new SimpleComponent(chatColor));
            this.components.add(new SimpleComponent(""));
            return this;
        }

        public Builder append(String value) {
            this.components.add(new SimpleComponent(value));
            return this;
        }

        public Builder append(int value) {
            this.components.add(new SimpleComponent(value));
            return this;
        }

        public Builder append(boolean value) {
            this.components.add(new SimpleComponent(value));
            return this;
        }

        public Builder append(double value) {
            this.components.add(new SimpleComponent(value));
            return this;
        }

        public Builder append(float value) {
            this.components.add(new SimpleComponent(value));
            return this;
        }

        public Builder append(Object value) {
            this.components.add(new SimpleComponent(value));
            return this;
        }

        public Builder append(char value) {
            this.components.add(new SimpleComponent(value));
            return this;
        }

        public Builder defArg(Object arg) {
            this.defArgs.add(arg);
            return this;
        }

    }
}


