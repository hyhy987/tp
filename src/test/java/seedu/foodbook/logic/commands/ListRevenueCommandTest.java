package seedu.foodbook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.foodbook.model.Model;
import seedu.foodbook.model.ModelManager;
import seedu.foodbook.model.UserPrefs;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.delivery.DeliveryPredicate;
import seedu.foodbook.testutil.DeliveryBuilder;
import seedu.foodbook.testutil.PersonBuilder;
import seedu.foodbook.testutil.TypicalFoodBook;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListRevenueCommand.
 */
public class ListRevenueCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(TypicalFoodBook.getTypicalFoodBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getFoodBook(), new UserPrefs());
    }

    @Test
    public void execute_noFilters_showsAllDeliveries() {
        // Create predicate with no filters
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        ListRevenueCommand command = new ListRevenueCommand(predicate);

        // Calculate expected revenue from all deliveries
        double expectedRevenue = 0.0;
        for (Delivery delivery : model.getFilteredDeliveryList()) {
            expectedRevenue += delivery.getCost();
        }

        int expectedCount = model.getFilteredDeliveryList().size();

        String expectedMessage = String.format(ListRevenueCommand.MESSAGE_SUCCESS,
                expectedRevenue, expectedCount, "Showing: All deliveries");

        expectedModel.updateFilteredDeliveryList(predicate);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_statusFilterDelivered_showsFilteredRevenue() {
        // Add some test deliveries
        Delivery deliveredDelivery1 = new DeliveryBuilder()
                .withId(100)
                .withCost(50.0)
                .asDelivered()
                .build();
        Delivery deliveredDelivery2 = new DeliveryBuilder()
                .withId(101)
                .withCost(30.0)
                .asDelivered()
                .build();
        Delivery pendingDelivery = new DeliveryBuilder()
                .withId(102)
                .withCost(20.0)
                .build();

        model.addDelivery(deliveredDelivery1);
        model.addDelivery(deliveredDelivery2);
        model.addDelivery(pendingDelivery);

        expectedModel.addDelivery(deliveredDelivery1);
        expectedModel.addDelivery(deliveredDelivery2);
        expectedModel.addDelivery(pendingDelivery);

        // Filter for delivered only
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(true));

        ListRevenueCommand command = new ListRevenueCommand(predicate);
        expectedModel.updateFilteredDeliveryList(predicate);

        CommandResult result = null;
        try {
            result = command.execute(model);
        } catch (Exception e) {
            throw new AssertionError("Execution should not fail", e);
        }

        // Verify the result contains correct information
        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Total Revenue:")); // Revenue is displayed
        assertTrue(feedback.contains("Delivered orders")); // Status filter is shown

        // Verify only delivered orders are counted
        assertTrue(model.getFilteredDeliveryList().stream()
                .allMatch(delivery -> delivery.getStatus()));
    }

    @Test
    public void execute_statusFilterNotDelivered_showsFilteredRevenue() {
        // Add test deliveries
        Delivery pendingDelivery1 = new DeliveryBuilder()
                .withId(200)
                .withCost(25.0)
                .build();
        Delivery pendingDelivery2 = new DeliveryBuilder()
                .withId(201)
                .withCost(15.0)
                .build();
        Delivery deliveredDelivery = new DeliveryBuilder()
                .withId(202)
                .withCost(50.0)
                .asDelivered()
                .build();

        model.addDelivery(pendingDelivery1);
        model.addDelivery(pendingDelivery2);
        model.addDelivery(deliveredDelivery);

        expectedModel.addDelivery(pendingDelivery1);
        expectedModel.addDelivery(pendingDelivery2);
        expectedModel.addDelivery(deliveredDelivery);

        // Filter for pending only
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(false));

        ListRevenueCommand command = new ListRevenueCommand(predicate);
        expectedModel.updateFilteredDeliveryList(predicate);

        CommandResult result = null;
        try {
            result = command.execute(model);
        } catch (Exception e) {
            throw new AssertionError("Execution should not fail", e);
        }

        // Verify the result
        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Total Revenue:")); // Revenue is displayed
        assertTrue(feedback.contains("Pending orders")); // Status filter is shown

        // Verify only pending orders are counted
        assertTrue(model.getFilteredDeliveryList().stream()
                .allMatch(delivery -> !delivery.getStatus()));
    }

    @Test
    public void execute_clientNameFilter_showsFilteredRevenue() {
        // Add deliveries with specific client names
        Delivery aliceDelivery = new DeliveryBuilder()
                .withId(300)
                .withClient(new PersonBuilder().withName("Alice Pauline").build())
                .withCost(40.0)
                .build();
        Delivery bobDelivery = new DeliveryBuilder()
                .withId(301)
                .withClient(new PersonBuilder().withName("Bob Builder").build())
                .withCost(30.0)
                .build();

        model.addDelivery(aliceDelivery);
        model.addDelivery(bobDelivery);

        expectedModel.addDelivery(aliceDelivery);
        expectedModel.addDelivery(bobDelivery);

        // Filter for Alice
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.of("Alice"), Optional.empty(), Optional.empty());

        ListRevenueCommand command = new ListRevenueCommand(predicate);
        expectedModel.updateFilteredDeliveryList(predicate);

        CommandResult result = null;
        try {
            result = command.execute(model);
        } catch (Exception e) {
            throw new AssertionError("Execution should not fail", e);
        }

        // Verify the result
        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Total Revenue:")); // Revenue is displayed
        assertTrue(feedback.contains("\"Alice\"")); // Client name filter is shown

        // Verify only Alice's deliveries are counted
        assertTrue(model.getFilteredDeliveryList().stream()
                .allMatch(delivery -> delivery.getClient().getName().fullName.contains("Alice")));
    }

    @Test
    public void execute_dateRangeFilter_showsFilteredRevenue() {
        // Add deliveries with different dates
        Delivery jan2024 = new DeliveryBuilder()
                .withId(400)
                .withDateTime("15/1/2024", "1400")
                .withCost(100.0)
                .build();
        Delivery jun2024 = new DeliveryBuilder()
                .withId(401)
                .withDateTime("15/6/2024", "1500")
                .withCost(200.0)
                .build();
        Delivery dec2025 = new DeliveryBuilder()
                .withId(402)
                .withDateTime("15/12/2025", "1600")
                .withCost(300.0)
                .build();

        model.addDelivery(jan2024);
        model.addDelivery(jun2024);
        model.addDelivery(dec2025);

        expectedModel.addDelivery(jan2024);
        expectedModel.addDelivery(jun2024);
        expectedModel.addDelivery(dec2025);

        // Filter for 2024
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.of(startDate), Optional.of(endDate), Optional.empty(), Optional.empty(), Optional.empty());

        ListRevenueCommand command = new ListRevenueCommand(predicate);
        expectedModel.updateFilteredDeliveryList(predicate);

        CommandResult result = null;
        try {
            result = command.execute(model);
        } catch (Exception e) {
            throw new AssertionError("Execution should not fail", e);
        }

        // Verify the result - should only include jan2024 and jun2024
        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Total Revenue:")); // Revenue is displayed
        assertTrue(feedback.contains("from")); // Date range is shown
        assertTrue(feedback.contains("to")); // Date range is shown
    }

    @Test
    public void execute_combinedFilters_showsFilteredRevenue() {
        // Add various deliveries
        Delivery targetDelivery = new DeliveryBuilder()
                .withId(500)
                .withClient(new PersonBuilder().withName("Charlie Brown").build())
                .withDateTime("15/6/2024", "1400")
                .withCost(150.0)
                .asDelivered()
                .build();

        Delivery wrongStatus = new DeliveryBuilder()
                .withId(501)
                .withClient(new PersonBuilder().withName("Charlie Brown").build())
                .withDateTime("15/6/2024", "1500")
                .withCost(100.0)
                .build(); // Not delivered

        Delivery wrongClient = new DeliveryBuilder()
                .withId(502)
                .withClient(new PersonBuilder().withName("David Lee").build())
                .withDateTime("15/6/2024", "1600")
                .withCost(120.0)
                .asDelivered()
                .build();

        model.addDelivery(targetDelivery);
        model.addDelivery(wrongStatus);
        model.addDelivery(wrongClient);

        expectedModel.addDelivery(targetDelivery);
        expectedModel.addDelivery(wrongStatus);
        expectedModel.addDelivery(wrongClient);

        // Combined filters
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.of(startDate), Optional.of(endDate),
                Optional.of("Charlie"), Optional.empty(), Optional.of(true));

        ListRevenueCommand command = new ListRevenueCommand(predicate);
        expectedModel.updateFilteredDeliveryList(predicate);

        CommandResult result = null;
        try {
            result = command.execute(model);
        } catch (Exception e) {
            throw new AssertionError("Execution should not fail", e);
        }

        // Verify combined filters are shown
        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Total Revenue:")); // Revenue is displayed
        assertTrue(feedback.contains("Delivered orders")); // Status is shown
        assertTrue(feedback.contains("Charlie")); // Client name is shown
    }

    @Test
    public void execute_helpMessageIncluded() {
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        ListRevenueCommand command = new ListRevenueCommand(predicate);

        CommandResult result = null;
        try {
            result = command.execute(model);
        } catch (Exception e) {
            throw new AssertionError("Execution should not fail", e);
        }

        // Verify help message is included
        assertTrue(result.getFeedbackToUser().contains("Usage:"));
        assertTrue(result.getFeedbackToUser().contains("list_revenue"));
        assertTrue(result.getFeedbackToUser().contains("Date format:"));
    }

    @Test
    public void equals() {
        DeliveryPredicate predicate1 = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        DeliveryPredicate predicate2 = new DeliveryPredicate(
                Optional.of(LocalDate.of(2024, 1, 1)), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());

        ListRevenueCommand command1 = new ListRevenueCommand(predicate1);
        ListRevenueCommand command1Copy = new ListRevenueCommand(predicate1);
        ListRevenueCommand command2 = new ListRevenueCommand(predicate2);

        // Same object -> returns true
        assertTrue(command1.equals(command1));

        // Same predicate -> returns true
        assertTrue(command1.equals(command1Copy));

        // Null -> returns false
        assertFalse(command1.equals(null));

        // Different type -> returns false
        assertFalse(command1.equals(5));

        // Different predicate -> returns false
        assertFalse(command1.equals(command2));
    }

    @Test
    public void toStringMethod() {
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        ListRevenueCommand command = new ListRevenueCommand(predicate);

        String expected = ListRevenueCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }
}

