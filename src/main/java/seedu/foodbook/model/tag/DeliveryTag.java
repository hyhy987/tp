package seedu.foodbook.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.foodbook.commons.util.AppUtil.checkArgument;

import seedu.foodbook.model.delivery.TagKind;

public final class DeliveryTag {

    public static final String MESSAGE_CONSTRAINT = "Tag name should be alphanumeric";

    public static final String VALIDATION_REGEX = "\\p{Alnum}+";

    public final String name;

    public DeliveryTag(String name) {
        requireNonNull(name);
        checkArgument(isValidTagName(name), MESSAGE_CONSTRAINT);
        this.name = name.toLowerCase();
    }

    public String getName() {
        return name;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test != null && test.trim().matches(VALIDATION_REGEX);
    }

    /**
     * Derives TagKind from the tag name
     */
    public TagKind getTagKind() {
        String s;
        if (this.name == null) {
            s = "";
        } else {
            s = this.name.trim();
        }
        if (s.equals("personal")) {
            return TagKind.PERSONAL;
        }
        if (s.equals("corporate")) {
            return TagKind.CORPORATE;
        }
        return TagKind.OTHER;
    }

    @Override
    public String toString() {
        return name;
    }

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

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

