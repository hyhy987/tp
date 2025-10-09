package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalFoodBook.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ClientMatchesPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindClientCommand}.
 */
public class FindClientCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        ClientMatchesPredicate firstPredicate =
                new ClientMatchesPredicate(Optional.of("Alice"), Optional.empty(), Optional.empty());
        ClientMatchesPredicate secondPredicate =
                new ClientMatchesPredicate(Optional.of("Bob"), Optional.empty(), Optional.empty());

        FindClientCommand findFirstCommand = new FindClientCommand(firstPredicate);
        FindClientCommand findSecondCommand = new FindClientCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindClientCommand findFirstCommandCopy = new FindClientCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_phoneOnly_onePersonFound() {
        String expectedMessage = "1 persons listed!";
        ClientMatchesPredicate predicate =
                new ClientMatchesPredicate(Optional.empty(), Optional.of("9435"), Optional.empty());
        FindClientCommand command = new FindClientCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(ALICE), model.getFilteredPersonList());
    }

    @Test
    public void execute_emailOnly_onePersonFound() {
        String expectedMessage = "1 persons listed!";
        ClientMatchesPredicate predicate =
                new ClientMatchesPredicate(Optional.empty(), Optional.empty(), Optional.of("alice"));
        FindClientCommand command = new FindClientCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(ALICE), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleFields_onePersonFound() {
        String expectedMessage = "1 persons listed!";
        ClientMatchesPredicate predicate =
                new ClientMatchesPredicate(Optional.of("Alice"), Optional.of("9435"),
                        Optional.of("alice"));
        FindClientCommand command = new FindClientCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(ALICE), model.getFilteredPersonList());
    }

    @Test
    public void execute_noMatch_noPersonFound() {
        String expectedMessage = FindClientCommand.MESSAGE_NO_CLIENTS_FOUND;
        ClientMatchesPredicate predicate =
                new ClientMatchesPredicate(Optional.of("NonExistentName"), Optional.empty(), Optional.empty());
        FindClientCommand command = new FindClientCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        ClientMatchesPredicate predicate =
                new ClientMatchesPredicate(Optional.of("Alice"), Optional.empty(), Optional.empty());
        FindClientCommand findClientCommand = new FindClientCommand(predicate);
        String expected = FindClientCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findClientCommand.toString());
    }
}
