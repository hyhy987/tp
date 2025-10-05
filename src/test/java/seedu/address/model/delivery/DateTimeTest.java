package seedu.address.model.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DateTimeTest {

    @Test
    void toString_validInput_formatsCorrectly() {
        DateTime dt = new DateTime("2/12/2019", "1800");
        assertEquals("2 December 2019 1800hrs", dt.toString());
    }

    @Test
    void toString_leadingZeros_areCanonicalized() {
        DateTime dt = new DateTime("01/01/2020", "0000");
        assertEquals("1 January 2020 0000hrs", dt.toString());
    }

    @Test
    void isBefore_returnsTrueWhenEarlier() {
        DateTime earlier = new DateTime("1/1/2020", "1000");
        DateTime later = new DateTime("1/1/2020", "1200");
        assertTrue(earlier.isBefore(later));
        assertFalse(later.isBefore(earlier));
    }

    @Test
    void isAfter_returnsTrueWhenLater() {
        DateTime earlier = new DateTime("1/1/2020", "1000");
        DateTime later = new DateTime("1/1/2020", "1200");
        assertTrue(later.isAfter(earlier));
        assertFalse(earlier.isAfter(later));
    }

    @Test
    void equals_sameDateTime_returnsTrue() {
        DateTime dt1 = new DateTime("2/2/2021", "1500");
        DateTime dt2 = new DateTime("2/2/2021", "1500");
        assertEquals(dt1, dt2);
    }

    @Test
    void equals_differentDateTime_returnsFalse() {
        DateTime dt1 = new DateTime("2/2/2021", "1500");
        DateTime dt2 = new DateTime("3/2/2021", "1500");
        assertNotEquals(dt1, dt2);
    }

    @Test
    void equals_withNonDateTime_returnsFalse() {
        DateTime dt = new DateTime("2/2/2021", "1500");
        assertNotEquals(dt, "not a datetime");
    }

    @Test
    void leapYearDate_validInput_parsesCorrectly() {
        DateTime leap = new DateTime("29/2/2020", "2359");
        assertEquals("29 February 2020 2359hrs", leap.toString());
    }

    @Test
    void midnight_validInput_formatsCorrectly() {
        DateTime midnight = new DateTime("5/5/2022", "0000");
        assertEquals("5 May 2022 0000hrs", midnight.toString());
    }
}
