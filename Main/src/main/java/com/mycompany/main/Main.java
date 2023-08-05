/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.main;

import model.Trie;

/**
 *
 * @author Kathy Morales
 */
public class Main {

    public static void main(String[] args) {
        Trie trie = new Trie();

        System.out.println(trie.isEmpty());

        trie.insert("Programming");
        trie.insert("is");
        trie.insert("a");
        trie.insert("way");
        trie.insert("of");
        trie.insert("life");

        System.out.println("");
        System.out.println(trie);
        
        System.out.println("");
        System.out.println(trie.isEmpty()); // Otro metodo para comprobar que el arbol sea vacio
        
        System.out.println("");
        System.out.println(trie.containsNode("3"));
        System.out.println(trie.containsNode("vida"));
        System.out.println(trie.containsNode("life"));

        System.out.println("");
        System.out.println(trie.containsNode("Programming"));
        trie.delete("Programming");
        System.out.println(trie.containsNode("Programming"));
    }
    
}

