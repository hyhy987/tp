package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.COST_DESC_AMY_DELIVERY;
import static seedu.address.logic.commands.CommandTestUtil.COST_DESC_BOB_DELIVERY;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_AMY_DELIVERY;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_BOB_DELIVERY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TIME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.REMARKS_DESC_AMY_DELIVERY;
import static seedu.address.logic.commands.CommandTestUtil.REMARKS_DESC_BOB_DELIVERY;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.TIME_DESC_AMY_DELIVERY;
import static seedu.address.logic.commands.CommandTestUtil.TIME_DESC_BOB_DELIVERY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARKS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalDeliveries.ALICE_DELIVERY;
import static seedu.address.testutil.TypicalDeliveries.AMY_DELIVERY;
import static seedu.address.testutil.TypicalDeliveries.BOB_DELIVERY;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddClientCommand;
import seedu.address.logic.commands.AddDeliveryCommand;
import seedu.address.model.delivery.DateTime;
import seedu.address.model.delivery.Delivery;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.DeliveryBuilder;
import seedu.address.testutil.PersonBuilder;

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
