package me.deltaorion.common.config.options;

import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.MemoryConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * @author org.bukkit, DeltaOrion
 */
public class FileConfigOptions extends MemoryConfigOptions {

    private List<String> header = Collections.emptyList();
    private List<String> footer = Collections.emptyList();

    private boolean parseComments = true;

    public FileConfigOptions(@NotNull MemoryConfig root) {
        super(root);
    }

    /**
     * Gets the header that will be applied to the top of the saved output.
     * <p>
     * This header will be commented out and applied directly at the top of
     * the generated output of the {@link me.deltaorion.common.config.FileConfig}. It is not
     * required to include a newline at the end of the header as it will
     * automatically be applied, but you may include one if you wish for extra
     * spacing.
     * <p>
     * If no comments exist, an empty list will be returned. A null entry
     * represents an empty line and an empty String represents an empty comment
     * line.
     *
     * @return Unmodifiable header, every entry represents one line.
     */
    @NotNull
    public List<String> getHeader() {
        return header;
    }

    /**
     * Sets the header that will be applied to the top of the saved output.
     * <p>
     * This header will be commented out and applied directly at the top of
     * the generated output of the {@link me.deltaorion.common.config.FileConfig}. It is not
     * required to include a newline at the end of the header as it will
     * automatically be applied, but you may include one if you wish for extra
     * spacing.
     * <p>
     * If no comments exist, an empty list will be returned. A null entry
     * represents an empty line and an empty String represents an empty comment
     * line.
     *
     * @param value New header, every entry represents one line.
     * @return This object, for chaining
     */
    @NotNull
    public FileConfigOptions setHeader(@Nullable List<String> value) {
        this.header = value;
        return this;
    }

    /**
     * Gets the footer that will be applied to the bottom of the saved output.
     * <p>
     * This footer will be commented out and applied directly at the bottom of
     * the generated output of the {@link FileConfig}. It is not required
     * to include a newline at the beginning of the footer as it will
     * automatically be applied, but you may include one if you wish for extra
     * spacing.
     * <p>
     * If no comments exist, an empty list will be returned. A null entry
     * represents an empty line and an empty String represents an empty comment
     * line.
     *
     * @return Unmodifiable footer, every entry represents one line.
     */
    @NotNull
    public List<String> getFooter() {
        return this.footer;
    }

    /**
     * Sets the footer that will be applied to the bottom of the saved output.
     * <p>
     * This footer will be commented out and applied directly at the bottom of
     * the generated output of the {@link FileConfig}. It is not required
     * to include a newline at the beginning of the footer as it will
     * automatically be applied, but you may include one if you wish for extra
     * spacing.
     * <p>
     * If no comments exist, an empty list will be returned. A null entry
     * represents an empty line and an empty String represents an empty comment
     * line.
     *
     * @param value New footer, every entry represents one line.
     * @return This object, for chaining
     */
    @NotNull
    public FileConfigOptions setFooter(@Nullable List<String> value) {
        this.footer = value;
        return this;
    }

    /**
     * Gets whether or not comments should be loaded and saved.
     * <p>
     * Defaults to true.
     *
     * @return Whether or not comments are parsed.
     */
    public boolean parseComments() {
        return parseComments;
    }

    /**
     * Sets whether or not comments should be loaded and saved.
     * <p>
     * Defaults to true.
     *
     * @param value Whether or not comments are parsed.
     * @return This object, for chaining
     */
    @NotNull
    public FileConfigOptions parseComments(boolean value) {
        this.parseComments = value;
        return this;
    }
}
