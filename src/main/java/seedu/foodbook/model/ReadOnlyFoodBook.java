package seedu.foodbook.model;

import javafx.collections.ObservableList;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;

/**
 * Unmodifiable view of an food book
 */
public interface ReadOnlyFoodBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the deliveries list.
     * This list will not contain any duplicate deliveries.
     */
    ObservableList<Delivery> getDeliveryList();

}
