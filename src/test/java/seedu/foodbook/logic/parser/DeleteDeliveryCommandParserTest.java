package seedu.foodbook.logic.parser;

import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.foodbook.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.foodbook.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.foodbook.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.jupiter.api.Test;

import seedu.foodbook.logic.commands.DeleteDeliveryCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteDeliveryCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteDeliveryCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteDeliveryCommandParserTest {

    private DeleteDeliveryCommandParser parser = new DeleteDeliveryCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteDeliveryCommand() {
        assertParseSuccess(parser, "1", new DeleteDeliveryCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validArgsWithWhitespace_returnsDeleteDeliveryCommand() {
        // Leading and trailing whitespaces should be trimmed
        assertParseSuccess(parser, "  1  ", new DeleteDeliveryCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validMultipleDigitIndex_returnsDeleteDeliveryCommand() {
        // Test with index 2
        assertParseSuccess(parser, "2", new DeleteDeliveryCommand(INDEX_SECOND_PERSON));

        // Test with index 3
        assertParseSuccess(parser, "3", new DeleteDeliveryCommand(INDEX_THIRD_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Non-numeric input
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidNegativeIndex_throwsParseException() {
        // Negative index
        assertParseFailure(parser, "-1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidZeroIndex_throwsParseException() {
        // Zero index (indices should start from 1)
        assertParseFailure(parser, "0",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        // Empty string
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));

        // Only whitespaces
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidMixedInput_throwsParseException() {
        // Index followed by text
        assertParseFailure(parser, "1 abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));

        // Text followed by index
        assertParseFailure(parser, "abc 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidSpecialCharacters_throwsParseException() {
        // Special characters
        assertParseFailure(parser, "!",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "@#$",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }
}
