package me.deltaorion.extapi.command.tabcompletion;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StringTabCompleter implements TabCompleter {

    @Override @NotNull
    public List<String> search(@NotNull List<String> words, @NotNull List<String> args) {
         String prefix;
         Objects.requireNonNull(words);
         if(Objects.requireNonNull(args).size()==0) {
             return Collections.unmodifiableList(words);
         } else {
             prefix = args.get(args.size()-1);
         }

         prefix = prefix.toLowerCase(Locale.ROOT);

         List<String> autoComplete = new ArrayList<>();
         for(String word : words) {
             if(word.toLowerCase(Locale.ROOT).startsWith(prefix)) {
                 autoComplete.add(word);
             }
         }

         return autoComplete;
    }

}
