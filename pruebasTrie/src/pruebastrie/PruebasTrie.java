/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pruebastrie;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Kathy Morales
 */
public class PruebasTrie {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String dictionaryFile = "src/files/dictionary.txt";
        Trie trie = new Trie();

        // Cargar el diccionario desde el archivo
        trie.loadFromFile(dictionaryFile);
        
        trie.printTrie();
        
        // Insertar palabra en el Trie       
        trie.insert("batman");
        
        // Guardar el diccionario en el archivo
        trie.saveToFile(dictionaryFile);
        
        // Buscar palabras en el Trie     
        System.out.println(trie.search("apple"));   // true
        System.out.println(trie.search("app"));     // true
        System.out.println(trie.search("banana"));  // true
        System.out.println(trie.search("bat"));     // true
        System.out.println(trie.search("ball"));    // false
        
        // Imprimir palabras de el Trie
        trie.printTrie();
        
        // Crear archivo dot para generar imagen de el Trie
        String dotFilePath = "src/files/trie.dot";
        String dotPath = "C:\\Program Files\\Graphviz\\bin\\dot.exe";  // Ruta al ejecutable 'dot' de Graphviz
        trie.generateDotFile(dotFilePath);        
        String inputDotFile = dotFilePath;
        String outputImageFile = "src/images/trie.png";
        try {
            Process process = Runtime.getRuntime().exec(dotPath + " -Tpng " + inputDotFile + " -o " + outputImageFile);
            process.waitFor();
            System.out.println("Imagen generada: " + outputImageFile);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        
        

        // Eliminar una palabra
        String wordToDelete = "apple";
        trie.delete(wordToDelete);

        // Después de la eliminación
        System.out.println("Después de la eliminación de '" + wordToDelete + "':");
        trie.printTrie();
        
        // Insertar palabras ingresadas por el usuario
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese una palabra (o 'salir' para detenerse): ");
        String input = scanner.nextLine();
        while (!input.equalsIgnoreCase("salir")) {
            trie.insert(input);
            System.out.print("Ingrese otra palabra (o 'salir' para detenerse): ");
            input = scanner.nextLine();
        }
        
        System.out.print("Ingrese una palabra para buscar: ");
        String searchWord = scanner.nextLine();

        if (trie.search(searchWord)) {
            System.out.println("La palabra '" + searchWord + "' está presente en el Trie.");
        } else {
            System.out.println("La palabra '" + searchWord + "' no está presente en el Trie.");
        }
        
        String prefix = "ba";
        List<String> suggestions = trie.autocomplete(prefix);

        System.out.println("Sugerencias de autocompletado para '" + prefix + "':");
        for (String suggestion : suggestions) {
            System.out.println(suggestion);
        }
        
    }
    
}
