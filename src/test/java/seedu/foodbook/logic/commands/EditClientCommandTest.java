package seedu.foodbook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.foodbook.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.foodbook.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.foodbook.model.Model.PREDICATE_SHOW_ALL_DELIVERIES;
import static seedu.foodbook.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.foodbook.testutil.TypicalFoodBook.getTypicalFoodBook;
import static seedu.foodbook.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.foodbook.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.foodbook.commons.core.index.Index;
import seedu.foodbook.logic.Messages;
import seedu.foodbook.logic.commands.EditClientCommand.EditPersonDescriptor;
import seedu.foodbook.model.FoodBook;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.ModelManager;
import seedu.foodbook.model.UserPrefs;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Name;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.testutil.EditPersonDescriptorBuilder;
import seedu.foodbook.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditClientCommandTest {

    private Model model = new ModelManager(getTypicalFoodBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person target = model.getFilteredPersonList().get(0);

        // edited person we want after the command (Amy)
        Person editedPerson = new PersonBuilder().build(); // Amy Bee by default

        // descriptor must reflect the NEW values (Amy), not Alice
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setName(editedPerson.getName());
        descriptor.setAddress(editedPerson.getAddress());
        descriptor.setEmail(editedPerson.getEmail());
        descriptor.setPhone(editedPerson.getPhone());
        descriptor.setTags(editedPerson.getTags());

        // command finds the CURRENT client by name (Alice) and applies descriptor (Amy)
        EditClientCommand editClientCommand =
                new EditClientCommand(target.getName(), descriptor);

        String expectedMessage = String.format(
                EditClientCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new FoodBook(model.getFoodBook()), new UserPrefs());
        expectedModel.setPerson(target, editedPerson);
        List<Delivery> deliveries = expectedModel.getDeliveriesByClientName(target.getName());
        for (Delivery d: deliveries) {
            Delivery newDelivery = d.copyWithNewClient(editedPerson);
            expectedModel.setDelivery(d, newDelivery);
        }

        assertCommandSuccess(editClientCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        // Only change a few fields
        Person editedPerson = new PersonBuilder(lastPerson)
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND)
                .build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND)
                .build();

        // IMPORTANT: find by current name, not the new name
        EditClientCommand editClientCommand =
                new EditClientCommand(lastPerson.getName(), descriptor);

        String expectedMessage = String.format(
                EditClientCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new FoodBook(model.getFoodBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);
        List<Delivery> deliveryList = expectedModel.getDeliveriesByClientName(lastPerson.getName());
        for (Delivery d : deliveryList) {
            Delivery newDelivery = d.copyWithNewClient(editedPerson);
            expectedModel.setDelivery(d, newDelivery);
        }

        assertCommandSuccess(editClientCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        Person target = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Name currentClientName = target.getName();
        EditClientCommand editCommand = new EditClientCommand(currentClientName, new EditPersonDescriptor());
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditClientCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new FoodBook(model.getFoodBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        // Arrange: filter actual model to only show the first person
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person target = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Name currentClientName = target.getName();

        Person editedPerson = new PersonBuilder(target).withName(VALID_NAME_BOB).build();
        EditClientCommand editCommand = new EditClientCommand(
                currentClientName,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build()
        );

        String expectedMessage = String.format(
                EditClientCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)
        );

        // Build expected model from a deep copy
        Model expectedModel = new ModelManager(new FoodBook(model.getFoodBook()), new UserPrefs());

        // IMPORTANT: mirror the same filter on expectedModel
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        // Replace the same logical person inside expectedModel
        Person expectedTarget = expectedModel.getFilteredPersonList().get(0);
        expectedModel.setPerson(expectedTarget, editedPerson);

        // Update deliveries that reference the old name, inside expectedModel
        List<Delivery> deliveries = expectedModel.getDeliveriesByClientName(currentClientName);
        for (Delivery d : deliveries) {
            Delivery newDelivery = d.copyWithNewClient(editedPerson); // ensure this preserves isDelivered
            expectedModel.setDelivery(d, newDelivery);
        }

        // Assert
        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // Target = second person; we’ll try to edit them to become the first person
        Person target = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Name currentClientName = target.getName();

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();

        EditClientCommand editClientCommand = new EditClientCommand(currentClientName, descriptor);

        // Expected model must have SAME filtered state as actual (unfiltered)
        Model expectedModel = new ModelManager(new FoodBook(model.getFoodBook()), new UserPrefs());
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        assertCommandFailure(editClientCommand, model, EditClientCommand.MESSAGE_DUPLICATE_PERSON);
    }


    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // Target: second person (e.g., Benson)
        Person target = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Name currentClientName = target.getName();

        // Descriptor: make target identical to FIRST person (e.g., Alice) -> duplicate
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();

        EditClientCommand cmd = new EditClientCommand(currentClientName, descriptor);

        // 3-arg helper: expects NO mutation to food book or filtered list on failure
        assertCommandFailure(cmd, model, EditClientCommand.MESSAGE_DUPLICATE_PERSON);

    }

    @Test
    public void equals() {
        Person target = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Name currentClientName = target.getName();
        Person targetNext = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Name nextClientName = targetNext.getName();

        final EditClientCommand standardCommand = new EditClientCommand(currentClientName, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditClientCommand commandWithSameValues = new EditClientCommand(currentClientName, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different name -> returns false
        assertFalse(standardCommand.equals(new EditClientCommand(nextClientName, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditClientCommand(currentClientName, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Person target = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Name currentClientName = target.getName();
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        EditClientCommand editCommand = new EditClientCommand(currentClientName, editPersonDescriptor);

        String expected = EditClientCommand.class.getCanonicalName()
                + "{currentClientName=" + currentClientName + ", editPersonDescriptor="
                + editPersonDescriptor + "}";

        assertEquals(expected, editCommand.toString());
    }

    @Test
    public void execute_findClientFilteredThenEditHiddenClient_success() {
        // Given: two clients Alice (index 0) and Bob (index 1)
        // Filter UI to show only Bob
        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        Person bob = model.getFilteredPersonList().get(0);
        Person alice = model.getFoodBook().getPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Edit Alice -> NewAlice (change name only)
        Person editedAlice = new PersonBuilder(alice).withName("NewAlice").build();
        EditClientCommand.EditPersonDescriptor descriptor =
                new EditPersonDescriptorBuilder().withName("NewAlice").build();

        EditClientCommand cmd = new EditClientCommand(alice.getName(), descriptor);

        // Expected model: Alice replaced with NewAlice; Bob unaffected
        Model expectedModel = new ModelManager(new FoodBook(model.getFoodBook()), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_SECOND_PERSON);
        Person expectedAliceInExpectedModel = expectedModel.getFoodBook()
                .getPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        expectedModel.setPerson(expectedAliceInExpectedModel, editedAlice);
        List<Delivery> deliveries = expectedModel.getDeliveriesByClientName(expectedAliceInExpectedModel.getName());
        for (Delivery d: deliveries) {
            Delivery newDelivery = d.copyWithNewClient(editedAlice);
            expectedModel.setDelivery(d, newDelivery);
        }

        // Assert success (don’t assume any filter reset unless your command guarantees it)
        String expectedMsg = String.format(EditClientCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedAlice));
        assertCommandSuccess(cmd, model, expectedMsg, expectedModel);
    }

    @Test
    public void execute_editClient_doesNotTouchOtherClientsDeliveries() {
        // Bob’s delivery should remain with Bob after editing Alice
        Person alice = model.getFoodBook().getPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person newAlice = new PersonBuilder(alice).withName("NewAlice").build();

        EditClientCommand cmd = new EditClientCommand(
                alice.getName(),
                new EditPersonDescriptorBuilder().withName("NewAlice").build()
        );

        // Build expected model: only deliveries with client == Alice change
        Model expectedModel = new ModelManager(new FoodBook(model.getFoodBook()), new UserPrefs());
        Person aliceInExpected = expectedModel.getFoodBook().getPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        expectedModel.setPerson(aliceInExpected, newAlice);

        expectedModel.updateFilteredDeliveryList(d -> true);
        for (Delivery d : new ArrayList<>(expectedModel.getFilteredDeliveryList())) {
            if (d.getClient().isSamePerson(aliceInExpected)) {
                Delivery updated = new Delivery(d.getId(), newAlice, d.getDeliveryDate(), d.getRemarks(),
                        d.getCost(), d.getTag());
                expectedModel.setDelivery(d, updated);
            }
        }

        String expectedMsg = String.format(EditClientCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(newAlice));
        assertCommandSuccess(cmd, model, expectedMsg, expectedModel);
    }

}
