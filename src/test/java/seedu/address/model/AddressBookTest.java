package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalDeliveries.ALICE_DELIVERY;
import static seedu.address.testutil.TypicalFoodBook.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.delivery.Delivery;
import seedu.address.model.delivery.exceptions.DuplicateDeliveryException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.testutil.DeliveryBuilder;
import seedu.address.testutil.PersonBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
        assertEquals(Collections.emptyList(), addressBook.getDeliveryList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        List<Delivery> newDeliveries = Collections.emptyList();
        AddressBookStub newData = new AddressBookStub(newPersons, newDeliveries);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB)
                .build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void resetData_withDuplicateDeliveries_throwsDuplicateDeliveryException() {
        // Two deliveries with the same identity fields
        Delivery editedAliceDelivery = new DeliveryBuilder(ALICE_DELIVERY).withDateTime("13/12/2023", "1200").build();

        List<Delivery> newDeliveries = Arrays.asList(ALICE_DELIVERY, editedAliceDelivery);
        List<Person> newPersons = Collections.emptyList();
        AddressBookStub newData = new AddressBookStub(newPersons, newDeliveries);

        assertThrows(DuplicateDeliveryException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasDelivery_nulDelivery_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasDelivery(null));
    }

    @Test
    public void hasDelivery_deliveryNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasDelivery(ALICE_DELIVERY));
    }

    @Test
    public void hasDelivery_deliveryInAddressBook_returnsTrue() {
        addressBook.addDelivery(ALICE_DELIVERY);
        assertTrue(addressBook.hasDelivery(ALICE_DELIVERY));
    }

    @Test
    public void hasDelivery_deliveryWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addDelivery(ALICE_DELIVERY);
        Delivery editedAliceDelivery = new DeliveryBuilder(ALICE_DELIVERY).withDateTime("13/12/2023", "1200").build();
        assertTrue(addressBook.hasDelivery(editedAliceDelivery));
    }

    @Test
    public void getDeliveryList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getDeliveryList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName()
                + "{persons=" + addressBook.getPersonList() + ", "
                + "deliveries=" + addressBook.getDeliveryList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Delivery> deliveries = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons, Collection<Delivery> deliveries) {
            this.persons.setAll(persons);
            this.deliveries.setAll(deliveries);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Delivery> getDeliveryList() {
            return deliveries;
        }
    }

}
