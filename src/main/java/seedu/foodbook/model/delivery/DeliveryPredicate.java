package seedu.foodbook.model.delivery;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
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

    private final Optional<LocalDate> startDate;
    private final Optional<LocalDate> endDate;
    private final Optional<String> clientName;
    private final Optional<String> tag;
    private final Optional<Boolean> isDelivered;

    /**
     * Constructs a DeliveryPredicate with the specified filters.
     *
     * @param startDate Optional start date for filtering (inclusive)
     * @param endDate Optional end date for filtering (inclusive)
     * @param clientName Optional client name to filter by (case-insensitive partial match)
     * @param tag Optional tag to filter by (case-insensitive partial match)
     * @param isDelivered Optional delivery status filter (true for delivered, false for not delivered)
     */
    public DeliveryPredicate(Optional<LocalDate> startDate,
                             Optional<LocalDate> endDate,
                             Optional<String> clientName,
                             Optional<String> tag,
                             Optional<Boolean> isDelivered) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.clientName = clientName;
        this.tag = tag;
        this.isDelivered = isDelivered;
    }

    @Override
    public boolean test(Delivery delivery) {
        boolean matchesStartDate = startDate.map(start -> {
            LocalDate deliveryDate = parseDate(delivery.getDeliveryDate().getDateString());
            return !deliveryDate.isBefore(start);
        }).orElse(true);

        boolean matchesEndDate = endDate.map(end -> {
            LocalDate deliveryDate = parseDate(delivery.getDeliveryDate().getDateString());
            return !deliveryDate.isAfter(end);
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

    /**
     * Parses a date string in d/M/yyyy format into a LocalDate.
     */
    private LocalDate parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);
        return LocalDate.parse(dateString, formatter);
    }

    public Optional<LocalDate> getStartDate() {
        return startDate;
    }

    public Optional<LocalDate> getEndDate() {
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
                .add("startDate", startDate.map(LocalDate::toString).orElse(""))
                .add("endDate", endDate.map(LocalDate::toString).orElse(""))
                .add("clientName", clientName.orElse(""))
                .add("tag", tag.orElse(""))
                .add("isDelivered", isDelivered.map(Object::toString).orElse(""))
                .toString();
    }
}
