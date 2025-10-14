package seedu.foodbook.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.foodbook.testutil.TypicalDeliveries.ALICE_DELIVERY;

import org.junit.jupiter.api.Test;

import seedu.foodbook.commons.exceptions.IllegalValueException;
import seedu.foodbook.model.FoodBook;
import seedu.foodbook.model.delivery.DateTime;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.testutil.TypicalPersons;

public class JsonAdaptedDeliveryTest {

    private static final String INVALID_CLIENT_NAME = "R@chel";
    private static final String INVALID_DATE = "32/12/2026"; // Invalid day
    private static final String INVALID_TIME = "2500"; // Invalid hour

    // Use a fixed future date to ensure tests remain valid over time.
    private static final String VALID_FUTURE_DATE = "01/01/2027";
    private static final String VALID_FUTURE_TIME = "1200";
    private static final DateTime VALID_FUTURE_DATETIME = new DateTime(VALID_FUTURE_DATE, VALID_FUTURE_TIME);

    @Test
    public void toModelType_validDeliveryDetails_returnsDelivery() throws Exception {
        // Create a test delivery object with a future date
        Delivery testDelivery = new Delivery(ALICE_DELIVERY.getId(), ALICE_DELIVERY.getClient(),
                VALID_FUTURE_DATETIME, ALICE_DELIVERY.getRemarks(), ALICE_DELIVERY.getCost(), ALICE_DELIVERY.getTag());
        JsonAdaptedDelivery adaptedDelivery = new JsonAdaptedDelivery(testDelivery);

        FoodBook foodBook = new FoodBook();
        foodBook.addPerson(TypicalPersons.ALICE);
        assertEquals(testDelivery, adaptedDelivery.toModelType(foodBook));
    }

    @Test
    public void toModelType_isDeliveredTrue_returnsDeliveredDelivery() throws Exception {
        Delivery testDelivery = new Delivery(ALICE_DELIVERY.getId(), ALICE_DELIVERY.getClient(),
                VALID_FUTURE_DATETIME, ALICE_DELIVERY.getRemarks(), ALICE_DELIVERY.getCost(), ALICE_DELIVERY.getTag());
        testDelivery.markAsDelivered();
        JsonAdaptedDelivery adaptedDelivery = new JsonAdaptedDelivery(testDelivery);

        FoodBook foodBook = new FoodBook();
        foodBook.addPerson(TypicalPersons.ALICE);
        assertEquals(testDelivery, adaptedDelivery.toModelType(foodBook));
    }

    @Test
    public void toModelType_nullId_throwsIllegalValueException() {
        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(null, TypicalPersons.ALICE.getName().fullName,
                VALID_FUTURE_DATE, VALID_FUTURE_TIME, "Remarks", 10.0, false, "Tag");
        FoodBook foodBook = new FoodBook();
        foodBook.addPerson(TypicalPersons.ALICE);
        String expectedMessage = String.format(JsonAdaptedDelivery.MISSING_FIELD_MESSAGE_FORMAT, "id");
        assertThrowsWithMessage(adapted, foodBook, expectedMessage);
    }

    @Test
    public void toModelType_nullClientName_throwsIllegalValueException() {
        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(0, null,
                VALID_FUTURE_DATE, VALID_FUTURE_TIME, "Remarks", 10.0, false, "Tag");
        FoodBook foodBook = new FoodBook();
        String expectedMessage = String.format(JsonAdaptedDelivery.MISSING_FIELD_MESSAGE_FORMAT, "clientName");
        assertThrowsWithMessage(adapted, foodBook, expectedMessage);
    }

    @Test
    public void toModelType_nonExistentClient_throwsIllegalValueException() {
        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(0, INVALID_CLIENT_NAME,
                VALID_FUTURE_DATE, VALID_FUTURE_TIME, "Remarks", 10.0, false, "Tag");
        FoodBook foodBook = new FoodBook(); // Client does not exist in this foodbook
        String expectedMessage = "Client not found: " + INVALID_CLIENT_NAME;
        assertThrowsWithMessage(adapted, foodBook, expectedMessage);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(0, TypicalPersons.ALICE.getName().fullName,
                null, VALID_FUTURE_TIME, "Remarks", 10.0, false, "Tag");
        FoodBook foodBook = new FoodBook();
        foodBook.addPerson(TypicalPersons.ALICE);
        String expectedMessage = String.format(JsonAdaptedDelivery.MISSING_FIELD_MESSAGE_FORMAT, "date");
        assertThrowsWithMessage(adapted, foodBook, expectedMessage);
    }

    @Test
    public void toModelType_nullTime_throwsIllegalValueException() {
        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(0, TypicalPersons.ALICE.getName().fullName,
                VALID_FUTURE_DATE, null, "Remarks", 10.0, false, "Tag");
        FoodBook foodBook = new FoodBook();
        foodBook.addPerson(TypicalPersons.ALICE);
        String expectedMessage = String.format(JsonAdaptedDelivery.MISSING_FIELD_MESSAGE_FORMAT, "time");
        assertThrowsWithMessage(adapted, foodBook, expectedMessage);
    }

    @Test
    public void toModelType_invalidDateTimeFormat_throwsIllegalValueException() {
        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(0, TypicalPersons.ALICE.getName().fullName,
                INVALID_DATE, INVALID_TIME, "Remarks", 10.0, false, "Tag");
        FoodBook foodBook = new FoodBook();
        foodBook.addPerson(TypicalPersons.ALICE);
        String expectedMessage = DateTime.MESSAGE_CONSTRAINTS;
        assertThrowsWithMessage(adapted, foodBook, expectedMessage);
    }

    @Test
    public void toModelType_nullRemarks_throwsIllegalValueException() {
        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(0, TypicalPersons.ALICE.getName().fullName,
                VALID_FUTURE_DATE, VALID_FUTURE_TIME, null, 10.0, false, "Tag");
        FoodBook foodBook = new FoodBook();
        foodBook.addPerson(TypicalPersons.ALICE);
        String expectedMessage = String.format(JsonAdaptedDelivery.MISSING_FIELD_MESSAGE_FORMAT, "remarks");
        assertThrowsWithMessage(adapted, foodBook, expectedMessage);
    }

    @Test
    public void toModelType_nullCost_throwsIllegalValueException() {
        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(0, TypicalPersons.ALICE.getName().fullName,
                VALID_FUTURE_DATE, VALID_FUTURE_TIME, "Remarks", null, false, "Tag");
        FoodBook foodBook = new FoodBook();
        foodBook.addPerson(TypicalPersons.ALICE);
        String expectedMessage = String.format(JsonAdaptedDelivery.MISSING_FIELD_MESSAGE_FORMAT, "cost");
        assertThrowsWithMessage(adapted, foodBook, expectedMessage);
    }

    /**
     * Helper method to assert that the toModelType() method throws an IllegalValueException
     * with the correct message.
     */
    private void assertThrowsWithMessage(JsonAdaptedDelivery adaptedDelivery,
                                         FoodBook foodBook, String expectedMessage) {
        IllegalValueException exception = assertThrows(IllegalValueException.class, () ->
                adaptedDelivery.toModelType(foodBook));
        assertEquals(expectedMessage, exception.getMessage());
    }
}
