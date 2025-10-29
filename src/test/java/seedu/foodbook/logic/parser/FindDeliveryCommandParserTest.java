package seedu.foodbook.logic.parser;

import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.foodbook.logic.commands.FindDeliveryCommand;
import seedu.foodbook.model.delivery.DeliveryPredicate;

/**
 * Contains unit tests for {@code FindDeliveryCommandParser}.
 */
public class FindDeliveryCommandParserTest {

    private FindDeliveryCommandParser parser = new FindDeliveryCommandParser();

    @Test
    public void parse_emptyArg_returnsFalse() {

        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validClientName_returnsFindDeliveryCommand() {
        FindDeliveryCommand expectedCommand = new FindDeliveryCommand(
                new DeliveryPredicate(Optional.empty(), Optional.empty(),
                        Optional.of("John Doe"), Optional.empty(), Optional.empty()));
        assertParseSuccess(parser, " n/John Doe", expectedCommand);

        // Multiple whitespaces
        assertParseSuccess(parser, "  n/John Doe  ", expectedCommand);
    }

    @Test
    public void parse_validDate_returnsFindDeliveryCommand() {
        FindDeliveryCommand expectedCommand = new FindDeliveryCommand(
                new DeliveryPredicate(Optional.of("25/12/2024"), Optional.of("25/12/2024"),
                        Optional.empty(), Optional.empty(), Optional.empty()));
        assertParseSuccess(parser, " d/25/12/2024", expectedCommand);
    }

    @Test
    public void parse_validSingleTag_returnsFindDeliveryCommand() {
        FindDeliveryCommand expectedCommand = new FindDeliveryCommand(
                new DeliveryPredicate(Optional.empty(), Optional.empty(),
                        Optional.empty(), Optional.of("urgent"), Optional.empty()));
        assertParseSuccess(parser, " t/urgent", expectedCommand);
    }

    @Test
    public void parse_validCombinedFilters_returnsFindDeliveryCommand() {

        // Client name + date
        FindDeliveryCommand expectedCommand1 = new FindDeliveryCommand(
                new DeliveryPredicate(Optional.of("25/12/2024"), Optional.of("25/12/2024"),
                        Optional.of("John"), Optional.empty(), Optional.empty()));
        assertParseSuccess(parser, " n/John d/25/12/2024", expectedCommand1);

        // Client name + tags
        FindDeliveryCommand expectedCommand2 = new FindDeliveryCommand(
                new DeliveryPredicate(Optional.empty(), Optional.empty(),
                        Optional.of("John"), Optional.of("urgent"), Optional.empty()));
        assertParseSuccess(parser, " n/John t/urgent", expectedCommand2);

        // Date + tags
        FindDeliveryCommand expectedCommand3 = new FindDeliveryCommand(
                new DeliveryPredicate(Optional.of("25/12/2024"), Optional.of("25/12/2024"),
                        Optional.empty(), Optional.of("urgent"), Optional.empty()));
        assertParseSuccess(parser, " d/25/12/2024 t/urgent", expectedCommand3);

        // All three filters
        FindDeliveryCommand expectedCommand4 = new FindDeliveryCommand(
                new DeliveryPredicate(Optional.of("25/12/2024"), Optional.of("25/12/2024"),
                        Optional.of("John Doe"), Optional.of("urgent"), Optional.empty()));
        assertParseSuccess(parser, " n/John Doe d/25/12/2024 t/urgent", expectedCommand4);
    }

    @Test
    public void parse_emptyDateValue_throwsParseException() {
        // Line 61: Check if dateValue.isEmpty() is true
        // Line 62: throw ParseException for empty date
        assertParseFailure(parser, " d/", "Date cannot be empty.");
    }

    @Test
    public void parse_dateWithOnlyWhitespace_throwsParseException() {
        // Line 61: Check if dateValue.trim().isEmpty() is true
        // Line 62: throw ParseException for whitespace-only date
        assertParseFailure(parser, " d/   ", "Date cannot be empty.");
        assertParseFailure(parser, " d/\t\n  ", "Date cannot be empty.");
    }

    @Test
    public void parse_invalidDate_throwsParseException() {
        assertParseFailure(parser, " d/31/02/2024",
                "Invalid date format. Please use d/M/yyyy format (e.g., 25/12/2024).");
        assertParseFailure(parser, " d/invalid",
                "Invalid date format. Please use d/M/yyyy format (e.g., 25/12/2024).");
    }

    @Test
    public void parse_emptyClientName_throwsParseException() {
        assertParseFailure(parser, " n/", "Client name cannot be empty.");
        assertParseFailure(parser, " n/   ", "Client name cannot be empty.");
    }

    @Test
    public void parse_preamblePresent_throwsParseException() {
        // Text before any prefix
        assertParseFailure(parser, "some text n/John",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noTagPrefix_returnsPredicateWithoutTags() {
        // Line 88-90: If tagList.isEmpty(), return Optional.empty()
        // This is the normal path when no t/ prefix is provided
        FindDeliveryCommand expectedCommand = new FindDeliveryCommand(
                new DeliveryPredicate(Optional.empty(), Optional.empty(),
                        Optional.of("John"), Optional.empty(), Optional.empty()));
        assertParseSuccess(parser, " n/John", expectedCommand);
    }
}
