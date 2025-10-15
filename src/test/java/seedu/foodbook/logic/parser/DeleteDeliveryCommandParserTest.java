package seedu.foodbook.logic.parser;

import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.foodbook.logic.commands.DeleteDeliveryCommand;

/**
 * Contains unit tests for {@code DeleteDeliveryCommandParser}.
 */
public class DeleteDeliveryCommandParserTest {

    private DeleteDeliveryCommandParser parser = new DeleteDeliveryCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteDeliveryCommand() {
        // Test with typical delivery ID
        assertParseSuccess(parser, "67", new DeleteDeliveryCommand(67));

        // Test with single digit
        assertParseSuccess(parser, "1", new DeleteDeliveryCommand(1));

        // Test with large number
        assertParseSuccess(parser, "99999", new DeleteDeliveryCommand(99999));
    }

    @Test
    public void parse_validArgsWithWhitespace_returnsDeleteDeliveryCommand() {
        // Leading whitespace
        assertParseSuccess(parser, "  67", new DeleteDeliveryCommand(67));

        // Trailing whitespace
        assertParseSuccess(parser, "67  ", new DeleteDeliveryCommand(67));

        // Both leading and trailing whitespace
        assertParseSuccess(parser, "  67  ", new DeleteDeliveryCommand(67));

        // Multiple spaces
        assertParseSuccess(parser, "    67    ", new DeleteDeliveryCommand(67));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Empty string
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));

        // Only whitespace
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonNumericArgs_throwsParseException() {
        // Alphabetic characters
        assertParseFailure(parser, "abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));

        // Alphanumeric mix
        assertParseFailure(parser, "12abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));

        // Special characters
        assertParseFailure(parser, "!@#",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));

        // Mix of numbers and special characters
        assertParseFailure(parser, "12#45",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_negativeNumber_throwsParseException() {
        assertParseFailure(parser, "-1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_zero_throwsParseException() {
        assertParseFailure(parser, "0",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_decimalNumber_throwsParseException() {
        // Decimal with dot
        assertParseFailure(parser, "1.5",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));

        // Decimal with comma
        assertParseFailure(parser, "1,5",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleNumbers_throwsParseException() {
        // Multiple numbers separated by space
        assertParseFailure(parser, "1 2",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));

        // Multiple numbers separated by comma
        assertParseFailure(parser, "1,2",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_leadingZeros_returnsDeleteDeliveryCommand() {
        // Leading zeros should be handled correctly
        assertParseSuccess(parser, "007", new DeleteDeliveryCommand(7));
        assertParseSuccess(parser, "00123", new DeleteDeliveryCommand(123));
    }

    @Test
    public void parse_maxInteger_returnsDeleteDeliveryCommand() {
        // Test with Integer.MAX_VALUE
        String maxInt = String.valueOf(Integer.MAX_VALUE);
        assertParseSuccess(parser, maxInt, new DeleteDeliveryCommand(Integer.MAX_VALUE));
    }

    @Test
    public void parse_overflowInteger_throwsParseException() {
        // Test with number larger than Integer.MAX_VALUE
        String overflowNumber = "9999999999999999999";
        assertParseFailure(parser, overflowNumber,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_scientificNotation_throwsParseException() {
        assertParseFailure(parser, "1e5",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "1E5",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }
}
