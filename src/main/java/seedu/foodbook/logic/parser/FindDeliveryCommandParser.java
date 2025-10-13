package seedu.foodbook.logic.parser;

import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.foodbook.logic.commands.FindDeliveryCommand;
import seedu.foodbook.logic.parser.exceptions.ParseException;
import seedu.foodbook.model.delivery.DateTime;
import seedu.foodbook.model.delivery.DeliveryContainsDatePredicate;

/**
 * Parses input arguments and creates a new FindDeliveryCommand object
 */
public class FindDeliveryCommandParser implements Parser<FindDeliveryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindDeliveryCommand
     * and returns a FindDeliveryCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindDeliveryCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindDeliveryCommand.MESSAGE_USAGE));
        }

        String date = trimmedArgs;

        if (!DateTime.isValidDate(date)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindDeliveryCommand.MESSAGE_USAGE));
        }

        return new FindDeliveryCommand(new DeliveryContainsDatePredicate(date));
    }

}
