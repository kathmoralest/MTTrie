/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDAs;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Kathy Morales
 */
public class Trie {

    private TrieNode root;
    
    public TrieNode getRoot() {
    return this.root;
}

    public Trie() {
        root = new TrieNode();
    }

    public boolean isEmpty() {
        return root == null;
    }
    
    public void clean() {
        root = new TrieNode();
    }


    public void insert(String word) {
        TrieNode current = root;

        for (char l : word.toCharArray()) {
            current = current.getChildren().computeIfAbsent(l, c -> new TrieNode());
        }
        System.out.println(current);
        current.setEndOfWord(true);
    }
    
    public boolean containsNode(String word) {
        TrieNode current = root;

        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return false;
            }
            current = node;
        }
        return current.isEndOfWord();
    }

    public void delete(String word) {
        delete(root, word, 0);
    }
    
    private boolean delete(TrieNode current, String word, int index) {
        if (index == word.length()) {
            if (!current.isEndOfWord()) {
                return false; // La palabra no está en el Trie
            }
            current.setEndOfWord(false); // Marcar como no final de palabra

            // Si el nodo no tiene más hijos y no es el final de otra palabra, se puede eliminar
            return current.getChildren().isEmpty();
        }

        char c = word.charAt(index);
        TrieNode child = current.getChildren().get(c);
        if (child == null) {
            return false; // La palabra no está en el Trie
        }

        boolean shouldDeleteChild = delete(child, word, index + 1);

        if (shouldDeleteChild) {
            current.getChildren().remove(c);
            // Si el nodo no tiene más hijos y no es el final de otra palabra, se puede eliminar
            return current.getChildren().isEmpty() && !current.isEndOfWord();
        }

        return false;
    }

    public List<String> getSuggestionsWithPrefix(String prefix) {
        List<String> suggestions = new ArrayList<>();
        TrieNode lastNode = getLastNodeOfPrefix(prefix);

        if (lastNode != null) {
            collectSuggestions(lastNode, prefix, suggestions);
        }

        return suggestions;
    }

    private TrieNode getLastNodeOfPrefix(String prefix) {
        TrieNode current = root;

        for (char ch : prefix.toCharArray()) {
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return null;
            }
            current = node;
        }

        return current;
    }

    private void collectSuggestions(TrieNode node, String prefix, List<String> suggestions) {
        if (node.isEndOfWord()) {
            suggestions.add(prefix);
        }

        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            collectSuggestions(entry.getValue(), prefix + entry.getKey(), suggestions);
        }
    }
    
    public void generateDotFile(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write("digraph Trie {\n");
            generateDot(root, "", writer);
            writer.write("}");
            writer.close();
            System.out.println("DOT file generated: " + filename);
        } catch (IOException e) {
            System.err.println("Error generating DOT file: " + e.getMessage());
        }
    }

    public void generateDot(TrieNode node, String prefix, FileWriter writer) throws IOException {
        if (node.isEndOfWord()) {
            writer.write("\"" + prefix + "\" [shape=box];\n");
        }

        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            char key = entry.getKey();
            TrieNode childNode = entry.getValue();
            writer.write("\"" + prefix + "\" -> \"" + prefix + key + "\" [label=\"" + key + "\"];\n");
            generateDot(childNode, prefix + key, writer);
        }
    }
    
}
