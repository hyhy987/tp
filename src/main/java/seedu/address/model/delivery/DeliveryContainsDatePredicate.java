package seedu.address.model.delivery;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Delivery}'s {@code DateTime} matches any of the keywords given.
 */
public class DeliveryContainsDatePredicate implements Predicate<Delivery> {
    private final String date;

    public DeliveryContainsDatePredicate(String date) {
        this.date = date;
    }

    @Override
    public boolean test(Delivery delivery) {
        return delivery.getDeliveryDate().getDateString().equals(date);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof DeliveryContainsDatePredicate
                && date.equals(((DeliveryContainsDatePredicate) other).date));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("date", date).toString();
    }
}
