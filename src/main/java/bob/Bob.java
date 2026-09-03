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
    private final String loadingError;
    private boolean isExit;

    /**
     * Creates Bob and loads tasks from the given storage file.
     *
     * @param filePath path of the file used to store tasks
     */
    public Bob(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        TaskList loadedTasks;
        String loadError = null;
        try {
            loadedTasks = new TaskList(storage.loadTasks());
        } catch (IOException | BobException e) {
            loadError = e.getMessage();
            loadedTasks = new TaskList();
        }
        tasks = loadedTasks;
        loadingError = loadError;
    }

    /**
     * Runs Bob's command loop until the user enters {@code bye}.
     */
    public void run() {
        ui.showWelcome();
        if (loadingError != null) {
            ui.showMessage(ui.getLoadingErrorMessage(loadingError));
        }

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showLine();
            ui.showMessage(getResponse(command));
            ui.showLine();
            if (isExit) {
                break;
            }
        }
    }

    /**
     * Executes one command and returns the response for either a console or graphical UI.
     *
     * @param command command entered by the user
     * @return response to display
     */
    public String getResponse(String command) {
        String input = command.trim();
        try {
            if (input.equals("bye")) {
                isExit = true;
                return ui.getGoodbyeMessage();
            } else if (input.equals("list")) {
                return ui.getTaskListMessage(tasks.getTasks());
            } else if (Parser.isCommand(input, "find")) {
                String keyword = Parser.parseFindKeyword(input);
                return ui.getMatchingTasksMessage(tasks.find(keyword));
            } else if (Parser.isCommand(input, "mark")) {
                int itemId = Parser.parseTaskNumber(input, "mark", tasks.size());
                String marked = tasks.mark(itemId);
                storage.saveTasks(tasks.getTasks());
                return ui.getTaskMarkedMessage(marked);
            } else if (Parser.isCommand(input, "unmark")) {
                int itemId = Parser.parseTaskNumber(input, "unmark", tasks.size());
                String marked = tasks.unmark(itemId);
                storage.saveTasks(tasks.getTasks());
                return ui.getTaskUnmarkedMessage(marked);
            } else if (Parser.isCommand(input, "delete")) {
                int itemId = Parser.parseTaskNumber(input, "delete", tasks.size());
                Task removedTask = tasks.delete(itemId);
                storage.saveTasks(tasks.getTasks());
                return ui.getTaskDeletedMessage(removedTask, tasks.size());
            } else if (Parser.isCommand(input, "todo")) {
                return addTask(Parser.parseTodo(input));
            } else if (Parser.isCommand(input, "deadline")) {
                return addTask(Parser.parseDeadline(input));
            } else if (Parser.isCommand(input, "event")) {
                return addTask(Parser.parseEvent(input));
            }
            throw new BobException("I couldn't match that to a command. "
                    + "Try todo, deadline, event, list, find, mark, unmark, delete, or bye.");
        } catch (BobException e) {
            return e.getMessage();
        } catch (IOException e) {
            return ui.getSavingErrorMessage();
        }
    }

    /**
     * Returns the greeting and any problem encountered while loading saved tasks.
     *
     * @return startup message for a graphical UI
     */
    public String getWelcomeMessage() {
        String message = ui.getWelcomeMessage();
        if (loadingError != null) {
            message += "\n\n" + ui.getLoadingErrorMessage(loadingError);
        }
        return message;
    }

    /**
     * Checks whether the user has entered the exit command.
     *
     * @return true after processing {@code bye}
     */
    public boolean isExit() {
        return isExit;
    }

    /**
     * Stores a task and creates its confirmation response.
     *
     * @param task task to add
     * @return confirmation to display
     * @throws IOException if the task list cannot be saved
     */
    private String addTask(Task task) throws IOException {
        tasks.add(task);
        storage.saveTasks(tasks.getTasks());
        return ui.getTaskAddedMessage(task, tasks.size());
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
