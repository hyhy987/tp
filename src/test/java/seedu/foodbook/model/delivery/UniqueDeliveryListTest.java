package seedu.foodbook.model.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.testutil.Assert.assertThrows;
import static seedu.foodbook.testutil.TypicalDeliveries.ALICE_DELIVERY;
import static seedu.foodbook.testutil.TypicalDeliveries.BENSON_DELIVERY;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.foodbook.model.delivery.exceptions.DeliveryNotFoundException;
import seedu.foodbook.model.delivery.exceptions.DuplicateDeliveryException;
import seedu.foodbook.testutil.DeliveryBuilder;

public class UniqueDeliveryListTest {

    private final UniqueDeliveryList uniqueDeliveryList = new UniqueDeliveryList();

    @Test
    public void contains_nullDelivery_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueDeliveryList.contains(null));
    }

    @Test
    public void contains_deliveryNotInList_returnsFalse() {
        assertFalse(uniqueDeliveryList.contains(ALICE_DELIVERY));
    }

    @Test
    public void contains_deliveryInList_returnsTrue() {
        uniqueDeliveryList.add(ALICE_DELIVERY);
        assertTrue(uniqueDeliveryList.contains(ALICE_DELIVERY));
    }

    @Test
    public void contains_deliveryWithSameIdentityFieldsInList_returnsTrue() {
        uniqueDeliveryList.add(ALICE_DELIVERY);
        Delivery editedAliceDelivery = new DeliveryBuilder(ALICE_DELIVERY).withDateTime("28/4/2006", "1300").build();
        assertTrue(uniqueDeliveryList.contains(editedAliceDelivery));
    }

    @Test
    public void add_nullDelivery_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueDeliveryList.add(null));
    }

    @Test
    public void add_duplicateDelivery_throwsDuplicateDeliveryException() {
        uniqueDeliveryList.add(ALICE_DELIVERY);
        assertThrows(DuplicateDeliveryException.class, () -> uniqueDeliveryList.add(ALICE_DELIVERY));
    }

    @Test
    public void setDelivery_nullTargetDelivery_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueDeliveryList.setDelivery(null, ALICE_DELIVERY));
    }

    @Test
    public void setDelivery_nullEditedDelivery_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueDeliveryList.setDelivery(ALICE_DELIVERY, null));
    }

    @Test
    public void setDelivery_targetDeliveryNotInList_throwsDeliveryNotFoundException() {
        assertThrows(DeliveryNotFoundException.class, () ->
                uniqueDeliveryList.setDelivery(ALICE_DELIVERY, ALICE_DELIVERY));
    }

    @Test
    public void setDelivery_editedDeliveryIsSameDelivery_success() {
        uniqueDeliveryList.add(ALICE_DELIVERY);
        uniqueDeliveryList.setDelivery(ALICE_DELIVERY, ALICE_DELIVERY);
        UniqueDeliveryList expectedUniqueDeliveryList = new UniqueDeliveryList();
        expectedUniqueDeliveryList.add(ALICE_DELIVERY);
        assertEquals(expectedUniqueDeliveryList, uniqueDeliveryList);
    }

    @Test
    public void setDelivery_editedDeliveryHasSameIdentity_success() {
        uniqueDeliveryList.add(ALICE_DELIVERY);
        Delivery editedAliceDelivery = new DeliveryBuilder(ALICE_DELIVERY).withDateTime("28/4/2006", "1300").build();
        uniqueDeliveryList.setDelivery(ALICE_DELIVERY, editedAliceDelivery);
        UniqueDeliveryList expectedUniqueDeliveryList = new UniqueDeliveryList();
        expectedUniqueDeliveryList.add(editedAliceDelivery);
        assertEquals(expectedUniqueDeliveryList, uniqueDeliveryList);
    }

    @Test
    public void setDelivery_editedDeliveryHasDifferentIdentity_success() {
        uniqueDeliveryList.add(ALICE_DELIVERY);
        uniqueDeliveryList.setDelivery(ALICE_DELIVERY, BENSON_DELIVERY);
        UniqueDeliveryList expectedUniqueDeliveryList = new UniqueDeliveryList();
        expectedUniqueDeliveryList.add(BENSON_DELIVERY);
        assertEquals(expectedUniqueDeliveryList, uniqueDeliveryList);
    }

    @Test
    public void setDelivery_editedDeliveryHasNonUniqueIdentity_throwsDuplicateDeliveryException() {
        uniqueDeliveryList.add(ALICE_DELIVERY);
        uniqueDeliveryList.add(BENSON_DELIVERY);
        assertThrows(DuplicateDeliveryException.class, () ->
                uniqueDeliveryList.setDelivery(ALICE_DELIVERY, BENSON_DELIVERY));
    }

    @Test
    public void remove_nullDelivery_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueDeliveryList.remove(null));
    }

    @Test
    public void remove_deliveryDoesNotExist_throwsDeliveryNotFoundException() {
        assertThrows(DeliveryNotFoundException.class, () -> uniqueDeliveryList.remove(ALICE_DELIVERY));
    }

    @Test
    public void remove_existingDelivery_removesDelivery() {
        uniqueDeliveryList.add(ALICE_DELIVERY);
        uniqueDeliveryList.remove(ALICE_DELIVERY);
        UniqueDeliveryList expectedUniqueDeliveryList = new UniqueDeliveryList();
        assertEquals(expectedUniqueDeliveryList, uniqueDeliveryList);
    }

    @Test
    public void setDeliverys_nullUniqueDeliveryList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueDeliveryList.setDeliveries((UniqueDeliveryList) null));
    }

    @Test
    public void setDeliverys_uniqueDeliveryList_replacesOwnListWithProvidedUniqueDeliveryList() {
        uniqueDeliveryList.add(ALICE_DELIVERY);
        UniqueDeliveryList expectedUniqueDeliveryList = new UniqueDeliveryList();
        expectedUniqueDeliveryList.add(BENSON_DELIVERY);
        uniqueDeliveryList.setDeliveries(expectedUniqueDeliveryList);
        assertEquals(expectedUniqueDeliveryList, uniqueDeliveryList);
    }

    @Test
    public void setDeliveries_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueDeliveryList.setDeliveries((List<Delivery>) null));
    }

    @Test
    public void setDeliveries_list_replacesOwnListWithProvidedList() {
        uniqueDeliveryList.add(ALICE_DELIVERY);
        List<Delivery> deliveryList = Collections.singletonList(BENSON_DELIVERY);
        uniqueDeliveryList.setDeliveries(deliveryList);
        UniqueDeliveryList expectedUniqueDeliveryList = new UniqueDeliveryList();
        expectedUniqueDeliveryList.add(BENSON_DELIVERY);
        assertEquals(expectedUniqueDeliveryList, uniqueDeliveryList);
    }

    @Test
    public void setDeliveries_listWithDuplicateDeliveries_throwsDuplicateDeliveryException() {
        List<Delivery> listWithDuplicateDeliveries = Arrays.asList(ALICE_DELIVERY, ALICE_DELIVERY);
        assertThrows(DuplicateDeliveryException.class, () ->
                uniqueDeliveryList.setDeliveries(listWithDuplicateDeliveries));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueDeliveryList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueDeliveryList.asUnmodifiableObservableList().toString(), uniqueDeliveryList.toString());
    }
}
