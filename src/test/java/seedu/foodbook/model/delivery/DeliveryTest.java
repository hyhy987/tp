package seedu.foodbook.model.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.foodbook.model.delivery.TagKind.CORPORATE;
import static seedu.foodbook.model.delivery.TagKind.OTHER;
import static seedu.foodbook.model.delivery.TagKind.PERSONAL;
import static seedu.foodbook.testutil.TypicalDeliveries.ALICE_DELIVERY;
import static seedu.foodbook.testutil.TypicalDeliveries.BENSON_DELIVERY;
import static seedu.foodbook.testutil.TypicalDeliveries.BOB_DELIVERY;
import static seedu.foodbook.testutil.TypicalDeliveries.CARL_DELIVERY;
import static seedu.foodbook.testutil.TypicalDeliveries.DELIVERY_SAME_ID_AS_ALICE;
import static seedu.foodbook.testutil.TypicalDeliveries.ELLE_DELIVERY;
import static seedu.foodbook.testutil.TypicalPersons.ALICE;
import static seedu.foodbook.testutil.TypicalPersons.BOB;
import static seedu.foodbook.testutil.TypicalPersons.CARL;

import org.junit.jupiter.api.Test;

import seedu.foodbook.model.person.Person;
import seedu.foodbook.testutil.DeliveryBuilder;
import seedu.foodbook.testutil.PersonBuilder;

public class DeliveryTest {

    @Test
    public void isSameDelivery() {
        // same object -> returns true
        assertTrue(ALICE_DELIVERY.isSameDelivery(ALICE_DELIVERY));

        // null -> returns false
        assertFalse(ALICE_DELIVERY.isSameDelivery(null));

        // same name, all other attributes different -> returns true
        Delivery editedAliceDelivery = new DeliveryBuilder(ALICE_DELIVERY)
                .withClient(BOB).withCost(50d)
                .withRemarks("abc").asDelivered()
                .build();
        assertTrue(ALICE_DELIVERY.isSameDelivery(editedAliceDelivery));

        // different name, all other attributes same -> returns false
        editedAliceDelivery = new DeliveryBuilder(ALICE_DELIVERY).withId(50).build();
        assertFalse(ALICE_DELIVERY.isSameDelivery(editedAliceDelivery));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Delivery aliceDeliveryCopy = new DeliveryBuilder(ALICE_DELIVERY).build();
        assertTrue(ALICE_DELIVERY.equals(aliceDeliveryCopy));

        // same object -> returns true
        assertTrue(ALICE_DELIVERY.equals(ALICE_DELIVERY));

        // null -> returns false
        assertFalse(ALICE_DELIVERY.equals(null));

        // different type -> returns false
        assertFalse(ALICE_DELIVERY.equals(5));

        // different person -> returns false
        assertFalse(ALICE_DELIVERY.equals(BOB_DELIVERY));

        // different name -> returns false
        Delivery editedAliceDelivery = new DeliveryBuilder(ALICE_DELIVERY).withId(50).build();
        assertFalse(ALICE_DELIVERY.equals(editedAliceDelivery));

        // different client -> returns false
        editedAliceDelivery = new DeliveryBuilder(ALICE_DELIVERY).withClient(BOB).build();
        assertFalse(ALICE_DELIVERY.equals(editedAliceDelivery));

        // different cost -> returns false
        editedAliceDelivery = new DeliveryBuilder(ALICE_DELIVERY).withCost(40d).build();
        assertFalse(ALICE_DELIVERY.equals(editedAliceDelivery));

        // different remarks -> returns false
        editedAliceDelivery = new DeliveryBuilder(ALICE_DELIVERY).withRemarks("abc").build();
        assertFalse(ALICE_DELIVERY.equals(editedAliceDelivery));

        // different tags -> returns false
        editedAliceDelivery = new DeliveryBuilder(ALICE_DELIVERY).withTag("personal").build();
        assertFalse(ALICE_DELIVERY.equals(editedAliceDelivery));
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
    public void getTagKind_carlDelivery_noTag() {
        assertEquals(OTHER, CARL_DELIVERY.getTagKind());
    }

    @Test
    public void getTagKind_ellieDelivery_otherTag() {
        assertEquals(OTHER, ELLE_DELIVERY.getTagKind());
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

        Delivery markedDelivery = testDelivery.markAsDelivered();
        assertTrue(markedDelivery.getStatus());

        Delivery unmarkedDelivery = markedDelivery.unmarkAsDelivered();
        assertFalse(unmarkedDelivery.getStatus());
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
