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
 */
public class FindDeliveryCommandParser implements Parser<FindDeliveryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindDeliveryCommand
     * and returns a FindDeliveryCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FindDeliveryCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_MISSING_ARGUMENT_FORMAT, FindDeliveryCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DATE, PREFIX_TAG);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindDeliveryCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_DATE, PREFIX_TAG);

        Optional<String> clientName = argMultimap.getValue(PREFIX_NAME);
        Optional<String> date = argMultimap.getValue(PREFIX_DATE);
        Optional<String> tag = argMultimap.getValue(PREFIX_TAG);

        if (clientName.isPresent() && clientName.get().trim().isEmpty()) {
            throw new ParseException("Client name cannot be empty.");
        }

        if (date.isPresent()) {
            String dateValue = date.get().trim();
            if (dateValue.isEmpty()) {
                throw new ParseException("Date cannot be empty.");
            }
            if (!DateTime.isValidDate(dateValue)) {
                throw new ParseException("Invalid date format. Please use d/M/yyyy format (e.g., 25/12/2024).");
            }
        }

        if (tag.isPresent() && tag.get().trim().isEmpty()) {
            throw new ParseException("Tag cannot be empty.");
        }

        // For find_delivery, use same date for start and end (exact date match)
        return new FindDeliveryCommand(new DeliveryPredicate(date, date, clientName, tag, Optional.empty()));
    }
}
