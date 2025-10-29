package seedu.foodbook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.foodbook.testutil.TypicalFoodBook.getTypicalFoodBook;

import org.junit.jupiter.api.Test;

import seedu.foodbook.logic.Messages;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.ModelManager;
import seedu.foodbook.model.UserPrefs;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.testutil.DeliveryBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for UnmarkCommand.
 */
public class UnmarkCommandTest {

    private Model model = new ModelManager(getTypicalFoodBook(), new UserPrefs());

    @Test
    public void execute_validDeliveryId_success() throws Exception {

        Delivery testDelivery = new DeliveryBuilder().asDelivered().build();

        ModelManager expectedModel = new ModelManager(model.getFoodBook(), new UserPrefs());
        Delivery markedDelivery = new DeliveryBuilder().asDelivered().build();
        Delivery unmarkedDelivery = markedDelivery.copyAsUndelivered();
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
