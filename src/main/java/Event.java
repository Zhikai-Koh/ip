/**
 * Represents a task that takes place between two date or time descriptions.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates a new event task.
     *
     * @param description description of the event
     * @param from start entered by the user
     * @param to end entered by the user
     */
    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event task in its display format.
     *
     * @return the task type, status, description, start, and end
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
