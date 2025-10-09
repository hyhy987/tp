package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class ClientMatchesPredicateTest {

    @Test
    public void equals() {
        ClientMatchesPredicate firstPredicate =
                new ClientMatchesPredicate(Optional.of("Alice"), Optional.empty(), Optional.empty());
        ClientMatchesPredicate secondPredicate =
                new ClientMatchesPredicate(Optional.of("Bob"), Optional.empty(), Optional.empty());

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        ClientMatchesPredicate firstPredicateCopy =
                new ClientMatchesPredicate(Optional.of("Alice"), Optional.empty(), Optional.empty());
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different name query -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameMatches_returnsTrue() {
        // Full name match
        ClientMatchesPredicate predicate =
                new ClientMatchesPredicate(Optional.of("Alice"), Optional.empty(), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Partial name match
        predicate = new ClientMatchesPredicate(Optional.of("Ali"), Optional.empty(), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Case insensitive match
        predicate = new ClientMatchesPredicate(Optional.of("alice"), Optional.empty(), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Match in multi-word name
        predicate = new ClientMatchesPredicate(Optional.of("Bob"), Optional.empty(), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_phoneMatches_returnsTrue() {
        // Full phone match
        ClientMatchesPredicate predicate =
                new ClientMatchesPredicate(Optional.empty(), Optional.of("12345678"), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withPhone("12345678").build()));

        // Partial phone match
        predicate = new ClientMatchesPredicate(Optional.empty(), Optional.of("1234"), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withPhone("12345678").build()));

        // Phone match at end
        predicate = new ClientMatchesPredicate(Optional.empty(), Optional.of("5678"), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withPhone("12345678").build()));
    }

    @Test
    public void test_emailMatches_returnsTrue() {
        // Full email match
        ClientMatchesPredicate predicate =
                new ClientMatchesPredicate(Optional.empty(), Optional.empty(), Optional.of("alice@example.com"));
        assertTrue(predicate.test(new PersonBuilder().withEmail("alice@example.com").build()));

        // Partial email match
        predicate = new ClientMatchesPredicate(Optional.empty(), Optional.empty(), Optional.of("alice"));
        assertTrue(predicate.test(new PersonBuilder().withEmail("alice@example.com").build()));

        // Case insensitive email match
        predicate = new ClientMatchesPredicate(Optional.empty(), Optional.empty(), Optional.of("ALICE"));
        assertTrue(predicate.test(new PersonBuilder().withEmail("alice@example.com").build()));

        // Domain match
        predicate = new ClientMatchesPredicate(Optional.empty(), Optional.empty(), Optional.of("example.com"));
        assertTrue(predicate.test(new PersonBuilder().withEmail("alice@example.com").build()));
    }

    @Test
    public void test_multipleFieldsMatch_returnsTrue() {
        // All fields match
        ClientMatchesPredicate predicate =
                new ClientMatchesPredicate(Optional.of("Alice"), Optional.of("12345678"),
                        Optional.of("alice@example.com"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345678")
                .withEmail("alice@example.com").build()));

        // Name and phone match
        predicate = new ClientMatchesPredicate(Optional.of("Alice"), Optional.of("1234"), Optional.empty());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345678").build()));

        // Name and email match
        predicate = new ClientMatchesPredicate(Optional.of("Alice"), Optional.empty(), Optional.of("alice"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withEmail("alice@example.com").build()));

        // Phone and email match
        predicate = new ClientMatchesPredicate(Optional.empty(), Optional.of("1234"), Optional.of("alice"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("12345678").withEmail("alice@example.com").build()));
    }

    @Test
    public void test_nameDoesNotMatch_returnsFalse() {
        ClientMatchesPredicate predicate =
                new ClientMatchesPredicate(Optional.of("Bob"), Optional.empty(), Optional.empty());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));
    }

    @Test
    public void test_phoneDoesNotMatch_returnsFalse() {
        ClientMatchesPredicate predicate =
                new ClientMatchesPredicate(Optional.empty(), Optional.of("99999999"), Optional.empty());
        assertFalse(predicate.test(new PersonBuilder().withPhone("12345678").build()));
    }

    @Test
    public void test_emailDoesNotMatch_returnsFalse() {
        ClientMatchesPredicate predicate =
                new ClientMatchesPredicate(Optional.empty(), Optional.empty(), Optional.of("bob"));
        assertFalse(predicate.test(new PersonBuilder().withEmail("alice@example.com").build()));
    }

    @Test
    public void test_oneFieldDoesNotMatch_returnsFalse() {
        // Name matches but phone doesn't (AND logic)
        ClientMatchesPredicate predicate =
                new ClientMatchesPredicate(Optional.of("Alice"), Optional.of("99999999"), Optional.empty());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345678").build()));

        // Name matches but email doesn't
        predicate = new ClientMatchesPredicate(Optional.of("Alice"), Optional.empty(), Optional.of("bob"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withEmail("alice@example.com").build()));

        // Phone matches but name doesn't
        predicate = new ClientMatchesPredicate(Optional.of("Bob"), Optional.of("1234"), Optional.empty());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345678").build()));
    }

    @Test
    public void test_hasSearchCriteria() {
        // Has name criteria
        ClientMatchesPredicate predicate =
                new ClientMatchesPredicate(Optional.of("Alice"), Optional.empty(), Optional.empty());
        assertTrue(predicate.hasSearchCriteria());

        // Has phone criteria
        predicate = new ClientMatchesPredicate(Optional.empty(), Optional.of("12345678"), Optional.empty());
        assertTrue(predicate.hasSearchCriteria());

        // Has email criteria
        predicate = new ClientMatchesPredicate(Optional.empty(), Optional.empty(), Optional.of("alice@example.com"));
        assertTrue(predicate.hasSearchCriteria());

        // Has multiple criteria
        predicate = new ClientMatchesPredicate(Optional.of("Alice"), Optional.of("12345678"),
                Optional.of("alice@example.com"));
        assertTrue(predicate.hasSearchCriteria());

        // No criteria (all empty)
        predicate = new ClientMatchesPredicate(Optional.empty(), Optional.empty(), Optional.empty());
        assertFalse(predicate.hasSearchCriteria());
    }

    @Test
    public void toStringMethod() {
        ClientMatchesPredicate predicate =
                new ClientMatchesPredicate(Optional.of("Alice"), Optional.of("12345678"),
                        Optional.of("alice@example.com"));
        String expected = ClientMatchesPredicate.class.getCanonicalName()
                + "{nameQuery=Alice, phoneQuery=12345678, emailQuery=alice@example.com}";
        assertEquals(expected, predicate.toString());
    }
}
