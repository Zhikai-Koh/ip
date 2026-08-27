package bob.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests task collection operations performed by {@link TaskList}.
 */
class TaskListTest {
    @Test
    void find_multipleMatchingDescriptions_returnsMatchesInOriginalOrder() {
        Task readBook = new Todo("read book");
        Task returnBook = new Deadline("return book", LocalDate.parse("2019-12-02"));
        Task projectMeeting = new Event("project meeting",
                LocalDate.parse("2019-12-03"), LocalDate.parse("2019-12-04"));
        TaskList tasks = new TaskList(List.of(readBook, returnBook, projectMeeting));

        List<Task> matches = tasks.find("book");

        assertEquals(List.of(readBook, returnBook), matches);
    }

    @Test
    void find_keywordOnlyInDateOrType_doesNotMatch() {
        TaskList tasks = new TaskList(List.of(
                new Deadline("return notes", LocalDate.parse("2019-12-02")),
                new Todo("read notes")));

        assertTrue(tasks.find("2019").isEmpty());
        assertTrue(tasks.find("[T]").isEmpty());
    }

    @Test
    void find_differentCaseOrAbsentKeyword_returnsNoMatches() {
        TaskList tasks = new TaskList(List.of(new Todo("read book")));

        assertTrue(tasks.find("Book").isEmpty());
        assertTrue(tasks.find("homework").isEmpty());
    }
}
