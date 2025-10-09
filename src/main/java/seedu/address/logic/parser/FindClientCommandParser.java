package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Optional;

import seedu.address.logic.commands.FindClientCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.ClientMatchesPredicate;

/**
 * Parses input arguments and creates a new FindClientCommand object
 */
public class FindClientCommandParser implements Parser<FindClientCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindClientCommand
     * and returns a FindClientCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindClientCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL);

        // Check that preamble is empty
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindClientCommand.MESSAGE_USAGE));
        }

        // Ensure no duplicate prefixes
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL);

        // Extract search queries
        Optional<String> nameQuery = argMultimap.getValue(PREFIX_NAME)
                .map(String::trim)
                .filter(s -> !s.isEmpty());

        Optional<String> phoneQuery = argMultimap.getValue(PREFIX_PHONE)
                .map(String::trim)
                .filter(s -> !s.isEmpty());

        Optional<String> emailQuery = argMultimap.getValue(PREFIX_EMAIL)
                .map(String::trim)
                .filter(s -> !s.isEmpty());

        // Check that at least one search criterion is provided
        if (!nameQuery.isPresent() && !phoneQuery.isPresent() && !emailQuery.isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindClientCommand.MESSAGE_USAGE));
        }

        ClientMatchesPredicate predicate = new ClientMatchesPredicate(nameQuery, phoneQuery, emailQuery);
        return new FindClientCommand(predicate);
    }
}
