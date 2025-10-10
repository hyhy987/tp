package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARKS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddClientCommand;
import seedu.address.logic.commands.AddDeliveryCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.delivery.DateTime;

/**
 * Parses input arguments and creates a new AddDeliveryCommand object.
 */
public class AddDeliveryCommandParser implements Parser<AddDeliveryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddDeliveryCommand
     * and returns an AddDeliveryCommand object for execution.
     *
     * Expected format: add_delivery n/CLIENT_NAME d/DATE tm/TIME r/REMARKS c/COST
     *
     * @param args The string containing user input arguments.
     * @return An AddDeliveryCommand object ready for execution.
     * @throws ParseException If the user input does not conform to the expected format,
     *                       or if any required parameters are missing or invalid.
     */
    public AddDeliveryCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DATE, PREFIX_TIME,
                        PREFIX_REMARKS, PREFIX_COST);

        // Check that all required prefixes are present and no preamble exists
        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_DATE, PREFIX_TIME,
                PREFIX_REMARKS, PREFIX_COST)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddDeliveryCommand.MESSAGE_USAGE));
        }

        // Ensure no duplicate prefixes
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_DATE, PREFIX_TIME,
                PREFIX_REMARKS, PREFIX_COST);

        // Parse individual components
        String clientName = argMultimap.getValue(PREFIX_NAME).get().trim();
        String date = argMultimap.getValue(PREFIX_DATE).get();
        String time = argMultimap.getValue(PREFIX_TIME).get();
        DateTime dateTime = ParserUtil.parseDateTime(date, time);
        String remarks = ParserUtil.parseRemarks(argMultimap.getValue(PREFIX_REMARKS).get());
        Double cost = ParserUtil.parseCost(argMultimap.getValue(PREFIX_COST).get());

        return new AddDeliveryCommand(clientName, dateTime, remarks, cost);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     *
     * This is a utility method to check that all required parameters are present.
     *
     * @param argumentMultimap The ArgumentMultimap to check.
     * @param prefixes The prefixes that must be present.
     * @return True if all prefixes are present with non-empty values, false otherwise.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
