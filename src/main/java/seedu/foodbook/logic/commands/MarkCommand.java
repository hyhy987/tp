package seedu.foodbook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.foodbook.commons.util.ToStringBuilder;
import seedu.foodbook.logic.Messages;
import seedu.foodbook.logic.commands.exceptions.CommandException;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.delivery.Delivery;

/**
 * Marks a delivery as completed in the food book.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the delivery identified by the delivery ID as completed.\n"
            + "Parameters: DELIVERY_ID (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 67";

    public static final String MESSAGE_MARK_DELIVERY_SUCCESS = "Delivery marked as completed: %1$s";
    public static final String MESSAGE_DELIVERY_NOT_FOUND = "Error: Delivery does not exist.";
    public static final String MESSAGE_DELIVERY_ALREADY_MARKED = "Delivery is already marked as completed.";

    private final Integer deliveryId;

    /**
     * Creates a MarkCommand to mark the delivery with the specified ID as completed.
     *
     * @param deliveryId The ID of the delivery to mark as completed.
     */
    public MarkCommand(Integer deliveryId) {
        requireNonNull(deliveryId);
        this.deliveryId = deliveryId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Delivery> lastShownList = model.getFilteredDeliveryList();

        // Find the delivery with the specified ID
        Delivery deliveryToMark = lastShownList.stream()
                .filter(delivery -> delivery.getId().equals(deliveryId))
                .findFirst()
                .orElseThrow(() -> new CommandException(MESSAGE_DELIVERY_NOT_FOUND));

        // Check if delivery is already marked as completed
        if (deliveryToMark.getStatus()) {
            throw new CommandException(MESSAGE_DELIVERY_ALREADY_MARKED);
        }

        // Create a new delivery object with marked status
        Delivery markedDelivery = new Delivery(
                deliveryToMark.getId(),
                deliveryToMark.getClient(),
                deliveryToMark.getDeliveryDate(),
                deliveryToMark.getRemarks(),
                deliveryToMark.getCost(),
                deliveryToMark.getTag()
        );
        markedDelivery.markAsDelivered();

        // Update the model
        model.setDelivery(deliveryToMark, markedDelivery);

        return new CommandResult(String.format(MESSAGE_MARK_DELIVERY_SUCCESS, Messages.format(markedDelivery)),
                CommandResult.UiPanel.DELIVERIES);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MarkCommand)) {
            return false;
        }

        MarkCommand otherMarkCommand = (MarkCommand) other;
        return deliveryId.equals(otherMarkCommand.deliveryId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("deliveryId", deliveryId)
                .toString();
    }
}
