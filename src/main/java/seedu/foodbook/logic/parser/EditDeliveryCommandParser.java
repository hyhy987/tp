package seedu.foodbook.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_DELIVERY_TAG;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_REMARKS;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.Optional;
import java.util.stream.Stream;

import seedu.foodbook.logic.commands.EditDeliveryCommand;
import seedu.foodbook.logic.commands.EditDeliveryCommand.EditDeliveryDescriptor;
import seedu.foodbook.logic.parser.exceptions.ParseException;
import seedu.foodbook.model.delivery.DateTime;
import seedu.foodbook.model.tag.DeliveryTag;

/**
 * Parses input arguments and creates a new EditDeliveryCommand object
 */
public class EditDeliveryCommandParser implements Parser<EditDeliveryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditDeliveryCommand
     * and returns an EditDeliveryCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public EditDeliveryCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DATE, PREFIX_TIME,
                        PREFIX_REMARKS, PREFIX_COST, PREFIX_DELIVERY_TAG);

        Integer deliveryId;

        if (argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditDeliveryCommand.MESSAGE_USAGE));
        }

        try {
            deliveryId = ParserUtil.parseDeliveryId(argMultimap.getPreamble().trim());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditDeliveryCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_DATE, PREFIX_TIME,
                PREFIX_REMARKS, PREFIX_COST, PREFIX_DELIVERY_TAG);

        EditDeliveryDescriptor editDeliveryDescriptor = new EditDeliveryDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editDeliveryDescriptor.setClientName(argMultimap.getValue(PREFIX_NAME).get().trim());
        }

        // Handle date and time together to create DateTime
        if (argMultimap.getValue(PREFIX_DATE).isPresent() ^ argMultimap.getValue(PREFIX_TIME).isPresent()) {
            throw new ParseException("Both date and time must be provided together to update delivery date/time.");
        }
        if (argMultimap.getValue(PREFIX_DATE).isPresent() && argMultimap.getValue(PREFIX_TIME).isPresent()) {
            String date = argMultimap.getValue(PREFIX_DATE).get();
            String time = argMultimap.getValue(PREFIX_TIME).get();
            DateTime dateTime = ParserUtil.parseDateTime(date, time);
            editDeliveryDescriptor.setDateTime(dateTime);
        }

        if (argMultimap.getValue(PREFIX_REMARKS).isPresent()) {
            editDeliveryDescriptor.setRemarks(ParserUtil.parseRemarks(argMultimap.getValue(PREFIX_REMARKS).get()));
        }

        if (argMultimap.getValue(PREFIX_COST).isPresent()) {
            editDeliveryDescriptor.setCost(ParserUtil.parseCost(argMultimap.getValue(PREFIX_COST).get()));
        }

        Optional<DeliveryTag> parsedTag = ParserUtil.parseOptionalDeliveryTag(
                argMultimap.getAllValues(PREFIX_DELIVERY_TAG)
        );
        parsedTag.ifPresent(editDeliveryDescriptor::setTag);

        if (!editDeliveryDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditDeliveryCommand.MESSAGE_NOT_EDITED);
        }

        return new EditDeliveryCommand(deliveryId, editDeliveryDescriptor);
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
