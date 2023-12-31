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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import TDAs.Trie;
import java.io.PrintWriter;
import java.util.*;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author H3nry
 */
public class HomeController {

    @FXML
    private Label notFoundLabel;

    @FXML
    private TextField wordInput;

    @FXML
    private Label wordsList;
    @FXML
    private ListView<String> suggestionsListView;

    private Trie trie;

    @FXML
    public void initialize() {
        trie = new Trie();

        suggestionsListView.setVisible(false);
        wordInput.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSuggestions(newValue);
            notFoundLabel.setVisible(false);
        });
        suggestionsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                wordInput.setText(newValue);
            }
        });
    }

    @FXML
    private void addWord(ActionEvent event) {
        String newWord = wordInput.getText().trim();

        if (!newWord.isEmpty()) {
            trie.insert(newWord);
            wordsList.setText(wordsList.getText() + "\n" + newWord);
            wordInput.clear();
            suggestionsListView.getItems().clear(); 
        }
    }

    @FXML
    private void searchWord(ActionEvent event) {
        String searchWord = wordInput.getText().trim();

        if (!searchWord.isEmpty()) {
            boolean wordExistsInWordList = trie.containsNode(searchWord);

            if (wordExistsInWordList) {
                try {
                    String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36";
                    Document document = Jsoup.connect("https://dle.rae.es/" + searchWord)
                            .userAgent(userAgent)
                            .get();

                    Elements elements = document.select(".j");
                    if (!elements.isEmpty()) {
                        StringBuilder resultText = new StringBuilder();
                        for (Element element : elements) {
                            String text = element.text();
                            resultText.append(text).append("\n");
                        }
                        showInfoNotification("Resultados de búsqueda para " + searchWord + ":\n" + resultText.toString());
                    } else {
                        showInfoNotification("La palabra está en el diccionario pero no hay resultados de búsqueda en la web");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                notFoundLabel.setVisible(true); 
            }
        }
    }

    @FXML
    private void deleteWord(ActionEvent event) {
        String wordToDelete = wordInput.getText().trim();

        if (!wordToDelete.isEmpty()) {
            boolean wordExists = trie.containsNode(wordToDelete);

            if (!wordExists) {
                showErrorNotification("La palabra no existe en el diccionario");
                return; 
            }

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmar Eliminación");
            confirmationAlert.setHeaderText("¿Estás seguro de que quieres eliminar esta palabra?");
            confirmationAlert.setContentText("Palabra: " + wordToDelete);

            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                trie.delete(wordToDelete); 

                String currentText = wordsList.getText();
                String newText = currentText.replace("\n" + wordToDelete, "");
                wordsList.setText(newText);
                wordInput.clear();

                if (!currentText.equals(newText)) {
                    showInfoNotification("Palabra eliminada con éxito del diccionario");
                }
            }
        }
    }

    @FXML
    private void saveDictionary(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo de Texto (*.txt)", "*.txt"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                String content = wordsList.getText();
                writer.write(content);
                showInfoNotification("Diccionario guardado exitosamente");
            } catch (IOException e) {
                showErrorNotification("Error al guardar el diccionario");
                e.printStackTrace();
            }
        }
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
        String dotFilePath = "resources/graphImage/files/trie.dot"; 
        trie.generateDotFile(dotFilePath);

        String dotPath = "resources/graphImage/dot.exe"; 
        String inputDotFile = dotFilePath;
        String outputImageFile = "resources/graphImage/trie.png"; 

        try {
            Process process = Runtime.getRuntime().exec(dotPath + " -Tpng " + inputDotFile + " -o " + outputImageFile);
            process.waitFor();
            System.out.println("Imagen generada: " + outputImageFile);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Árbol Trie");
        ImageView imageView = new ImageView(new Image("file:" + outputImageFile));
        imageView.setPreserveRatio(true);

        ScrollPane scrollPane = new ScrollPane(imageView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Scene scene = new Scene(scrollPane, 800, 600); 
        stage.setScene(scene);
        stage.show();
    }

    private void updateSuggestions(String prefix) {
        suggestionsListView.getItems().clear();
        if (!prefix.isEmpty() && !prefix.trim().isEmpty()) {
            List<String> suggestions = trie.getSuggestionsWithPrefix(prefix);
            suggestionsListView.getItems().addAll(suggestions);
            suggestionsListView.setVisible(true);
            notFoundLabel.setVisible(false);
        } else {
            suggestionsListView.setVisible(false);
            notFoundLabel.setVisible(false);
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
