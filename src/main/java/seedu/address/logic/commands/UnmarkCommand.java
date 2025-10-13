package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.delivery.Delivery;

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
        List<Delivery> lastShownList = model.getFilteredDeliveryList();

        // Find the delivery with the specified ID
        Delivery deliveryToUnmark = lastShownList.stream()
                .filter(delivery -> delivery.getId().equals(deliveryId))
                .findFirst()
                .orElseThrow(() -> new CommandException(MESSAGE_DELIVERY_NOT_FOUND));

        // Check if delivery is already unmarked as completed
        if (!deliveryToUnmark.getStatus()) {
            throw new CommandException(MESSAGE_DELIVERY_ALREADY_UNMARKED);
        }

        // Create a new delivery object with unmarked status
        Delivery unmarkedDelivery = new Delivery(
                deliveryToUnmark.getId(),
                deliveryToUnmark.getClient(),
                deliveryToUnmark.getDeliveryDate(),
                deliveryToUnmark.getRemarks(),
                deliveryToUnmark.getCost()
        );
        unmarkedDelivery.unmarkAsDelivered();

        // Update the model
        model.setDelivery(deliveryToUnmark, unmarkedDelivery);

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
