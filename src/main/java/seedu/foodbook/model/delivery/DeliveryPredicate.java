package seedu.foodbook.model.delivery;

import java.util.Optional;
import java.util.function.Predicate;

import seedu.foodbook.commons.util.StringUtil;
import seedu.foodbook.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Delivery}'s attributes match any of the specified criteria.
 * Supports filtering by client name, date, and tags.
 * If no filters are specified, matches all deliveries.
 */
public class DeliveryPredicate implements Predicate<Delivery> {

    private final Optional<? extends String> clientName;
    private final Optional<? extends String> date;
    private final Optional<? extends String> tag;

    /**
     * Constructs a DeliveryPredicate with the specified filters.
     *
     * @param clientName Optional client name to filter by (case-insensitive partial match)
     * @param date Optional date to filter by (exact match in dd/MM/yyyy format)
     * @param tag Optional list of tags to filter by (delivery must have at least one matching tag)
     */
    public DeliveryPredicate(Optional<? extends String> clientName,
                             Optional<? extends String> date,
                             Optional<? extends String> tag) {
        this.clientName = clientName;
        this.date = date;
        this.tag = tag;
    }

    @Override
    public boolean test(Delivery delivery) {
        // If no filters specified, show all deliveries
        if (clientName.isEmpty() && date.isEmpty() && tag.isEmpty()) {
            return false;
        }

        boolean matchesClientName = clientName.isEmpty()
                || StringUtil.containsWordIgnoreCase(delivery.getClient().getName().fullName, clientName.get());

        boolean matchesDate = date.isEmpty()
                || delivery.getDeliveryDate().getDateString().equals(date.get());

        boolean matchesTags = tag.isEmpty()
                || (delivery.getTag() != null
                && delivery.getTag().toLowerCase().contains(tag.get().toLowerCase()));

        // All specified filters must match (AND logic)
        return matchesClientName && matchesDate && matchesTags;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeliveryPredicate)) {
            return false;
        }

        DeliveryPredicate otherPredicate = (DeliveryPredicate) other;
        return clientName.equals(otherPredicate.clientName)
                && date.equals(otherPredicate.date)
                && tag.equals(otherPredicate.tag);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("clientName", clientName.map(String::valueOf).orElse(""))
                .add("date", date.map(String::valueOf).orElse(""))
                .add("tags", tag.map(String::valueOf).orElse(""))
                .toString();
    }
}
