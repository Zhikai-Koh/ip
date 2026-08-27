import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves Bob's task list to a file on the hard disk.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates a storage object that writes to the given file path.
     *
     * @param filePath path of the file used to store tasks
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Loads tasks from the storage file.
     *
     * @return tasks reconstructed from the saved data
     * @throws IOException if the file cannot be read
     * @throws BobException if a saved task has an invalid format
     */
    public ArrayList<Task> loadTasks() throws IOException, BobException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }

        List<String> taskLines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        for (String taskLine : taskLines) {
            if (!taskLine.isBlank()) {
                tasks.add(parseTask(taskLine));
            }
        }
        return tasks;
    }

    /**
     * Reconstructs one task from a pipe-separated storage line.
     *
     * @param taskLine saved task data
     * @return reconstructed task
     * @throws BobException if the task type, status, or number of fields is invalid
     */
    private Task parseTask(String taskLine) throws BobException {
        String[] fields = taskLine.split(" \\| ", -1);
        if (fields.length < 3) {
            throw new BobException("I found an invalid entry in the task file: " + taskLine);
        }

        Task task;
        try {
            switch (fields[0]) {
            case "T":
                requireFieldCount(fields, 3, taskLine);
                task = new Todo(fields[2]);
                break;
            case "D":
                requireFieldCount(fields, 4, taskLine);
                task = new Deadline(fields[2], LocalDate.parse(fields[3]));
                break;
            case "E":
                requireFieldCount(fields, 5, taskLine);
                task = new Event(fields[2], LocalDate.parse(fields[3]), LocalDate.parse(fields[4]));
                break;
            default:
                throw new BobException("I found an unknown task type in the task file: " + fields[0]);
            }
        } catch (DateTimeParseException e) {
            throw new BobException("I found an invalid date and time in the task file: " + taskLine);
        }

        if (fields[1].equals("1")) {
            task.mark();
        } else if (!fields[1].equals("0")) {
            throw new BobException("I found an invalid task status in the task file: " + fields[1]);
        }
        return task;
    }

    /**
     * Checks that a saved task contains the expected number of fields.
     *
     * @param fields fields parsed from the saved task
     * @param expectedCount required number of fields
     * @param taskLine original saved task data
     * @throws BobException if the field count is incorrect
     */
    private void requireFieldCount(String[] fields, int expectedCount, String taskLine) throws BobException {
        if (fields.length != expectedCount) {
            throw new BobException("I found an invalid entry in the task file: " + taskLine);
        }
    }

    /**
     * Writes all tasks to the storage file, replacing its previous contents.
     *
     * @param tasks tasks to save
     * @throws IOException if the directory or file cannot be written
     */
    public void saveTasks(List<Task> tasks) throws IOException {
        Path parentDirectory = filePath.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        List<String> taskLines = new ArrayList<>();
        for (Task task : tasks) {
            taskLines.add(task.toDataString());
        }
        Files.write(filePath, taskLines, StandardCharsets.UTF_8);
    }
}
