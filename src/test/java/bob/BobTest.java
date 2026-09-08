package bob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class BobTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    void getResponse_taskWorkflow_returnsResponsesAndUpdatesList() {
        Bob bob = createBob();

        assertEquals("Got it. I've added this task:\n"
                + "  [T][ ] read book\n"
                + "Now you have 1 tasks in the list.", bob.getResponse("todo read book"));
        assertEquals("Here are the tasks in your list:\n1.[T][ ] read book", bob.getResponse("list"));
        assertEquals("Nice! I've marked this task as done:\n  [T][X] read book", bob.getResponse("mark 1"));
        assertEquals("Here are the matching tasks in your list:\n1.[T][X] read book",
                bob.getResponse("find book"));
        assertEquals("Noted. I've removed this task:\n"
                + "  [T][X] read book\n"
                + "Now you have 0 tasks in the list.", bob.getResponse("delete 1"));
    }

    @Test
    void getResponse_scheduleCommand_returnsTasksOnRequestedDate() {
        Bob bob = createBob();
        bob.getResponse("todo read book");
        bob.getResponse("deadline return book /by 2019-12-02");
        bob.getResponse("event camp /from 2019-12-01 /to 2019-12-03");

        assertEquals("Here is your schedule for Dec 2 2019:\n"
                + "1.[D][ ] return book (by: Dec 2 2019)\n"
                + "2.[E][ ] camp (from: Dec 1 2019 to: Dec 3 2019)",
                bob.getResponse("schedule 2019-12-02"));
    }

    @Test
    void getResponse_invalidAndByeCommands_returnsErrorsAndTracksExit() {
        Bob bob = createBob();

        assertFalse(bob.isExit());
        assertEquals("A todo needs something to do. Add a description after todo.", bob.getResponse("todo"));
        assertEquals("I couldn't match that to a command. "
                + "Try todo, deadline, event, list, find, schedule, mark, unmark, delete, or bye.",
                bob.getResponse("unknown"));
        assertEquals("Bye. Hope to see you again soon!", bob.getResponse("bye"));
        assertTrue(bob.isExit());
    }

    @Test
    void constructorOrGetResponse_nullInternalInput_throwsAssertionError() {
        Bob bob = createBob();

        assertThrows(AssertionError.class, () -> new Bob(null));
        assertThrows(AssertionError.class, () -> bob.getResponse(null));
    }

    private Bob createBob() {
        return new Bob(temporaryDirectory.resolve("data/bob.txt").toString());
    }
}
