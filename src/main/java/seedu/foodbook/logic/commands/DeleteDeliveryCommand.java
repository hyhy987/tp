package seedu.foodbook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.foodbook.commons.core.index.Index;
import seedu.foodbook.commons.util.ToStringBuilder;
import seedu.foodbook.logic.Messages;
import seedu.foodbook.logic.commands.exceptions.CommandException;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;

public class DeleteDeliveryCommand {
    public static final String COMMAND_WORD = "delete_delivery";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the delivery identified by its delviery ID.\n"
            + "Parameters: DELIVERY_ID (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 67";

    public static final String MESSAGE_DELETE_DELIVERY_SUCCESS = "Deleted Delivery: %1$s";
    public static final String MESSAGE_DELIVERY_NOT_FOUND = "Error: No delivery found with id"

    private final Index targetIndex;

    public DeleteDeliveryCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deletePerson(personToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_DELIVERY_SUCCESS, Messages.format(personToDelete)),
                CommandResult.UiPanel.PERSONS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }

}
