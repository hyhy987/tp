package seedu.foodbook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.foodbook.model.Model.PREDICATE_SHOW_ALL_DELIVERIES;
import static seedu.foodbook.testutil.TypicalFoodBook.getTypicalFoodBook;

import org.junit.jupiter.api.Test;

import seedu.foodbook.logic.Messages;
import seedu.foodbook.logic.commands.EditDeliveryCommand.EditDeliveryDescriptor;
import seedu.foodbook.logic.commands.exceptions.CommandException;
import seedu.foodbook.model.FoodBook;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.ModelManager;
import seedu.foodbook.model.UserPrefs;
import seedu.foodbook.model.delivery.DateTime;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;

public class EditDeliveryCommandTest {

    private final Model model = new ModelManager(getTypicalFoodBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecified_success() {
        model.updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES);
        Delivery target = model.getFilteredDeliveryList().get(0);

        Person currentClient = target.getClient();
        Person replacementClient = model.getFilteredPersonList().stream()
                .filter(p -> !p.getName().fullName.equals(currentClient.getName().fullName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("TypicalFoodBook should contain at least two persons"));

        EditDeliveryDescriptor descriptor = new EditDeliveryDescriptor();
        descriptor.setClientName(replacementClient.getName().fullName);
        descriptor.setDateTime(new DateTime("21/10/2029", "1830"));
        descriptor.setRemarks("Updated remarks");
        descriptor.setCost(88.80);

        EditDeliveryCommand command = new EditDeliveryCommand(target.getId(), descriptor);

        // expected
        Model expectedModel = new ModelManager(new FoodBook(model.getFoodBook()), new UserPrefs());
        expectedModel.updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES);

        // grab the base delivery + replacement person from expectedModel
        Delivery base = expectedModel.getFilteredDeliveryList().stream()
                .filter(d -> d.getId().equals(target.getId()))
                .findFirst().orElseThrow();

        Person expectedReplacement = expectedModel.getFilteredPersonList().stream()
                .filter(p -> p.getName().fullName.equals(replacementClient.getName().fullName))
                .findFirst().orElseThrow();

        Delivery expectedEdited = new Delivery(
                base.getId(),
                expectedReplacement,
                new DateTime("21/10/2029", "1830"),
                "Updated remarks",
                88.80,
                base.getTag(),
                base.getStatus()
        );

        expectedModel.setDelivery(base, expectedEdited);

        String expectedMessage = String.format(
                EditDeliveryCommand.MESSAGE_EDIT_DELIVERY_SUCCESS,
                Messages.format(expectedEdited)
        );

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_someFieldsSpecified_success() {
        // Make actual model show all deliveries
        model.updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES);
        Delivery target = model.getFilteredDeliveryList().get(0);

        EditDeliveryDescriptor descriptor = new EditDeliveryDescriptor();
        descriptor.setRemarks("Leave with guard");
        descriptor.setCost(12.34);

        EditDeliveryCommand command = new EditDeliveryCommand(target.getId(), descriptor);

        // expected model
        Model expectedModel = new ModelManager(new FoodBook(model.getFoodBook()), new UserPrefs());
        // Mirror the same filter if your assert compares filtered lists
        expectedModel.updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES);

        Delivery base = expectedModel.getFilteredDeliveryList().stream()
                .filter(d -> d.getId().equals(target.getId()))
                .findFirst().orElseThrow();

        Delivery expectedEdited = new Delivery(
                base.getId(),
                base.getClient(),
                base.getDeliveryDate(),
                "Leave with guard",
                12.34,
                base.getTag(),
                base.getStatus()
        );

        expectedModel.setDelivery(base, expectedEdited);

        String expectedMessage = String.format(
                EditDeliveryCommand.MESSAGE_EDIT_DELIVERY_SUCCESS,
                Messages.format(expectedEdited)
        );

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_changeClientToNonexistent_failure() {
        model.updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES);
        Delivery target = model.getFilteredDeliveryList().get(0);

        EditDeliveryDescriptor descriptor = new EditDeliveryDescriptor();
        descriptor.setClientName("THIS CLIENT DOES NOT EXIST 123");

