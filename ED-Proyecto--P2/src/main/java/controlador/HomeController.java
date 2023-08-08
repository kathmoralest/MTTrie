package controlador;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import TDAs.Trie;
import TDAs.TrieNode;
import java.util.Map;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.view.Viewer;

/**
 *
 * @author H3nry
 */
public class HomeController {

    @FXML
    private Label msgLabel;

    @FXML
    private ImageView newDic;

    @FXML
    private ImageView saveDic;

    @FXML
    private ImageView uplDic;

    @FXML
    private TextField wordInput;

    @FXML
    private Label wordsList;

    private Trie trie;

    @FXML
    public void initialize() {
        trie = new Trie();
    }

    @FXML
    private void createDictionary(MouseEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Crear Diccionario");
        stage.setResizable(false);
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        Label label = new Label("Nombre del diccionario");
        TextField textField = new TextField();
        Button button = new Button("Crear Diccionario");

        vbox.getChildren().addAll(label, textField, button);

        button.setOnAction(e -> {
            String dictionaryName = textField.getText().trim();
            if (dictionaryName.isEmpty()) {
                showErrorNotification("No se admiten nombres de diccionarios vacíos");
            } else {
                if (createDictionaryFile(dictionaryName)) {
                    showInfoNotification("El archivo de diccionario fue creado exitosamente");
                    stage.close();
                } else {
                    showErrorNotification("El archivo de diccionario ya existe");
                }
            }
        });

        Scene scene = new Scene(vbox, 300, 100);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private boolean createDictionaryFile(String dictionaryName) {
        String path = "resources/dictionaries/" + dictionaryName + ".txt";
        try {
            Files.createDirectories(Paths.get("resources/dictionaries/"));
            File file = new File(path);
            if (file.createNewFile()) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            showErrorNotification("Un error ocurrió al crear el archivo de diccionario");
            e.printStackTrace();
            return false;
        }

    }

    @FXML
    private void uploadDictionary(MouseEvent event) {
        wordsList.setText("");
        trie.clean();

        Stage stage = new Stage();

        VBox vbox = new VBox();
        Label label = new Label("Selecciona el diccionario a cargar");
        vbox.getChildren().add(label);
        ToggleGroup group = new ToggleGroup();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        try {
            Files.list(Paths.get("resources/dictionaries/"))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .forEach(path -> {
                        RadioButton radioButton = new RadioButton(path.getFileName().toString());
                        radioButton.setToggleGroup(group);
                        vbox.getChildren().add(radioButton);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button loadButton = new Button("Cargar diccionario");
        loadButton.setOnAction(e -> {
            if (group.getSelectedToggle() != null) {
                File file = new File("resources/dictionaries/" + ((RadioButton) group.getSelectedToggle()).getText());
                try (Stream<String> stream = Files.lines(Paths.get(file.getPath()))) {
                    stream.forEach(word -> {
                        trie.insert(word);
                        wordsList.setText(wordsList.getText() + "\n" + word);
                    });
                    showInfoNotification("Diccionario cargado con éxito");
                    stage.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        vbox.getChildren().add(loadButton);

        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void showTree(MouseEvent event) {
        // Crear un nuevo Stage
        Stage stage = new Stage();
        System.setProperty("org.graphstream.ui", "javafx");
        // Crear el grafo
        Graph graph = new SingleGraph("Trie");
        graph.setStrict(false);
        graph.setAutoCreate(true);
        showTrie(trie.getRoot(), "", graph);

        // Crear la vista de GraphStream en el FX EDT
        Viewer viewer = graph.display(false);
        viewer.enableAutoLayout();

        FxViewPanel viewPanel = (FxViewPanel) viewer.addDefaultView(false);

        StackPane pane = new StackPane();
        pane.getChildren().add(viewPanel);

        stage.setScene(new Scene(pane));
        stage.show();
    }

    private void showTrie(TrieNode node, String prefix, Graph graph) {
        if (node.isEndOfWord()) {
            graph.addNode(prefix).setAttribute("ui.label", prefix);
        }

        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            char ch = entry.getKey();
            TrieNode child = entry.getValue();
            String newPrefix = prefix + ch;
            graph.addEdge(prefix + ch, prefix, newPrefix, true);
            showTrie(child, newPrefix, graph);
        }
    }

    private void showInfoNotification(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorNotification(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
