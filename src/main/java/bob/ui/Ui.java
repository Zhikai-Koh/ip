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
     * Displays Bob's goodbye message.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Displays all tasks with one-based numbering.
     *
     * @param tasks tasks to display
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays tasks whose descriptions match a search keyword.
     *
     * @param tasks matching tasks to display
     */
    public void showMatchingTasks(List<Task> tasks) {
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays confirmation that a task was marked as done.
     *
     * @param taskDisplay updated task display text
     */
    public void showTaskMarked(String taskDisplay) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + taskDisplay);
    }

    /**
     * Displays confirmation that a task was marked as not done.
     *
     * @param taskDisplay updated task display text
     */
    public void showTaskUnmarked(String taskDisplay) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + taskDisplay);
    }

    /**
     * Displays confirmation that a task was removed.
     *
     * @param task removed task
     * @param taskCount number of remaining tasks
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays confirmation that a task was added.
     *
     * @param task added task
     * @param taskCount number of stored tasks
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays an error message.
     *
     * @param message explanation of the error
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays an error encountered while loading tasks.
     *
     * @param message explanation of the loading error
     */
    public void showLoadingError(String message) {
        System.out.println("I couldn't load your saved tasks: " + message);
    }

    /**
     * Displays an error encountered while saving tasks.
     */
    public void showSavingError() {
        System.out.println("I couldn't save your tasks. Please try again.");
    }
}
