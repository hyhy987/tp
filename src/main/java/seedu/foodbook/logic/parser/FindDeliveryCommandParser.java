package seedu.foodbook.logic.parser;

import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.foodbook.logic.Messages.MESSAGE_MISSING_ARGUMENT_FORMAT;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Optional;

import seedu.foodbook.logic.commands.FindDeliveryCommand;
import seedu.foodbook.logic.parser.exceptions.ParseException;
import seedu.foodbook.model.delivery.DateTime;
import seedu.foodbook.model.delivery.DeliveryPredicate;

/**
 * Parses input arguments and creates a new FindDeliveryCommand object.
 * Supports multiple filter options: client name, date, and tags.
 * When no arguments are provided, creates a command that shows all deliveries.
 */
public class FindDeliveryCommandParser implements Parser<FindDeliveryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindDeliveryCommand
     * and returns a FindDeliveryCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FindDeliveryCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        // If no arguments provided, return new ParseException
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_MISSING_ARGUMENT_FORMAT, FindDeliveryCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DATE, PREFIX_TAG);

        // Check for invalid preamble (text before any prefix)
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindDeliveryCommand.MESSAGE_USAGE));
        }

        // Verify no duplicate prefixes for single-value fields
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_DATE, PREFIX_TAG);

        Optional<String> clientName = argMultimap.getValue(PREFIX_NAME);
        Optional<String> date = argMultimap.getValue(PREFIX_DATE);
        Optional<String> tag = argMultimap.getValue(PREFIX_TAG);

        // Validate client name is not empty if provided
        if (clientName.isPresent() && clientName.get().trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindDeliveryCommand.MESSAGE_USAGE));
        }

        // Validate date format if provided
        if (date.isPresent()) {
            String dateValue = date.get().trim();
            if (dateValue.isEmpty()) {
                throw new ParseException("Date cannot be empty.");
            }
            if (!DateTime.isValidDate(dateValue)) {
                throw new ParseException("Invalid date format. Please use d/M/yyyy format (e.g., 25/12/2024).");
            }
        }

        // Validate tag is not empty if provided
        if (tag.isPresent()) {
            if (tag.get().isEmpty()) {
                throw new ParseException("Tag cannot be empty.");
            }
        }

        return new FindDeliveryCommand(new DeliveryPredicate(clientName, date, tag));
    }
}
