package katheryne.command;

import katheryne.KatheryneException;
import katheryne.Storage;
import katheryne.TaskList;
import katheryne.Ui;
import katheryne.WrongIndexException;
import katheryne.task.Task;

/**
 * Command to delete a task by the index.
 */
public class DeleteCommand extends Command {
    /**
     * The constant name to refer to this command by
     */
    public static final String COMMAND = "DELETE";
    private final int index;

    /**
     * Creates a new delete command that will delete a task by the index. Keep in mind
     * that the user given index will be one more than the actual desired index.
     *
     * @param processedRemainingText
     * @throws KatheryneException if number format is wrong
     */
    DeleteCommand(String[] processedRemainingText) throws KatheryneException {
        // Will not throw a NullPointerException as it is checked for nulls in the factory method
        try {
            this.index = Integer.parseInt(processedRemainingText[0]) - 1;
        } catch (NumberFormatException e) {
            throw new WrongIndexException();
        }
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws KatheryneException {
        try {
            Task taskToDelete = taskList.getTask(index);
            taskList.deleteTask(index);
            ui.say("Task deleted: " + taskToDelete);
        } catch (IndexOutOfBoundsException e) {
            throw new WrongIndexException();
        }
        ui.countTasksInList(taskList);
    }
}
