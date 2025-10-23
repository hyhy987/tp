package seedu.foodbook.model.delivery;

import static java.util.Objects.requireNonNull;
import static seedu.foodbook.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.foodbook.model.delivery.exceptions.DeliveryNotFoundException;
import seedu.foodbook.model.delivery.exceptions.DuplicateDeliveryException;

/**
 * A list of deliveries that enforces uniqueness between its elements and does not allow nulls.
 * A delivery is considered unique by comparing using its identifier field (see {@code Delivery#equals(Object)}).
 *
 * Supports a minimal set of list operations.
 *
 * @see Delivery#equals(Object)
 */
public class UniqueDeliveryList implements Iterable<Delivery> {

    private final ObservableList<Delivery> internalList = FXCollections.observableArrayList();
    private final ObservableList<Delivery> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent Delivery as the given argument.
     */
    public boolean contains(Delivery toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameDelivery);
    }

    /**
     * Adds a Delivery to the list.
     * The Delivery must not already exist in the list.
     */
    public void add(Delivery toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateDeliveryException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the Delivery {@code target} in the list with {@code editedDelivery}.
     * {@code target} must exist in the list.
     * The Delivery identity of {@code editedDelivery} must not be the same as another existing Delivery in the list.
     */
    public void setDelivery(Delivery target, Delivery editedDelivery) {
        requireAllNonNull(target, editedDelivery);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new DeliveryNotFoundException();
        }

        if (!target.isSameDelivery(editedDelivery) && contains(editedDelivery)) {
            throw new DuplicateDeliveryException();
        }

        internalList.set(index, editedDelivery);
    }

    /**
     * Removes the equivalent Delivery from the list.
     * The Delivery must exist in the list.
     */
    public void remove(Delivery toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new DeliveryNotFoundException();
        }
    }

    public void setDeliveries(UniqueDeliveryList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code Deliveries}.
     * {@code Deliveries} must not contain duplicate Deliveries.
     */
    public void setDeliveries(List<Delivery> deliveries) {
        requireAllNonNull(deliveries);
        if (!deliveriesAreUnique(deliveries)) {
            throw new DuplicateDeliveryException();
        }

        internalList.setAll(deliveries);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Delivery> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Delivery> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueDeliveryList)) {
            return false;
        }

        UniqueDeliveryList otherUniqueDeliveryList = (UniqueDeliveryList) other;
        return internalList.equals(otherUniqueDeliveryList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code Deliveries} contains only unique Deliveries.
     */
    private boolean deliveriesAreUnique(List<Delivery> deliveries) {
        for (int i = 0; i < deliveries.size() - 1; i++) {
            for (int j = i + 1; j < deliveries.size(); j++) {
                if (deliveries.get(i).isSameDelivery(deliveries.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
