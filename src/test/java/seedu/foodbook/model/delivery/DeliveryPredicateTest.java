package seedu.foodbook.model.delivery;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.testutil.TypicalDeliveries.ALICE_DELIVERY;
import static seedu.foodbook.testutil.TypicalDeliveries.BOB_DELIVERY;
import static seedu.foodbook.testutil.TypicalDeliveries.CARL_DELIVERY;

import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * Contains unit tests for {@code DeliveryPredicate}.
 */
public class DeliveryPredicateTest {

    @Test
    public void equals() {
        DeliveryPredicate firstPredicate = new DeliveryPredicate(
                Optional.of("25/12/2024"), Optional.of("25/12/2024"), Optional.of("Alice"),
                Optional.of("personal"), Optional.empty());
        DeliveryPredicate secondPredicate = new DeliveryPredicate(
                Optional.of("26/12/2024"), Optional.of("26/12/2024"), Optional.of("Bob"),
                Optional.of("corporate"), Optional.empty());

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        DeliveryPredicate firstPredicateCopy = new DeliveryPredicate(
                Optional.of("25/12/2024"), Optional.of("25/12/2024"), Optional.of("Alice"),
                Optional.of("personal"), Optional.empty());
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different predicate -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_noFiltersProvided_returnsTrue() {
        // No filters -> return true
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        assertTrue(predicate.test(ALICE_DELIVERY));
        assertTrue(predicate.test(BOB_DELIVERY));
        assertTrue(predicate.test(CARL_DELIVERY));
    }

    @Test
    public void test_clientNameMatches_returnsTrue() {
        // Client name contains "Alice"
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.of("Alice"), Optional.empty(), Optional.empty());

        // Assuming ALICE_DELIVERY has client name "Alice Pauline"
        assertTrue(predicate.test(ALICE_DELIVERY));
    }

    @Test
    public void test_clientNameDoesNotMatch_returnsFalse() {
        // Client name does not contain "Bob"
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.of("Bob"), Optional.empty(), Optional.empty());

        // ALICE_DELIVERY's client is "Alice Pauline", doesn't contain "Bob"
        assertFalse(predicate.test(ALICE_DELIVERY));
    }

    @Test
    public void test_clientNameCaseInsensitive_returnsTrue() {
        // Case insensitive matching
        DeliveryPredicate predicateLower = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.of("alice"), Optional.empty(), Optional.empty());
        DeliveryPredicate predicateUpper = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.of("ALICE"), Optional.empty(), Optional.empty());

        assertTrue(predicateLower.test(ALICE_DELIVERY));
        assertTrue(predicateUpper.test(ALICE_DELIVERY));
    }

    @Test
    public void test_clientNamePartialMatch_returnsTrue() {
        // Partial name matching (word-based)
        DeliveryPredicate predicateFirstName = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.of("Alice"), Optional.empty(), Optional.empty());
        DeliveryPredicate predicateLastName = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.of("Pauline"), Optional.empty(), Optional.empty());

        assertTrue(predicateFirstName.test(ALICE_DELIVERY));
        assertTrue(predicateLastName.test(ALICE_DELIVERY));
    }

    @Test
    public void test_dateDoesNotMatch_returnsFalse() {
        // Different date
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.of("31/12/2024"), Optional.of("31/12/2024"), Optional.empty(),
                Optional.empty(), Optional.empty());

        // ALICE_DELIVERY has date "25/12/2024", not "31/12/2024"
        assertFalse(predicate.test(ALICE_DELIVERY));
    }

    @Test
    public void test_singleTagMatches_returnsTrue() {
        // Single tag matching
        // Assuming ALICE_DELIVERY has tag "personal"
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("personal"), Optional.empty());

        assertTrue(predicate.test(ALICE_DELIVERY));
    }

    @Test
    public void test_singleTagDoesNotMatch_returnsFalse() {
        // Tag doesn't match
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("corporate"), Optional.empty());

        // ALICE_DELIVERY has tag "personal", not "corporate"
        assertFalse(predicate.test(ALICE_DELIVERY));
    }

    @Test
    public void test_tagCaseInsensitive_returnsTrue() {
        // Tag matching is case-insensitive
        DeliveryPredicate predicateLower = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("personal"), Optional.empty());
        DeliveryPredicate predicateUpper = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("PERSONAL"), Optional.empty());
        DeliveryPredicate predicateMixed = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("PeRsOnAl"), Optional.empty());

        // Assuming ALICE_DELIVERY has tag "personal"
        assertTrue(predicateLower.test(ALICE_DELIVERY));
        assertTrue(predicateUpper.test(ALICE_DELIVERY));
        assertTrue(predicateMixed.test(ALICE_DELIVERY));
    }

    @Test
    public void test_tagPartialMatch_returnsTrue() {
        // Tag uses contains (substring matching)
        // Assuming ALICE_DELIVERY has tag "personal"
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("person"), Optional.empty());

        assertTrue(predicate.test(ALICE_DELIVERY)); // "personal" contains "person"
    }

    @Test
    public void test_someFiltersMatch_returnsFalse() {
        // AND logic: if ANY filter doesn't match, return false

        // Client matches, date matches, but tag doesn't match
        DeliveryPredicate predicate1 = new DeliveryPredicate(
                Optional.of("25/12/2024"), Optional.of("25/12/2024"), Optional.of("Alice"),
                Optional.of("corporate"), Optional.empty());
        assertFalse(predicate1.test(ALICE_DELIVERY));

        // Client matches, tag matches, but date doesn't match
        DeliveryPredicate predicate2 = new DeliveryPredicate(
                Optional.of("31/12/2024"), Optional.of("31/12/2024"), Optional.of("Alice"),
                Optional.of("personal"), Optional.empty());
        assertFalse(predicate2.test(ALICE_DELIVERY));

        // Date matches, tag matches, but client doesn't match
        DeliveryPredicate predicate3 = new DeliveryPredicate(
                Optional.of("25/12/2024"), Optional.of("25/12/2024"), Optional.of("Bob"),
                Optional.of("personal"), Optional.empty());
        assertFalse(predicate3.test(ALICE_DELIVERY));
    }

    @Test
    public void test_multipleDeliveries_filterCorrectly() {
        // Test filtering multiple deliveries
        DeliveryPredicate predicateForAlice = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.of("Alice"), Optional.empty(), Optional.empty());
        DeliveryPredicate predicateForBob = new DeliveryPredicate(
                Optional.empty(), Optional.empty(), Optional.of("Bob"), Optional.empty(), Optional.empty());

        // ALICE_DELIVERY should match Alice filter
        assertTrue(predicateForAlice.test(ALICE_DELIVERY));
        assertFalse(predicateForAlice.test(BOB_DELIVERY));

        // BOB_DELIVERY should match Bob filter
        assertTrue(predicateForBob.test(BOB_DELIVERY));
        assertFalse(predicateForBob.test(ALICE_DELIVERY));
    }

    @Test
    public void test_toString() {
        DeliveryPredicate predicate = new DeliveryPredicate(
                Optional.of("25/12/2024"), Optional.of("25/12/2024"), Optional.of("Alice"),
                Optional.of("urgent"), Optional.empty());

        String result = predicate.toString();

        // Verify toString contains all the fields
        assertTrue(result.contains("clientName"));
        assertTrue(result.contains("startDate"));
        assertTrue(result.contains("endDate"));
        assertTrue(result.contains("tag"));
    }
}
