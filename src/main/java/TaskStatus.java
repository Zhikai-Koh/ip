/**
 * Identifies whether a task is completed and provides its display icon.
 */
public enum TaskStatus {
    NOT_DONE("[ ]"),
    DONE("[X]");

    private final String icon;

    /**
     * Creates a task status with its display icon.
     *
     * @param icon icon used when displaying this status
     */
    TaskStatus(String icon) {
        this.icon = icon;
    }

    /**
     * Returns this status's display icon.
     *
     * @return task status icon
     */
    public String getIcon() {
        return icon;
    }
}
