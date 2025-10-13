package seedu.foodbook.logic.parser;

import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.foodbook.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.foodbook.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.foodbook.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.foodbook.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.foodbook.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.foodbook.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.foodbook.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.foodbook.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.foodbook.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.foodbook.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.foodbook.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.foodbook.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.foodbook.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.foodbook.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.foodbook.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.foodbook.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.foodbook.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.foodbook.commons.core.index.Index;
import seedu.foodbook.logic.Messages;
import seedu.foodbook.logic.commands.EditClientCommand;
import seedu.foodbook.logic.commands.EditClientCommand.EditPersonDescriptor;
import seedu.foodbook.model.person.Address;
import seedu.foodbook.model.person.Email;
import seedu.foodbook.model.person.Name;
import seedu.foodbook.model.person.Phone;
import seedu.foodbook.model.tag.Tag;
import seedu.foodbook.testutil.EditPersonDescriptorBuilder;

public class EditClientCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditClientCommand.MESSAGE_USAGE);

    private EditClientCommandParser parser = new EditClientCommandParser();

    @Test
    public void parse_missingParts_failure() {
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "   ", MESSAGE_INVALID_FORMAT);

        // name present but no fields
        assertParseFailure(parser, VALID_NAME_AMY, EditClientCommand.MESSAGE_NOT_EDITED);

        // numeric token used as a "name" but still no fields -> still NOT_EDITED
        assertParseFailure(parser, "1", EditClientCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // prefixes without a leading OLD_NAME -> invalid format
        assertParseFailure(parser, NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, PHONE_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, NAME_DESC_AMY + PHONE_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // extra non-prefixed tokens after the name: parser treats as "no valid fields"
        assertParseFailure(parser, VALID_NAME_AMY + " some random string",
                EditClientCommand.MESSAGE_NOT_EDITED);

        // unknown prefix after the name: also ends up as "no valid fields"
        assertParseFailure(parser, VALID_NAME_AMY + " i/ string",
                EditClientCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS); // invalid email
        assertParseFailure(parser, "1" + INVALID_ADDRESS_DESC, Address.MESSAGE_CONSTRAINTS); // invalid address
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS); // invalid tag

        // invalid phone followed by valid email
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC + EMAIL_DESC_AMY, Phone.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Person} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + TAG_EMPTY, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_FRIEND + TAG_EMPTY + TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_FRIEND + TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + INVALID_EMAIL_DESC + VALID_ADDRESS_AMY + VALID_PHONE_AMY,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        String userInput = VALID_NAME_BOB + PHONE_DESC_BOB + TAG_DESC_HUSBAND
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + NAME_DESC_AMY + TAG_DESC_FRIEND;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
        EditClientCommand expectedCommand = new EditClientCommand(VALID_NAME_BOB, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        String userInput = VALID_NAME_AMY + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        EditClientCommand expectedCommand = new EditClientCommand(VALID_NAME_AMY, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        String userInput = VALID_NAME_BOB + NAME_DESC_AMY;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditClientCommand expectedCommand = new EditClientCommand(VALID_NAME_BOB, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = VALID_NAME_BOB + PHONE_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditClientCommand(VALID_NAME_BOB, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = VALID_NAME_BOB + EMAIL_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditClientCommand(VALID_NAME_BOB, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = VALID_NAME_BOB + ADDRESS_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new EditClientCommand(VALID_NAME_BOB, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = VALID_NAME_BOB + TAG_DESC_FRIEND;
        descriptor = new EditPersonDescriptorBuilder().withTags(VALID_TAG_FRIEND).build();
        expectedCommand = new EditClientCommand(VALID_NAME_BOB, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonTagValue_failure()

        // valid followed by invalid
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + INVALID_PHONE_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // mulltiple valid fields repeated
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY
                + TAG_DESC_FRIEND + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY + TAG_DESC_FRIEND
                + PHONE_DESC_BOB + ADDRESS_DESC_BOB + EMAIL_DESC_BOB + TAG_DESC_HUSBAND;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS));

        // multiple invalid values
        userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC
                + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS));
    }

    @Test
    public void parse_resetTags_success() {
        String userInput = VALID_NAME_BOB + TAG_EMPTY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTags().build();
        EditClientCommand expectedCommand = new EditClientCommand(VALID_NAME_BOB, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
