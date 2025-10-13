package seedu.foodbook.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.foodbook.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.foodbook.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.foodbook.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.foodbook.testutil.Assert.assertThrows;
import static seedu.foodbook.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.foodbook.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.foodbook.logic.commands.AddClientCommand;
import seedu.foodbook.logic.commands.AddDeliveryCommand;
import seedu.foodbook.logic.commands.ClearCommand;
import seedu.foodbook.logic.commands.DeleteCommand;
import seedu.foodbook.logic.commands.EditClientCommand;
import seedu.foodbook.logic.commands.ExitCommand;
import seedu.foodbook.logic.commands.FindClientCommand;
import seedu.foodbook.logic.commands.FindDeliveryCommand;
import seedu.foodbook.logic.commands.HelpCommand;
import seedu.foodbook.logic.commands.ListCommand;
import seedu.foodbook.logic.commands.MarkCommand;
import seedu.foodbook.logic.commands.UnmarkCommand;
import seedu.foodbook.logic.parser.exceptions.ParseException;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.testutil.DeliveryBuilder;
import seedu.foodbook.testutil.DeliveryUtil;
import seedu.foodbook.testutil.EditPersonDescriptorBuilder;
import seedu.foodbook.testutil.PersonBuilder;
import seedu.foodbook.testutil.PersonUtil;

public class FoodBookParserTest {

    private final FoodBookParser parser = new FoodBookParser();

    @Test
    public void parseCommand_addClient() throws Exception {
        Person person = new PersonBuilder().build();
        AddClientCommand command = (AddClientCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddClientCommand(person), command);
    }

    @Test
    public void parseCommand_addDelivery() throws Exception {
        Delivery delivery = new DeliveryBuilder().build();
        AddDeliveryCommand command = (AddDeliveryCommand) parser.parseCommand(DeliveryUtil.getAddCommand(delivery));
        assertEquals(new AddDeliveryCommand(delivery), command);
    }

    @Test
    public void parseCommand_mark() throws Exception {
        MarkCommand command = (MarkCommand) parser.parseCommand(MarkCommand.COMMAND_WORD + " 1");
        assertEquals(new MarkCommand(1), command);
    }

    @Test
    public void parseCommand_unmark() throws Exception {
        UnmarkCommand command = (UnmarkCommand) parser.parseCommand(UnmarkCommand.COMMAND_WORD + " 1");
        assertEquals(new UnmarkCommand(1), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditClientCommand.EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditClientCommand command = (EditClientCommand) parser.parseCommand(EditClientCommand.COMMAND_WORD + " "
                + BOB.getName().fullName + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditClientCommand(BOB.getName().fullName, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_findClient() throws Exception {
        String findClientCommand = FindClientCommand.COMMAND_WORD + NAME_DESC_BOB + PHONE_DESC_BOB;
        FindClientCommand command = (FindClientCommand) parser.parseCommand(findClientCommand);
        assertTrue(command instanceof FindClientCommand);
    }

    @Test
    public void parseCommand_findDelivery() throws Exception {
        String findDeliveryCommand = FindDeliveryCommand.COMMAND_WORD + " 12/10/2003";
        FindDeliveryCommand command = (FindDeliveryCommand) parser.parseCommand(findDeliveryCommand);
        assertTrue(command instanceof FindDeliveryCommand);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_markDelivery() throws Exception {
        assertTrue(parser.parseCommand(MarkCommand.COMMAND_WORD + " 3") instanceof MarkCommand);
    }

    @Test
    public void parseCommand_unmarkDelivery() throws Exception {
        assertTrue(parser.parseCommand(UnmarkCommand.COMMAND_WORD + " 3") instanceof UnmarkCommand);
    }


    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
