package bob;

import java.io.IOException;

import bob.ui.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Loads and displays Bob's JavaFX interface.
 */
public class Main extends Application {
    private static final String STORAGE_PATH = "./data/bob.txt";

    /**
     * Creates Bob's main window and connects it to the chatbot.
     *
     * @param stage primary stage supplied by JavaFX
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane mainLayout = loader.load();
            loader.<MainWindow>getController().setBob(new Bob(STORAGE_PATH));

            stage.setTitle("Bob");
            stage.setMinHeight(500.0);
            stage.setMinWidth(440.0);
            stage.setScene(new Scene(mainLayout));
            stage.show();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load Bob's main window.", e);
        }
    }
}
