package me.deltaorion.extapi.command.tabcompletion;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Trie {

    @NotNull private final TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(Iterable<String> words) {
        for(String word : words) {
            insert(word);
        }
    }

    public void insert(@NotNull String s) {

        Objects.requireNonNull(s);

        TrieNode current = root;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            TrieNode node = current.children.get(ch);
            if (node == null) {
                node = new TrieNode();
                current.children.put(ch, node);
            }
            current = node;
        }
        current.endOfWord = true;
    }

    public List<String> search(@NotNull String prefix) {
        Objects.requireNonNull(prefix);
        List<String> autoCompWords = new ArrayList<String>();

        TrieNode currentNode=root;

        for(int i=0;i<prefix.length();i++) {
            currentNode=currentNode.children.get(prefix.charAt(i));
            if(currentNode==null) return autoCompWords;
        }

        searchWords(currentNode,autoCompWords,prefix);
        return autoCompWords;
    }

    private void searchWords(TrieNode currentNode, List<String> autoCompWords, String word) {

        if(currentNode==null) return;

        if(currentNode.endOfWord) {
            autoCompWords.add(word);
        }

        Map<Character,TrieNode> map=currentNode.children;
        for(Character c:map.keySet()) {
            searchWords(map.get(c),autoCompWords, word + c);
        }

    }

    public static class TrieNode {
        private final Map<Character, TrieNode> children;
        private boolean endOfWord;

        TrieNode() {
            children = new HashMap<Character, TrieNode>();
            endOfWord = false;
        }
    }

}