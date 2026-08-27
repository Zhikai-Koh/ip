package bob;

import java.io.IOException;

import bob.exception.BobException;
import bob.parser.Parser;
import bob.storage.Storage;
import bob.task.Task;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * Starts the Bob chatbot application.
 */
public class Bob {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates Bob and loads tasks from the given storage file.
     *
     * @param filePath path of the file used to store tasks
     */
    public Bob(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.loadTasks());
        } catch (IOException | BobException e) {
            ui.showLoadingError(e.getMessage());
            loadedTasks = new TaskList();
        }
        tasks = loadedTasks;
    }

    /**
     * Runs Bob's command loop until the user enters {@code bye}.
     */
    public void run() {
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
                    ui.showTaskList(tasks.getTasks());
                } else if (Parser.isCommand(command, "mark")) {
                    int itemId = Parser.parseTaskNumber(command, "mark", tasks.size());
                    String marked = tasks.mark(itemId);
                    storage.saveTasks(tasks.getTasks());

                    ui.showTaskMarked(marked);
                } else if (Parser.isCommand(command, "unmark")) {
                    int itemId = Parser.parseTaskNumber(command, "unmark", tasks.size());
                    String marked = tasks.unmark(itemId);
                    storage.saveTasks(tasks.getTasks());

                    ui.showTaskUnmarked(marked);
                } else if (Parser.isCommand(command, "delete")) {
                    int itemId = Parser.parseTaskNumber(command, "delete", tasks.size());
                    Task removedTask = tasks.delete(itemId);
                    storage.saveTasks(tasks.getTasks());

                    ui.showTaskDeleted(removedTask, tasks.size());
                } else if (Parser.isCommand(command, "todo")) {
                    Task task = Parser.parseTodo(command);
                    addTask(task);
                } else if (Parser.isCommand(command, "deadline")) {
                    Task task = Parser.parseDeadline(command);
                    addTask(task);
                } else if (Parser.isCommand(command, "event")) {
                    Task task = Parser.parseEvent(command);
                    addTask(task);
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
     * @param task task to add
     * @throws IOException if the task list cannot be saved
     */
    private void addTask(Task task) throws IOException {
        tasks.add(task);
        storage.saveTasks(tasks.getTasks());
        ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Starts Bob using the default task storage path.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        new Bob("./data/bob.txt").run();
    }
}
