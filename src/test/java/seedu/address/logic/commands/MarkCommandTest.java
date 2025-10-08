package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalFoodBook.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.delivery.DateTime;
import seedu.address.model.delivery.Delivery;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for MarkCommand.
 */
public class MarkCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validDeliveryId_success() throws Exception {
        // Create a test person and delivery
        Person testPerson = new PersonBuilder().withName("Test Person").build();
        model.addPerson(testPerson);
        
        DateTime testDateTime = new DateTime("15/1/2025", "1000");
        Delivery testDelivery = new Delivery(1, testPerson, testDateTime, "Test delivery", 50.0);
        model.addDelivery(testDelivery);

        MarkCommand markCommand = new MarkCommand(1);

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_DELIVERY_SUCCESS, 
                Messages.format(testDelivery));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Delivery markedDelivery = new Delivery(1, testPerson, testDateTime, "Test delivery", 50.0);
        markedDelivery.markAsDelivered();
        expectedModel.setDelivery(testDelivery, markedDelivery);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidDeliveryId_throwsCommandException() {
        MarkCommand markCommand = new MarkCommand(999);
        assertCommandFailure(markCommand, model, MarkCommand.MESSAGE_DELIVERY_NOT_FOUND);
    }

    @Test
    public void execute_alreadyMarkedDelivery_throwsCommandException() throws Exception {
        // Create a test person and delivery
        Person testPerson = new PersonBuilder().withName("Test Person").build();
        model.addPerson(testPerson);
        
        DateTime testDateTime = new DateTime("15/1/2025", "1000");
        Delivery testDelivery = new Delivery(1, testPerson, testDateTime, "Test delivery", 50.0);
        testDelivery.markAsDelivered(); // Mark as delivered first
        model.addDelivery(testDelivery);

        MarkCommand markCommand = new MarkCommand(1);
        assertCommandFailure(markCommand, model, MarkCommand.MESSAGE_DELIVERY_ALREADY_MARKED);
    }

    @Test
    public void equals() {
        MarkCommand markFirstCommand = new MarkCommand(1);
        MarkCommand markSecondCommand = new MarkCommand(2);

        // same object -> returns true
        assertTrue(markFirstCommand.equals(markFirstCommand));

        // same values -> returns true
        MarkCommand markFirstCommandCopy = new MarkCommand(1);
        assertTrue(markFirstCommand.equals(markFirstCommandCopy));

        // different types -> returns false
        assertFalse(markFirstCommand.equals(1));

        // null -> returns false
        assertFalse(markFirstCommand.equals(null));

        // different delivery ID -> returns false
        assertFalse(markFirstCommand.equals(markSecondCommand));
    }

    @Test
    public void toString_validDeliveryId_correctStringRepresentation() {
        MarkCommand markCommand = new MarkCommand(1);
        String expected = MarkCommand.class.getCanonicalName() + "{deliveryId=1}";
        assertEquals(expected, markCommand.toString());
    }
}
