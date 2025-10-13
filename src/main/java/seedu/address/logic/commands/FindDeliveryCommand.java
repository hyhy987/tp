package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.delivery.DeliveryContainsDatePredicate;

/**
 * Retrieves and displays all deliveries in FoodBook scheduled for the specified date.
 */
public class FindDeliveryCommand extends Command {

    /** The command word that users type to execute this command. */
    public static final String COMMAND_WORD = "find_delivery";

    /** Usage message showing the correct format for this command. */
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds deliveries by date and displays them as a list "
            + "with index numbers.\n"
            + "Parameters: DATE [MORE_\n"
            + "Example: " + COMMAND_WORD + " 25/12/2024 31/12/2024";

    /** Predicate indicating the filter condition for find_delivery. */
    private final DeliveryContainsDatePredicate predicate;

    public FindDeliveryCommand(DeliveryContainsDatePredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredDeliveryList(predicate);
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

