package seedu.foodbook.logic.parser;

import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.foodbook.logic.commands.EditDeliveryCommand.MESSAGE_NOT_EDITED;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.foodbook.logic.commands.EditDeliveryCommand;
import seedu.foodbook.logic.commands.EditDeliveryCommand.EditDeliveryDescriptor;
import seedu.foodbook.model.delivery.DateTime;

public class EditDeliveryCommandParserTest {

    private final EditDeliveryCommandParser parser = new EditDeliveryCommandParser();

    // === Basic Error Cases ===

    @Test
    public void parse_missingDeliveryId_throwsParseException() {
        String userInput = " n/Client A";
        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDeliveryId_throwsParseException() {
        String userInput = "a n/Client A";
        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditDeliveryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noFieldsEdited_throwsParseException() {
        String userInput = "1";
        assertParseFailure(parser, userInput, MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        String userInput = "1 n/Client A n/Client B";
        assertParseFailure(parser, userInput,
                "Multiple values specified for the following single-valued field(s): n/");
    }

    // === Date/Time Edge Cases ===

    @Test
    public void parse_partialDateOrTimeOnly_throwsParseException() {
        String onlyDate = "1 d/21/10/2024";
        assertParseFailure(parser, onlyDate,
                "Both date and time must be provided together to update delivery date/time.");

        String onlyTime = "1 tm/1430";
        assertParseFailure(parser, onlyTime,
                "Both date and time must be provided together to update delivery date/time.");
    }

    @Test
    public void parse_invalidDateFormat_throwsParseException() {
        String userInput = "1 d/2024-10-21 tm/1430";
        assertParseFailure(parser, userInput,
                "Date should be in d/M/yyyy format (e.g., 21/10/2003) and time in HHmm format (e.g., 1430, 0800)");
    }

    @Test
    public void parse_invalidTimeFormat_throwsParseException() {
        String userInput = "1 d/21/10/2024 tm/14:30";
        assertParseFailure(parser, userInput,
                "Date should be in d/M/yyyy format (e.g., 21/10/2003) and time in HHmm format (e.g., 1430, 0800)");
    }

    // === Valid Cases ===

    @Test
    public void parse_validAllFields_success() {
        String userInput = "1 n/Client A d/21/10/2024 tm/1430 r/Fragile c/15.50";

        EditDeliveryDescriptor descriptor = new EditDeliveryDescriptor();
        descriptor.setClientName("Client A");
        descriptor.setDateTime(new DateTime("21/10/2024", "1430"));
        descriptor.setRemarks("Fragile");
        descriptor.setCost(15.50);

        EditDeliveryCommand expectedCommand = new EditDeliveryCommand(1, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validNameOnly_success() {
        String userInput = "5 n/John Tan";

        EditDeliveryDescriptor descriptor = new EditDeliveryDescriptor();
        descriptor.setClientName("John Tan");

        EditDeliveryCommand expectedCommand = new EditDeliveryCommand(5, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validRemarksAndCost_success() {
        String userInput = "2 r/Handle with care c/8.90";

        EditDeliveryDescriptor descriptor = new EditDeliveryDescriptor();
        descriptor.setRemarks("Handle with care");
        descriptor.setCost(8.90);

        EditDeliveryCommand expectedCommand = new EditDeliveryCommand(2, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    // === Additional Robustness Tests ===

    @Test
    public void parse_extraWhitespace_success() {
        String userInput = "   3    n/  Alice Tan    d/21/10/2024   tm/1000   r/  Morning delivery   c/10.00   ";

        EditDeliveryDescriptor descriptor = new EditDeliveryDescriptor();
        descriptor.setClientName("Alice Tan");
        descriptor.setDateTime(new DateTime("21/10/2024", "1000"));
        descriptor.setRemarks("Morning delivery");
        descriptor.setCost(10.00);

        EditDeliveryCommand expectedCommand = new EditDeliveryCommand(3, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_costWithInvalidNumberFormat_throwsParseException() {
        String userInput = "1 c/abc";
        assertParseFailure(parser, userInput, "Cost must be a valid number");
    }

    @Test
    public void parse_negativeCost_throwsParseException() {
        String userInput = "1 c/-12.50";
        assertParseFailure(parser, userInput, "Cost must be non-negative");
    }

    @Test
    public void parse_validRemarksOnly_success() {
        String userInput = "7 r/Customer prefers evening delivery";
        EditDeliveryDescriptor descriptor = new EditDeliveryDescriptor();
        descriptor.setRemarks("Customer prefers evening delivery");
        EditDeliveryCommand expectedCommand = new EditDeliveryCommand(7, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
