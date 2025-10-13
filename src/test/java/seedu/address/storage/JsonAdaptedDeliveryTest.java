package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.delivery.DateTime;
import seedu.address.model.delivery.Delivery;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

public class JsonAdaptedDeliveryTest {

    private static final int VALID_ID = 1;
    private static final String VALID_NAME = "Alice Yeoh";
    private static final String VALID_DATE = "05/10/2025";
    private static final String VALID_TIME = "0930";
    private static final String VALID_REMARKS = "Breakfast order";
    private static final Double VALID_COST = 30.50;
    private static final Boolean VALID_STATUS = false;

    /**
     * Helper to create a dummy Person matching VALID_NAME.
     */
    private Person createDummyClient() {
        Name name = new Name(VALID_NAME);
        Phone phone = new Phone("91234567");
        Email email = new Email("alice@example.com");
        Address address = new Address("123 Orchard Road");
        Set<Tag> emptyTags = new HashSet<>(Collections.emptySet());
        return new Person(name, phone, email, address);
    }

    @Test
    public void toModelType_validDeliveryDetails_returnsDelivery() throws Exception {
        Person dummyClient = createDummyClient();
        DateTime dt = new DateTime(VALID_DATE, VALID_TIME);
        Delivery source = new Delivery(VALID_ID, dummyClient, dt, VALID_REMARKS, VALID_COST);

        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(source);

        // Use AddressBook for lookup
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(dummyClient);

        Delivery model = adapted.toModelType(addressBook);
        assertEquals(source.getId(), model.getId());
        assertEquals(source.getDeliveryDate(), model.getDeliveryDate());
        assertEquals(source.getRemarks(), model.getRemarks());
        assertEquals(source.getCost(), model.getCost());
        assertEquals(source.getStatus(), model.getStatus());
    }

    @Test
    public void toModelType_missingField_throwsIllegalValueException() throws Exception {
        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(
                null, VALID_NAME, VALID_DATE, VALID_TIME, VALID_REMARKS, VALID_COST, VALID_STATUS);

        AddressBook addressBook = new AddressBook();
        assertThrows(IllegalValueException.class, () -> adapted.toModelType(addressBook));
    }
}
