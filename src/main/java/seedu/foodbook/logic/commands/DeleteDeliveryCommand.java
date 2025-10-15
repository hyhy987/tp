package seedu.foodbook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.foodbook.commons.core.index.Index;
import seedu.foodbook.commons.util.ToStringBuilder;
import seedu.foodbook.logic.Messages;
import seedu.foodbook.logic.commands.exceptions.CommandException;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.delivery.Delivery;

public class DeleteDeliveryCommand extends Command {

    public static final String COMMAND_WORD = "delete_delivery";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the delivery identified by the index number used in the displayed delivery list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_DELIVERY_SUCCESS = "Delivery deleted.";

    private final Index targetIndex;

    public DeleteDeliveryCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Delivery> lastShownList = model.getFilteredDeliveryList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_DELIVERY_DISPLAYED_INDEX);
        }

        Delivery deliveryToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteDelivery(deliveryToDelete);
        return new CommandResult(MESSAGE_DELETE_DELIVERY_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeleteDeliveryCommand)) {
            return false;
        }
        DeleteDeliveryCommand otherCmd = (DeleteDeliveryCommand) other;
        return targetIndex.equals(otherCmd.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
