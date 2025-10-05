package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.delivery.DateTime;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

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
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
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
        if (!DateTime.isValidDateTime(trimmedDate, trimmedTime)) {
            throw new ParseException(DateTime.MESSAGE_CONSTRAINTS);
        }
        return new DateTime(trimmedDate, trimmedTime);
    }

    /**
     * Parses a {@code String cost} into a {@code Double}.
     * Leading and trailing whitespaces will be trimmed.
     * The cost must be a non-negative number.
     *
     * @param cost The cost string to parse (e.g., "50.00", "150").
     * @return A Double representing the parsed cost.
     * @throws ParseException If the given cost is not a valid non-negative number.
     * @throws NullPointerException If {@code cost} is null.
     */
    public static Double parseCost(String cost) throws ParseException {
        requireNonNull(cost);
        String trimmedCost = cost.trim();
        try {
            Double parsedCost = Double.parseDouble(trimmedCost);
            if (parsedCost < 0) {
                throw new ParseException("Cost must be non-negative");
            }
            return parsedCost;
        } catch (NumberFormatException e) {
            throw new ParseException("Cost must be a valid number");
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
}
