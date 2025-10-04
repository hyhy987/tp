package seedu.address.model.delivery;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

// TODO: ADD PROPER VALIDATION as per person!

/**
 * The DateTime class represents a date and time in FOodBook's deliveries.
 * It is a wrapper around the LocalDateTime class.
 */
public class DateTime {

    /**
     * The LocalDateTime containing this object's dateTime
     */
    private final LocalDateTime dateTime;

    /**
     * Constructor for DateTime object
     *
     * @param dateString The dateString in d/M/yyyy format to be stored (e.g 21/10/2003)
     * @param timeString The timeString in HHmm format to be stored (e.g 2359, 0000)
     * @throws DateTimeParseException If input is unable to be parsed into a
     *     LocalDateTime object
     */
    public DateTime(String dateString, String timeString) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/uuuu HHmm")
            .withResolverStyle(ResolverStyle.STRICT);
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateString + " " + timeString, formatter);

        this.dateTime = parsedDateTime;

        assert this.dateTime != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM uuuu HHmm'hrs'");
        return this.dateTime.format(formatter);
    }

    /**
     * Returns True if the current DateTime is before the other one
     */
    public boolean isBefore(DateTime otherDateTime) {
        return this.dateTime.isBefore(otherDateTime.dateTime);
    }

    /**
     * Returns True if the current DateTime is after the other one
     */
    public boolean isAfter(DateTime otherDateTime) {
        return this.dateTime.isAfter(otherDateTime.dateTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DateTime)) {
            return false;
        }
        DateTime otherDateTime = (DateTime) other;
        return this.dateTime.equals(otherDateTime.dateTime);
    }
}
