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
            if (command.equals("bye")) {
                System.out.print(goodbyes);
                System.out.println(separator);
                break;
            } else if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < currCount; i++) {
                    System.out.println((i + 1) + "." + database[i]);
                }
            } else if (command.startsWith("mark ")) {
                int itemID = Integer.parseInt(command.substring(5).trim());
                String marked = database[itemID - 1].mark();

                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + marked);
            } else if (command.startsWith("unmark ")) {
                int itemID = Integer.parseInt(command.substring(7).trim());
                String marked = database[itemID - 1].unmark();

                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + marked);
            } else if (command.startsWith("todo ")) {
                String description = command.substring(5);
                database[currCount] = new Todo(description);
                currCount++;
                printTaskAdded(database[currCount - 1], currCount);
            } else if (command.startsWith("deadline ")) {
                String details = command.substring(9);
                String[] deadlineParts = details.split(" /by ", 2);
                database[currCount] = new Deadline(deadlineParts[0], deadlineParts[1]);
                currCount++;
                printTaskAdded(database[currCount - 1], currCount);
            } else if (command.startsWith("event ")) {
                String details = command.substring(6);
                String[] eventParts = details.split(" /from ", 2);
                String[] timeParts = eventParts[1].split(" /to ", 2);
                database[currCount] = new Event(eventParts[0], timeParts[0], timeParts[1]);
                currCount++;
                printTaskAdded(database[currCount - 1], currCount);
            } else {
                database[currCount] = new Task(command);
                currCount++;
                System.out.println("added: " + command);
            }
            System.out.println(separator);
        }
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
