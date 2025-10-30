package seedu.foodbook.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.foodbook.commons.util.AppUtil.checkArgument;

import seedu.foodbook.model.delivery.TagKind;

/**
 * Immutable value object representing a single delivery tag.
 * <p>
 * A tag has a name that:
 * <ul>
 *   <li>must be alphanumeric ({@code \\p{Alnum}+}),</li>
 *   <li>is normalized to lowercase on construction, and</li>
 *   <li>derives a {@link TagKind} for basic categorization.</li>
 * </ul>
 */
public final class DeliveryTag {

    /** Error message shown when a tag name violates {@link #VALIDATION_REGEX}. */
    public static final String MESSAGE_CONSTRAINT = "Tag name should be a single alphanumeric word";

    /** Valid tag names: one or more alphanumeric characters; no spaces, underscores, or hyphens. */
    public static final String VALIDATION_REGEX = "\\p{Alnum}+";

    /** Canonical (lowercased) tag name. Never {@code null}. */
    public final String name;

    /**
     * Creates a {@code DeliveryTag} with the given name.
     * The name is validated against {@link #VALIDATION_REGEX} and stored in lowercase.
     *
     * @param name the tag name (must be non-null and alphanumeric)
     * @throws NullPointerException if {@code name} is null
     * @throws IllegalArgumentException if {@code name} is not alphanumeric
     */
    public DeliveryTag(String name) {
        requireNonNull(name);
        checkArgument(isValidTagName(name), MESSAGE_CONSTRAINT);
        this.name = name;
    }

    /**
     * Returns the canonical (lowercased) tag name.
     *
     * @return tag name, never {@code null}
     */
    public String getName() {
        return name;
    }

    /**
     * Returns {@code true} if the given string is a valid tag name.
     * A valid name is non-null, when trimmed matches {@link #VALIDATION_REGEX}.
     *
     * @param test candidate string to validate
     * @return {@code true} if valid; {@code false} otherwise
     */
    public static boolean isValidTagName(String test) {
        return test != null && test.trim().matches(VALIDATION_REGEX);
    }

    /**
     * Derives the {@link TagKind} from this tag's name.
     * <ul>
     *   <li>{@code "personal"} → {@link TagKind#PERSONAL}</li>
     *   <li>{@code "corporate"} → {@link TagKind#CORPORATE}</li>
     *   <li>anything else → {@link TagKind#OTHER}</li>
     * </ul>
     *
     * @return the derived {@link TagKind}, never {@code null}
     */
    public TagKind getTagKind() {
        switch (name.toLowerCase()) {
        case "personal":
            return TagKind.PERSONAL;
        case "corporate":
            return TagKind.CORPORATE;
        default:
            return TagKind.OTHER;
        }
    }

    /**
     * Returns the canonical name, suitable for display or logging.
     *
     * @return the tag name
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Two {@code DeliveryTag}s are equal if they have the same canonical name.
     *
     * @param other the other object
     * @return {@code true} if both are {@code DeliveryTag} with equal names; {@code false} otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeliveryTag)) {
            return false;
        }
        DeliveryTag o = (DeliveryTag) other;
        return this.name.equals(o.name);
    }

    /**
     * Hash code based solely on the canonical name.
     *
     * @return hash code of {@link #name}
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
