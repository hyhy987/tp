package seedu.foodbook.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.foodbook.commons.exceptions.IllegalValueException;
import seedu.foodbook.model.FoodBook;
import seedu.foodbook.model.delivery.DateTime;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Address;
import seedu.foodbook.model.person.Email;
import seedu.foodbook.model.person.Name;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.model.person.Phone;
import seedu.foodbook.model.tag.Tag;

public class JsonAdaptedDeliveryTest {

    private static final int VALID_ID = 1;
    private static final String VALID_NAME = "Alice Yeoh";
    private static final String VALID_DATE = "05/10/2025";
    private static final String VALID_TIME = "0930";
    private static final String VALID_REMARKS = "Breakfast order";
    private static final Double VALID_COST = 30.50;
    private static final String VALID_TAG = "Personal";
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
        return new Person(name, phone, email, address, emptyTags);
    }

    @Test
    public void toModelType_validDeliveryDetails_returnsDelivery() throws Exception {
        Person dummyClient = createDummyClient();
        DateTime dt = new DateTime(VALID_DATE, VALID_TIME);
        Delivery source = new Delivery(VALID_ID, dummyClient, dt, VALID_REMARKS, VALID_COST, VALID_TAG);

        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(source);

        // Use FoodBook for lookup
        FoodBook foodBook = new FoodBook();
        foodBook.addPerson(dummyClient);

        Delivery model = adapted.toModelType(foodBook);
        assertEquals(source.getId(), model.getId());
        assertEquals(source.getDeliveryDate(), model.getDeliveryDate());
        assertEquals(source.getRemarks(), model.getRemarks());
        assertEquals(source.getCost(), model.getCost());
        assertEquals(source.getStatus(), model.getStatus());
    }

    @Test
    public void toModelType_missingField_throwsIllegalValueException() throws Exception {
        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(
                null, VALID_NAME, VALID_DATE, VALID_TIME, VALID_REMARKS, VALID_COST, VALID_STATUS, VALID_TAG);

        FoodBook foodBook = new FoodBook();
        assertThrows(IllegalValueException.class, () -> adapted.toModelType(foodBook));
    }
}
