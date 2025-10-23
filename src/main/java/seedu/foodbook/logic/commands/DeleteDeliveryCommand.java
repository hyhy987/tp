package seedu.foodbook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import seedu.foodbook.commons.util.ToStringBuilder;
import seedu.foodbook.logic.Messages;
import seedu.foodbook.logic.commands.exceptions.CommandException;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.delivery.Delivery;

/**
 * Deletes a delivery identified using its delivery ID from the food book.
 */
public class DeleteDeliveryCommand extends Command {

    public static final String COMMAND_WORD = "delete_delivery";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the delivery identified by its delivery ID.\n"
            + "Parameters: DELIVERY_ID (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 67";

    public static final String MESSAGE_DELETE_DELIVERY_SUCCESS = "Delivery deleted.";
    public static final String MESSAGE_DELIVERY_NOT_FOUND = "Error: No delivery found with id %1$d";

    private final Integer deliveryId;

    /**
     * Creates a DeleteDeliveryCommand to delete the delivery with the specified {@code deliveryId}.
     *
     * @param deliveryId The ID of the delivery to delete.
     */
    public DeleteDeliveryCommand(Integer deliveryId) {
        requireNonNull(deliveryId);
        this.deliveryId = deliveryId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Find the delivery with the matching ID
        Optional<Delivery> maybeDelivery = model.getDeliveryById(deliveryId);

        if (maybeDelivery.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_DELIVERY_NOT_FOUND, deliveryId));
        }

        Delivery deliveryToDelete = maybeDelivery.get();

        model.checkpoint(COMMAND_WORD, CommandResult.UiPanel.PERSONS);

        model.deleteDelivery(deliveryToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_DELIVERY_SUCCESS,
                Messages.format(deliveryToDelete)),
                CommandResult.UiPanel.DELIVERIES);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteDeliveryCommand)) {
            return false;
        }

        DeleteDeliveryCommand otherDeleteDeliveryCommand = (DeleteDeliveryCommand) other;
        return deliveryId.equals(otherDeleteDeliveryCommand.deliveryId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("deliveryId", deliveryId)
                .toString();
    }
}
