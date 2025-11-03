package seedu.foodbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.foodbook.commons.util.ToStringBuilder;
import seedu.foodbook.logic.Messages;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.person.ClientMatchesPredicate;

/**
 * Finds and lists all clients in food book whose name, phone, or email matches the search criteria.
 * Keyword matching is case-insensitive for names and emails.
 */
public class FindClientCommand extends Command {

    public static final String COMMAND_WORD = "find_client";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all clients whose name contains "
            + "the specified name keyword, phone contains the specified phone digits, "
            + "and email contains the specified email keyword (all case-insensitive) "
            + "and displays them as a list with index numbers.\n"
            + "At least one search parameter must be provided.\n"
            + "Parameters: "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Alice "
            + PREFIX_PHONE + "98765 "
            + PREFIX_EMAIL + "example.com";

    public static final String MESSAGE_NO_CLIENTS_FOUND = "Error: No clients found.";

    private final ClientMatchesPredicate predicate;

    /**
     * Creates a FindClientCommand with the given predicate.
     * @param predicate The predicate to filter clients by.
     */
    public FindClientCommand(ClientMatchesPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        model.updateFilteredPersonList(predicate);

        int numClientsFound = model.getFilteredPersonList().size();

        if (numClientsFound == 0) {
            return new CommandResult(MESSAGE_NO_CLIENTS_FOUND, CommandResult.UiPanel.PERSONS);
        }

        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, numClientsFound),
                CommandResult.UiPanel.PERSONS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindClientCommand)) {
            return false;
        }

        FindClientCommand otherFindClientCommand = (FindClientCommand) other;
        return predicate.equals(otherFindClientCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
