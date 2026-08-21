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
        String[] database = new String[100];
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
                for(int i = 1; i < currCount + 1; i++){
                    System.out.println(i + ". " + database[i - 1]);
                }
            } else{
                database[currCount] = command;
                currCount += 1;
                System.out.println("added: " + command);
            }
            System.out.println(separator);
        }
    }
}
