package seedu.foodbook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.foodbook.commons.util.ToStringBuilder;
import seedu.foodbook.logic.Messages;
import seedu.foodbook.logic.commands.exceptions.CommandException;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.delivery.Delivery;

/**
 * Unmarks a delivery as completed in the food book.
 */
public class UnmarkCommand extends Command {

    public static final String COMMAND_WORD = "unmark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unmarks the delivery identified by the delivery ID as completed.\n"
            + "Parameters: DELIVERY_ID (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 67";

    public static final String MESSAGE_UNMARK_DELIVERY_SUCCESS = "Delivery unmarked: %1$s";
    public static final String MESSAGE_DELIVERY_NOT_FOUND = "Error: Delivery does not exist.";
    public static final String MESSAGE_DELIVERY_ALREADY_UNMARKED = "Delivery is already unmarked.";

    private final Integer deliveryId;

    /**
     * Creates an UnmarkCommand to unmark the delivery with the specified ID as completed.
     *
     * @param deliveryId The ID of the delivery to unmark as completed.
     */
    public UnmarkCommand(Integer deliveryId) {
        requireNonNull(deliveryId);
        this.deliveryId = deliveryId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Optional<Delivery> maybeDelivery = model.getDeliveryById(deliveryId);

        if (maybeDelivery.isEmpty()) {
            throw new CommandException(
                    String.format(MESSAGE_DELIVERY_NOT_FOUND, deliveryId));
        }

        Delivery delivery = maybeDelivery.get();

        // Check if delivery is already marked as completed
        if (!delivery.getStatus()) {
            throw new CommandException(MESSAGE_DELIVERY_ALREADY_UNMARKED);
        }

        Delivery unmarkedDelivery = delivery.unmarkAsDelivered();

        this.checkpoint(model, CommandResult.UiPanel.DELIVERIES);

        // Update the model
        model.setDelivery(delivery, unmarkedDelivery);

        return new CommandResult(String.format(MESSAGE_UNMARK_DELIVERY_SUCCESS, Messages.format(unmarkedDelivery)),
                CommandResult.UiPanel.DELIVERIES);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnmarkCommand)) {
            return false;
        }

        UnmarkCommand otherUnmarkCommand = (UnmarkCommand) other;
        return deliveryId.equals(otherUnmarkCommand.deliveryId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("deliveryId", deliveryId)
                .toString();
    }
}
