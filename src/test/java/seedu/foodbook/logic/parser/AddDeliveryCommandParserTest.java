package seedu.foodbook.logic.parser;

import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.foodbook.logic.commands.CommandTestUtil.COST_DESC_AMY_DELIVERY;
import static seedu.foodbook.logic.commands.CommandTestUtil.COST_DESC_BOB_DELIVERY;
import static seedu.foodbook.logic.commands.CommandTestUtil.DATE_DESC_AMY_DELIVERY;
import static seedu.foodbook.logic.commands.CommandTestUtil.DATE_DESC_BOB_DELIVERY;
import static seedu.foodbook.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static seedu.foodbook.logic.commands.CommandTestUtil.INVALID_TIME_DESC;
import static seedu.foodbook.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.foodbook.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.foodbook.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.foodbook.logic.commands.CommandTestUtil.REMARKS_DESC_AMY_DELIVERY;
import static seedu.foodbook.logic.commands.CommandTestUtil.REMARKS_DESC_BOB_DELIVERY;
import static seedu.foodbook.logic.commands.CommandTestUtil.TIME_DESC_AMY_DELIVERY;
import static seedu.foodbook.logic.commands.CommandTestUtil.TIME_DESC_BOB_DELIVERY;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_REMARKS;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.foodbook.testutil.TypicalDeliveries.BOB_DELIVERY;

import org.junit.jupiter.api.Test;

import seedu.foodbook.logic.Messages;
import seedu.foodbook.logic.commands.AddDeliveryCommand;
import seedu.foodbook.model.delivery.DateTime;
import seedu.foodbook.model.delivery.Delivery;

public class AddDeliveryCommandParserTest {
    private AddDeliveryCommandParser parser = new AddDeliveryCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Delivery expectedDelivery = BOB_DELIVERY;

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE
                + NAME_DESC_BOB
                + DATE_DESC_BOB_DELIVERY
                + TIME_DESC_BOB_DELIVERY
                + REMARKS_DESC_BOB_DELIVERY
                + COST_DESC_BOB_DELIVERY,
                new AddDeliveryCommand(expectedDelivery));
    }

    @Test
    public void parse_repeatedValue_failure() {
        String validExpectedDeliveryString = NAME_DESC_BOB
                + DATE_DESC_BOB_DELIVERY
                + TIME_DESC_BOB_DELIVERY
                + REMARKS_DESC_BOB_DELIVERY
                + COST_DESC_BOB_DELIVERY;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedDeliveryString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple Dates
        assertParseFailure(parser, DATE_DESC_AMY_DELIVERY + validExpectedDeliveryString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE));

        // multiple Times
        assertParseFailure(parser, TIME_DESC_AMY_DELIVERY + validExpectedDeliveryString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TIME));

        // multiple Remarks
        assertParseFailure(parser, REMARKS_DESC_AMY_DELIVERY + validExpectedDeliveryString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_REMARKS));

        // multiple Times
        assertParseFailure(parser, COST_DESC_AMY_DELIVERY + validExpectedDeliveryString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COST));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedDeliveryString + NAME_DESC_BOB + DATE_DESC_AMY_DELIVERY + TIME_DESC_AMY_DELIVERY
                        + REMARKS_DESC_AMY_DELIVERY + COST_DESC_AMY_DELIVERY
                        + validExpectedDeliveryString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_DATE, PREFIX_TIME,
                        PREFIX_REMARKS, PREFIX_COST));

        // invalid value followed by valid value

        // invalid date
        assertParseFailure(parser, INVALID_DATE_DESC + validExpectedDeliveryString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE));

        // invalid time
        assertParseFailure(parser, INVALID_TIME_DESC + validExpectedDeliveryString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TIME));

        // valid value followed by invalid value

        // invalid date
        assertParseFailure(parser, validExpectedDeliveryString + INVALID_DATE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE));

        // invalid time
        assertParseFailure(parser, validExpectedDeliveryString + INVALID_TIME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TIME));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddDeliveryCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser,
                VALID_NAME_BOB
                        + DATE_DESC_BOB_DELIVERY
                        + TIME_DESC_BOB_DELIVERY
                        + REMARKS_DESC_BOB_DELIVERY
                        + COST_DESC_BOB_DELIVERY,
                expectedMessage);

        // missing date prefix
        assertParseFailure(parser,
                NAME_DESC_BOB
                        + BOB_DELIVERY.getDeliveryDate().getDateString()
                        + TIME_DESC_BOB_DELIVERY
                        + REMARKS_DESC_BOB_DELIVERY
                        + COST_DESC_BOB_DELIVERY,
                expectedMessage);

        // missing time prefix
        assertParseFailure(parser,
                NAME_DESC_BOB
                        + DATE_DESC_BOB_DELIVERY
                        + BOB_DELIVERY.getDeliveryDate().getTimeString()
                        + REMARKS_DESC_BOB_DELIVERY
                        + COST_DESC_BOB_DELIVERY,
                expectedMessage);

        // missing remarks prefix
        assertParseFailure(parser,
                NAME_DESC_BOB
                        + DATE_DESC_BOB_DELIVERY
                        + TIME_DESC_BOB_DELIVERY
                        + BOB_DELIVERY.getRemarks()
                        + COST_DESC_BOB_DELIVERY,
                expectedMessage);

        // missing cost prefix
        assertParseFailure(parser,
                NAME_DESC_BOB
                        + DATE_DESC_BOB_DELIVERY
                        + TIME_DESC_BOB_DELIVERY
                        + REMARKS_DESC_BOB_DELIVERY
                        + BOB_DELIVERY.getCost(),
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {

        // invalid date
        assertParseFailure(parser,
                NAME_DESC_BOB
                        + INVALID_DATE_DESC
                        + TIME_DESC_BOB_DELIVERY
                        + REMARKS_DESC_BOB_DELIVERY
                        + COST_DESC_BOB_DELIVERY,
                DateTime.MESSAGE_CONSTRAINTS);

        // invalid date
        assertParseFailure(parser,
                NAME_DESC_BOB
                        + DATE_DESC_BOB_DELIVERY
                        + INVALID_TIME_DESC
                        + REMARKS_DESC_BOB_DELIVERY
                        + COST_DESC_BOB_DELIVERY,
                DateTime.MESSAGE_CONSTRAINTS);

    }
}
