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
 * Contains integration tests (interaction with the Model) and unit tests for MarkCommand.
 */
public class MarkCommandTest {

    private Model model = new ModelManager(getTypicalFoodBook(), new UserPrefs());

    @Test
    public void execute_validDeliveryId_success() throws Exception {

        Delivery testDelivery = new DeliveryBuilder().build();

        ModelManager expectedModel = new ModelManager(model.getFoodBook(), new UserPrefs());
        Delivery markedDelivery = new DeliveryBuilder().asDelivered().build();
        expectedModel.addDelivery(markedDelivery);

        model.addDelivery(testDelivery);

        MarkCommand markCommand = new MarkCommand(testDelivery.getId());

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_DELIVERY_SUCCESS,
                Messages.format(testDelivery));

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

        Delivery testDelivery = new DeliveryBuilder().build();
        Delivery markedDelivery = testDelivery.copyAsDelivered(); // Mark as delivered first
        model.addDelivery(markedDelivery);

        MarkCommand markCommand = new MarkCommand(testDelivery.getId());
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
