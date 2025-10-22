package seedu.foodbook.logic.parser;

import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.foodbook.logic.commands.DeleteDeliveryCommand;

public class DeleteDeliveryCommandParserTest {

    private DeleteDeliveryCommandParser parser = new DeleteDeliveryCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteDeliveryCommand() {
        assertParseSuccess(parser, "1", new DeleteDeliveryCommand(1));
    }

    @Test
    public void parse_validArgsWithWhitespace_returnsDeleteDeliveryCommand() {
        assertParseSuccess(parser, "  67  ", new DeleteDeliveryCommand(67));
    }

    @Test
    public void parse_validLargeDeliveryId_returnsDeleteDeliveryCommand() {
        assertParseSuccess(parser, "999999", new DeleteDeliveryCommand(999999));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_whitespaceOnly_throwsParseException() {
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_zeroDeliveryId_throwsParseException() {
        assertParseFailure(parser, "0",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_negativeDeliveryId_throwsParseException() {
        assertParseFailure(parser, "-1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_deliveryIdWithDecimal_throwsParseException() {
        assertParseFailure(parser, "1.5",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_deliveryIdWithSpecialCharacters_throwsParseException() {
        assertParseFailure(parser, "1@",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleArguments_throwsParseException() {
        assertParseFailure(parser, "1 2",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeliveryCommand.MESSAGE_USAGE));
    }
}
