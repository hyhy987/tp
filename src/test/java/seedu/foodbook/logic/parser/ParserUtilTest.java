package seedu.foodbook.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.foodbook.testutil.Assert.assertThrows;
import static seedu.foodbook.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.foodbook.logic.parser.exceptions.ParseException;
import seedu.foodbook.model.person.Address;
import seedu.foodbook.model.person.Email;
import seedu.foodbook.model.person.Name;
import seedu.foodbook.model.person.Phone;
import seedu.foodbook.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }


    @Test
    public void parseCost_validInput_success() throws Exception {
        assertEquals(10.0, ParserUtil.parseCost("10"));
        assertEquals(10.5, ParserUtil.parseCost(" 10.5 "));
    }

    @Test
    public void parseCost_invalidInput_throwsException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseCost("-1"));
        assertThrows(ParseException.class, () -> ParserUtil.parseCost("ten"));
    }

    @Test
    public void parseCost_nullInput_throwsException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseCost("null"));
    }

    @Test
    public void parseCost_emptyInput_throwsException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseCost(""));
    }

    @Test
    public void parseCost_empty_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseCost(""));
    }

    @Test
    public void parseCost_whitespaceOnly_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseCost("   "));
    }

    @Test
    public void parseCost_nan_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseCost("NaN"));
    }

    @Test
    public void parseCost_infinite_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseCost("Infinity"));
        assertThrows(ParseException.class, () -> ParserUtil.parseCost("-Infinity"));
    }

    @Test
    public void parseCost_negative_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseCost("-0.01"));
    }

    @Test
    public void parseCost_intValue_returnsDouble() throws Exception {
        assertEquals(0.0, ParserUtil.parseCost("0"));
        assertEquals(42.0, ParserUtil.parseCost("42"));
    }

    @Test
    public void parseCost_decimalValue_returnsDouble() throws Exception {
        assertEquals(3.14, ParserUtil.parseCost("3.14"));
        assertEquals(2.5, ParserUtil.parseCost(" 2.50 "));
    }

    // parseRemarks tests
    @Test
    public void parseRemarks_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseRemarks(null));
    }

    @Test
    public void parseRemarks_empty_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseRemarks(""));
    }

    @Test
    public void parseRemarks_whitespace_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseRemarks("   "));
    }

    @Test
    public void parseRemarks_valid_returnsTrimmed() throws Exception {
        assertEquals("Note", ParserUtil.parseRemarks("Note"));
        assertEquals("Leading and trailing trimmed",
                ParserUtil.parseRemarks("  Leading and trailing trimmed  "));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parseDeliveryId_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseDeliveryId(null));
    }

    @Test
    public void parseDeliveryId_invalidValue_throwsParseException() {
        // empty string
        assertThrows(ParseException.class, () -> ParserUtil.parseDeliveryId(""));

        // whitespace only
        assertThrows(ParseException.class, () -> ParserUtil.parseDeliveryId("   "));

        // non-numeric
        assertThrows(ParseException.class, () -> ParserUtil.parseDeliveryId("abc"));

        // negative number
        assertThrows(ParseException.class, () -> ParserUtil.parseDeliveryId("-1"));

        // zero
        assertThrows(ParseException.class, () -> ParserUtil.parseDeliveryId("0"));

        // decimal number
        assertThrows(ParseException.class, () -> ParserUtil.parseDeliveryId("1.5"));
    }

    @Test
    public void parseDeliveryId_validValue_returnsDeliveryId() throws Exception {
        // valid positive integer
        assertEquals(Integer.valueOf(1), ParserUtil.parseDeliveryId("1"));

        // valid positive integer with whitespace
        assertEquals(Integer.valueOf(123), ParserUtil.parseDeliveryId(" 123 "));

        // large valid integer
        assertEquals(Integer.valueOf(999999), ParserUtil.parseDeliveryId("999999"));
    }

    // parseTagLoose
    @Test
    public void parseTagLoose_nullInput_returnsNull() {
        assertNull(ParserUtil.parseTagLoose(null));
    }

    @Test
    public void parseTagLoose_empty_returnsNull() {
        assertNull(ParserUtil.parseTagLoose(""));
    }

    @Test
    public void parseTagLoose_whitespace_returnsNull() {
        assertNull(ParserUtil.parseTagLoose("   "));
    }

    @Test
    public void parseTagLoose_validInput_returnsTrimmed() {
        assertEquals("Personal", ParserUtil.parseTagLoose("Personal"));
        assertEquals("Work", ParserUtil.parseTagLoose("  Work  "));
    }

}
