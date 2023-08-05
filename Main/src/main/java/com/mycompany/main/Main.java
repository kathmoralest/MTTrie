/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.main;

import com.mycompany.model.Trie;

/**
 *
 * @author Kathy Morales
 */
public class Main {

    public static void main(String[] args) {
        Trie trie = new Trie();
        
        System.out.println("trie.isEmpty(): "+trie.isEmpty());
        System.out.println("\nINSERTAR PALABRA"); // Modificar impresion
        trie.insert("Tres");
        trie.insert("tristes");
        trie.insert("tigres");
        trie.insert("tragan");
        trie.insert("trigo");
        trie.insert("en");
        trie.insert("un");
        trie.insert("trigal");

        System.out.println("");
        System.out.println("trie: "+trie);
        
        System.out.println("");
        System.out.println("trie.isEmpty(): "+trie.isEmpty()); // Otro metodo para comprobar que el arbol sea vacio
        
        System.out.println("\nBUSCAR PALABRA");
        System.out.println("trie.containsNode(\"7\"): "+trie.containsNode("7"));
        System.out.println("trie.containsNode(\"hola\"): "+trie.containsNode("hola"));
        System.out.println("trie.containsNode(\"tigres\"): "+trie.containsNode("tigres"));

        System.out.println("\nELIMINAR");
        System.out.println("trie.containsNode(\"Tres\"): "+trie.containsNode("Tres"));
        trie.delete("Tres"); // Validar que no elimine si no existe la palabra
        System.out.println("trie.containsNode(\"Tres\"): "+trie.containsNode("Tres"));
    }
    
}

