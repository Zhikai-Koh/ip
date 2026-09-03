package bob.ui;

import java.util.List;
import java.util.Scanner;

import bob.task.Task;

/**
 * Handles all interactions between Bob and the user.
 */
public class Ui {
    private static final String SEPARATOR =
            "____________________________________________________________";
    private static final String BANNER = " ____        _     \n"
            + "| __ )  ___ | |__  \n"
            + "|  _ \\ / _ \\| '_ \\ \n"
            + "| |_) | (_) | |_) |\n"
            + "|____/ \\___/|_.__/ \n";

    private final Scanner scanner;

    /**
     * Creates a UI that reads commands from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays Bob's welcome message.
     */
    public void showWelcome() {
        System.out.println(SEPARATOR + "\n" + BANNER
                + "Hello! I'm Bob.\nWhat can I do for you?\n" + SEPARATOR);
    }

    /**
     * Returns Bob's greeting without console-specific decoration.
     *
     * @return greeting for a graphical UI
     */
    public String getWelcomeMessage() {
        return "Hello! I'm Bob.\nWhat can I do for you?";
    }

    /**
     * Checks whether another command is available.
     *
     * @return true if another line can be read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return full command entered by the user
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the horizontal separator used between responses.
     */
    public void showLine() {
        System.out.println(SEPARATOR);
    }

    /**
     * Displays a response in the console.
     *
     * @param message response to display
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Creates Bob's goodbye message.
     *
     * @return goodbye response
     */
    public String getGoodbyeMessage() {
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Creates a list of all tasks with one-based numbering.
     *
     * @param tasks tasks to display
     * @return formatted task-list response
     */
    public String getTaskListMessage(List<Task> tasks) {
        StringBuilder message = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            message.append("\n").append(i + 1).append(".").append(tasks.get(i));
        }
        return message.toString();
    }

    /**
     * Creates a list of tasks whose descriptions match a search keyword.
     *
     * @param tasks matching tasks to display
     * @return formatted matching-task response
     */
    public String getMatchingTasksMessage(List<Task> tasks) {
        StringBuilder message = new StringBuilder("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            message.append("\n").append(i + 1).append(".").append(tasks.get(i));
        }
        return message.toString();
    }

    /**
     * Creates confirmation that a task was marked as done.
     *
     * @param taskDisplay updated task display text
     * @return task-marked response
     */
    public String getTaskMarkedMessage(String taskDisplay) {
        return "Nice! I've marked this task as done:\n  " + taskDisplay;
    }

    /**
     * Creates confirmation that a task was marked as not done.
     *
     * @param taskDisplay updated task display text
     * @return task-unmarked response
     */
    public String getTaskUnmarkedMessage(String taskDisplay) {
        return "OK, I've marked this task as not done yet:\n  " + taskDisplay;
    }

    /**
     * Creates confirmation that a task was removed.
     *
     * @param task removed task
     * @param taskCount number of remaining tasks
     * @return task-deleted response
     */
    public String getTaskDeletedMessage(Task task, int taskCount) {
        return "Noted. I've removed this task:\n  " + task
                + "\nNow you have " + taskCount + " tasks in the list.";
    }

    /**
     * Creates confirmation that a task was added.
     *
     * @param task added task
     * @param taskCount number of stored tasks
     * @return task-added response
     */
    public String getTaskAddedMessage(Task task, int taskCount) {
        return "Got it. I've added this task:\n  " + task
                + "\nNow you have " + taskCount + " tasks in the list.";
    }

    /**
     * Creates an error encountered while loading tasks.
     *
     * @param message explanation of the loading error
     * @return loading-error response
     */
    public String getLoadingErrorMessage(String message) {
        return "I couldn't load your saved tasks: " + message;
    }

    /**
     * Creates an error encountered while saving tasks.
     *
     * @return saving-error response
     */
    public String getSavingErrorMessage() {
        return "I couldn't save your tasks. Please try again.";
    }
}
