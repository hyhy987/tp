package seedu.foodbook.logic.parser;

import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.foodbook.logic.commands.FindClientCommand;
import seedu.foodbook.model.person.ClientMatchesPredicate;

public class FindClientCommandParserTest {

    private final FindClientCommandParser parser = new FindClientCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindClientCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noValidPrefix_throwsParseException() {
        assertParseFailure(parser, VALID_NAME_BOB,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindClientCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noPrefixValue_throwsParseException() {
        // Empty name value
        assertParseFailure(parser, " " + PREFIX_NAME,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindClientCommand.MESSAGE_USAGE));

        // Empty phone value
        assertParseFailure(parser, " " + PREFIX_PHONE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindClientCommand.MESSAGE_USAGE));

        // Empty email value
        assertParseFailure(parser, " " + PREFIX_EMAIL,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindClientCommand.MESSAGE_USAGE));

        // All empty values
        assertParseFailure(parser, " " + PREFIX_NAME + " " + PREFIX_PHONE + " " + PREFIX_EMAIL,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindClientCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validNameOnly_returnsFindClientCommand() {
        ClientMatchesPredicate expectedPredicate =
                new ClientMatchesPredicate(Optional.of("Alice"), Optional.empty(), Optional.empty());
        FindClientCommand expectedCommand = new FindClientCommand(expectedPredicate);

        // no leading and trailing whitespaces
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alice", expectedCommand);

        // multiple whitespaces between prefix and value
        assertParseSuccess(parser, " " + PREFIX_NAME + " \n Alice  \t", expectedCommand);
    }

    @Test
    public void parse_validPhoneOnly_returnsFindClientCommand() {
        ClientMatchesPredicate expectedPredicate =
                new ClientMatchesPredicate(Optional.empty(), Optional.of("12345678"), Optional.empty());
        FindClientCommand expectedCommand = new FindClientCommand(expectedPredicate);

        assertParseSuccess(parser, " " + PREFIX_PHONE + "12345678", expectedCommand);
    }

    @Test
    public void parse_validEmailOnly_returnsFindClientCommand() {
        ClientMatchesPredicate expectedPredicate =
                new ClientMatchesPredicate(Optional.empty(), Optional.empty(), Optional.of("alice@example.com"));
        FindClientCommand expectedCommand = new FindClientCommand(expectedPredicate);

        assertParseSuccess(parser, " " + PREFIX_EMAIL + "alice@example.com", expectedCommand);
    }

    @Test
    public void parse_validMultipleFields_returnsFindClientCommand() {
        // Name and phone
        ClientMatchesPredicate predicate1 =
                new ClientMatchesPredicate(Optional.of("Alice"), Optional.of("12345678"), Optional.empty());
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alice " + PREFIX_PHONE + "12345678",
                new FindClientCommand(predicate1));

        // Name and email
        ClientMatchesPredicate predicate2 =
                new ClientMatchesPredicate(Optional.of("Alice"), Optional.empty(), Optional.of("alice@example.com"));
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alice " + PREFIX_EMAIL + "alice@example.com",
                new FindClientCommand(predicate2));

        // Phone and email
        ClientMatchesPredicate predicate3 =
                new ClientMatchesPredicate(Optional.empty(), Optional.of("12345678"),
                        Optional.of("alice@example.com"));
        assertParseSuccess(parser, " " + PREFIX_PHONE + "12345678 " + PREFIX_EMAIL + "alice@example.com",
                new FindClientCommand(predicate3));

        // All three fields
        ClientMatchesPredicate predicate4 =
                new ClientMatchesPredicate(Optional.of("Alice"), Optional.of("12345678"),
                        Optional.of("alice@example.com"));
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alice " + PREFIX_PHONE + "12345678 "
                + PREFIX_EMAIL + "alice@example.com", new FindClientCommand(predicate4));
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        // Duplicate name prefix
        assertParseFailure(parser, " " + PREFIX_NAME + "Alice " + PREFIX_NAME + "Bob",
                "Multiple values specified for the following single-valued field(s): n/");

        // Duplicate phone prefix
        assertParseFailure(parser, " " + PREFIX_PHONE + "12345678 " + PREFIX_PHONE + "87654321",
                "Multiple values specified for the following single-valued field(s): p/");

        // Duplicate email prefix
        assertParseFailure(parser, " " + PREFIX_EMAIL + "alice@example.com " + PREFIX_EMAIL + "bob@example.com",
                "Multiple values specified for the following single-valued field(s): e/");
    }

    @Test
    public void parse_preamblePresent_throwsParseException() {
        // Preamble before valid command
        assertParseFailure(parser, "some random text " + PREFIX_NAME + "Alice",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindClientCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgsWithWhitespace_returnsFindClientCommand() {
        // Leading and trailing whitespaces in values are trimmed
        ClientMatchesPredicate expectedPredicate =
                new ClientMatchesPredicate(Optional.of("Alice"), Optional.of("12345678"),
                        Optional.of("alice@example.com"));
        FindClientCommand expectedCommand = new FindClientCommand(expectedPredicate);

        assertParseSuccess(parser, " " + PREFIX_NAME + "  Alice  " + PREFIX_PHONE + "  12345678  "
                + PREFIX_EMAIL + "  alice@example.com  ", expectedCommand);
    }
}
