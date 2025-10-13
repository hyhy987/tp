package seedu.foodbook.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.foodbook.testutil.Assert.assertThrows;
import static seedu.foodbook.testutil.TypicalDeliveries.ALICE_DELIVERY;
import static seedu.foodbook.testutil.TypicalFoodBook.getTypicalFoodBook;
import static seedu.foodbook.testutil.TypicalPersons.ALICE;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.delivery.exceptions.DuplicateDeliveryException;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.model.person.exceptions.DuplicatePersonException;
import seedu.foodbook.testutil.DeliveryBuilder;
import seedu.foodbook.testutil.PersonBuilder;

public class FoodBookTest {

    private final FoodBook foodBook = new FoodBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), foodBook.getPersonList());
        assertEquals(Collections.emptyList(), foodBook.getDeliveryList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> foodBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyFoodBook_replacesData() {
        FoodBook newData = getTypicalFoodBook();
        foodBook.resetData(newData);
        assertEquals(newData, foodBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        List<Delivery> newDeliveries = Collections.emptyList();
        FoodBookStub newData = new FoodBookStub(newPersons, newDeliveries);

        assertThrows(DuplicatePersonException.class, () -> foodBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> foodBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInFoodBook_returnsFalse() {
        assertFalse(foodBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInFoodBook_returnsTrue() {
        foodBook.addPerson(ALICE);
        assertTrue(foodBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInFoodBook_returnsTrue() {
        foodBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(foodBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> foodBook.getPersonList().remove(0));
    }

    @Test
    public void resetData_withDuplicateDeliveries_throwsDuplicateDeliveryException() {
        // Two deliveries with the same identity fields
        Delivery editedAliceDelivery = new DeliveryBuilder(ALICE_DELIVERY).withDateTime("13/12/2023", "1200").build();

        List<Delivery> newDeliveries = Arrays.asList(ALICE_DELIVERY, editedAliceDelivery);
        List<Person> newPersons = Collections.emptyList();
        FoodBookStub newData = new FoodBookStub(newPersons, newDeliveries);

        assertThrows(DuplicateDeliveryException.class, () -> foodBook.resetData(newData));
    }

    @Test
    public void hasDelivery_nulDelivery_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> foodBook.hasDelivery(null));
    }

    @Test
    public void hasDelivery_deliveryNotInFoodBook_returnsFalse() {
        assertFalse(foodBook.hasDelivery(ALICE_DELIVERY));
    }

    @Test
    public void hasDelivery_deliveryInFoodBook_returnsTrue() {
        foodBook.addDelivery(ALICE_DELIVERY);
        assertTrue(foodBook.hasDelivery(ALICE_DELIVERY));
    }

    @Test
    public void hasDelivery_deliveryWithSameIdentityFieldsInFoodBook_returnsTrue() {
        foodBook.addDelivery(ALICE_DELIVERY);
        Delivery editedAliceDelivery = new DeliveryBuilder(ALICE_DELIVERY).withDateTime("13/12/2023", "1200").build();
        assertTrue(foodBook.hasDelivery(editedAliceDelivery));
    }

    @Test
    public void getDeliveryList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> foodBook.getDeliveryList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = FoodBook.class.getCanonicalName()
                + "{persons=" + foodBook.getPersonList() + ", "
                + "deliveries=" + foodBook.getDeliveryList() + "}";
        assertEquals(expected, foodBook.toString());
    }

    /**
     * A stub ReadOnlyFoodBook whose persons list can violate interface constraints.
     */
    private static class FoodBookStub implements ReadOnlyFoodBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Delivery> deliveries = FXCollections.observableArrayList();

        FoodBookStub(Collection<Person> persons, Collection<Delivery> deliveries) {
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
