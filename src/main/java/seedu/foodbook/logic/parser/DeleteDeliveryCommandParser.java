package seedu.foodbook.logic.parser;

import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.foodbook.commons.core.index.Index;
import seedu.foodbook.logic.commands.DeleteDeliveryCommand;
import seedu.foodbook.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteDeliveryCommand object.
 *
 * ALTERNATIVE VERSION: Uses Index (display index) instead of delivery ID.
 */
public class DeleteDeliveryCommandParser implements Parser<DeleteDeliveryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteDeliveryCommand
     * and returns a DeleteDeliveryCommand object for execution.
     */
    public DeleteDeliveryCommand parse(String args) throws ParseException {
        try {
            String trimmedArgs = args.trim();
            if (trimmedArgs.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
            }

            Integer deliveryId = ParserUtil.parseDeliveryId(trimmedArgs);  // ✅ Using it here!
            return new DeleteDeliveryCommand(deliveryId);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE), pe);
        }
    }
}
