import java.util.Scanner;

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
        Task[] database = new Task[100];
        int currCount = 0;

        String separator = "____________________________________________________________";
        String banner = " ____        _     \n"
                + "| __ )  ___ | |__  \n"
                + "|  _ \\ / _ \\| '_ \\ \n"
                + "| |_) | (_) | |_) |\n"
                + "|____/ \\___/|_.__/ \n";
        String greetings = "Hello! I'm Bob.\n"
                + "What can I do for you?\n";
        String goodbyes = "Bye. Hope to see you again soon!\n";

        System.out.println(separator + "\n" + banner + greetings + separator);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(separator);
            try {
                if (command.equals("bye")) {
                    System.out.print(goodbyes);
                    System.out.println(separator);
                    break;
                } else if (command.equals("list")) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < currCount; i++) {
                        System.out.println((i + 1) + "." + database[i]);
                    }
                } else if (isCommand(command, "mark")) {
                    int itemID = parseTaskNumber(command, "mark", currCount);
                    String marked = database[itemID - 1].mark();

                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + marked);
                } else if (isCommand(command, "unmark")) {
                    int itemID = parseTaskNumber(command, "unmark", currCount);
                    String marked = database[itemID - 1].unmark();

                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + marked);
                } else if (isCommand(command, "todo")) {
                    Task task = parseTodo(command);
                    currCount = addTask(database, currCount, task);
                } else if (isCommand(command, "deadline")) {
                    Task task = parseDeadline(command);
                    currCount = addTask(database, currCount, task);
                } else if (isCommand(command, "event")) {
                    Task task = parseEvent(command);
                    currCount = addTask(database, currCount, task);
                } else {
                    throw new BobException("I couldn't match that to a command. "
                            + "Try todo, deadline, event, list, mark, unmark, or bye.");
                }
            } catch (BobException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(separator);
        }
    }

    /**
     * Checks whether input contains the given command, with or without arguments.
     *
     * @param input full user input
     * @param command command word to check
     * @return true if the input begins with the complete command word
     */
    private static boolean isCommand(String input, String command) {
        return input.equals(command) || input.startsWith(command + " ");
    }

    /**
     * Extracts and validates the task number supplied to mark or unmark.
     *
     * @param input full user input
     * @param command command whose argument is being parsed
     * @param taskCount number of tasks currently stored
     * @return a valid one-based task number
     * @throws BobException if the number is missing, invalid, or outside the task list
     */
    private static int parseTaskNumber(String input, String command, int taskCount) throws BobException {
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
     * @return the parsed todo task
     * @throws BobException if the description is empty
     */
    private static Task parseTodo(String input) throws BobException {
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
     * @return the parsed deadline task
     * @throws BobException if its description or deadline is missing
     */
    private static Task parseDeadline(String input) throws BobException {
        String details = input.substring("deadline".length()).trim();
        int byPosition = details.indexOf("/by");
        if (byPosition < 0) {
            throw new BobException("This deadline is missing its due date. Add it using /by, "
                    + "for example: deadline return book /by Sunday.");
        }

        String description = details.substring(0, byPosition).trim();
        String by = details.substring(byPosition + "/by".length()).trim();
        if (description.isEmpty()) {
            throw new BobException("Tell me what the deadline is for before adding /by.");
        }
        if (by.isEmpty()) {
            throw new BobException("The /by field cannot be empty. Tell me when this task is due.");
        }
        return new Deadline(description, by);
    }

    /**
     * Creates an event task from user input.
     *
     * @param input full event command
     * @return the parsed event task
     * @throws BobException if its description, start, or end is missing
     */
    private static Task parseEvent(String input) throws BobException {
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

        String from = schedule.substring(0, toPosition).trim();
        String to = schedule.substring(toPosition + "/to".length()).trim();
        if (from.isEmpty()) {
            throw new BobException("The /from field cannot be empty. Tell me when the event starts.");
        }
        if (to.isEmpty()) {
            throw new BobException("The /to field cannot be empty. Tell me when the event ends.");
        }
        return new Event(description, from, to);
    }

    /**
     * Stores a task, displays confirmation, and returns the updated task count.
     *
     * @param database array in which tasks are stored
     * @param taskCount number of tasks currently stored
     * @param task task to add
     * @return the updated task count
     * @throws BobException if the task array is full
     */
    private static int addTask(Task[] database, int taskCount, Task task) throws BobException {
        if (taskCount >= database.length) {
            throw new BobException("Your task list is full. Complete or remove a task before adding another.");
        }
        database[taskCount] = task;
        int updatedTaskCount = taskCount + 1;
        printTaskAdded(task, updatedTaskCount);
        return updatedTaskCount;
    }

    /**
     * Displays confirmation that a task was added and reports the new task count.
     *
     * @param task task that was added
     * @param taskCount number of tasks currently stored
     */
    private static void printTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }
}
