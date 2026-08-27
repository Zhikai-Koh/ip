package bob.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import bob.exception.BobException;
import bob.task.Deadline;
import bob.task.Event;
import bob.task.Task;
import bob.task.Todo;

/**
 * Interprets user commands and converts their arguments into application data.
 */
public final class Parser {
    private Parser() {
    }

    /**
     * Checks whether input contains the given command, with or without arguments.
     *
     * @param input full user input
     * @param command command word to check
     * @return true if the input begins with the complete command word
     */
    public static boolean isCommand(String input, String command) {
        return input.equals(command) || input.startsWith(command + " ");
    }

    /**
     * Extracts and validates a task number from a command.
     *
     * @param input full user input
     * @param command command whose argument is being parsed
     * @param taskCount number of tasks currently stored
     * @return a valid one-based task number
     * @throws BobException if the number is missing, invalid, or outside the task list
     */
    public static int parseTaskNumber(String input, String command, int taskCount) throws BobException {
        String numberText = input.substring(command.length()).trim();
        if (numberText.isEmpty()) {
            throw new BobException("Tell me which task to update, for example: " + command + " 2.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            throw new BobException("Task numbers must be whole numbers, such as 1 or 2.");
        }

        if (taskCount == 0) {
            throw new BobException("Your task list is empty, so there is nothing to " + command + ".");
        }
        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new BobException("I couldn't find that task. Choose a number between 1 and "
                    + taskCount + ".");
        }
        return taskNumber;
    }

    /**
     * Creates a todo task from user input.
     *
     * @param input full todo command
     * @return parsed todo task
     * @throws BobException if the description is empty
     */
    public static Task parseTodo(String input) throws BobException {
        String description = input.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new BobException("A todo needs something to do. Add a description after todo.");
        }
        return new Todo(description);
    }

    /**
     * Creates a deadline task from user input.
     *
     * @param input full deadline command
     * @return parsed deadline task
     * @throws BobException if its description or deadline is invalid
     */
    public static Task parseDeadline(String input) throws BobException {
        String details = input.substring("deadline".length()).trim();
        int byPosition = details.indexOf("/by");
        if (byPosition < 0) {
            throw new BobException("This deadline is missing its due date. Add it using /by, "
                    + "for example: deadline return book /by Sunday.");
        }

        String description = details.substring(0, byPosition).trim();
        String byText = details.substring(byPosition + "/by".length()).trim();
        if (description.isEmpty()) {
            throw new BobException("Tell me what the deadline is for before adding /by.");
        }
        if (byText.isEmpty()) {
            throw new BobException("The /by field cannot be empty. Tell me when this task is due.");
        }
        return new Deadline(description, parseDate(byText));
    }

    /**
     * Creates an event task from user input.
     *
     * @param input full event command
     * @return parsed event task
     * @throws BobException if its description, start date, or end date is invalid
     */
    public static Task parseEvent(String input) throws BobException {
        String details = input.substring("event".length()).trim();
        int fromPosition = details.indexOf("/from");
        if (fromPosition < 0) {
            throw new BobException("This event needs a starting time. Add one using /from.");
        }

        String description = details.substring(0, fromPosition).trim();
        String schedule = details.substring(fromPosition + "/from".length()).trim();
        int toPosition = schedule.indexOf("/to");
        if (description.isEmpty()) {
            throw new BobException("Tell me what the event is before adding its time.");
        }
        if (toPosition < 0) {
            throw new BobException("This event needs an ending time. Add one using /to.");
        }

        String fromText = schedule.substring(0, toPosition).trim();
        String toText = schedule.substring(toPosition + "/to".length()).trim();
        if (fromText.isEmpty()) {
            throw new BobException("The /from field cannot be empty. Tell me when the event starts.");
        }
        if (toText.isEmpty()) {
            throw new BobException("The /to field cannot be empty. Tell me when the event ends.");
        }
        return new Event(description, parseDate(fromText), parseDate(toText));
    }

    /**
     * Parses a date in the {@code yyyy-MM-dd} format used by task commands.
     *
     * @param dateText date entered by the user
     * @return parsed date
     * @throws BobException if the text is not a valid date in the required format
     */
    private static LocalDate parseDate(String dateText) throws BobException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            throw new BobException("I couldn't understand that date. Use yyyy-MM-dd, "
                    + "for example: 2019-12-02.");
        }
    }
}
