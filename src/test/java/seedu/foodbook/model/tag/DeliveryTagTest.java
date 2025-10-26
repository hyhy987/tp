package seedu.foodbook.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.model.delivery.TagKind.CORPORATE;
import static seedu.foodbook.model.delivery.TagKind.OTHER;
import static seedu.foodbook.model.delivery.TagKind.PERSONAL;

import org.junit.jupiter.api.Test;

class DeliveryTagTest {

    // --- constructor & validation ---

    @Test
    void constructor_null_throwsNpe() {
        assertThrows(NullPointerException.class, () -> new DeliveryTag(null));
    }

    @Test
    void constructor_invalid_throwsIllegalArgument() {
        // not alphanumeric (space, underscore, hyphen are all invalid per your regex)
        assertThrows(IllegalArgumentException.class, () -> new DeliveryTag(""));
        assertThrows(IllegalArgumentException.class, () -> new DeliveryTag(" "));
        assertThrows(IllegalArgumentException.class, () -> new DeliveryTag("bad tag"));
        assertThrows(IllegalArgumentException.class, () -> new DeliveryTag("alpha_1"));
        assertThrows(IllegalArgumentException.class, () -> new DeliveryTag("alpha-1"));
    }

    @Test
    void constructor_valid_okAndLowercased() {
        DeliveryTag t = new DeliveryTag("VIP123");
        assertEquals("vip123", t.getName()); // canonical lowercase
    }

    // --- static validator ---

    @Test
    void isValidTagName() {
        // invalid
        assertFalse(DeliveryTag.isValidTagName(null));
        assertFalse(DeliveryTag.isValidTagName("")); // empty after trim
        assertFalse(DeliveryTag.isValidTagName(" ")); // whitespace
        assertFalse(DeliveryTag.isValidTagName("bad tag")); // space present
        assertFalse(DeliveryTag.isValidTagName("alpha_1")); // underscore not allowed
        assertFalse(DeliveryTag.isValidTagName("alpha-1")); // hyphen not allowed

        // valid (alphanumeric only)
        assertTrue(DeliveryTag.isValidTagName("vip"));
        assertTrue(DeliveryTag.isValidTagName("VIP"));
        assertTrue(DeliveryTag.isValidTagName("alpha1"));
        assertTrue(DeliveryTag.isValidTagName("A1B2C3"));
    }

    // --- kind derivation ---

    @Test
    void getTagKind_type() {
        assertEquals(PERSONAL, new DeliveryTag("personal").getTagKind());
        assertEquals(CORPORATE, new DeliveryTag("corporate").getTagKind());
        assertEquals(OTHER, new DeliveryTag("vip").getTagKind()); // anything else -> OTHER
        assertEquals(OTHER, new DeliveryTag("x123").getTagKind());
    }

    // --- equality / hashCode / toString ---

    @Test
    void equality_hashcode_string() {
        DeliveryTag a = new DeliveryTag("VIP");
        DeliveryTag b = new DeliveryTag("vip");
        DeliveryTag c = new DeliveryTag("vip123");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        assertNotEquals(a, c);
        assertNotEquals(a.hashCode(), c.hashCode());

        assertEquals("vip", a.toString());
    }

    @Test
    void equals_sameInstance_true() {
        DeliveryTag a = new DeliveryTag("vip");
        assertEquals(a, a); // other == this
    }

    @Test
    void equals_nullAndDifferentType_false() {
        DeliveryTag a = new DeliveryTag("vip");
        assertNotEquals(a, null);
        assertNotEquals(a, "vip");
    }

    @Test
    void equals_sameCanonicalName_caseInsensitive() {
        DeliveryTag a = new DeliveryTag("VIP");
        DeliveryTag b = new DeliveryTag("vip");
        assertEquals(a, b);
        assertEquals(b, a);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equals_differentNames_false() {
        DeliveryTag a = new DeliveryTag("vip");
        DeliveryTag c = new DeliveryTag("vip1");
        assertNotEquals(a, c);
    }

    @Test
    void equals_transitive() {
        DeliveryTag a = new DeliveryTag("Personal");
        DeliveryTag b = new DeliveryTag("PERSONAL");
        DeliveryTag c = new DeliveryTag("personal");
        // transitivity
        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(a, c);
        // consistency
        for (int i = 0; i < 3; i++) {
            assertEquals(a, c);
        }
    }

    @Test
    void toString_hashCode_consistentWithEquals() {
        DeliveryTag x1 = new DeliveryTag("Corporate");
        DeliveryTag x2 = new DeliveryTag("corporate");
        assertEquals("corporate", x1.toString());
        assertEquals(x1, x2);
        assertEquals(x1.hashCode(), x2.hashCode());
    }
}
