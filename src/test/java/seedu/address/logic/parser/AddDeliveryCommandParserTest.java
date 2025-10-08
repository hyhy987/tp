package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARKS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;

public class AddDeliveryCommandParserTest {

    private final AddDeliveryCommandParser parser = new AddDeliveryCommandParser();

    @Test
    public void parse_missingField_throwsParseException() {
        String missingDate = PREFIX_NAME + "Alice Yeoh "
                + PREFIX_TIME + "0930 "
                + PREFIX_REMARKS + "Breakfast "
                + PREFIX_COST + "30.50";
        assertThrows(ParseException.class, () -> parser.parse(missingDate));

        String missingName = PREFIX_DATE + "15/10/2025 "
                + PREFIX_TIME + "0930 "
                + PREFIX_REMARKS + "Breakfast "
                + PREFIX_COST + "30.50";
        assertThrows(ParseException.class, () -> parser.parse(missingName));
    }

    @Test
    public void parse_invalidDateTime_throwsParseException() {
        String badDate = PREFIX_NAME + "Alice Yeoh "
                + PREFIX_DATE + "bad "
                + PREFIX_TIME + "0930 "
                + PREFIX_REMARKS + "Breakfast "
                + PREFIX_COST + "30.50";
        assertThrows(ParseException.class, () -> parser.parse(badDate));

        String badTime = PREFIX_NAME + "Alice Yeoh "
                + PREFIX_DATE + "15/10/2025 "
                + PREFIX_TIME + "bad "
                + PREFIX_REMARKS + "Breakfast "
                + PREFIX_COST + "30.50";
        assertThrows(ParseException.class, () -> parser.parse(badTime));
    }

    @Test
    public void parse_invalidCost_throwsParseException() {
        String badCost = PREFIX_NAME + "Alice Yeoh "
                + PREFIX_DATE + "15/10/2025 "
                + PREFIX_TIME + "0930 "
                + PREFIX_REMARKS + "Breakfast "
                + PREFIX_COST + "-5.00";
        assertThrows(ParseException.class, () -> parser.parse(badCost));
    }
}
