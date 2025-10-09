package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindDeliveryCommand;
import seedu.address.model.delivery.DeliveryContainsDatePredicate;

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
}
