package seedu.foodbook.logic.parser;

import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.foodbook.logic.commands.ListRevenueCommand;
import seedu.foodbook.model.delivery.RevenueFilterPredicate;

/**
 * Contains unit tests for ListRevenueCommandParser.
 */
public class ListRevenueCommandParserTest {

    private ListRevenueCommandParser parser = new ListRevenueCommandParser();

    @Test
    public void parse_emptyArg_returnsListRevenueCommand() {
        // No parameters - should return command with empty filters
        RevenueFilterPredicate expectedPredicate = new RevenueFilterPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        ListRevenueCommand expectedCommand = new ListRevenueCommand(expectedPredicate);

        assertParseSuccess(parser, "", expectedCommand);
        assertParseSuccess(parser, "    ", expectedCommand); // whitespace only
    }

    @Test
    public void parse_startDateOnly_returnsListRevenueCommand() {
        String args = " " + PREFIX_START_DATE + "1/1/2024";
        RevenueFilterPredicate expectedPredicate = new RevenueFilterPredicate(
                Optional.of(LocalDate.of(2024, 1, 1)), Optional.empty(),
                Optional.empty(), Optional.empty());
        ListRevenueCommand expectedCommand = new ListRevenueCommand(expectedPredicate);

        assertParseSuccess(parser, args, expectedCommand);
    }

    @Test
    public void parse_endDateOnly_returnsListRevenueCommand() {
        String args = " " + PREFIX_END_DATE + "31/12/2024";
        RevenueFilterPredicate expectedPredicate = new RevenueFilterPredicate(
                Optional.empty(), Optional.of(LocalDate.of(2024, 12, 31)),
                Optional.empty(), Optional.empty());
        ListRevenueCommand expectedCommand = new ListRevenueCommand(expectedPredicate);

        assertParseSuccess(parser, args, expectedCommand);
    }

    @Test
    public void parse_bothDates_returnsListRevenueCommand() {
        String args = " " + PREFIX_START_DATE + "1/1/2024 " + PREFIX_END_DATE + "31/12/2024";
        RevenueFilterPredicate expectedPredicate = new RevenueFilterPredicate(
                Optional.of(LocalDate.of(2024, 1, 1)),
                Optional.of(LocalDate.of(2024, 12, 31)),
                Optional.empty(), Optional.empty());
        ListRevenueCommand expectedCommand = new ListRevenueCommand(expectedPredicate);

        assertParseSuccess(parser, args, expectedCommand);
    }

    @Test
    public void parse_clientNameOnly_returnsListRevenueCommand() {
        String args = " " + PREFIX_NAME + "Alice";
        RevenueFilterPredicate expectedPredicate = new RevenueFilterPredicate(
                Optional.empty(), Optional.empty(),
                Optional.of("Alice"), Optional.empty());
        ListRevenueCommand expectedCommand = new ListRevenueCommand(expectedPredicate);

        assertParseSuccess(parser, args, expectedCommand);
    }

    @Test
    public void parse_statusDelivered_returnsListRevenueCommand() {
        String args = " " + PREFIX_STATUS + "delivered";
        RevenueFilterPredicate expectedPredicate = new RevenueFilterPredicate(
                Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of(true));
        ListRevenueCommand expectedCommand = new ListRevenueCommand(expectedPredicate);

        assertParseSuccess(parser, args, expectedCommand);
    }

    @Test
    public void parse_statusNotDelivered_returnsListRevenueCommand() {
        String args = " " + PREFIX_STATUS + "not_delivered";
        RevenueFilterPredicate expectedPredicate = new RevenueFilterPredicate(
                Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of(false));
        ListRevenueCommand expectedCommand = new ListRevenueCommand(expectedPredicate);

        assertParseSuccess(parser, args, expectedCommand);
    }

    @Test
    public void parse_allParameters_returnsListRevenueCommand() {
        String args = " " + PREFIX_START_DATE + "1/1/2024 "
                + PREFIX_END_DATE + "31/12/2024 "
                + PREFIX_NAME + "Alice "
                + PREFIX_STATUS + "delivered";

        RevenueFilterPredicate expectedPredicate = new RevenueFilterPredicate(
                Optional.of(LocalDate.of(2024, 1, 1)),
                Optional.of(LocalDate.of(2024, 12, 31)),
                Optional.of("Alice"), Optional.of(true));
        ListRevenueCommand expectedCommand = new ListRevenueCommand(expectedPredicate);

        assertParseSuccess(parser, args, expectedCommand);
    }

    @Test
    public void parse_invalidDateFormat_throwsParseException() {
        // Invalid date format
        String args = " " + PREFIX_START_DATE + "01-01-2024";
        assertParseFailure(parser, args,
                "Invalid start date format. Expected format: d/M/yyyy (e.g., 25/12/2024)");
    }

    @Test
    public void parse_invalidDate_throwsParseException() {
        // Invalid date (Feb 31 doesn't exist)
        String args = " " + PREFIX_START_DATE + "31/2/2024";
        assertParseFailure(parser, args,
                "Invalid start date format. Expected format: d/M/yyyy (e.g., 25/12/2024)");
    }

