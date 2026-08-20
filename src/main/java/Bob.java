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
        String seperator = "____________________________________________________________";
        String banner = " ____        _     \n"
                + "| __ )  ___ | |__  \n"
                + "|  _ \\ / _ \\| '_ \\ \n"
                + "| |_) | (_) | |_) |\n"
                + "|____/ \\___/|_.__/ \n";
        String greetings = "Hello! I'm Bob.\n" +
                "What can I do for you?\n";
        String goodbyes = "Bye. Hope to see you again soon!\n";
        System.out.println(seperator + "\n" + banner +  greetings + seperator + "\n" + goodbyes + seperator);
    }
}
