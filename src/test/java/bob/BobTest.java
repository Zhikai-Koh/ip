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

        assertEquals("Mission logged:\n"
                + "  [T][ ] read book\n"
                + "Flight plan now contains 1 mission.", bob.getResponse("todo read book"));
        assertEquals("Here is your flight plan:\n1.[T][ ] read book", bob.getResponse("list"));
        assertEquals("Mission accomplished! Marked as complete:\n  [T][X] read book", bob.getResponse("mark 1"));
        assertEquals("Scanner found these matching missions:\n1.[T][X] read book",
                bob.getResponse("find book"));
        assertEquals("Mission scrubbed from the flight plan:\n"
                + "  [T][X] read book\n"
                + "Flight plan now contains 0 missions.", bob.getResponse("delete 1"));
    }

    @Test
    void getResponse_scheduleCommand_returnsTasksOnRequestedDate() {
        Bob bob = createBob();
        bob.getResponse("todo read book");
        bob.getResponse("deadline return book /by 2019-12-02");
        bob.getResponse("event camp /from 2019-12-01 /to 2019-12-03");

        assertEquals("Mission timeline for Dec 2 2019:\n"
                + "1.[D][ ] return book (by: Dec 2 2019)\n"
                + "2.[E][ ] camp (from: Dec 1 2019 to: Dec 3 2019)",
                bob.getResponse("schedule 2019-12-02"));
    }

    @Test
    void getResponse_invalidAndByeCommands_returnsErrorsAndTracksExit() {
        Bob bob = createBob();

        assertEquals("Orbit online.\nMission control is ready. What shall we accomplish?",
                bob.getWelcomeMessage());
        assertFalse(bob.isExit());
        assertEquals("Every mission needs an objective. Add a description after todo.", bob.getResponse("todo"));
        assertTrue(bob.isLastResponseError());
        assertEquals("Command not recognized by mission control. "
                + "Try todo, deadline, event, list, find, schedule, mark, unmark, delete, or bye.",
                bob.getResponse("unknown"));
        assertTrue(bob.isLastResponseError());
        assertEquals("Orbit signing off. Clear skies!", bob.getResponse("bye"));
        assertFalse(bob.isLastResponseError());
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
