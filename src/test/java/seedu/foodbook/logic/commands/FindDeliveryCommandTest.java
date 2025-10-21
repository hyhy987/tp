package seedu.foodbook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.logic.Messages.MESSAGE_DELIVERIES_LISTED_OVERVIEW;
import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.foodbook.testutil.TypicalFoodBook.getTypicalFoodBook;

import java.util.Collections;
import java.util.List;
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
        DeliveryPredicate firstPredicate = new DeliveryPredicate(
                Optional.of("John"), Optional.of("25/12/2024"), Optional.empty());
        DeliveryPredicate secondPredicate = new DeliveryPredicate(
                Optional.of("Jane"), Optional.of("31/12/2024"), Optional.empty());

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
    public void execute_noFilters_allDeliveriesFound() {
        String expectedMessage = String.format(MESSAGE_DELIVERIES_LISTED_OVERVIEW,
                model.getFilteredDeliveryList().size());
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.empty());
        FindDeliveryCommand command = new FindDeliveryCommand(predicate);
        expectedModel.updateFilteredDeliveryList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_clientNameFilter_deliveriesFound() {
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.of("Alice"), Optional.empty(), Optional.empty());
        FindDeliveryCommand command = new FindDeliveryCommand(predicate);
        expectedModel.updateFilteredDeliveryList(predicate);
        String expectedMessage = String.format(MESSAGE_DELIVERIES_LISTED_OVERVIEW,
                expectedModel.getFilteredDeliveryList().size());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_dateFilter_deliveriesFound() {
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.empty(), Optional.of("25/12/2024"), Optional.empty());
        FindDeliveryCommand command = new FindDeliveryCommand(predicate);
        expectedModel.updateFilteredDeliveryList(predicate);
        String expectedMessage = String.format(MESSAGE_DELIVERIES_LISTED_OVERVIEW,
                expectedModel.getFilteredDeliveryList().size());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_tagFilter_deliveriesFound() {
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.of(List.of("urgent")));
        FindDeliveryCommand command = new FindDeliveryCommand(predicate);
        expectedModel.updateFilteredDeliveryList(predicate);
        String expectedMessage = String.format(MESSAGE_DELIVERIES_LISTED_OVERVIEW,
                expectedModel.getFilteredDeliveryList().size());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_combinedFilters_deliveriesFound() {
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.of("Alice"), Optional.of("25/12/2024"), Optional.of(List.of("urgent")));
        FindDeliveryCommand command = new FindDeliveryCommand(predicate);
        expectedModel.updateFilteredDeliveryList(predicate);
        String expectedMessage = String.format(MESSAGE_DELIVERIES_LISTED_OVERVIEW,
                expectedModel.getFilteredDeliveryList().size());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_nonMatchingFilters_noDeliveryFound() {
        String expectedMessage = String.format(MESSAGE_DELIVERIES_LISTED_OVERVIEW, 0);
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.of("NonExistentClient"), Optional.empty(), Optional.empty());
        FindDeliveryCommand command = new FindDeliveryCommand(predicate);
        expectedModel.updateFilteredDeliveryList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredDeliveryList());
    }

    @Test
    public void toStringMethod() {
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.of("John"), Optional.of("25/12/2024"), Optional.of(List.of("urgent")));
        FindDeliveryCommand findCommand = new FindDeliveryCommand(predicate);
        String expected = FindDeliveryCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }
}
