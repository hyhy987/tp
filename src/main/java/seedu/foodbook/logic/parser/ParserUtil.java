package seedu.foodbook.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.foodbook.model.tag.Tag.MAX_TAGS;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import seedu.foodbook.commons.core.index.Index;
import seedu.foodbook.commons.util.StringUtil;
import seedu.foodbook.logic.parser.exceptions.ParseException;
import seedu.foodbook.model.delivery.DateTime;
import seedu.foodbook.model.person.Address;
import seedu.foodbook.model.person.Email;
import seedu.foodbook.model.person.Name;
import seedu.foodbook.model.person.Phone;
import seedu.foodbook.model.tag.DeliveryTag;
import seedu.foodbook.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INVALID_COST =
            "Cost must be a non-negative number, with up to 2 decimal places (e.g., 0, 3, 12.50).";

    private static final Pattern COST_PATTERN =
            Pattern.compile("^\\d+(?:\\.\\d{1,2})?$"); // e.g. 0, 12, 12.3, 12.34

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     *
     * @throws ParseException if more than 3 tags are provided
     *     (A client can only have a maximum of 3 tags)
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        if (tagSet.size() > MAX_TAGS) {
            throw new ParseException(Tag.MESSAGE_TOO_MANY_TAGS);
        }
        return tagSet;
    }

    /**
     * Returns a trimmed tag string or {@code null} when input is null/blank.
     * <p>No validation is performed; callers may impose their own policies.</p>
     *
     * @param raw tag text as entered by the user.
     * @return trimmed tag, or {@code null} if empty.
     */
    public static String parseTagLoose(String raw) {
        if (raw == null) {
            return null;
        }
        String s = raw.trim();
        if (s.isEmpty()) {
            return null;
        } else {
            return s;
        }
    }

    /**
     * Parses date and time strings into a {@code DateTime}.
     * Leading and trailing whitespaces will be trimmed from both inputs.
     *
     * @param date The date string in d/M/yyyy format (e.g., "25/12/2024").
     * @param time The time string in HHmm format (e.g., "1430").
     * @return A DateTime object representing the parsed date and time.
     * @throws ParseException If the given date or time cannot be parsed into a valid DateTime.
     * @throws NullPointerException If either {@code date} or {@code time} is null.
     */
    public static DateTime parseDateTime(String date, String time) throws ParseException {
        requireNonNull(date);
        requireNonNull(time);
        String trimmedDate = date.trim();
        String trimmedTime = time.trim();

        // If wrong format is provided
        if (!DateTime.hasCorrectFormat(trimmedDate, trimmedTime)) {
            throw new ParseException(DateTime.MESSAGE_CONSTRAINTS);
        }

        // Impossible date/time
        if (!DateTime.isValidDateTime(trimmedDate, trimmedTime)) {
            throw new ParseException(DateTime.MESSAGE_IMPOSSIBLE);
        }
        return new DateTime(trimmedDate, trimmedTime);
    }

    /**
     * Parses a {@code String cost} into a {@code Double}.
     * Leading and trailing whitespaces will be trimmed.
     * The cost must be a non-negative number.
     *
     * @param raw The cost string to parse (e.g., "50.00", "150").
     * @return A Double representing the parsed cost.
     * @throws ParseException If the given cost is not a valid non-negative number.
     * @throws NullPointerException If {@code cost} is null.
     */
    public static Double parseCost(String raw) throws ParseException {
        if (raw == null) {
            throw new ParseException(MESSAGE_INVALID_COST);
        }
        String s = raw.trim();
        if (s.isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_COST);
        }

        // Reject 1e3, 20.00f, 1.222, 10., etc.
        if (!COST_PATTERN.matcher(s).matches()) {
            throw new ParseException(MESSAGE_INVALID_COST);
        }

        // Safe numeric parse (avoids floating quirks while parsing)
        try {
            BigDecimal bd = new BigDecimal(s);
            if (bd.compareTo(BigDecimal.ZERO) < 0) {
                throw new ParseException(MESSAGE_INVALID_COST);
            }
            return bd.doubleValue();
        } catch (NumberFormatException ex) {
            throw new ParseException(MESSAGE_INVALID_COST);
        }
    }

    /**
     * Parses a {@code String remarks} into a trimmed {@code String}.
     * Leading and trailing whitespaces will be trimmed.
     * The remarks cannot be empty after trimming.
     *
     * @param remarks The remarks string to parse.
     * @return A trimmed String representing the remarks.
     * @throws ParseException If the remarks string is empty after trimming.
     * @throws NullPointerException If {@code remarks} is null.
     */
    public static String parseRemarks(String remarks) throws ParseException {
        requireNonNull(remarks);
        String trimmedRemarks = remarks.trim();
        if (trimmedRemarks.isEmpty()) {
            throw new ParseException("Remarks cannot be empty");
        }
        return trimmedRemarks;
    }

    /**
     * Parses a {@code String deliveryId} into an {@code Integer}.
     * Leading and trailing whitespaces will be trimmed.
     * The delivery ID must be a positive integer.
     *
     * @param deliveryId The delivery ID string to parse.
     * @return An Integer representing the parsed delivery ID.
     * @throws ParseException If the given delivery ID is not a valid positive integer.
     * @throws NullPointerException If {@code deliveryId} is null.
     */
    public static Integer parseDeliveryId(String deliveryId) throws ParseException {
        requireNonNull(deliveryId);
        String trimmedId = deliveryId.trim();
        try {
            Integer parsedId = Integer.parseInt(trimmedId);
            if (parsedId <= 0) {
                throw new ParseException("Delivery ID must be a positive integer");
            }
            return parsedId;
        } catch (NumberFormatException e) {
            throw new ParseException("Delivery ID must be a valid positive integer");
        }
    }

    /**
     * Parses zero or one delivery tag from the given values.
     *
     * <p>Returns {@code Optional.empty()} if no value is supplied.
     * Throws {@code ParseException} if more than one value is given,
     * or if the single value is invalid per {@link DeliveryTag#MESSAGE_CONSTRAINT}.
     *
     * @param values raw tag values (e.g., from PREFIX_TAG); may be null or empty
     * @return an {@code Optional} of {@link DeliveryTag}, or {@code Optional.empty()}
     * @throws ParseException if multiple values are provided or the value is invalid
     */
    public static Optional<DeliveryTag> parseOptionalDeliveryTag(Collection<String> values) throws ParseException {
        if (values == null || values.isEmpty()) {
            return Optional.empty();
        }
        if (values.size() > 1) {
            throw new ParseException("Only one tag is allowed. Remove extra t/ prefixes.");
        }
        String raw = values.iterator().next();
        if (!DeliveryTag.isValidTagName(raw)) {
            throw new ParseException(DeliveryTag.MESSAGE_CONSTRAINT);
        }
        return Optional.of(new DeliveryTag(raw));
    }

}
