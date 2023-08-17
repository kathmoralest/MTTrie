/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebastrie;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kathy Morales
 */
public class TrieNode {
    private Map<Character, TrieNode> children;
    private boolean endOfWord;
    
    public TrieNode() {
        children = new HashMap<>();
        endOfWord = false;
    }
    
    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public boolean isEndOfWord() {
        return endOfWord;
    }

    public void setEndOfWord(boolean endOfWord) {
        this.endOfWord = endOfWord;
    } 
    
    public boolean isRoot() {
        return children.isEmpty();
    }
}
