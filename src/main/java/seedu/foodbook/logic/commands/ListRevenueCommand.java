package seedu.foodbook.logic.commands;

import static java.util.Objects.requireNonNull;

import javafx.collections.ObservableList;
import seedu.foodbook.commons.util.ToStringBuilder;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.delivery.RevenueFilterPredicate;

/**
 * Calculates and displays revenue from deliveries with optional filtering.
 */
public class ListRevenueCommand extends Command {

    public static final String COMMAND_WORD = "list_revenue";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Calculates and displays revenue from deliveries with optional filters.\n"
            + "Parameters (all optional): "
            + "[sd/START_DATE] "
            + "[ed/END_DATE] "
            + "[n/CLIENT_NAME] "
            + "[s/STATUS]\n"
            + "STATUS can be 'delivered' or 'not_delivered'. Omit to include all deliveries.\n"
            + "Date format: d/M/yyyy (e.g., 25/12/2024)\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " (shows revenue from all deliveries)\n"
            + "  " + COMMAND_WORD + " sd/1/1/2024 ed/31/12/2024 (shows revenue for date range)\n"
            + "  " + COMMAND_WORD + " s/delivered (shows revenue from completed deliveries only)\n"
            + "  " + COMMAND_WORD + " n/John (shows revenue from deliveries for clients named John)\n"
            + "  " + COMMAND_WORD + " sd/1/1/2024 s/delivered (shows revenue from completed deliveries in 2024)";

    public static final String MESSAGE_SUCCESS = "Total Revenue: $%.2f\n"
            + "Number of deliveries: %d\n"
            + "%s\n\n"
            + "Usage: " + COMMAND_WORD + " [sd/START_DATE] [ed/END_DATE] [n/CLIENT_NAME] [s/STATUS]\n"
            + "Date format: d/M/yyyy | Status: delivered or not_delivered";

    private final RevenueFilterPredicate predicate;

    /**
     * Creates a ListRevenueCommand with the specified filter predicate.
     *
     * @param predicate The predicate to filter deliveries
     */
    public ListRevenueCommand(RevenueFilterPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        // Apply the filter to get matching deliveries
        model.updateFilteredDeliveryList(predicate);
        ObservableList<Delivery> filteredDeliveries = model.getFilteredDeliveryList();

        // Calculate total revenue
        double totalRevenue = 0.0;
        for (Delivery delivery : filteredDeliveries) {
            totalRevenue += delivery.getCost();
        }

        // Build filter description
        String filterDescription = buildFilterDescription();

        String message = String.format(MESSAGE_SUCCESS, totalRevenue,
                filteredDeliveries.size(), filterDescription);

        return new CommandResult(message, CommandResult.UiPanel.DELIVERIES);
    }

    /**
     * Builds a human-readable description of the applied filters.
     */
    private String buildFilterDescription() {
        StringBuilder sb = new StringBuilder("Showing: ");

        // Check delivery status filter
        if (predicate.getIsDelivered().isPresent()) {
            if (predicate.getIsDelivered().get()) {
                sb.append("Delivered orders");
            } else {
                sb.append("Pending orders");
            }
        } else {
            sb.append("All deliveries");
        }

        // Check date range filters
        if (predicate.getStartDate().isPresent() || predicate.getEndDate().isPresent()) {
            sb.append(" from ");

            if (predicate.getStartDate().isPresent() && predicate.getEndDate().isPresent()) {
                sb.append(formatDate(predicate.getStartDate().get()))
                        .append(" to ")
                        .append(formatDate(predicate.getEndDate().get()));
            } else if (predicate.getStartDate().isPresent()) {
                sb.append(formatDate(predicate.getStartDate().get())).append(" onwards");
            } else {
                sb.append("beginning to ").append(formatDate(predicate.getEndDate().get()));
            }
        }

        // Check client name filter
        if (predicate.getClientName().isPresent()) {
            sb.append(" for client \"").append(predicate.getClientName().get()).append("\"");
        }

        return sb.toString();
    }

    /**
     * Formats a LocalDate to a readable string format.
     */
    private String formatDate(java.time.LocalDate date) {
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("d MMM yyyy");
        return date.format(formatter);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ListRevenueCommand)) {
            return false;
        }

        ListRevenueCommand otherCommand = (ListRevenueCommand) other;
        return predicate.equals(otherCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}

