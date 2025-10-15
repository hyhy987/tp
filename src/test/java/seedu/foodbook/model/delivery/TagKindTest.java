package seedu.foodbook.model.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class TagKindTest {

    @Test
    public void values_containsAllKinds() {
        TagKind[] kinds = TagKind.values();
        // Should contain exactly PERSONAL, CORPORATE, OTHER
        assertEquals(3, kinds.length);
        assertEquals(TagKind.PERSONAL, kinds[0]);
        assertEquals(TagKind.CORPORATE, kinds[1]);
        assertEquals(TagKind.OTHER, kinds[2]);
    }

    @Test
    public void valueOf_validName_returnsKind() {
        assertEquals(TagKind.PERSONAL, TagKind.valueOf("PERSONAL"));
        assertEquals(TagKind.CORPORATE, TagKind.valueOf("CORPORATE"));
        assertEquals(TagKind.OTHER, TagKind.valueOf("OTHER"));
    }

    @Test
    public void valueOf_invalidName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> TagKind.valueOf("INVALID"));
        assertThrows(IllegalArgumentException.class, () -> TagKind.valueOf(""));
    }

    @Test
    public void name_matchesEnumName() {
        assertEquals("PERSONAL", TagKind.PERSONAL.name());
        assertEquals("CORPORATE", TagKind.CORPORATE.name());
        assertEquals("OTHER", TagKind.OTHER.name());
    }

    @Test
    public void toString_returnsName() {
        // default toString() is name()
        assertEquals("PERSONAL", TagKind.PERSONAL.toString());
        assertEquals("CORPORATE", TagKind.CORPORATE.toString());
        assertEquals("OTHER", TagKind.OTHER.toString());
    }
}
