package seedu.foodbook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.logic.commands.AddDeliveryCommand.MESSAGE_CLIENT_NOT_FOUND;
import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.foodbook.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.foodbook.model.Model.PREDICATE_SHOW_ALL_DELIVERIES;
import static seedu.foodbook.testutil.TypicalFoodBook.getTypicalFoodBook;
import static seedu.foodbook.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.foodbook.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.foodbook.testutil.TypicalPersons.HOON;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.foodbook.logic.Messages;
import seedu.foodbook.model.FoodBook;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.ModelManager;
import seedu.foodbook.model.UserPrefs;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Name;
import seedu.foodbook.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteClientCommand}.
 */
public class DeleteClientCommandTest {

    private Model model = new ModelManager(getTypicalFoodBook(), new UserPrefs());

    @Test
    public void execute_validNameUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteClientCommand deleteClientCommand = new DeleteClientCommand(personToDelete.getName());

        String expectedMessage = String.format(DeleteClientCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getFoodBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        List<Delivery> toDelete = expectedModel.getFilteredDeliveryList().stream()
                .filter(d -> d.getClient().getName().equals(personToDelete.getName()))
                .collect(java.util.stream.Collectors.toList());
        toDelete.forEach(expectedModel::deleteDelivery);
        expectedModel.updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES);

        assertCommandSuccess(deleteClientCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidNameUnfilteredList_throwsCommandException() {
        Name nameNotInBook = HOON.getName();
        DeleteClientCommand deleteClientCommand = new DeleteClientCommand(nameNotInBook);

        assertCommandFailure(deleteClientCommand, model, String.format(MESSAGE_CLIENT_NOT_FOUND, nameNotInBook));
    }

    @Test
    public void equals() {
        Person firstPersonToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPersonToDelete = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        DeleteClientCommand deleteFirstCommand = new DeleteClientCommand(firstPersonToDelete.getName());
        DeleteClientCommand deleteSecondCommand = new DeleteClientCommand(secondPersonToDelete.getName());

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteClientCommand deleteFirstCommandCopy = new DeleteClientCommand(firstPersonToDelete.getName());
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getOneBased());
        DeleteClientCommand deleteClientCommand = new DeleteClientCommand(personToDelete.getName());
        String expected = DeleteClientCommand.class.getCanonicalName()
                + "{toBeDeleted=" + personToDelete.getName() + "}";
        assertEquals(expected, deleteClientCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
