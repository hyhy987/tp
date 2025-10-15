package seedu.foodbook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.foodbook.testutil.TypicalFoodBook.getTypicalFoodBook;
import static seedu.foodbook.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.foodbook.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.foodbook.commons.core.index.Index;
import seedu.foodbook.logic.Messages;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.ModelManager;
import seedu.foodbook.model.UserPrefs;
import seedu.foodbook.model.delivery.Delivery;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteDeliveryCommand}.
 */
public class DeleteDeliveryCommandTest {

    private Model model = new ModelManager(getTypicalFoodBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Delivery deliveryToDelete = model.getFilteredDeliveryList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteDeliveryCommand deleteDeliveryCommand = new DeleteDeliveryCommand(INDEX_FIRST_PERSON);

        String expectedMessage = DeleteDeliveryCommand.MESSAGE_DELETE_DELIVERY_SUCCESS;

        ModelManager expectedModel = new ModelManager(model.getFoodBook(), new UserPrefs());
        expectedModel.deleteDelivery(deliveryToDelete);

        assertCommandSuccess(deleteDeliveryCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredDeliveryList().size() + 1);
        DeleteDeliveryCommand deleteDeliveryCommand = new DeleteDeliveryCommand(outOfBoundIndex);

        assertCommandFailure(deleteDeliveryCommand, model, Messages.MESSAGE_INVALID_DELIVERY_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        // Filter the list to show only first delivery
        showDeliveryAtIndex(model, INDEX_FIRST_PERSON);

        Delivery deliveryToDelete = model.getFilteredDeliveryList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteDeliveryCommand deleteDeliveryCommand = new DeleteDeliveryCommand(INDEX_FIRST_PERSON);

        String expectedMessage = DeleteDeliveryCommand.MESSAGE_DELETE_DELIVERY_SUCCESS;

        Model expectedModel = new ModelManager(model.getFoodBook(), new UserPrefs());
        expectedModel.deleteDelivery(deliveryToDelete);
        showNoDelivery(expectedModel);

        assertCommandSuccess(deleteDeliveryCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showDeliveryAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // Ensures that outOfBoundIndex is still in bounds of food book delivery list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getFoodBook().getDeliveryList().size());

        DeleteDeliveryCommand deleteDeliveryCommand = new DeleteDeliveryCommand(outOfBoundIndex);

        assertCommandFailure(deleteDeliveryCommand, model, Messages.MESSAGE_INVALID_DELIVERY_DISPLAYED_INDEX);
    }

    @Test
    public void execute_multipleDeliveries_deletesCorrectOne() {
        List<Delivery> deliveryList = model.getFilteredDeliveryList();

        // Ensure we have at least 2 deliveries
        assertTrue(deliveryList.size() >= 2);

        Delivery deliveryToDelete = deliveryList.get(INDEX_FIRST_PERSON.getZeroBased());
        Delivery deliveryToKeep = deliveryList.get(INDEX_SECOND_PERSON.getZeroBased());

        DeleteDeliveryCommand deleteDeliveryCommand = new DeleteDeliveryCommand(INDEX_FIRST_PERSON);

        String expectedMessage = DeleteDeliveryCommand.MESSAGE_DELETE_DELIVERY_SUCCESS;

        ModelManager expectedModel = new ModelManager(model.getFoodBook(), new UserPrefs());
        expectedModel.deleteDelivery(deliveryToDelete);

        assertCommandSuccess(deleteDeliveryCommand, model, expectedMessage, expectedModel);

        // Verify correct delivery was deleted
        assertFalse(model.getFilteredDeliveryList().contains(deliveryToDelete));

        // Verify other delivery still exists
        assertTrue(model.getFilteredDeliveryList().contains(deliveryToKeep));
    }

    @Test
    public void equals() {
        DeleteDeliveryCommand deleteFirstCommand = new DeleteDeliveryCommand(INDEX_FIRST_PERSON);
        DeleteDeliveryCommand deleteSecondCommand = new DeleteDeliveryCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteDeliveryCommand deleteFirstCommandCopy = new DeleteDeliveryCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different delivery -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteDeliveryCommand deleteDeliveryCommand = new DeleteDeliveryCommand(targetIndex);
        String expected = DeleteDeliveryCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteDeliveryCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no delivery.
     */
    private void showNoDelivery(Model model) {
        model.updateFilteredDeliveryList(d -> false);
        assertTrue(model.getFilteredDeliveryList().isEmpty());
    }

    /**
     * Updates {@code model}'s filtered list to show only the delivery at the given {@code targetIndex} in the
     * {@code model}'s food book.
     */
    private void showDeliveryAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredDeliveryList().size());

        Delivery delivery = model.getFilteredDeliveryList().get(targetIndex.getZeroBased());
        model.updateFilteredDeliveryList(d -> d.equals(delivery));

        assertEquals(1, model.getFilteredDeliveryList().size());
    }
}