    @Test
    public void parse_invalidStatus_throwsParseException() {
        // Invalid status value
        String args = " " + PREFIX_STATUS + "pending";
        assertParseFailure(parser, args, "Invalid status. Use 'delivered' or 'not_delivered'.");
    }

    @Test
    public void parse_startDateAfterEndDate_throwsParseException() {
        // Start date after end date
        String args = " " + PREFIX_START_DATE + "31/12/2024 " + PREFIX_END_DATE + "1/1/2024";
        assertParseFailure(parser, args, "Start date must be before or equal to end date.");
    }

    @Test
    public void parse_duplicateStartDate_throwsParseException() {
        String args = " " + PREFIX_START_DATE + "1/1/2024 " + PREFIX_START_DATE + "2/1/2024";
        assertParseFailure(parser, args, "Multiple values specified for the following single-valued field(s): sd/");
    }

    @Test
    public void parse_duplicateEndDate_throwsParseException() {
        String args = " " + PREFIX_END_DATE + "31/12/2024 " + PREFIX_END_DATE + "30/12/2024";
        assertParseFailure(parser, args, "Multiple values specified for the following single-valued field(s): ed/");
    }

    @Test
    public void parse_duplicateClientName_throwsParseException() {
        String args = " " + PREFIX_NAME + "Alice " + PREFIX_NAME + "Bob";
        assertParseFailure(parser, args, "Multiple values specified for the following single-valued field(s): n/");
    }

    @Test
    public void parse_duplicateStatus_throwsParseException() {
        String args = " " + PREFIX_STATUS + "delivered " + PREFIX_STATUS + "not_delivered";
        assertParseFailure(parser, args, "Multiple values specified for the following single-valued field(s): s/");
    }

    @Test
    public void parse_preamblePresent_throwsParseException() {
        // Preamble before prefixes
        String args = "some preamble " + PREFIX_STATUS + "delivered";
        assertParseFailure(parser, args,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListRevenueCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyStartDate_returnsListRevenueCommand() {
        // Empty start date value should be treated as not provided
        String args = " " + PREFIX_START_DATE + " " + PREFIX_END_DATE + "31/12/2024";
        RevenueFilterPredicate expectedPredicate = new RevenueFilterPredicate(
                Optional.empty(), Optional.of(LocalDate.of(2024, 12, 31)),
                Optional.empty(), Optional.empty());
        ListRevenueCommand expectedCommand = new ListRevenueCommand(expectedPredicate);

        assertParseSuccess(parser, args, expectedCommand);
    }

    @Test
    public void parse_emptyClientName_returnsListRevenueCommand() {
        // Empty client name value should be treated as not provided
        String args = " " + PREFIX_NAME + " " + PREFIX_STATUS + "delivered";
        RevenueFilterPredicate expectedPredicate = new RevenueFilterPredicate(
                Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of(true));
        ListRevenueCommand expectedCommand = new ListRevenueCommand(expectedPredicate);

        assertParseSuccess(parser, args, expectedCommand);
    }

    @Test
    public void parse_emptyStatus_returnsListRevenueCommand() {
        // Empty status value should be treated as not provided
        String args = " " + PREFIX_STATUS + " " + PREFIX_NAME + "Alice";
        RevenueFilterPredicate expectedPredicate = new RevenueFilterPredicate(
                Optional.empty(), Optional.empty(),
                Optional.of("Alice"), Optional.empty());
        ListRevenueCommand expectedCommand = new ListRevenueCommand(expectedPredicate);

        assertParseSuccess(parser, args, expectedCommand);
    }

    @Test
    public void parse_clientNameWithSpaces_returnsListRevenueCommand() {
        // Client name with spaces should be preserved
        String args = " " + PREFIX_NAME + "Alice Pauline";
        RevenueFilterPredicate expectedPredicate = new RevenueFilterPredicate(
                Optional.empty(), Optional.empty(),
                Optional.of("Alice Pauline"), Optional.empty());
        ListRevenueCommand expectedCommand = new ListRevenueCommand(expectedPredicate);

        assertParseSuccess(parser, args, expectedCommand);
    }

    @Test
    public void parse_leapYearDate_returnsListRevenueCommand() {
        // Test leap year date (Feb 29, 2024)
        String args = " " + PREFIX_START_DATE + "29/2/2024";
        RevenueFilterPredicate expectedPredicate = new RevenueFilterPredicate(
                Optional.of(LocalDate.of(2024, 2, 29)), Optional.empty(),
                Optional.empty(), Optional.empty());
        ListRevenueCommand expectedCommand = new ListRevenueCommand(expectedPredicate);

        assertParseSuccess(parser, args, expectedCommand);
    }

    @Test
    public void parse_nonLeapYearDate_throwsParseException() {
        // Test non-leap year date (Feb 29, 2023 doesn't exist)
        String args = " " + PREFIX_START_DATE + "29/2/2023";
        assertParseFailure(parser, args,
                "Invalid start date format. Expected format: d/M/yyyy (e.g., 25/12/2024)");
    }
}

