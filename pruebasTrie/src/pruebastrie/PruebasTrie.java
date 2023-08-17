/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pruebastrie;

import java.io.IOException;
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
        
        Trie trie = new Trie();
        
        String dictionaryFile = "src/files/dictionary.txt";

        // Cargar el diccionario desde el archivo
        trie.loadFromFile(dictionaryFile);

        // Insertar palabras ingresadas por el usuario
        // (asegúrate de mantener las palabras en memoria antes de guardar)        
        trie.insert("batman");
        
        // Guardar el diccionario en el archivo
        trie.saveToFile(dictionaryFile);
        
        // Buscar palabras en el Trie
        System.out.println(trie.search("apple"));   // true
        System.out.println(trie.search("app"));     // true
        System.out.println(trie.search("banana"));  // true
        System.out.println(trie.search("bat"));     // true
        System.out.println(trie.search("ball"));    // false
        
        trie.printTrie();
        
        String dotFilePath = "src/files/trie.dot";
        trie.generateDotFile(dotFilePath);
        
        String dotPath = "C:\\Program Files\\Graphviz\\bin\\dot.exe";  // Ruta al ejecutable 'dot' de Graphviz
        String inputDotFile = dotFilePath;
        String outputImageFile = "src/images/trie.png";

        try {
            Process process = Runtime.getRuntime().exec(dotPath + " -Tpng " + inputDotFile + " -o " + outputImageFile);
            process.waitFor();
            System.out.println("Imagen generada: " + outputImageFile);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        
        // Antes de la eliminación
        System.out.println("Antes de la eliminación:");
        trie.printTrie();

        // Eliminar una palabra
        String wordToDelete = "apple";
        trie.delete(wordToDelete);

        // Después de la eliminación
        System.out.println("Después de la eliminación de '" + wordToDelete + "':");
        trie.printTrie();
        
        String dotFilePath_e = "src/files/trie_e.dot";
        trie.generateDotFile(dotFilePath_e);
        
        String dotPath_e = "C:\\Program Files\\Graphviz\\bin\\dot.exe";  // Ruta al ejecutable 'dot' de Graphviz
        String inputDotFile_e = dotFilePath_e;
        String outputImageFile_e = "src/images/trie_e.png";

        try {
            Process process = Runtime.getRuntime().exec(dotPath_e + " -Tpng " + inputDotFile_e + " -o " + outputImageFile_e);
            process.waitFor();
            System.out.println("Imagen generada: " + outputImageFile_e);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        
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
     /**
        

        }*/
        
    }
    
}
