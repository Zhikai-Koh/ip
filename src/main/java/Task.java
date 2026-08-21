/**
 * Represents a task with a description and completion status.
 */
public class Task {
    private final String description;
    private final TaskType type;
    private TaskStatus status;

    /**
     * Creates a new task that is initially not completed.
     *
     * @param description description of the task
     * @param type category of the task
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.type = type;
        this.status = TaskStatus.NOT_DONE;
    }

    /**
     * Returns an icon that represents the task's completion status.
     *
     * @return {@code "[X]"} if the task is completed, or {@code "[ ]"} otherwise
     */
    public String getStatusIcon() {
        return status.getIcon();
    }

    /**
     * Marks this task as completed and returns its updated display text.
     *
     * @return the task's status icon and description
     */
    public String mark() {
        this.status = TaskStatus.DONE;

        return toString();
    }

    /**
     * Marks this task as not completed and returns its updated display text.
     *
     * @return the task's status icon and description
     */
    public String unmark() {
        this.status = TaskStatus.NOT_DONE;

        return toString();
    }

    /**
     * Returns this task's description.
     *
     * @return the task description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the task in the format used when displaying it to the user.
     *
     * @return the task's type icon, status icon, and description
     */
    @Override
    public String toString() {
        return type.getIcon() + getStatusIcon() + " " + description;
    }
}
