package bob.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bob.exception.BobException;
import bob.task.Task;

/**
 * Tests command recognition and validation performed by {@link Parser}.
 */
class ParserTest {
    @Test
    void isCommand_exactCommandOrCommandWithArguments_returnsTrue() {
        assertTrue(Parser.isCommand("list", "list"));
        assertTrue(Parser.isCommand("todo read book", "todo"));
    }

    @Test
    void isCommand_partialOrPrefixedCommand_returnsFalse() {
        assertFalse(Parser.isCommand("listing", "list"));
        assertFalse(Parser.isCommand("please list", "list"));
    }

    @Test
    void parseTaskNumber_validNumber_returnsOneBasedNumber() throws BobException {
        assertEquals(2, Parser.parseTaskNumber("mark   2  ", "mark", 3));
    }

    @Test
    void parseTaskNumber_invalidArguments_throwsBobException() {
        assertThrows(BobException.class, () -> Parser.parseTaskNumber("mark", "mark", 3));
        assertThrows(BobException.class, () -> Parser.parseTaskNumber("mark two", "mark", 3));
        assertThrows(BobException.class, () -> Parser.parseTaskNumber("mark 0", "mark", 3));
        assertThrows(BobException.class, () -> Parser.parseTaskNumber("mark 4", "mark", 3));
        assertThrows(BobException.class, () -> Parser.parseTaskNumber("mark 1", "mark", 0));
    }

    @Test
    void parseFindKeyword_validKeyword_trimsAndReturnsKeyword() throws BobException {
        assertEquals("return book", Parser.parseFindKeyword("find   return book  "));
    }

    @Test
    void parseFindKeyword_missingKeyword_throwsBobException() {
        assertThrows(BobException.class, () -> Parser.parseFindKeyword("find"));
        assertThrows(BobException.class, () -> Parser.parseFindKeyword("find   "));
    }

    @Test
    void parseTodo_validDescription_trimsAndCreatesTodo() throws BobException {
        Task task = Parser.parseTodo("todo   read book  ");

        assertEquals("[T][ ] read book", task.toString());
        assertEquals("T | 0 | read book", task.toDataString());
    }

    @Test
    void parseTodo_emptyDescription_throwsBobException() {
        assertThrows(BobException.class, () -> Parser.parseTodo("todo   "));
    }

    @Test
    void parseDeadline_validCommand_parsesAndFormatsDate() throws BobException {
        Task task = Parser.parseDeadline("deadline return book /by 2019-12-02");

        assertEquals("[D][ ] return book (by: Dec 2 2019)", task.toString());
        assertEquals("D | 0 | return book | 2019-12-02", task.toDataString());
    }

    @Test
    void parseDeadline_missingFieldsOrInvalidDate_throwsBobException() {
        assertThrows(BobException.class, () -> Parser.parseDeadline("deadline return book"));
        assertThrows(BobException.class, () -> Parser.parseDeadline("deadline /by 2019-12-02"));
        assertThrows(BobException.class, () -> Parser.parseDeadline("deadline return book /by"));
        assertThrows(BobException.class, () -> Parser.parseDeadline("deadline return book /by 2019-02-30"));
    }

    @Test
    void parseEvent_validCommand_parsesAndFormatsDates() throws BobException {
        Task task = Parser.parseEvent("event project meeting /from 2019-12-02 /to 2019-12-03");

        assertEquals("[E][ ] project meeting (from: Dec 2 2019 to: Dec 3 2019)", task.toString());
        assertEquals("E | 0 | project meeting | 2019-12-02 | 2019-12-03", task.toDataString());
    }

    @Test
    void parseEvent_missingFieldsOrInvalidDates_throwsBobException() {
        assertThrows(BobException.class, () -> Parser.parseEvent("event project meeting"));
        assertThrows(BobException.class, () -> Parser.parseEvent("event /from 2019-12-02 /to 2019-12-03"));
        assertThrows(BobException.class, () -> Parser.parseEvent("event meeting /from 2019-12-02"));
        assertThrows(BobException.class, () -> Parser.parseEvent("event meeting /from /to 2019-12-03"));
        assertThrows(BobException.class, () -> Parser.parseEvent("event meeting /from 2019-12-02 /to"));
        assertThrows(BobException.class,
                () -> Parser.parseEvent("event meeting /from 2019-02-30 /to 2019-12-03"));
    }
}
