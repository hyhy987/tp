package seedu.address.model.delivery;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.DeliveryBuilder;

/**
 * Unit tests for {@code DeliveryContainsDatePredicate}.
 */
public class DeliveryContainsDatePredicateTest {

    @Test
    public void equals() {
        var firstPredicate = new DeliveryContainsDatePredicate("25/12/2024");
        var secondPredicate = new DeliveryContainsDatePredicate("31/12/2024");

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        var firstPredicateCopy = new DeliveryContainsDatePredicate("25/12/2024");
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different date -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_dateContainsKeyword_returnsTrue() {
        // One keyword
        DeliveryContainsDatePredicate predicate =
                new DeliveryContainsDatePredicate("25/12/2024");
        assertTrue(predicate.test(new DeliveryBuilder().withDateTime("25/12/2024" , "1300").build()));
    }

    @Test
    public void test_dateDoesNotContainKeyword_returnsFalse() {
        // Zero keywords
        DeliveryContainsDatePredicate predicate = new DeliveryContainsDatePredicate("");
        assertFalse(predicate.test(new DeliveryBuilder().withDateTime("25/12/2024" , "1300").build()));

        // Non-matching keyword
        predicate = new DeliveryContainsDatePredicate("01/01/2025");
        assertFalse(predicate.test(new DeliveryBuilder().withDateTime("25/12/2024" , "1300").build()));
    }
}
