package me.deltaorion.extapi.command.tabcompletion;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * This class suggests tab completions by using the {@link String#startsWith} function
 */
public class StringTabCompleter implements TabCompleter {

    @Override @NotNull
    public List<String> search(@NotNull List<String> words, @NotNull List<String> args) {
         String prefix;
         Objects.requireNonNull(words);
         //get the prefix, if it does not exist then return all available tab completions
         if(Objects.requireNonNull(args).size()==0) {
             return Collections.unmodifiableList(words);
         } else {
             prefix = args.get(args.size()-1);
         }

         prefix = prefix.toLowerCase(Locale.ROOT);

         //otherwise loop through the words, check if it starts with the prefix and return
         List<String> autoComplete = new ArrayList<>();
         for(String word : words) {
             if(word.toLowerCase(Locale.ROOT).startsWith(prefix)) {
                 autoComplete.add(word);
             }
         }

         return autoComplete;
    }

}
