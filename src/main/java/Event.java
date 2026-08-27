import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that takes place between two date or time descriptions.
 */
public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy");

    private final LocalDate from;
    private final LocalDate to;

    /**
     * Creates a new event task.
     *
     * @param description description of the event
     * @param from date when the event starts
     * @param to date when the event ends
     */
    public Event(String description, LocalDate from, LocalDate to) {
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
        return super.toString() + " (from: " + from.format(DISPLAY_FORMAT)
                + " to: " + to.format(DISPLAY_FORMAT) + ")";
    }

    /**
     * Returns a representation of this event suitable for saving to a file.
     *
     * @return pipe-separated event data
     */
    @Override
    public String toDataString() {
        return super.toDataString() + " | " + from + " | " + to;
    }
}