        EditDeliveryCommand command = new EditDeliveryCommand(target.getId(), descriptor);

        // On failure, state should not change. Use the helper to assert failure.
        // Message comes from createEditedDelivery: "Client not found: <name>"
        assertCommandFailure(command, model, "Client not found: THIS CLIENT DOES NOT EXIST 123");
    }

    @Test
    public void execute_invalidDeliveryId_failure() {
        EditDeliveryDescriptor descriptor = new EditDeliveryDescriptor();
        descriptor.setRemarks("Anything"); // ensure "not edited" isn't the reason

        EditDeliveryCommand command = new EditDeliveryCommand(999_999, descriptor);
        assertCommandFailure(command, model,
                String.format(EditDeliveryCommand.MESSAGE_DELIVERY_NOT_FOUND, 999_999));
    }

    @Test
    public void execute_noFieldsSpecified_failure() {
        model.updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES);
        Delivery target = model.getFilteredDeliveryList().get(0);

        EditDeliveryDescriptor empty = new EditDeliveryDescriptor();
        EditDeliveryCommand command = new EditDeliveryCommand(target.getId(), empty);

        assertCommandFailure(command, model, EditDeliveryCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void equals_and_toString() {
        EditDeliveryDescriptor d1 = new EditDeliveryDescriptor();
        d1.setRemarks("R1");
        EditDeliveryDescriptor d2 = new EditDeliveryDescriptor();
        d2.setRemarks("R1");
        EditDeliveryDescriptor d3 = new EditDeliveryDescriptor();
        d3.setRemarks("R2");

        EditDeliveryCommand c1 = new EditDeliveryCommand(1, d1);
        EditDeliveryCommand c2 = new EditDeliveryCommand(1, d2);
        EditDeliveryCommand c3 = new EditDeliveryCommand(2, d1);
        EditDeliveryCommand c4 = new EditDeliveryCommand(1, d3);

        // equals
        assertTrue(c1.equals(c1)); // same obj
        assertTrue(c1.equals(c2)); // same deliveryId + same descriptor values
        assertFalse(c1.equals(null)); // null
        assertFalse(c1.equals(5)); // different type
        assertFalse(c1.equals(c3)); // different id
        assertFalse(c1.equals(c4)); // different descriptor

        // toString
        String expectedToString = EditDeliveryCommand.class.getCanonicalName()
                + "{deliveryId=" + 1 + ", editDeliveryDescriptor=" + d1 + "}";
        assertEquals(expectedToString, c1.toString());
    }

    @Test
    public void execute_preservesDeliveredStatus_success() throws Exception {
        Model model = new ModelManager(getTypicalFoodBook(), new UserPrefs());
        model.updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES);
        Delivery target = model.getFilteredDeliveryList().get(0);
        Delivery markedTarget = target.markAsDelivered();
        model.setDelivery(model.getFilteredDeliveryList().get(0), markedTarget);

        EditDeliveryCommand.EditDeliveryDescriptor desc = new EditDeliveryCommand.EditDeliveryDescriptor();
        desc.setRemarks("updated");
        EditDeliveryCommand cmd = new EditDeliveryCommand(target.getId(), desc);
        cmd.execute(model);

        Delivery updated = model.getFilteredDeliveryList().stream()
                .filter(d -> d.getId().equals(target.getId())).findFirst().orElseThrow();

        assertTrue(updated.getStatus(), "Edited delivery should remain delivered");
    }

    @Test
    public void execute_negativeCost_throwsCommandException() {
        Model model = new ModelManager(getTypicalFoodBook(), new UserPrefs());
        model.updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES);
        Delivery target = model.getFilteredDeliveryList().get(0);

        EditDeliveryCommand.EditDeliveryDescriptor desc = new EditDeliveryCommand.EditDeliveryDescriptor();
        desc.setCost(-10.0);

        EditDeliveryCommand cmd = new EditDeliveryCommand(target.getId(), desc);

        CommandException exception = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(EditDeliveryCommand.MESSAGE_INVALID_COST, exception.getMessage());
    }
}
