package seedu.foodbook.model.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.model.delivery.TagKind.CORPORATE;
import static seedu.foodbook.model.delivery.TagKind.OTHER;
import static seedu.foodbook.model.delivery.TagKind.PERSONAL;
import static seedu.foodbook.testutil.TypicalDeliveries.ALICE_DELIVERY;
import static seedu.foodbook.testutil.TypicalDeliveries.BENSON_DELIVERY;
import static seedu.foodbook.testutil.TypicalDeliveries.CARL_DELIVERY;
import static seedu.foodbook.testutil.TypicalDeliveries.DELIVERY_SAME_ID_AS_ALICE;
import static seedu.foodbook.testutil.TypicalPersons.ALICE;
import static seedu.foodbook.testutil.TypicalPersons.CARL;

import org.junit.jupiter.api.Test;


public class DeliveryTest {

    @Test
    public void equals_sameId_returnsTrue() {
        // Same id => equal, regardless of other fields
        assertTrue(ALICE_DELIVERY.equals(DELIVERY_SAME_ID_AS_ALICE));
    }

    @Test
    public void equals_differentId_returnsFalse() {
        assertFalse(ALICE_DELIVERY.equals(BENSON_DELIVERY));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        assertTrue(ALICE_DELIVERY.equals(ALICE_DELIVERY));
    }

    @Test
    public void equals_null_returnsFalse() {
        assertFalse(ALICE_DELIVERY.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        assertFalse(ALICE_DELIVERY.equals("not a delivery"));
    }

    @Test
    public void getTagKind_aliceDelivery_returnsPersonal() {
        assertEquals(PERSONAL, ALICE_DELIVERY.getTagKind());
    }

    @Test
    public void getTagKind_bensonDelivery_returnsCorporate() {
        assertEquals(CORPORATE, BENSON_DELIVERY.getTagKind());
    }

    @Test
    public void getTagKind_carlDelivery_noTag_returnsOther() {
        assertEquals(OTHER, CARL_DELIVERY.getTagKind());
    }

    @Test
    public void getId_gettersReturnExpected() {
        assertEquals(0, ALICE_DELIVERY.getId());
        assertEquals(ALICE, ALICE_DELIVERY.getClient());
        assertEquals("NIL", ALICE_DELIVERY.getRemarks());
        assertEquals(10.0, ALICE_DELIVERY.getCost());
        assertEquals("Personal", ALICE_DELIVERY.getTag());
    }

    @Test
    public void getters_deliveryWithoutTag_returnsExpected() {
        assertEquals(2, CARL_DELIVERY.getId());
        assertEquals(CARL, CARL_DELIVERY.getClient());
        assertEquals("NIL", CARL_DELIVERY.getRemarks());
        assertEquals(30.00, CARL_DELIVERY.getCost());
        // Test default tag behavior if applicable
    }

    @Test
    public void markAndUnmark_statusTransitions() {
        // Use a fresh delivery for this test
        Delivery testDelivery = new Delivery(999,
                ALICE,
                new DateTime("01/01/2025", "1000"),
                "Test order",
                10.0, null);

        // Initially not delivered
        assertFalse(testDelivery.getStatus());

        testDelivery.markAsDelivered();
        assertTrue(testDelivery.getStatus());

        testDelivery.unmarkAsDelivered();
        assertFalse(testDelivery.getStatus());
    }

    @Test
    public void hashCode_sameIdSameHash() {
        assertEquals(ALICE_DELIVERY.hashCode(), DELIVERY_SAME_ID_AS_ALICE.hashCode());
    }

    @Test
    public void hashCode_differentIdDifferentHash() {
        assertFalse(ALICE_DELIVERY.hashCode() == BENSON_DELIVERY.hashCode());
    }

    @Test
    public void toString_containsExpectedFields() {
        String result = ALICE_DELIVERY.toString();
        assertTrue(result.contains("0")); // ID
        assertTrue(result.contains("Alice")); // Client name
        assertTrue(result.contains("NIL")); // Remarks
        assertTrue(result.contains("10.0")); // Cost
        assertTrue(result.contains("Personal")); // Tag
    }

}
