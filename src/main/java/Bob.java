import java.io.IOException;
import java.util.ArrayList;

/**
 * Starts the Bob chatbot application.
 */

public class Bob {
    /**
     * Runs Bob's command loop until the user enters {@code bye}.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage("./data/bob.txt");
        ArrayList<Task> tasks;
        try {
            tasks = storage.loadTasks();
        } catch (IOException | BobException e) {
            ui.showLoadingError(e.getMessage());
            tasks = new ArrayList<>();
        }

        ui.showWelcome();

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showLine();
            try {
                if (command.equals("bye")) {
                    ui.showGoodbye();
                    ui.showLine();
                    break;
                } else if (command.equals("list")) {
                    ui.showTaskList(tasks);
                } else if (Parser.isCommand(command, "mark")) {
                    int itemID = Parser.parseTaskNumber(command, "mark", tasks.size());
                    String marked = tasks.get(itemID - 1).mark();
                    storage.saveTasks(tasks);

                    ui.showTaskMarked(marked);
                } else if (Parser.isCommand(command, "unmark")) {
                    int itemID = Parser.parseTaskNumber(command, "unmark", tasks.size());
                    String marked = tasks.get(itemID - 1).unmark();
                    storage.saveTasks(tasks);

                    ui.showTaskUnmarked(marked);
                } else if (Parser.isCommand(command, "delete")) {
                    int itemID = Parser.parseTaskNumber(command, "delete", tasks.size());
                    Task removedTask = tasks.remove(itemID - 1);
                    storage.saveTasks(tasks);

                    ui.showTaskDeleted(removedTask, tasks.size());
                } else if (Parser.isCommand(command, "todo")) {
                    Task task = Parser.parseTodo(command);
                    addTask(tasks, task, storage, ui);
                } else if (Parser.isCommand(command, "deadline")) {
                    Task task = Parser.parseDeadline(command);
                    addTask(tasks, task, storage, ui);
                } else if (Parser.isCommand(command, "event")) {
                    Task task = Parser.parseEvent(command);
                    addTask(tasks, task, storage, ui);
                } else {
                    throw new BobException("I couldn't match that to a command. "
                            + "Try todo, deadline, event, list, mark, unmark, delete, or bye.");
                }
            } catch (BobException e) {
                ui.showError(e.getMessage());
            } catch (IOException e) {
                ui.showSavingError();
            }
            ui.showLine();
        }
    }

    /**
     * Stores a task and displays confirmation.
     *
     * @param tasks list in which tasks are stored
     * @param task task to add
     * @param storage storage used to save the updated task list
     * @param ui user interface used to display confirmation
     * @throws IOException if the task list cannot be saved
     */
    private static void addTask(ArrayList<Task> tasks, Task task, Storage storage, Ui ui) throws IOException {
        tasks.add(task);
        storage.saveTasks(tasks);
        ui.showTaskAdded(task, tasks.size());
    }
}
