package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalFoodBook.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.delivery.Delivery;
import seedu.address.testutil.DeliveryBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for UnmarkCommand.
 */
public class UnmarkCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validDeliveryId_success() throws Exception {

        Delivery testDelivery = new DeliveryBuilder().asDelivered().build();

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Delivery unmarkedDelivery = new DeliveryBuilder().asDelivered().build();
        unmarkedDelivery.unmarkAsDelivered();
        expectedModel.addDelivery(unmarkedDelivery);

        model.addDelivery(testDelivery);

        UnmarkCommand unmarkCommand = new UnmarkCommand(testDelivery.getId());

        String expectedMessage = String.format(UnmarkCommand.MESSAGE_UNMARK_DELIVERY_SUCCESS,
                Messages.format(testDelivery));

        assertCommandSuccess(unmarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidDeliveryId_throwsCommandException() {
        UnmarkCommand unmarkCommand = new UnmarkCommand(999);
        assertCommandFailure(unmarkCommand, model, UnmarkCommand.MESSAGE_DELIVERY_NOT_FOUND);
    }

    @Test
    public void execute_alreadyUnmarkedDelivery_throwsCommandException() throws Exception {
        // Create a test person and delivery

        Delivery testDelivery = new DeliveryBuilder().build();
        model.addDelivery(testDelivery);

        UnmarkCommand unmarkCommand = new UnmarkCommand(testDelivery.getId());
        assertCommandFailure(unmarkCommand, model, UnmarkCommand.MESSAGE_DELIVERY_ALREADY_UNMARKED);
    }

    @Test
    public void equals() {
        UnmarkCommand unmarkFirstCommand = new UnmarkCommand(1);
        UnmarkCommand unmarkSecondCommand = new UnmarkCommand(2);

        // same object -> returns true
        assertTrue(unmarkFirstCommand.equals(unmarkFirstCommand));

        // same values -> returns true
        UnmarkCommand unmarkFirstCommandCopy = new UnmarkCommand(1);
        assertTrue(unmarkFirstCommand.equals(unmarkFirstCommandCopy));

        // different types -> returns false
        assertFalse(unmarkFirstCommand.equals(1));

        // null -> returns false
        assertFalse(unmarkFirstCommand.equals(null));

        // different delivery ID -> returns false
        assertFalse(unmarkFirstCommand.equals(unmarkSecondCommand));
    }

    @Test
    public void toString_validDeliveryId_correctStringRepresentation() {
        UnmarkCommand unmarkCommand = new UnmarkCommand(1);
        String expected = UnmarkCommand.class.getCanonicalName() + "{deliveryId=1}";
        assertEquals(expected, unmarkCommand.toString());
    }
}
