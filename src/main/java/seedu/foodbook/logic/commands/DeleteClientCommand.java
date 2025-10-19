package seedu.foodbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.foodbook.model.Model.PREDICATE_SHOW_ALL_DELIVERIES;
import static seedu.foodbook.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.foodbook.commons.util.ToStringBuilder;
import seedu.foodbook.logic.Messages;
import seedu.foodbook.logic.commands.exceptions.CommandException;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Name;
import seedu.foodbook.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the food book.
 */
public class DeleteClientCommand extends Command {

    public static final String COMMAND_WORD = "delete_client";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the client identified by the name supplied to the CLI from the displayed person list.\n"
            + "Parameters: OLD_NAME (must be an existing person name in our FoodBook)\n"
            + "Example: " + COMMAND_WORD + " Alex Yeoh";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    private final Name toBeDeleted;

    public DeleteClientCommand(Name toBeDeleted) {
        this.toBeDeleted = toBeDeleted;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        List<Person> lastShownList = model.getFilteredPersonList();

        Person target = lastShownList.stream()
                .filter(p -> p.getName().fullName.equals(toBeDeleted.fullName))
                .findFirst()
                .orElseThrow(() -> new CommandException("The name provided is not found"));

        model.deletePerson(target);


        //deleting all deliveries from FoodBook associated with the deleted client
        List<Delivery> toDelete = model.getFoodBook().getDeliveryList().stream()
                .filter(d -> d.getClient().getName().equals(toBeDeleted))
                .collect(java.util.stream.Collectors.toList());

        toDelete.forEach(model::deleteDelivery);

        model.updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(target)),
                CommandResult.UiPanel.PERSONS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteClientCommand)) {
            return false;
        }

        DeleteClientCommand otherDeleteClientCommand = (DeleteClientCommand) other;
        return toBeDeleted.equals(otherDeleteClientCommand.toBeDeleted);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toBeDeleted", toBeDeleted)
                .toString();
    }
}
