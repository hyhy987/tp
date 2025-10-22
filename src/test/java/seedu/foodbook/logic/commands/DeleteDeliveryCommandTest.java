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

public class DeleteDeliveryCommandTest {

    private Model model = new ModelManager(getTypicalFoodBook(), new UserPrefs());

    @Test
    public void execute_validDeliveryId_success() {
        // Assuming there's a delivery with ID 1 in typical food book
        Delivery deliveryToDelete = model.getFilteredDeliveryList().stream()
                .filter(delivery -> delivery.getId().equals(1))
                .findFirst()
                .get();

        DeleteDeliveryCommand deleteDeliveryCommand = new DeleteDeliveryCommand(1);

        String expectedMessage = String.format(DeleteDeliveryCommand.MESSAGE_DELETE_DELIVERY_SUCCESS,
                Messages.format(deliveryToDelete));

        ModelManager expectedModel = new ModelManager(model.getFoodBook(), new UserPrefs());
        expectedModel.deleteDelivery(deliveryToDelete);

        assertCommandSuccess(deleteDeliveryCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidDeliveryId_throwsCommandException() {
        // Use a delivery ID that doesn't exist
        Integer invalidDeliveryId = 99999;
        DeleteDeliveryCommand deleteDeliveryCommand = new DeleteDeliveryCommand(invalidDeliveryId);

        assertCommandFailure(deleteDeliveryCommand, model,
                String.format(DeleteDeliveryCommand.MESSAGE_DELIVERY_NOT_FOUND, 99999));
    }

    @Test
    public void execute_deliveryIdNotInFilteredList_throwsCommandException() {
        // Assuming delivery with ID 100 doesn't exist
        DeleteDeliveryCommand deleteDeliveryCommand = new DeleteDeliveryCommand(100);

        assertCommandFailure(deleteDeliveryCommand, model,
                String.format(DeleteDeliveryCommand.MESSAGE_DELIVERY_NOT_FOUND, 100));
    }

    @Test
    public void equals() {
        DeleteDeliveryCommand deleteFirstCommand = new DeleteDeliveryCommand(1);
        DeleteDeliveryCommand deleteSecondCommand = new DeleteDeliveryCommand(2);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteDeliveryCommand deleteFirstCommandCopy = new DeleteDeliveryCommand(1);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different delivery ID -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toString_validDeliveryId_correctStringRepresentation() {
        DeleteDeliveryCommand deleteDeliveryCommand = new DeleteDeliveryCommand(67);
        String expected = DeleteDeliveryCommand.class.getCanonicalName()
                + "{deliveryId=67}";
        assertEquals(expected, deleteDeliveryCommand.toString());
    }

    @Test
    public void execute_returnsCorrectUiPanel() throws Exception {
        Delivery deliveryToDelete = model.getFilteredDeliveryList().stream()
                .filter(delivery -> delivery.getId().equals(1))
                .findFirst()
                .get();

        DeleteDeliveryCommand deleteDeliveryCommand = new DeleteDeliveryCommand(1);
        CommandResult result = deleteDeliveryCommand.execute(model);

        assertEquals(CommandResult.UiPanel.DELIVERIES, result.getPanelToShow());
    }
}
