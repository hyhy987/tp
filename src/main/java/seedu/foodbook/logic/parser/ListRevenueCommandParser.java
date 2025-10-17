package seedu.foodbook.logic.parser;

import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_STATUS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Optional;

import seedu.foodbook.logic.commands.ListRevenueCommand;
import seedu.foodbook.logic.parser.exceptions.ParseException;
import seedu.foodbook.model.delivery.RevenueFilterPredicate;

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
                        PREFIX_NAME, PREFIX_STATUS);

        // Check that preamble is empty
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ListRevenueCommand.MESSAGE_USAGE));
        }

        // Ensure no duplicate prefixes
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_START_DATE, PREFIX_END_DATE,
                PREFIX_NAME, PREFIX_STATUS);

        // Parse start date (optional)
        Optional<LocalDate> startDate = Optional.empty();
        if (argMultimap.getValue(PREFIX_START_DATE).isPresent()) {
            String startDateString = argMultimap.getValue(PREFIX_START_DATE).get().trim();
            if (!startDateString.isEmpty()) {
                startDate = Optional.of(parseDate(startDateString, "start date"));
            }
        }

        // Parse end date (optional)
        Optional<LocalDate> endDate = Optional.empty();
        if (argMultimap.getValue(PREFIX_END_DATE).isPresent()) {
            String endDateString = argMultimap.getValue(PREFIX_END_DATE).get().trim();
            if (!endDateString.isEmpty()) {
                endDate = Optional.of(parseDate(endDateString, "end date"));
            }
        }

        // Validate date range if both dates are provided
        if (startDate.isPresent() && endDate.isPresent()) {
            if (startDate.get().isAfter(endDate.get())) {
                throw new ParseException("Start date must be before or equal to end date.");
            }
        }

        // Parse client name (optional)
        Optional<String> clientName = argMultimap.getValue(PREFIX_NAME)
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

        RevenueFilterPredicate predicate = new RevenueFilterPredicate(startDate, endDate,
                clientName, isDelivered);
        return new ListRevenueCommand(predicate);
    }

    /**
     * Parses a date string in d/M/yyyy format into a LocalDate.
     *
     * @param dateString The date string to parse
     * @param fieldName The name of the field (for error messages)
     * @return The parsed LocalDate
     * @throws ParseException If the date string is invalid
     */
    private LocalDate parseDate(String dateString, String fieldName) throws ParseException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new ParseException("Invalid " + fieldName + " format. "
                    + "Expected format: d/M/yyyy (e.g., 25/12/2024)");
        }
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

