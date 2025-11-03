package seedu.foodbook.model.delivery;

import static java.util.Objects.requireNonNull;
import static seedu.foodbook.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Pattern;

/**
 * The DateTime class represents a date and time in FoodBook's deliveries.
 * It is a wrapper around the LocalDateTime class.
 */
public class DateTime {

    public static final String MESSAGE_CONSTRAINTS =
            "Date should be in d/M/yyyy format (e.g. 21/10/2003) and time in HHmm format (e.g. 1430, 0800)";

    public static final String MESSAGE_IMPOSSIBLE =
            "That date/time does not exist.";


    private static final String DATE_PATTERN = "d/M/uuuu";
    private static final String TIME_PATTERN = "HHmm";
    private static final String FULL_DATETIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    private static final DateTimeFormatter FULL_FMT =
            DateTimeFormatter.ofPattern(FULL_DATETIME_PATTERN)
                    .withResolverStyle(ResolverStyle.STRICT);


    private static final Pattern DATE_SHAPE = Pattern.compile("\\d{1,2}/\\d{1,2}/\\d{4}");
    private static final Pattern TIME_SHAPE = Pattern.compile("\\d{4}");

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
        requireNonNull(dateString);
        requireNonNull(timeString);

        checkArgument(isValidDateTime(dateString, timeString), MESSAGE_CONSTRAINTS);

        this.dateTime = LocalDateTime.parse(dateString + " " + timeString, FULL_FMT);

        assert this.dateTime != null;
    }

    /**
     * Returns true if the given date and time string are valid.
     * @param dateString The string representing the date
     * @param timeString The string representing the time
     */
    public static boolean isValidDateTime(String dateString, String timeString) {
        try {
            LocalDateTime.parse(dateString + " " + timeString, FULL_FMT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Returns true if the given string is a valid date in d/M/yyyy format.
     * @param dateString The string representing the date
     */
    public static boolean isValidDate(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDate.parse(dateString, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Returns true if the date and time match the expected regex
     * Does not check whether it is actually a valid datetime
     * @param dateString The string representing the date
     * @param timeString The string representing the time
     */
    public static boolean hasCorrectFormat(String dateString, String timeString) {
        return DATE_SHAPE.matcher(dateString).matches() && TIME_SHAPE.matcher(timeString).matches();
    }

    /**
     * Returns the delivery date as a string in foodboook format (d/MM/uuuu).
     * Example: "8/10/2025"
     *
     * @return String representation of the date
     */
    public String getDateString() {
        return dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("d/M/uuuu"));
    }

    /**
     * Returns the delivery time as a string in foodbook format (HHmm).
     * Example: "1036"
     *
     * @return String representation of the time
     */
    public String getTimeString() {
        return dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HHmm"));
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

    @Override
    public int hashCode() {
        return dateTime.hashCode();
    }

}
