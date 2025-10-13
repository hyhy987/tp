package seedu.foodbook.model.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.foodbook.model.person.Address;
import seedu.foodbook.model.person.Email;
import seedu.foodbook.model.person.Name;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.model.person.Phone;
import seedu.foodbook.model.tag.Tag;

public class DeliveryTest {

    private Person clientA;
    private Person clientB;
    private DateTime dt1;
    private DateTime dt2;
    private Delivery delivery1;
    private Delivery delivery2;
    private Delivery delivery3;

    @BeforeEach
    public void setUp() {
        // create dummy clients
        Name nameA = new Name("Alice Yeoh");
        Phone phoneA = new Phone("91234567");
        Email emailA = new Email("alice@example.com");
        Address addressA = new Address("123 Orchard Road");
        Set<Tag> tagsA = new HashSet<>(Collections.emptySet());
        clientA = new Person(nameA, phoneA, emailA, addressA, tagsA);

        Name nameB = new Name("Bob Tan");
        Phone phoneB = new Phone("92345678");
        Email emailB = new Email("bob@example.com");
        Address addressB = new Address("456 Bukit Timah");
        Set<Tag> tagsB = new HashSet<>(Collections.emptySet());
        clientB = new Person(nameB, phoneB, emailB, addressB, tagsB);

        dt1 = new DateTime("01/01/2025", "1000");
        dt2 = new DateTime("02/01/2025", "1100");

        delivery1 = new Delivery(1, clientA, dt1, "Order 1", 25.0);
        delivery2 = new Delivery(1, clientA, dt1, "Order 1", 25.0);
        delivery3 = new Delivery(2, clientB, dt2, "Order 2", 30.0);
    }

    @Test
    public void equals_sameId_returnsTrue() {
        // Same id => equal, regardless of other fields
        assertTrue(delivery1.equals(delivery2));
    }

    @Test
    public void equals_differentId_returnsFalse() {
        assertFalse(delivery1.equals(delivery3));
    }

    @Test
    public void getId_gettersReturnExpected() {
        assertEquals(1, delivery1.getId());
        assertEquals(clientA, delivery1.getClient());
        assertEquals(dt1, delivery1.getDeliveryDate());
        assertEquals("Order 1", delivery1.getRemarks());
        assertEquals(25.0, delivery1.getCost());
    }

    @Test
    public void markAndUnmark_statusTransitions() {
        // Initially not delivered
        assertFalse(delivery1.getStatus());

        delivery1.markAsDelivered();
        assertTrue(delivery1.getStatus());

        delivery1.unmarkAsDelivered();
        assertFalse(delivery1.getStatus());
    }

    @Test
    public void hashCode_sameIdSameHash() {
        assertEquals(delivery1.hashCode(), delivery2.hashCode());
    }

}
