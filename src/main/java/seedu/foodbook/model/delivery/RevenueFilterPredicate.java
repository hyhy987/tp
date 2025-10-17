package seedu.foodbook.model.delivery;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.foodbook.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Delivery} matches the revenue filter criteria.
 * All criteria are optional and use AND logic (all provided criteria must match).
 */
public class RevenueFilterPredicate implements Predicate<Delivery> {
    private final Optional<LocalDate> startDate;
    private final Optional<LocalDate> endDate;
    private final Optional<String> clientName;
    private final Optional<Boolean> isDelivered;

    /**
     * Creates a RevenueFilterPredicate with the given filter criteria.
     * All parameters are optional.
     *
     * @param startDate Optional start date for filtering (inclusive)
     * @param endDate Optional end date for filtering (inclusive)
     * @param clientName Optional client name for filtering (case-insensitive substring match)
     * @param isDelivered Optional delivery status filter (true for delivered, false for not delivered)
     */
    public RevenueFilterPredicate(Optional<LocalDate> startDate, Optional<LocalDate> endDate,
                                  Optional<String> clientName, Optional<Boolean> isDelivered) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.clientName = clientName;
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
                delivery.getClient().getName().fullName.toLowerCase().contains(name.toLowerCase())
        ).orElse(true);

        boolean matchesStatus = isDelivered.map(status ->
                delivery.getStatus() == status
        ).orElse(true);

        // Return true if ALL provided criteria match (AND logic)
        return matchesStartDate && matchesEndDate && matchesClientName && matchesStatus;
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

    public Optional<Boolean> getIsDelivered() {
        return isDelivered;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RevenueFilterPredicate)) {
            return false;
        }

        RevenueFilterPredicate otherPredicate = (RevenueFilterPredicate) other;
        return startDate.equals(otherPredicate.startDate)
                && endDate.equals(otherPredicate.endDate)
                && clientName.equals(otherPredicate.clientName)
                && isDelivered.equals(otherPredicate.isDelivered);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("startDate", startDate.map(LocalDate::toString).orElse(""))
                .add("endDate", endDate.map(LocalDate::toString).orElse(""))
                .add("clientName", clientName.orElse(""))
                .add("isDelivered", isDelivered.map(Object::toString).orElse(""))
                .toString();
    }
}

