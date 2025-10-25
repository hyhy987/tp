package seedu.foodbook.model.delivery;

import java.util.Optional;
import java.util.function.Predicate;

import seedu.foodbook.commons.util.StringUtil;
import seedu.foodbook.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Delivery}'s attributes match any of the specified criteria.
 * Supports filtering by client name, date range, tags, and delivery status.
 * If no filters are specified, matches all deliveries.
 * All criteria are optional and use AND logic (all provided criteria must match).
 */
public class DeliveryPredicate implements Predicate<Delivery> {

    private final Optional<String> startDate;
    private final Optional<String> endDate;
    private final Optional<String> clientName;
    private final Optional<String> tag;
    private final Optional<Boolean> isDelivered;

    /**
     * Constructs a DeliveryPredicate with the specified filters.
     *
     * @param startDateString Optional start date string for filtering (inclusive, d/M/yyyy format)
     * @param endDateString Optional end date string for filtering (inclusive, d/M/yyyy format)
     * @param clientName Optional client name to filter by (case-insensitive partial match)
     * @param tag Optional tag to filter by (case-insensitive partial match)
     * @param isDelivered Optional delivery status filter (true for delivered, false for not delivered)
     */
    public DeliveryPredicate(Optional<String> startDateString,
                             Optional<String> endDateString,
                             Optional<String> clientName,
                             Optional<String> tag,
                             Optional<Boolean> isDelivered) {
        // Store string dates directly
        this.startDate = startDateString.map(String::trim).filter(s -> !s.isEmpty());
        this.endDate = endDateString.map(String::trim).filter(s -> !s.isEmpty());

        this.clientName = clientName.map(String::trim).filter(s -> !s.isEmpty());
        this.tag = tag.map(String::trim).filter(s -> !s.isEmpty());
        this.isDelivered = isDelivered;
    }

    @Override
    public boolean test(Delivery delivery) {
        boolean matchesStartDate = startDate.map(start -> {
            DateTime startDateTime = new DateTime(start, "0000");
            return !delivery.getDeliveryDate().isBefore(startDateTime);
        }).orElse(true);

        boolean matchesEndDate = endDate.map(end -> {
            DateTime endDateTime = new DateTime(end, "2359");
            return !delivery.getDeliveryDate().isAfter(endDateTime);
        }).orElse(true);

        boolean matchesClientName = clientName.map(name ->
                StringUtil.containsWordIgnoreCase(delivery.getClient().getName().fullName, name)
        ).orElse(true);

        boolean matchesTag = tag.map(t ->
                delivery.getTag() != null && delivery.getTag().toLowerCase().contains(t.toLowerCase())
        ).orElse(true);

        boolean matchesStatus = isDelivered.map(status ->
                delivery.getStatus() == status
        ).orElse(true);

        // Return true if ALL provided criteria match (AND logic)
        return matchesStartDate && matchesEndDate && matchesClientName && matchesTag && matchesStatus;
    }


    public Optional<String> getStartDate() {
        return startDate;
    }

    public Optional<String> getEndDate() {
        return endDate;
    }

    public Optional<String> getClientName() {
        return clientName;
    }

    public Optional<String> getTag() {
        return tag;
    }

    public Optional<Boolean> getIsDelivered() {
        return isDelivered;
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
        return startDate.equals(otherPredicate.startDate)
                && endDate.equals(otherPredicate.endDate)
                && clientName.equals(otherPredicate.clientName)
                && tag.equals(otherPredicate.tag)
                && isDelivered.equals(otherPredicate.isDelivered);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("startDate", startDate.orElse(""))
                .add("endDate", endDate.orElse(""))
                .add("clientName", clientName.orElse(""))
                .add("tag", tag.orElse(""))
                .add("isDelivered", isDelivered.map(Object::toString).orElse(""))
                .toString();
    }
}
