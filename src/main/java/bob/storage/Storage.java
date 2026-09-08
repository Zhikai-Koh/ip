package bob.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import bob.exception.BobException;
import bob.task.Deadline;
import bob.task.Event;
import bob.task.Task;
import bob.task.TaskStatus;
import bob.task.TaskType;
import bob.task.Todo;

/**
 * Saves Bob's task list to a file on the hard disk.
 */
public class Storage {
    private static final String FIELD_SEPARATOR_PATTERN = " \\| ";
    private static final int MINIMUM_FIELD_COUNT = 3;
    private static final int TODO_FIELD_COUNT = 3;
    private static final int DEADLINE_FIELD_COUNT = 4;
    private static final int EVENT_FIELD_COUNT = 5;
    private static final int TYPE_FIELD_INDEX = 0;
    private static final int STATUS_FIELD_INDEX = 1;
    private static final int DESCRIPTION_FIELD_INDEX = 2;
    private static final int START_DATE_FIELD_INDEX = 3;
    private static final int END_DATE_FIELD_INDEX = 4;

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
        String[] fields = taskLine.split(FIELD_SEPARATOR_PATTERN, -1);
        if (fields.length < MINIMUM_FIELD_COUNT) {
            throw new BobException("I found an invalid entry in the task file: " + taskLine);
        }

        TaskType taskType = parseTaskType(fields[TYPE_FIELD_INDEX]);
        TaskStatus taskStatus = parseTaskStatus(fields[STATUS_FIELD_INDEX]);
        Task task = createTask(taskType, fields, taskLine);
        if (taskStatus == TaskStatus.DONE) {
            task.mark();
        }
        return task;
    }

    /**
     * Creates a task of the stored type using its remaining fields.
     *
     * @param taskType stored task type
     * @param fields fields parsed from the storage line
     * @param taskLine original saved task data
     * @return reconstructed task
     * @throws BobException if the field count or a date is invalid
     */
    private Task createTask(TaskType taskType, String[] fields, String taskLine) throws BobException {
        try {
            return switch (taskType) {
                case TODO -> {
                    requireFieldCount(fields, TODO_FIELD_COUNT, taskLine);
                    yield new Todo(fields[DESCRIPTION_FIELD_INDEX]);
                }
                case DEADLINE -> {
                    requireFieldCount(fields, DEADLINE_FIELD_COUNT, taskLine);
                    yield new Deadline(fields[DESCRIPTION_FIELD_INDEX],
                            LocalDate.parse(fields[START_DATE_FIELD_INDEX]));
                }
                case EVENT -> {
                    requireFieldCount(fields, EVENT_FIELD_COUNT, taskLine);
                    yield new Event(fields[DESCRIPTION_FIELD_INDEX],
                            LocalDate.parse(fields[START_DATE_FIELD_INDEX]),
                            LocalDate.parse(fields[END_DATE_FIELD_INDEX]));
                }
            };
        } catch (DateTimeParseException e) {
            throw new BobException("I found an invalid date and time in the task file: " + taskLine);
        }
    }

    /**
     * Converts a stored task-type code into its enum value.
     *
     * @param storageCode task-type code read from storage
     * @return matching task type
     * @throws BobException if the code is unknown
     */
    private TaskType parseTaskType(String storageCode) throws BobException {
        try {
            return TaskType.fromStorageCode(storageCode);
        } catch (IllegalArgumentException e) {
            throw new BobException("I found an unknown task type in the task file: " + storageCode);
        }
    }

    /**
     * Converts a stored task-status value into its enum value.
     *
     * @param storageValue task-status value read from storage
     * @return matching task status
     * @throws BobException if the value is unknown
     */
    private TaskStatus parseTaskStatus(String storageValue) throws BobException {
        try {
            return TaskStatus.fromStorageValue(storageValue);
        } catch (IllegalArgumentException e) {
            throw new BobException("I found an invalid task status in the task file: " + storageValue);
        }
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

        List<String> taskLines = tasks.stream()
                .map(Task::toDataString)
                .toList();
        Files.write(filePath, taskLines, StandardCharsets.UTF_8);
    }
}
