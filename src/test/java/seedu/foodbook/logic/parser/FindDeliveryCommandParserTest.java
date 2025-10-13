package seedu.foodbook.logic.parser;

import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.foodbook.logic.commands.FindDeliveryCommand;
import seedu.foodbook.model.delivery.DeliveryContainsDatePredicate;

/**
 * Contains unit tests for {@code FindDeliveryCommandParser}.
 */
public class FindDeliveryCommandParserTest {

    private FindDeliveryCommandParser parser = new FindDeliveryCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindDeliveryCommand() {
        // no leading and trailing whitespaces
        FindDeliveryCommand expectedFindDeliveryCommand =
                new FindDeliveryCommand(new DeliveryContainsDatePredicate("25/12/2024"));
        assertParseSuccess(parser, "25/12/2024", expectedFindDeliveryCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " 25/12/2024 ", expectedFindDeliveryCommand);
    }

    @Test
    public void parse_blankArg_throwsParseException() {
        // truly empty string (after trim still empty)
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDate_throwsParseException() {
        // clearly invalid date for your dd/MM/yyyy format (and should fail DateTime.isValidDate)
        assertParseFailure(parser, "31/02/2024",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindDeliveryCommand.MESSAGE_USAGE));
    }
}
