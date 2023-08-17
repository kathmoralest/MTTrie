/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDAs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Kathy Morales
 */
public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public TrieNode getRoot() {
        return root;
    }

    public void setRoot(TrieNode root) {
        this.root = root;
    }
    
    public void clean() {
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            current.getChildren().putIfAbsent(c, new TrieNode());
            current = current.getChildren().get(c);
        }
        current.setEndOfWord(true) ;
    }

    public boolean search(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            if (!current.getChildren().containsKey(c)) {
                return false;
            }
            current = current.getChildren().get(c);
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
    
    public void loadFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine();
                insert(word);
            }
            System.out.println("Diccionario cargado desde el archivo: " + filename);
        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado: " + filename);
        }
    }
    
    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            saveToFile(root, "", writer);
            System.out.println("Diccionario guardado en el archivo: " + filename);
        } catch (IOException e) {
            System.err.println("Error al guardar el diccionario en el archivo: " + e.getMessage());
        }
    }
    
    private void saveToFile(TrieNode node, String prefix, PrintWriter writer) {
        if (node.isEndOfWord()) {
            writer.println(prefix);
        }

        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            char key = entry.getKey();
            TrieNode childNode = entry.getValue();
            saveToFile(childNode, prefix + key, writer);
        }
    }
    

    public void printTrie() {
        printTrie(root, "");
    }
    
    private void printTrie(TrieNode node, String prefix) {
        if (node.isEndOfWord()) {
            System.out.println(prefix);
        }

        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            char key = entry.getKey();
            TrieNode childNode = entry.getValue();
            printTrie(childNode, prefix + key);
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

        private void generateDot(TrieNode node, String prefix, FileWriter writer) throws IOException {
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
        
    public void generateDotFileByLevels(String dotFilePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(dotFilePath))) {
            writer.println("digraph TrieByLevels {");
            writer.println("  node [shape=box];");

            generateDotNodeByLevels(writer, root, "ROOT");

            writer.println("}");
            System.out.println("Archivo DOT generado: " + dotFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void generateDotNodeByLevels(PrintWriter writer, TrieNode node, String nodeName) {
        if (node.isEndOfWord()) {
            writer.println("  \"" + nodeName + "\" [label=\"" + nodeName + "\", shape=doublecircle];");
        } else {
            writer.println("  \"" + nodeName + "\";");
        }

        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            char key = entry.getKey();
            TrieNode childNode = entry.getValue();
            String childName = nodeName + key;
            writer.println("  \"" + nodeName + "\" -> \"" + childName + "\" [label=\"" + key + "\"];");
            generateDotNodeByLevels(writer, childNode, childName);
        }
    }
}
