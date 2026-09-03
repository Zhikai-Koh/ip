package bob.ui;

import bob.Bob;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controls Bob's main chat window.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private Bob bob;

    /**
     * Keeps the newest messages visible as the conversation grows.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Connects the view to Bob and displays his greeting.
     *
     * @param bob chatbot that processes commands
     */
    public void setBob(Bob bob) {
        this.bob = bob;
        dialogContainer.getChildren().add(DialogBox.getBobDialog(bob.getWelcomeMessage()));
        userInput.requestFocus();
    }

    /**
     * Sends the current input to Bob and displays both sides of the exchange.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty() || bob == null) {
            return;
        }

        String response = bob.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input),
                DialogBox.getBobDialog(response));
        userInput.clear();

        if (bob.isExit()) {
            userInput.setDisable(true);
            userInput.setPromptText("Bob has signed off");
            sendButton.setDisable(true);
        }
    }
}
