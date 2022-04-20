package me.deltaorion.common.locale.message;

import com.google.common.math.IntMath;
import me.deltaorion.common.locale.IChatColor;
import me.deltaorion.common.plugin.EServer;
import net.jcip.annotations.GuardedBy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class Message {

    public final static char PLACEHOLDER_START = '{';
    public final static char PLACEHOLDER_STOP = '}';
    public final static char ESCAPE = '\\';
    private final List<MessageComponent> composition;
    @Nullable @GuardedBy("this") private String[] defaultArgs;

    private Message(MessageComponent... components) {
        List<MessageComponent> temp = new ArrayList<>(Arrays.asList(components));
        this.composition = Collections.unmodifiableList(temp);
    }

    private Message(Builder builder) {
        this.composition = Collections.unmodifiableList(builder.components);
        defaultArgs = new String[builder.defArgs.size()];
        for(int i=0;i<builder.defArgs.size();i++) {
            defaultArgs[i] = String.valueOf(builder .defArgs.get(i));
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

        return IChatColor.translateAlternateColorCodes('&',
                substitutePlaceHolders(builder.toString(),args));
    }

    public synchronized void setDefaults(@Nullable Object... defaultArgs) {
        if(defaultArgs==null) {
            this.defaultArgs = null;
        } else {
            this.defaultArgs = new String[defaultArgs.length];
            for(int i=0;i<defaultArgs.length;i++) {
                this.defaultArgs[i] = String.valueOf(defaultArgs[i]);
            }
        }
    }

    private String substitutePlaceHolders(@NotNull String rendered, Object... args) {
        StringBuilder finished = new StringBuilder();
        if(args==null)
            args = new Object[0];
        String[] defArgs;
        synchronized (this) {
            defArgs = defaultArgs;
        }

        int i = 0;
        while(i<rendered.length()) {
            //look for place holder
            char character = rendered.charAt(i);
            if(character==ESCAPE) {
                PlaceHolder placeHolder = getPlaceholderIndex(rendered,i+1);
                if(placeHolder!=null) {
                    finished.append(PLACEHOLDER_START);
                    i+=2;
                    continue;
                }
            }
            PlaceHolder placeholder = getPlaceholderIndex(rendered,i);
            if(placeholder == null) {
                finished.append(character);
                i++;
            } else {
                String arg = getArg(placeholder.index,args,defArgs);
                if(arg==null) {
                    finished.append(character);
                    i++;
                } else {
                    finished.append(arg);
                    i+= placeholder.len;
                }
            }
        }
        return finished.toString();
    }

    private PlaceHolder getPlaceholderIndex(String rendered, int position) {
        if(rendered.charAt(position)!=PLACEHOLDER_START)
            return null;

        int len = 1;
        int j = position;
        StringBuilder intString = new StringBuilder();
        //begin searching for the placeholder
        while (true) {
            j++;
            if(j>=rendered.length()) {
                return null;
            }

            char placeChar = rendered.charAt(j);
            if(!(placeChar<='9' && placeChar>='0')) {
                if(placeChar!=PLACEHOLDER_STOP)
                    return null;

                break;
            }

            intString.append(placeChar);
        }
        String num = intString.toString();
        if(num.length()==0)
            return null;
        return new PlaceHolder(Integer.parseInt(num),num.length()+2);
    }


    /*
    private String substitutePlaceHolders(@NotNull String rendered, Object... args) {
        StringBuilder fin = new StringBuilder();
        int i = 0;
        int objCount = 0;
        String[] defArgs;
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
     */

    private String getArg(int objCount, Object[] args, String[] defaultArgs) {
        if (objCount < args.length) {
            return String.valueOf(args[objCount]);
        } else {
            if (defaultArgs != null) {
                if (objCount < defaultArgs.length) {
                    return defaultArgs[objCount];
                }
            }
        }
        return null;
    }

    /*
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

     */

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Message))
            return false;

        Message message = (Message) o;
        if(message.composition.size()!=this.composition.size())
            return false;

        for(int i=0;i<composition.size();i++) {
            MessageComponent component = composition.get(i);
            if(!component.equals(message.composition.get(i))) {
                return false;
            }
        }

        return Arrays.equals(this.defaultArgs, message.defaultArgs);
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

        public Builder style(IChatColor chatColor) {
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

    private final class PlaceHolder {
        private final int index;
        private final int len;

        private PlaceHolder(int index, int len) {
            this.index = index;
            this.len = len;
        }
    }
}


