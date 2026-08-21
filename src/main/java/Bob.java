import java.util.Scanner;

/**
 * Starts the Bob chatbot application.
 */

public class Bob {
    /**
     * Displays Bob's welcome banner.
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
            String[] commands = command.split(" ");

            if (commands[0].equals("bye")) {
                System.out.print(goodbyes);
                System.out.println(separator);
                break;
            } else if (commands[0].equals("list")) {
                for (int i = 1; i < currCount + 1; i++){
                    System.out.println(i + ". " + database[i - 1].getStatusIcon() + " " + database[i-1].getDescription());
                }
            } else if (commands[0].equals("mark")){
                int itemID = Integer.parseInt(commands[1]);
                String marked = database[itemID - 1].mark();

                System.out.println("Nice! I've marked this task as done:");
                System.out.println(marked);
            } else if (commands[0].equals("unmark")) {
                int itemID = Integer.parseInt(commands[1]);
                String marked = database[itemID - 1].unmark();

                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(marked);
            } else{
                database[currCount] = new Task(command);
                currCount += 1;
                System.out.println("added: " + command);
            }
            System.out.println(separator);
        }
    }
}
