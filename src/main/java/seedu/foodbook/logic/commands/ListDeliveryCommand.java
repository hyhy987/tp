package seedu.foodbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.foodbook.model.Model.PREDICATE_SHOW_ALL_DELIVERIES;

import seedu.foodbook.model.Model;

/**
 * Lists all deliveries in the food book to the user.
 */
public class ListDeliveryCommand extends Command {

    public static final String COMMAND_WORD = "list_delivery";

    public static final String MESSAGE_SUCCESS = "Listed all deliveries";


    /**
     * Executes the list delivery command.
     * <p>
     * This command updates the model to display all deliveries by resetting the
     * {@code FilteredDeliveryList} with the {@code PREDICATE_SHOW_ALL_DELIVERIES}.
     * It also clears the {@code FilteredPersonList} (sets it to false for all entries)
     * to ensure that the main window automatically switches to display the delivery panel.
     *
     * @param model The {@code Model} which the command operates on. Must not be null.
     * @return A {@code CommandResult} indicating successful execution of the command.
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES);
        return new CommandResult(MESSAGE_SUCCESS, CommandResult.UiPanel.DELIVERIES);
    }
}
