package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UnmarkCommand;

public class UnmarkCommandParserTest {

    private UnmarkCommandParser parser = new UnmarkCommandParser();

    @Test
    public void parse_validArgs_returnsUnmarkCommand() {
        assertParseSuccess(parser, "1", new UnmarkCommand(1));
        assertParseSuccess(parser, " 1 ", new UnmarkCommand(1));
        assertParseSuccess(parser, "123", new UnmarkCommand(123));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // empty string
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));

        // whitespace only
        assertParseFailure(parser, "   ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));

        // non-numeric
        assertParseFailure(parser, "abc", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));

        // negative number
        assertParseFailure(parser, "-1", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));

        // zero
        assertParseFailure(parser, "0", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));

        // decimal number
        assertParseFailure(parser, "1.5", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));

        // multiple arguments
        assertParseFailure(parser, "1 2", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));
    }
}
