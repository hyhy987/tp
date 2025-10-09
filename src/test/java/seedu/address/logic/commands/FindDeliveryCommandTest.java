package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_DELIVERIES_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalFoodBook.getTypicalAddressBook;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.delivery.DeliveryContainsDatePredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindDeliveryCommand}.
 */
public class FindDeliveryCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        DeliveryContainsDatePredicate firstPredicate =
                new DeliveryContainsDatePredicate("25/12/2024");
        DeliveryContainsDatePredicate secondPredicate =
                new DeliveryContainsDatePredicate("31/12/2024");

        FindDeliveryCommand findFirstCommand = new FindDeliveryCommand(firstPredicate);
        FindDeliveryCommand findSecondCommand = new FindDeliveryCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindDeliveryCommand findFirstCommandCopy = new FindDeliveryCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different delivery -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noDeliveryFound() {
        String expectedMessage = String.format(MESSAGE_DELIVERIES_LISTED_OVERVIEW, 0);
        DeliveryContainsDatePredicate predicate = preparePredicate(" ");
        FindDeliveryCommand command = new FindDeliveryCommand(predicate);
        expectedModel.updateFilteredDeliveryList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredDeliveryList());
    }

    @Test
    public void toStringMethod() {
        DeliveryContainsDatePredicate predicate = new DeliveryContainsDatePredicate("25/12/2024");
        FindDeliveryCommand findCommand = new FindDeliveryCommand(predicate);
        String expected = FindDeliveryCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code DeliveryContainsDatePredicate}.
     */
    private DeliveryContainsDatePredicate preparePredicate(String userInput) {
        return new DeliveryContainsDatePredicate(userInput);
    }
}
