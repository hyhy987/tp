package seedu.foodbook.logic.commands;

import seedu.foodbook.logic.commands.exceptions.CommandException;
import seedu.foodbook.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(Model model) throws CommandException;

    public void checkpoint(Model model, CommandResult.UiPanel nextUiPanel) {
        model.checkpoint(this.toString());
        model.setCurUiPanel(nextUiPanel);
    }

}
