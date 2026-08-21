/**
 * Represents a task without a specific date or time.
 */
public class Todo extends Task {
    /**
     * Creates a new todo task.
     *
     * @param description description of the task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the todo task in its display format.
     *
     * @return the task type, status, and description
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
