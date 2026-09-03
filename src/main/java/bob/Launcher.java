package bob;

import javafx.application.Application;

/**
 * Launches Bob through a regular Java entry point so the packaged JAR starts reliably.
 */
public final class Launcher {
    private Launcher() {
    }

    /**
     * Starts the JavaFX application.
     *
     * @param args command-line arguments passed to JavaFX
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
