package seedu.foodbook.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.foodbook.commons.util.ToStringBuilder;
import seedu.foodbook.logic.commands.exceptions.CommandException;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.undo.ModelRecord;
import seedu.foodbook.model.undo.exceptions.NoMoreUndoException;

/**
 * Adds a person to the food book.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undoes the most recent command to food book. ";

    public static final String MESSAGE_SUCCESS = "Command undone: \"%1$s\"";
    public static final String MESSAGE_NO_MORE_UNDO = "You cannot undo any further";


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        ModelRecord prevState;
        try {
            prevState = model.undo();
        } catch (NoMoreUndoException e) {
            throw new CommandException(MESSAGE_NO_MORE_UNDO);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, prevState.commandString()),
                prevState.uiPanel());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UndoCommand)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
