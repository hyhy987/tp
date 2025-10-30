package seedu.foodbook.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.foodbook.commons.util.ToStringBuilder;
import seedu.foodbook.logic.Messages;
import seedu.foodbook.logic.commands.exceptions.CommandException;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.delivery.DeliveryPredicate;

/**
 * Retrieves and displays all deliveries in FoodBook scheduled for the specified date.
 */
public class FindDeliveryCommand extends Command {

    /** The command word that users type to execute this command. */
    public static final String COMMAND_WORD = "find_delivery";

    /** Usage message showing the correct format for this command. */
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds deliveries by various filters "
            + "and displays them as a list with index numbers.\n"
            + "When no filters are specified, displays all deliveries.\n"
            + "Parameters: [n/CLIENT_NAME] [d/DATE] [t/TAG]...\n"
            + "Examples:\n"
            + "- " + COMMAND_WORD + " n/John Doe\n"
            + "- " + COMMAND_WORD + " d/25/12/2024\n"
            + "- " + COMMAND_WORD + " t/urgent\n"
            + "- " + COMMAND_WORD + " n/John d/25/12/2024 t/urgent";

    public static final String MESSAGE_NO_DELIVERY_FOUND = "Error: No delivery found.";

    /** Predicate indicating the filter condition for find_delivery. */
    private final DeliveryPredicate predicate;

    public FindDeliveryCommand(DeliveryPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredDeliveryList(predicate);

        int numDeliveriesFound = model.getFilteredDeliveryList().size();

        if (numDeliveriesFound == 0) {
            return new CommandResult(MESSAGE_NO_DELIVERY_FOUND, CommandResult.UiPanel.DELIVERIES);
        }
        return new CommandResult(
                String.format(Messages.MESSAGE_DELIVERIES_LISTED_OVERVIEW, model.getFilteredDeliveryList().size()),
                CommandResult.UiPanel.DELIVERIES);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof FindDeliveryCommand
                && predicate.equals(((FindDeliveryCommand) other).predicate));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
