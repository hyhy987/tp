package seedu.foodbook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.logic.Messages.MESSAGE_DELIVERIES_LISTED_OVERVIEW;
import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.foodbook.testutil.TypicalFoodBook.getTypicalFoodBook;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.foodbook.model.Model;
import seedu.foodbook.model.ModelManager;
import seedu.foodbook.model.UserPrefs;
import seedu.foodbook.model.delivery.DeliveryPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindDeliveryCommand}.
 */
public class FindDeliveryCommandTest {

    private Model model = new ModelManager(getTypicalFoodBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalFoodBook(), new UserPrefs());

    @Test
    public void equals() {
        String date1 = "25/12/2024";
        String date2 = "31/12/2024";

        DeliveryPredicate firstPredicate = new DeliveryPredicate(
                Optional.of(date1), Optional.of(date1), Optional.of("John"), Optional.empty(), Optional.empty());
        DeliveryPredicate secondPredicate = new DeliveryPredicate(
                Optional.of(date2), Optional.of(date2), Optional.of("Jane"), Optional.empty(), Optional.empty());

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

        // different predicate -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_clientNameFilter_deliveriesFound() {
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.of("Alice"), Optional.empty(), Optional.empty());
        FindDeliveryCommand command = new FindDeliveryCommand(predicate);
        expectedModel.updateFilteredDeliveryList(predicate);
        String expectedMessage = String.format(MESSAGE_DELIVERIES_LISTED_OVERVIEW,
                expectedModel.getFilteredDeliveryList().size());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_dateFilter_deliveriesFound() {
        String date = "25/12/2024";
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.of(date), Optional.of(date), Optional.empty(), Optional.empty(), Optional.empty());
        FindDeliveryCommand command = new FindDeliveryCommand(predicate);
        expectedModel.updateFilteredDeliveryList(predicate);

        int numFound = expectedModel.getFilteredDeliveryList().size();
        if (numFound > 0) {
            String expectedMessage = String.format(MESSAGE_DELIVERIES_LISTED_OVERVIEW, numFound);
            assertCommandSuccess(command, model, expectedMessage, expectedModel);
        }
    }

    @Test
    public void execute_tagFilter_deliveriesFound() {
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("urgent"), Optional.empty());
        FindDeliveryCommand command = new FindDeliveryCommand(predicate);
        expectedModel.updateFilteredDeliveryList(predicate);

        int numFound = expectedModel.getFilteredDeliveryList().size();
        if (numFound > 0) {
            String expectedMessage = String.format(MESSAGE_DELIVERIES_LISTED_OVERVIEW, numFound);
            assertCommandSuccess(command, model, expectedMessage, expectedModel);
        }
    }

    @Test
    public void execute_combinedFilters_deliveriesFound() {
        String date = "25/12/2024";
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.of(date), Optional.of(date), Optional.of("Alice"), Optional.of("urgent"), Optional.empty());
        FindDeliveryCommand command = new FindDeliveryCommand(predicate);
        expectedModel.updateFilteredDeliveryList(predicate);

        int numFound = expectedModel.getFilteredDeliveryList().size();
        if (numFound > 0) {
            String expectedMessage = String.format(MESSAGE_DELIVERIES_LISTED_OVERVIEW, numFound);
            assertCommandSuccess(command, model, expectedMessage, expectedModel);
        }
    }

    @Test
    public void execute_nonMatchingFilters_noDeliveryFound() {
        String expectedMessage = FindDeliveryCommand.MESSAGE_NO_DELIVERY_FOUND;
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.of("NonExistentClient"),
                Optional.empty(), Optional.empty());
        FindDeliveryCommand command = new FindDeliveryCommand(predicate);

        Model expectedModel = new ModelManager(getTypicalFoodBook(), new UserPrefs());
        expectedModel.updateFilteredDeliveryList(predicate);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(0, model.getFilteredDeliveryList().size());
    }

    @Test
    public void toStringMethod() {
        String date = "25/12/2024";
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.of(date), Optional.of(date), Optional.of("John"), Optional.of("urgent"), Optional.empty());
        FindDeliveryCommand findCommand = new FindDeliveryCommand(predicate);
        String expected = FindDeliveryCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }
}
