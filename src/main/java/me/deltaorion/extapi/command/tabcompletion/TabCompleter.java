package me.deltaorion.extapi.command.tabcompletion;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A TabCompleter is an abstract interface which represents any function that can retrieve the correct tab completions that should
 * be given to the user given what the user has currently typed, and the available completions.
 */
public interface TabCompleter {

    /**
     * This method should search through the supplied words and return sensible completions that should be sent to the user.
     * This method does not supply a list of possible words given the arguments, it simply sorts
     * the possible words to give appropriate tab completions. The final argument in the list of args is the prefix.
     * All suggested completions should begin with the prefix.
     *
     * For example
     *   words = {“abc”, “abcd”, “aa”, “abbbaba”} prefix = “ab” then he must be shown {“abc”, “abcd”, “abbbaba”}.
     *
     * @param words A list of possible words
     * @param args All the arguments sent by the user thus far
     * @return A list of words given the prefix that is typed
     */

    @NotNull
    public List<String> search(@NotNull List<String> words, @NotNull List<String> args);
}
