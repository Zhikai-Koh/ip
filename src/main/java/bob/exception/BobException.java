package bob.exception;

/**
 * Represents an error caused by a command that Bob cannot process.
 */
public class BobException extends Exception {
    /**
     * Creates an exception with an explanation that can be shown to the user.
     *
     * @param message explanation of the command error
     */
    public BobException(String message) {
        super(message);
    }
}
