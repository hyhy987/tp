package seedu.foodbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.foodbook.logic.commands.AddDeliveryCommand.MESSAGE_CLIENT_NOT_FOUND;
import static seedu.foodbook.model.Model.PREDICATE_SHOW_ALL_DELIVERIES;
import static seedu.foodbook.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Optional;

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

        Optional<Person> maybePerson = model.getPersonByName(toBeDeleted);
        if (maybePerson.isEmpty()) {
            throw new CommandException(
                    String.format(MESSAGE_CLIENT_NOT_FOUND, toBeDeleted)
            );
        }

        Person clientToDelete = maybePerson.get();
        List<Delivery> deliveriesToDelete = model.getDeliveriesByClientName(toBeDeleted);

        this.checkpoint(model, CommandResult.UiPanel.PERSONS);

        deliveriesToDelete.forEach(model::deleteDelivery);
        model.deletePerson(clientToDelete);

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(clientToDelete)),
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
