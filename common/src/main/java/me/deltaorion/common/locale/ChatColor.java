package me.deltaorion.common.locale;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

public enum ChatColor {

    BLACK('0', 0x00),
    /**
     * Represents dark blue
     */
    DARK_BLUE('1', 0x1),
    /**
     * Represents dark green
     */
    DARK_GREEN('2', 0x2),
    /**
     * Represents dark blue (aqua)
     */
    DARK_AQUA('3', 0x3),
    /**
     * Represents dark red
     */
    DARK_RED('4', 0x4),
    /**
     * Represents dark purple
     */
    DARK_PURPLE('5', 0x5),
    /**
     * Represents gold
     */
    GOLD('6', 0x6),
    /**
     * Represents gray
     */
    GRAY('7', 0x7),
    /**
     * Represents dark gray
     */
    DARK_GRAY('8', 0x8),
    /**
     * Represents blue
     */
    BLUE('9', 0x9),
    /**
     * Represents green
     */
    GREEN('a', 0xA),
    /**
     * Represents aqua
     */
    AQUA('b', 0xB),
    /**
     * Represents red
     */
    RED('c', 0xC),
    /**
     * Represents light purple
     */
    LIGHT_PURPLE('d', 0xD),
    /**
     * Represents yellow
     */
    YELLOW('e', 0xE),
    /**
     * Represents white
     */
    WHITE('f', 0xF),
    /**
     * Represents magical characters that change around randomly
     */
    MAGIC('k', 0x10, true),
    /**
     * Makes the text bold.
     */
    BOLD('l', 0x11, true),
    /**
     * Makes a line appear through the text.
     */
    STRIKETHROUGH('m', 0x12, true),
    /**
     * Makes the text appear underlined.
     */
    UNDERLINE('n', 0x13, true),
    /**
     * Makes the text italic.
     */
    ITALIC('o', 0x14, true),
    /**
     * Resets all previous chat colors or formats.
     */
    RESET('r', 0x15)
    ;


    private int intCode;
    private char code;
    private boolean isFormat;
    @Nullable private  String toString;
    private boolean initialised = false;

    @Nullable private static TranslateFunction translateFunction;
    @Nullable private static Function<String,String> stripFunction;

    private ChatColor(char code, int intCode) {
        this(code, intCode, false);
    }

    private ChatColor(char code, int intCode, boolean isFormat) {
        this.code = code;
        this.intCode = intCode;
        this.isFormat = isFormat;
        this.toString = new String(new char[] {COLOR_CHAR, code});
    }

    public static final char COLOR_CHAR = '\u00A7';

    public void initialise(char code, boolean isFormat, @NotNull String toString) {
        if(initialised)
            throw new IllegalStateException("Cannot initialise a ChatColor that has already been initialised!");

        initialised = true;
        Objects.requireNonNull(toString);
        this.toString = toString;
        this.isFormat = isFormat;
        this.intCode =code;
        this.code = code;
    }

    /**
     * Gets the char value associated with this color
     *
     * @return A char value of this color code
     */
    public char getChar() {
        return code;
    }

    @Override
    public String toString() {
        return toString;
    }

    /**
     * Checks if this code is a format code as opposed to a color code.
     *
     * @return whether this ChatColor is a format code
     */
    public boolean isFormat() {
        return isFormat;
    }

    /**
     * Checks if this code is a color code as opposed to a format code.
     *
     * @return whether this ChatColor is a color code
     */
    public boolean isColor() {
        return !isFormat && this != RESET;
    }


    public static boolean isInitialised() {
        return ChatColor.translateFunction!=null;
    }

    public static void initialise(@NotNull TranslateFunction function, @NotNull Function<String,String> stripFunction) {
        if(isInitialised())
            throw new IllegalStateException("Translate function has already been initialised");
        ChatColor.translateFunction = Objects.requireNonNull(function);
        ChatColor.stripFunction = Objects.requireNonNull(stripFunction);
    }

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        if(translateFunction==null)
            throw new IllegalStateException("ChatColor has not been initialised properly, or at all");
        return translateFunction.apply(altColorChar,textToTranslate);
    }

    public static String stripColor(final String input) {
        if(stripFunction==null)
            throw new IllegalStateException("ChatColor has not been initialised properly, or at all");

        return stripFunction.apply(input);
    }

    public static interface TranslateFunction {

        public String apply(char altColorChar, String textToTranslate);
    }
}
