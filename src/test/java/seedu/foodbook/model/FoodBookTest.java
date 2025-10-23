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
import seedu.foodbook.model.undo.exceptions.NoMoreUndoException;
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

    @Test
    public void undo_onEmptyStack_throwsNoMoreUndoException() {
        assertThrows(NoMoreUndoException.class, () -> foodBook.undo());
    }

    @Test
    public void checkpoint_thenUndo_restoresPersonsAndDeliveries() throws Exception {
        // Start with typical data
        FoodBook initial = getTypicalFoodBook();
        foodBook.resetData(initial);

        // Snapshot current state (for equality check after undo)
        FoodBook snapshot = new FoodBook(foodBook);

        // Take checkpoint of current state
        foodBook.checkpoint();

        // --- Mutate: remove a person and a delivery
        // (ALICE and ALICE_DELIVERY are in Typical data per your imports)
        foodBook.removePerson(ALICE);
        foodBook.removeDelivery(ALICE_DELIVERY);

        // Sanity: state changed
        assertFalse(foodBook.getPersonList().contains(ALICE));
        assertFalse(foodBook.getDeliveryList().contains(ALICE_DELIVERY));

        // --- Undo
        foodBook.undo();

        // Restored to exact snapshot
        assertEquals(snapshot, foodBook);
        // And ALICE / ALICE_DELIVERY are back
        assertTrue(foodBook.getPersonList().contains(ALICE));
        assertTrue(foodBook.getDeliveryList().contains(ALICE_DELIVERY));
    }

    @Test
    public void multipleCheckpoints_multipleUndos_restoreStepByStep() throws Exception {
        // State S0: empty
        foodBook.resetData(new FoodBook());
        FoodBook s0 = new FoodBook(foodBook);
        foodBook.checkpoint(); // save S0

        // State S1: add ALICE person
        foodBook.addPerson(ALICE);
        FoodBook s1 = new FoodBook(foodBook);
        foodBook.checkpoint(); // save S1

        // State S2: add ALICE_DELIVERY as well
        foodBook.addDelivery(ALICE_DELIVERY);
        FoodBook s2 = new FoodBook(foodBook);
        foodBook.checkpoint(); // save S2

        // Now mutate away from S2 (remove both to be clearly different)
        foodBook.removeDelivery(ALICE_DELIVERY);
        foodBook.removePerson(ALICE);

        // Undo #1 -> back to S2
        foodBook.undo();
        assertEquals(s2, foodBook);
        assertTrue(foodBook.getPersonList().contains(ALICE));
        assertTrue(foodBook.getDeliveryList().contains(ALICE_DELIVERY));

        // Undo #2 -> back to S1 (person only, no delivery)
        foodBook.undo();
        assertEquals(s1, foodBook);
        assertTrue(foodBook.getPersonList().contains(ALICE));
        assertFalse(foodBook.getDeliveryList().contains(ALICE_DELIVERY));

        // Undo #3 -> back to S0 (empty)
        foodBook.undo();
        assertEquals(s0, foodBook);
        assertTrue(foodBook.getPersonList().isEmpty());
        assertTrue(foodBook.getDeliveryList().isEmpty());

        // One more undo should fail
        assertThrows(NoMoreUndoException.class, () -> foodBook.undo());
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
