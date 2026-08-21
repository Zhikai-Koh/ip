/**
 * Represents a task that must be completed by a given date or time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates a new deadline task.
     *
     * @param description description of the task
     * @param by deadline entered by the user
     */
    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    /**
     * Returns the deadline task in its display format.
     *
     * @return the task type, status, description, and deadline
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
