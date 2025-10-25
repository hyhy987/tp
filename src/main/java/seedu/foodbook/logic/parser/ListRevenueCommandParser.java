package seedu.foodbook.logic.parser;

import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Optional;

import seedu.foodbook.logic.commands.ListRevenueCommand;
import seedu.foodbook.logic.parser.exceptions.ParseException;
import seedu.foodbook.model.delivery.DateTime;
import seedu.foodbook.model.delivery.DeliveryPredicate;

/**
 * Parses input arguments and creates a new ListRevenueCommand object.
 */
public class ListRevenueCommandParser implements Parser<ListRevenueCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListRevenueCommand
     * and returns a ListRevenueCommand object for execution.
     *
     * @param args The string containing user input arguments.
     * @return A ListRevenueCommand object ready for execution.
     * @throws ParseException If the user input does not conform to the expected format.
     */
    public ListRevenueCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_START_DATE, PREFIX_END_DATE,
                        PREFIX_NAME, PREFIX_STATUS, PREFIX_TAG);

        // Check that preamble is empty
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ListRevenueCommand.MESSAGE_USAGE));
        }

        // Ensure no duplicate prefixes
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_START_DATE, PREFIX_END_DATE,
                PREFIX_NAME, PREFIX_STATUS, PREFIX_TAG);

        // Parse dates as strings and validate
        Optional<String> startDateString = argMultimap.getValue(PREFIX_START_DATE)
                .map(String::trim)
                .filter(s -> !s.isEmpty());
        if (startDateString.isPresent() && !DateTime.isValidDate(startDateString.get())) {
            throw new ParseException("Invalid start date format. Expected format: d/M/yyyy (e.g., 25/12/2024)");
        }

        Optional<String> endDateString = argMultimap.getValue(PREFIX_END_DATE)
                .map(String::trim)
                .filter(s -> !s.isEmpty());
        if (endDateString.isPresent() && !DateTime.isValidDate(endDateString.get())) {
            throw new ParseException("Invalid end date format. Expected format: d/M/yyyy (e.g., 25/12/2024)");
        }


        // Parse client name (optional)
        Optional<String> clientName = argMultimap.getValue(PREFIX_NAME)
                .map(String::trim)
                .filter(s -> !s.isEmpty());

        // Parse tag (optional)
        Optional<String> tag = argMultimap.getValue(PREFIX_TAG)
                .map(String::trim)
                .filter(s -> !s.isEmpty());

        // Parse status (optional)
        Optional<Boolean> isDelivered = Optional.empty();
        if (argMultimap.getValue(PREFIX_STATUS).isPresent()) {
            String statusString = argMultimap.getValue(PREFIX_STATUS).get().trim().toLowerCase();
            if (!statusString.isEmpty()) {
                isDelivered = Optional.of(parseStatus(statusString));
            }
        }

        DeliveryPredicate predicate = new DeliveryPredicate(startDateString, endDateString,
                clientName, tag, isDelivered);
        return new ListRevenueCommand(predicate);
    }


    /**
     * Parses a status string into a boolean.
     *
     * @param statusString The status string to parse
     * @return true for "delivered", false for "not_delivered"
     * @throws ParseException If the status string is invalid
     */
    private boolean parseStatus(String statusString) throws ParseException {
        switch (statusString) {
        case "delivered":
            return true;
        case "not_delivered":
            return false;
        default:
            throw new ParseException("Invalid status. Use 'delivered' or 'not_delivered'.");
        }
    }
}

