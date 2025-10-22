package seedu.foodbook.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.foodbook.commons.util.ToStringBuilder;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.delivery.UniqueDeliveryList;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.model.person.UniquePersonList;
import seedu.foodbook.model.undo.FoodBookRecord;
import seedu.foodbook.model.undo.UndoStack;
import seedu.foodbook.model.undo.exceptions.NoMoreUndoException;

/**
 * Wraps all data at the food-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class FoodBook implements ReadOnlyFoodBook {

    private final UniquePersonList persons;
    private final UniqueDeliveryList deliveries;

    private final UndoStack<FoodBookRecord> undoStack;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        deliveries = new UniqueDeliveryList();
        undoStack = new UndoStack<>();
    }

    public FoodBook() {}

    /**
     * Creates an FoodBook using the Persons in the {@code toBeCopied}
     */
    public FoodBook(ReadOnlyFoodBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Resets the existing data of this {@code FoodBook} with {@code newData}.
     */
    public void resetData(ReadOnlyFoodBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setDeliveries(newData.getDeliveryList());
    }

    //// person-level operations
    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Returns true if a person with the same identity as {@code person} exists in the food book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the food book.
     * The person must not already exist in the food book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the food book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the food book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code FoodBook}.
     * {@code key} must exist in the food book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// delivery-level operations

    /**
     * Replaces the contents of the delivery list with {@code deliveries}.
     * {@code deliveries} must not contain duplicate deliveries.
     */
    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries.setDeliveries(deliveries);
    }

    /**
     * Returns true if a delivery with the same identity as {@code delivery} exists
     */
    public boolean hasDelivery(Delivery delivery) {
        requireNonNull(delivery);
        return deliveries.contains(delivery);
    }

    /**
     * Adds a delivery to the food book.
     */
    public void addDelivery(Delivery d) {
        deliveries.add(d);
    }

    /**
     * Replaces the given delivery {@code target} in the list with {@code editedDelivery}.
     * {@code target} must exist in the food book.
     */
    public void setDelivery(Delivery target, Delivery editedDelivery) {
        requireNonNull(editedDelivery);

        deliveries.setDelivery(target, editedDelivery);
    }

    /**
     * Removes {@code key} from this {@code FoodBok}.
     * {@code key} must exist in the food book.
     */
    public void removeDelivery(Delivery key) {
        deliveries.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("deliveries", deliveries)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Delivery> getDeliveryList() {
        return deliveries.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FoodBook)) {
            return false;
        }

        FoodBook otherFoodBook = (FoodBook) other;
        return persons.equals(otherFoodBook.persons) && deliveries.equals(otherFoodBook.deliveries);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }

    //=========== Undo State Management =============================================================

    /**
     * Takes a checkpoint of the current foodBook state for undo
     * The state of the foodBook consists of the current person and delivery list.
     */
    public void checkpoint() {
        FoodBookRecord record = new FoodBookRecord(
                List.copyOf(getPersonList()),
                List.copyOf(getDeliveryList())
        );

        this.undoStack.checkpoint(record);
    }

    /**
     * Reverts the state of foodBook to before the most previous edit
     * @throws NoMoreUndoException If no more stored states remain
     */
    public void undo() throws NoMoreUndoException {
        FoodBookRecord record = this.undoStack.undo();

        this.setPersons(record.personList());
        this.setDeliveries(record.deliveryList());

    }
}
