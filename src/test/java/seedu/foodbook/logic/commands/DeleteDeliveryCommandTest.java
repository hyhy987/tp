package seedu.foodbook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.foodbook.testutil.TypicalFoodBook.getTypicalFoodBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.foodbook.logic.commands.exceptions.CommandException;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.ModelManager;
import seedu.foodbook.model.UserPrefs;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.testutil.DeliveryBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteDeliveryCommand}.
 */
public class DeleteDeliveryCommandTest {

    private Model model;
    private Delivery sampleDelivery;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalFoodBook(), new UserPrefs());

        // Get a person from the typical food book to associate with delivery
        Person person = model.getFilteredPersonList().get(0);

        // Create a sample delivery with explicit ID
        sampleDelivery = new DeliveryBuilder()
                .withId(100) // Use explicit ID for testing
                .withClient(person)
                .withDateTime("20/10/2025", "1400")
                .withRemarks("Test delivery")
                .withCost(50.00)
                .build();

        model.addDelivery(sampleDelivery);
    }

    @Test
    public void execute_validDeliveryId_success() {
        Integer deliveryId = sampleDelivery.getId();
        DeleteDeliveryCommand deleteDeliveryCommand = new DeleteDeliveryCommand(deliveryId);

        String expectedMessage = DeleteDeliveryCommand.MESSAGE_DELETE_DELIVERY_SUCCESS;

        Model expectedModel = new ModelManager(model.getFoodBook(), new UserPrefs());
        expectedModel.deleteDelivery(sampleDelivery);

        assertCommandSuccess(deleteDeliveryCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidDeliveryId_throwsCommandException() {
        Integer invalidDeliveryId = 99999;
        DeleteDeliveryCommand deleteDeliveryCommand = new DeleteDeliveryCommand(invalidDeliveryId);

        String expectedMessage = String.format(
                DeleteDeliveryCommand.MESSAGE_DELIVERY_NOT_FOUND, invalidDeliveryId);

        assertCommandFailure(deleteDeliveryCommand, model, expectedMessage);
    }

    @Test
    public void execute_deletedDeliveryId_throwsCommandException() {
        Integer deliveryId = sampleDelivery.getId();

        // Delete the delivery first
        model.deleteDelivery(sampleDelivery);

        // Try to delete again
        DeleteDeliveryCommand deleteDeliveryCommand = new DeleteDeliveryCommand(deliveryId);

        String expectedMessage = String.format(
                DeleteDeliveryCommand.MESSAGE_DELIVERY_NOT_FOUND, deliveryId);

        assertCommandFailure(deleteDeliveryCommand, model, expectedMessage);
    }

    @Test
    public void execute_multipleDeliveries_deletesCorrectOne() throws CommandException {
        // Add another delivery with explicit ID
        Person person = model.getFilteredPersonList().get(1);
        Delivery anotherDelivery = new DeliveryBuilder()
                .withId(101) // Different ID
                .withClient(person)
                .withDateTime("21/10/2025", "1500")
                .withRemarks("Another delivery")
                .withCost(75.00)
                .build();
        model.addDelivery(anotherDelivery);

        Integer deliveryIdToDelete = sampleDelivery.getId(); // Should be 100
        Integer deliveryIdToKeep = anotherDelivery.getId(); // Should be 101

        // Delete the first delivery
        DeleteDeliveryCommand deleteDeliveryCommand = new DeleteDeliveryCommand(deliveryIdToDelete);
        deleteDeliveryCommand.execute(model);

        // Verify first delivery is deleted
        assertFalse(model.getFilteredDeliveryList().stream()
                .anyMatch(d -> d.getId().equals(deliveryIdToDelete)));

        // Verify second delivery still exists
        assertTrue(model.getFilteredDeliveryList().stream()
                .anyMatch(d -> d.getId().equals(deliveryIdToKeep)));
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
    public void equals_sameDeliveryId_returnsTrue() {
        Integer deliveryId = 123;
        DeleteDeliveryCommand command1 = new DeleteDeliveryCommand(deliveryId);
        DeleteDeliveryCommand command2 = new DeleteDeliveryCommand(deliveryId);

        assertTrue(command1.equals(command2));
    }

    @Test
    public void equals_differentDeliveryId_returnsFalse() {
        DeleteDeliveryCommand command1 = new DeleteDeliveryCommand(123);
        DeleteDeliveryCommand command2 = new DeleteDeliveryCommand(456);

        assertFalse(command1.equals(command2));
    }

    @Test
    public void equals_null_returnsFalse() {
        DeleteDeliveryCommand command = new DeleteDeliveryCommand(123);
        assertFalse(command.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        DeleteDeliveryCommand command = new DeleteDeliveryCommand(123);
        assertFalse(command.equals("not a command"));
    }

    @Test
    public void toStringMethod() {
        Integer targetDeliveryId = 123;
        DeleteDeliveryCommand deleteDeliveryCommand = new DeleteDeliveryCommand(targetDeliveryId);
        String expected = DeleteDeliveryCommand.class.getCanonicalName()
                + "{deliveryId=" + targetDeliveryId + "}";
        assertEquals(expected, deleteDeliveryCommand.toString());
    }

    @Test
    public void execute_afterFiltering_stillDeletesCorrectDelivery() throws CommandException {
        // Add multiple deliveries with explicit IDs
        Person person1 = model.getFilteredPersonList().get(0);
        Person person2 = model.getFilteredPersonList().get(1);

        Delivery delivery1 = new DeliveryBuilder()
                .withId(200)
                .withClient(person1)
                .withDateTime("20/10/2025", "1000")
                .withRemarks("Delivery 1")
                .withCost(100.00)
                .build();

        Delivery delivery2 = new DeliveryBuilder()
                .withId(201)
                .withClient(person2)
                .withDateTime("21/10/2025", "1100")
                .withRemarks("Delivery 2")
                .withCost(150.00)
                .build();

        model.addDelivery(delivery1);
        model.addDelivery(delivery2);

        Integer idToDelete = delivery1.getId(); // Should be 200

        // Even after filtering, deletion by ID should work
        DeleteDeliveryCommand deleteCommand = new DeleteDeliveryCommand(idToDelete);
        deleteCommand.execute(model);

        // Verify correct delivery was deleted
        assertFalse(model.getFilteredDeliveryList().stream()
                .anyMatch(d -> d.getId().equals(idToDelete)));

        // Verify other delivery still exists
        assertTrue(model.getFilteredDeliveryList().stream()
                .anyMatch(d -> d.getId().equals(delivery2.getId())));
    }
}
