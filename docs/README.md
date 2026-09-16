# Bob User Guide

Bob is a friendly desktop chatbot that helps you keep track of todos, deadlines, and events.

![Bob's graphical interface](Ui.png)

## Quick start

1. Ensure that Java 25 is installed.
2. Open a terminal in the project folder.
3. Start Bob with:

   ```bash
   ./gradlew run
   ```

4. Type a command in the text box and press **Enter** or click **Send**.

Dates must use the `yyyy-MM-dd` format, such as `2026-09-20`. Commands and search keywords are case-sensitive.

## Command summary

| Action | Command format | Example |
|---|---|---|
| Add a todo | `todo DESCRIPTION` | `todo read book` |
| Add a deadline | `deadline DESCRIPTION /by DATE` | `deadline submit report /by 2026-09-20` |
| Add an event | `event DESCRIPTION /from START /to END` | `event project meeting /from 2026-09-18 /to 2026-09-19` |
| List all tasks | `list` | `list` |
| Mark a task as done | `mark NUMBER` | `mark 1` |
| Mark a task as not done | `unmark NUMBER` | `unmark 1` |
| Delete a task | `delete NUMBER` | `delete 2` |
| Find tasks | `find KEYWORD` | `find book` |
| View a day's schedule | `schedule DATE` | `schedule 2026-09-18` |
| Exit Bob | `bye` | `bye` |

## Adding tasks

### Adding a todo

Use `todo` for a task without a date:

```text
todo read book
```

Bob adds the task and assigns it a number. New tasks start as not done, shown by `[ ]`.

### Adding a deadline

Use `deadline` with `/by` followed by its due date:

```text
deadline submit report /by 2026-09-20
```

### Adding an event

Use `event` with `/from` and `/to` dates:

```text
event project meeting /from 2026-09-18 /to 2026-09-19
```

## Managing tasks

Enter `list` to see every task and its current number:

```text
1.[T][ ] read book
2.[D][ ] submit report (by: Sep 20 2026)
3.[E][ ] project meeting (from: Sep 18 2026 to: Sep 19 2026)
```

The task symbols are:

- `[T]`: todo
- `[D]`: deadline
- `[E]`: event
- `[ ]`: not done
- `[X]`: done

Use the number shown by `list` when changing a task:

```text
mark 1
unmark 1
delete 2
```

Task numbers can change after a task is deleted, so use `list` again before the next numbered command.

## Finding tasks

Use `find` to show tasks whose descriptions contain a keyword:

```text
find book
```

The search checks task descriptions and is case-sensitive. For example, `find book` does not match `Read Book`.

## Viewing a schedule

Use `schedule` to show deadlines due on a date and events occurring on that date:

```text
schedule 2026-09-18
```

An event appears for every date from its start date through its end date.

## Saving data

Bob saves changes automatically in `data/bob.txt` and loads them the next time the app starts. You do not need to enter a save command.

## Handling mistakes

If a command is incomplete or invalid, Bob explains what needs to be corrected. For example:

```text
deadline submit report
```

Bob will explain that the `/by` date is missing. Your existing tasks remain unchanged.

## Exiting

Enter:

```text
bye
```

Bob displays a farewell and disables further input. Close the window when you are finished.
