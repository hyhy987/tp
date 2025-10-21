package seedu.foodbook.model.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.foodbook.testutil.DeliveryBuilder;
import seedu.foodbook.testutil.PersonBuilder;

/**
 * Contains unit tests for RevenueFilterPredicate.
 */
public class RevenueFilterPredicateTest {

    @Test
    public void test_noFilters_returnsTrue() {
        // No filters - should match all deliveries
        RevenueFilterPredicate predicate = new RevenueFilterPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        Delivery delivery = new DeliveryBuilder().build();
        assertTrue(predicate.test(delivery));
    }

    @Test
    public void test_statusFilter_delivered() {
        // Filter for delivered orders
        RevenueFilterPredicate predicate = new RevenueFilterPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(true));

        Delivery deliveredOrder = new DeliveryBuilder().asDelivered().build();
        assertTrue(predicate.test(deliveredOrder));

        Delivery pendingOrder = new DeliveryBuilder().build();
        assertFalse(predicate.test(pendingOrder));
    }

    @Test
    public void test_statusFilter_notDelivered() {
        // Filter for pending orders
        RevenueFilterPredicate predicate = new RevenueFilterPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(false));

        Delivery pendingOrder = new DeliveryBuilder().build();
        assertTrue(predicate.test(pendingOrder));

        Delivery deliveredOrder = new DeliveryBuilder().asDelivered().build();
        assertFalse(predicate.test(deliveredOrder));
    }

    @Test
    public void test_dateRangeFilter_withinRange() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        RevenueFilterPredicate predicate = new RevenueFilterPredicate(
                Optional.of(startDate), Optional.of(endDate), Optional.empty(), Optional.empty());

        // Delivery within range
        Delivery delivery = new DeliveryBuilder().withDateTime("15/6/2024", "1430").build();
        assertTrue(predicate.test(delivery));

        // Delivery before range
        Delivery deliveryBefore = new DeliveryBuilder().withDateTime("31/12/2023", "1430").build();
        assertFalse(predicate.test(deliveryBefore));

        // Delivery after range
        Delivery deliveryAfter = new DeliveryBuilder().withDateTime("1/1/2025", "1430").build();
        assertFalse(predicate.test(deliveryAfter));
    }

    @Test
    public void test_dateRangeFilter_edgeCases() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        RevenueFilterPredicate predicate = new RevenueFilterPredicate(
                Optional.of(startDate), Optional.of(endDate), Optional.empty(), Optional.empty());

        // Delivery on start date (inclusive)
        Delivery deliveryOnStart = new DeliveryBuilder().withDateTime("1/1/2024", "0000").build();
        assertTrue(predicate.test(deliveryOnStart));

        // Delivery on end date (inclusive)
        Delivery deliveryOnEnd = new DeliveryBuilder().withDateTime("31/12/2024", "2359").build();
        assertTrue(predicate.test(deliveryOnEnd));
    }

    @Test
    public void test_startDateOnly() {
        LocalDate startDate = LocalDate.of(2024, 6, 1);

        RevenueFilterPredicate predicate = new RevenueFilterPredicate(
                Optional.of(startDate), Optional.empty(), Optional.empty(), Optional.empty());

        // Delivery on or after start date
        Delivery delivery = new DeliveryBuilder().withDateTime("15/6/2024", "1430").build();
        assertTrue(predicate.test(delivery));

        // Delivery before start date
        Delivery deliveryBefore = new DeliveryBuilder().withDateTime("31/5/2024", "1430").build();
        assertFalse(predicate.test(deliveryBefore));
    }

    @Test
    public void test_endDateOnly() {
        LocalDate endDate = LocalDate.of(2024, 6, 30);

        RevenueFilterPredicate predicate = new RevenueFilterPredicate(
                Optional.empty(), Optional.of(endDate), Optional.empty(), Optional.empty());

        // Delivery on or before end date
        Delivery delivery = new DeliveryBuilder().withDateTime("15/6/2024", "1430").build();
        assertTrue(predicate.test(delivery));

        // Delivery after end date
        Delivery deliveryAfter = new DeliveryBuilder().withDateTime("1/7/2024", "1430").build();
        assertFalse(predicate.test(deliveryAfter));
    }

    @Test
    public void test_clientNameFilter_exactMatch() {
        RevenueFilterPredicate predicate = new RevenueFilterPredicate(
                Optional.empty(), Optional.empty(), Optional.of("Alice"), Optional.empty());

        Delivery delivery = new DeliveryBuilder()
                .withClient(new PersonBuilder().withName("Alice Pauline").build())
                .build();
        assertTrue(predicate.test(delivery));
    }

    @Test
    public void test_clientNameFilter_partialMatch() {
        RevenueFilterPredicate predicate = new RevenueFilterPredicate(
                Optional.empty(), Optional.empty(), Optional.of("ali"), Optional.empty());

        Delivery delivery = new DeliveryBuilder()
                .withClient(new PersonBuilder().withName("Alice Pauline").build())
                .build();
        assertTrue(predicate.test(delivery));
    }

    @Test
    public void test_clientNameFilter_noMatch() {
        RevenueFilterPredicate predicate = new RevenueFilterPredicate(
                Optional.empty(), Optional.empty(), Optional.of("Bob"), Optional.empty());

        Delivery delivery = new DeliveryBuilder()
                .withClient(new PersonBuilder().withName("Alice Pauline").build())
                .build();
        assertFalse(predicate.test(delivery));
    }

    @Test
    public void test_combinedFilters_allMatch() {
        // Multiple filters - all must match
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        RevenueFilterPredicate predicate = new RevenueFilterPredicate(
                Optional.of(startDate), Optional.of(endDate),
                Optional.of("Alice"), Optional.of(true));

        Delivery delivery = new DeliveryBuilder()
                .withClient(new PersonBuilder().withName("Alice Pauline").build())
                .withDateTime("15/6/2024", "1430")
                .asDelivered()
                .build();

        assertTrue(predicate.test(delivery));
    }

    @Test
    public void test_combinedFilters_oneFails() {
        // Multiple filters - if one fails, overall result is false
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        RevenueFilterPredicate predicate = new RevenueFilterPredicate(
                Optional.of(startDate), Optional.of(endDate),
                Optional.of("Alice"), Optional.of(true));

        // Delivery not delivered (fails status filter)
        Delivery delivery = new DeliveryBuilder()
                .withClient(new PersonBuilder().withName("Alice Pauline").build())
                .withDateTime("15/6/2024", "1430")
                .build(); // Not marked as delivered

        assertFalse(predicate.test(delivery));
    }

    @Test
    public void equals() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        RevenueFilterPredicate firstPredicate = new RevenueFilterPredicate(
                Optional.of(startDate), Optional.of(endDate),
                Optional.of("Alice"), Optional.of(true));

        // Same values -> returns true
        RevenueFilterPredicate firstPredicateCopy = new RevenueFilterPredicate(
                Optional.of(startDate), Optional.of(endDate),
                Optional.of("Alice"), Optional.of(true));
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // Same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // Null -> returns false
        assertFalse(firstPredicate.equals(null));

        // Different type -> returns false
        assertFalse(firstPredicate.equals(5));

        // Different start date -> returns false
        RevenueFilterPredicate differentStartDate = new RevenueFilterPredicate(
                Optional.of(LocalDate.of(2024, 2, 1)), Optional.of(endDate),
                Optional.of("Alice"), Optional.of(true));
        assertFalse(firstPredicate.equals(differentStartDate));

        // Different client name -> returns false
        RevenueFilterPredicate differentClientName = new RevenueFilterPredicate(
                Optional.of(startDate), Optional.of(endDate),
                Optional.of("Bob"), Optional.of(true));
        assertFalse(firstPredicate.equals(differentClientName));

        // Different status -> returns false
        RevenueFilterPredicate differentStatus = new RevenueFilterPredicate(
                Optional.of(startDate), Optional.of(endDate),
                Optional.of("Alice"), Optional.of(false));
        assertFalse(firstPredicate.equals(differentStatus));
    }

    @Test
    public void test_getters() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        RevenueFilterPredicate predicate = new RevenueFilterPredicate(
                Optional.of(startDate), Optional.of(endDate),
                Optional.of("Alice"), Optional.of(true));

        assertEquals(Optional.of(startDate), predicate.getStartDate());
        assertEquals(Optional.of(endDate), predicate.getEndDate());
        assertEquals(Optional.of("Alice"), predicate.getClientName());
        assertEquals(Optional.of(true), predicate.getIsDelivered());
    }

    @Test
    public void test_getters_emptyOptionals() {
        RevenueFilterPredicate predicate = new RevenueFilterPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        assertEquals(Optional.empty(), predicate.getStartDate());
        assertEquals(Optional.empty(), predicate.getEndDate());
        assertEquals(Optional.empty(), predicate.getClientName());
        assertEquals(Optional.empty(), predicate.getIsDelivered());
    }
}

