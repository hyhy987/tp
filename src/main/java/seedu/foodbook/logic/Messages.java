package seedu.foodbook.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.foodbook.logic.parser.Prefix;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_NAME = "The name provided is not found";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DELIVERIES_LISTED_OVERVIEW = "%1$d deliveries found!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Formats a delivery into a readable string for display purposes.
     *
     * The formatted string includes the delivery ID, client name, date/time, and cost.
     * Format: "Delivery ID: X; Client: Y; Date/Time: Z; Cost: $W"
     *
     * @param delivery The delivery to format.
     * @return A formatted string representation of the delivery.
     * @throws NullPointerException If {@code delivery} is null.
     */
    public static String format(Delivery delivery) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Delivery ID: ")
                .append(delivery.getId())
                .append("; Client: ")
                .append(delivery.getClient().getName().fullName)
                .append("; Date/Time: ")
                .append(delivery.getDeliveryDate())
                .append("; Cost: $")
                .append(String.format("%.2f", delivery.getCost()));
        return builder.toString();
    }


}
