/**
 * Represents a task with a description and completion status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a new task that is initially not completed.
     *
     * @param description description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns an icon that represents the task's completion status.
     *
     * @return {@code "[X]"} if the task is completed, or {@code "[ ]"} otherwise
     */
    public String getStatusIcon() {
        return (isDone ? "[X]" : "[ ]"); // mark done task with X
    }

    /**
     * Marks this task as completed and returns its updated display text.
     *
     * @return the task's status icon and description
     */
    public String mark() {
        this.isDone = true;

        return getStatusIcon() + " " + this.description;
    }

    /**
     * Marks this task as not completed and returns its updated display text.
     *
     * @return the task's status icon and description
     */
    public  String unmark(){
        this.isDone = false;

        return getStatusIcon() + " " + this.description;
    }

    /**
     * Returns this task's description.
     *
     * @return the task description
     */
    public String getDescription(){
        return this.description;
    }
}
