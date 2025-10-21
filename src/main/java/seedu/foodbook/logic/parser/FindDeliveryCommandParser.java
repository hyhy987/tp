package seedu.foodbook.logic.parser;

import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
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

        // If no arguments provided, return command that shows all deliveries
        if (trimmedArgs.isEmpty()) {
            return new FindDeliveryCommand(new DeliveryPredicate(
                    Optional.empty(), Optional.empty(), Optional.empty()));
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DATE, PREFIX_TAG);

        // Check for invalid preamble (text before any prefix)
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindDeliveryCommand.MESSAGE_USAGE));
        }

        // Verify no duplicate prefixes for single-value fields
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_DATE);

        Optional<String> clientName = argMultimap.getValue(PREFIX_NAME);
        Optional<String> date = argMultimap.getValue(PREFIX_DATE);
        Optional<List<String>> tags = parseTags(argMultimap);

        // Validate client name is not empty if provided
        if (clientName.isPresent() && clientName.get().trim().isEmpty()) {
            throw new ParseException("Client name cannot be empty.");
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

        // Validate tags are not empty if provided
        if (tags.isPresent()) {
            if (tags.get().isEmpty()) {
                throw new ParseException("At least one tag must be provided when using t/ prefix.");
            }
            if (tags.get().stream().anyMatch(String::isEmpty)) {
                throw new ParseException("Tags cannot be empty.");
            }
        }

        return new FindDeliveryCommand(new DeliveryPredicate(clientName, date, tags));
    }

    /**
     * Parses all tag values from the argument multimap.
     * Returns Optional.empty() if no tags are present.
     * Filters out empty/whitespace-only tags.
     */
    private Optional<List<String>> parseTags(ArgumentMultimap argMultimap) {
        List<String> tagList = argMultimap.getAllValues(PREFIX_TAG);
        if (tagList.isEmpty()) {
            return Optional.empty();
        }

        List<String> filteredTags = tagList.stream()
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .toList();

        if (filteredTags.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(filteredTags);
    }
}
