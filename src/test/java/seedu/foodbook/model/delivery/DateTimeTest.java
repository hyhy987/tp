package seedu.foodbook.model.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void hasCorrectFormat_acceptsValidShapes() {
        assertTrue(DateTime.hasCorrectFormat("1/1/2020", "0000"));
        assertTrue(DateTime.hasCorrectFormat("01/01/2020", "2359"));
        assertTrue(DateTime.hasCorrectFormat("9/12/1999", "0930")); // 1-2 digits day/month, 4 digits time
    }

    @Test
    void hasCorrectFormat_rejectsInvalidShapes() {
        assertFalse(DateTime.hasCorrectFormat("1-1-2020", "0900")); // wrong date separator
        assertFalse(DateTime.hasCorrectFormat("1/1/20", "0900")); // 2-digit year
        assertFalse(DateTime.hasCorrectFormat("1/1/2020", "930")); // 3-digit time
        assertFalse(DateTime.hasCorrectFormat("1/1/2020", "09:30")); // colon not allowed
        assertFalse(DateTime.hasCorrectFormat(" 1/1/2020", "0900")); // leading space
        assertFalse(DateTime.hasCorrectFormat("1/1/2020 ", "0900")); // trailing space
        assertFalse(DateTime.hasCorrectFormat("1/1/2020", "0900 ")); // trailing space
    }

    @Test
    void isValidDateTime_strictlyValid_true() {
        assertTrue(DateTime.isValidDateTime("29/2/2024", "0900")); // leap year
        assertTrue(DateTime.isValidDateTime("1/12/2023", "2359")); // upper bound minute ok
        assertTrue(DateTime.isValidDateTime("2/12/2019", "1800"));
    }

    @Test
    void isValidDateTime_impossibleDateOrTime_false() {
        assertFalse(DateTime.isValidDateTime("31/11/2021", "1200")); // Nov has 30 days
        assertFalse(DateTime.isValidDateTime("29/2/2021", "0900")); // not a leap year
        assertFalse(DateTime.isValidDateTime("1/1/2020", "2400")); // 24:00 not allowed
        assertFalse(DateTime.isValidDateTime("1/1/2020", "2360")); // invalid minute
        assertFalse(DateTime.isValidDateTime("1/1/2020", "2461")); // invalid hour & minute
    }

    @Test
    void isValidDate_strictLeapYearRules() {
        assertTrue(DateTime.isValidDate("29/2/2024")); // leap year
        assertFalse(DateTime.isValidDate("29/2/2023")); // not a leap year
        assertFalse(DateTime.isValidDate("31/11/2021")); // Nov 31st impossible
    }

    @Test
    void constructor_invalidFormat_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, ()
                -> new DateTime("1/1/20", "0900")); // 2-digit year
        assertEquals(DateTime.MESSAGE_CONSTRAINTS, ex.getMessage());
    }

    @Test
    void constructor_impossibleDate_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, ()
                -> new DateTime("29/2/2021", "0900")); // impossible date
        assertEquals(DateTime.MESSAGE_CONSTRAINTS, ex.getMessage());
    }

    @Test
    void getters_canonicalizeDateAndTime() {
        DateTime dt = new DateTime("01/12/2020", "0905");
        assertEquals("1/12/2020", dt.getDateString()); // no leading zero on day
        assertEquals("0905", dt.getTimeString()); // keeps leading zero for time
    }



}
