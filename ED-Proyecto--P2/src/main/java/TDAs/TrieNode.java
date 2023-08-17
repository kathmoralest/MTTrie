/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TDAs;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kathy Morales
 */
public class TrieNode {
    private Map<Character, TrieNode> children = new HashMap<>();
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
}

