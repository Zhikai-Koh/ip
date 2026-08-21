/**
 * Identifies the supported task categories and their display icons.
 */
public enum TaskType {
    TODO("[T]"),
    DEADLINE("[D]"),
    EVENT("[E]");

    private final String icon;

    /**
     * Creates a task type with its display icon.
     *
     * @param icon icon used when displaying this task type
     */
    TaskType(String icon) {
        this.icon = icon;
    }

    /**
     * Returns this task type's display icon.
     *
     * @return task type icon
     */
    public String getIcon() {
        return icon;
    }
}
