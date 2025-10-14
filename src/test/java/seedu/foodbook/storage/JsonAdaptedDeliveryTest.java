package seedu.foodbook.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.foodbook.testutil.TypicalDeliveries.ALICE_DELIVERY;
import static seedu.foodbook.testutil.TypicalDeliveries.BENSON_DELIVERY;
import static seedu.foodbook.testutil.TypicalDeliveries.CARL_DELIVERY;

import org.junit.jupiter.api.Test;

import seedu.foodbook.commons.exceptions.IllegalValueException;
import seedu.foodbook.model.FoodBook;
import seedu.foodbook.model.delivery.DateTime;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.testutil.TypicalPersons;

public class JsonAdaptedDeliveryTest {

    private static final String INVALID_CLIENT_NAME = "R@chel";
    private static final String INVALID_DATE = "32/12/2025";

    // Define a valid future date to use throughout the tests
    private static final String VALID_FUTURE_DATE = "01/01/2026";
    private static final String VALID_FUTURE_TIME = "1200";
    private static final DateTime VALID_FUTURE_DATETIME = new DateTime(VALID_FUTURE_DATE, VALID_FUTURE_TIME);


    @Test
    public void toModelType_validDeliveryDetails_returnsDelivery() throws Exception {
        // Create a new delivery object with a future date based on ALICE_DELIVERY
        Delivery aliceDeliveryFuture = new Delivery(ALICE_DELIVERY.getId(), ALICE_DELIVERY.getClient(),
                VALID_FUTURE_DATETIME, ALICE_DELIVERY.getRemarks(), ALICE_DELIVERY.getCost(), ALICE_DELIVERY.getTag());
        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(aliceDeliveryFuture);

        FoodBook foodBook = new FoodBook();
        foodBook.addPerson(TypicalPersons.ALICE);
        assertEquals(aliceDeliveryFuture, adapted.toModelType(foodBook));
    }

    @Test
    public void toModelType_validDeliveredStatus_returnsDelivery() throws Exception {
        // Create a new delivery object with a future date, then mark as delivered
        Delivery bensonDeliveryFuture = new Delivery(BENSON_DELIVERY.getId(), BENSON_DELIVERY.getClient(),
                VALID_FUTURE_DATETIME, BENSON_DELIVERY.getRemarks(), BENSON_DELIVERY.getCost(),
                BENSON_DELIVERY.getTag());
        bensonDeliveryFuture.markAsDelivered();
        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(bensonDeliveryFuture);

        FoodBook foodBook = new FoodBook();
        foodBook.addPerson(TypicalPersons.BENSON);
        assertEquals(bensonDeliveryFuture, adapted.toModelType(foodBook));
    }

    @Test
    public void toModelType_validNullTag_returnsDelivery() throws Exception {
        // Create a new delivery object with a future date for CARL_DELIVERY (which has a null tag)
        Delivery carlDeliveryFuture = new Delivery(CARL_DELIVERY.getId(), CARL_DELIVERY.getClient(),
                VALID_FUTURE_DATETIME, CARL_DELIVERY.getRemarks(), CARL_DELIVERY.getCost(), CARL_DELIVERY.getTag());
        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(carlDeliveryFuture);

        FoodBook foodBook = new FoodBook();
        foodBook.addPerson(TypicalPersons.CARL);
        assertEquals(carlDeliveryFuture, adapted.toModelType(foodBook));
    }

    @Test
    public void toModelType_nullId_throwsIllegalValueException() {
        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(
                null,
                ALICE_DELIVERY.getClient().getName().fullName,
                VALID_FUTURE_DATE, // Use valid future date to isolate the null ID error
                VALID_FUTURE_TIME,
                ALICE_DELIVERY.getRemarks(),
                ALICE_DELIVERY.getCost(),
                ALICE_DELIVERY.getStatus(),
                ALICE_DELIVERY.getTag());

        FoodBook foodBook = new FoodBook();
        foodBook.addPerson(TypicalPersons.ALICE);
        String expectedMessage = String.format(JsonAdaptedDelivery.MISSING_FIELD_MESSAGE_FORMAT, "id");

        IllegalValueException exception = assertThrows(IllegalValueException.class, ()
                -> adapted.toModelType(foodBook));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void toModelType_nullClientName_throwsIllegalValueException() {
        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(
                ALICE_DELIVERY.getId(), null,
                VALID_FUTURE_DATE,
                VALID_FUTURE_TIME,
                ALICE_DELIVERY.getRemarks(),
                ALICE_DELIVERY.getCost(),
                ALICE_DELIVERY.getStatus(),
                ALICE_DELIVERY.getTag());

        FoodBook foodBook = new FoodBook();
        String expectedMessage = String.format(JsonAdaptedDelivery.MISSING_FIELD_MESSAGE_FORMAT, "clientName");

        IllegalValueException exception = assertThrows(IllegalValueException.class, ()
                -> adapted.toModelType(foodBook));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void toModelType_nonExistentClient_throwsIllegalValueException() {
        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(
                ALICE_DELIVERY.getId(), INVALID_CLIENT_NAME,
                VALID_FUTURE_DATE,
                VALID_FUTURE_TIME,
                ALICE_DELIVERY.getRemarks(),
                ALICE_DELIVERY.getCost(),
                ALICE_DELIVERY.getStatus(),
                ALICE_DELIVERY.getTag());

        FoodBook foodBook = new FoodBook();
        String expectedMessage = "Client not found: " + INVALID_CLIENT_NAME;

        IllegalValueException exception = assertThrows(IllegalValueException.class, ()
                -> adapted.toModelType(foodBook));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        JsonAdaptedDelivery adapted = new JsonAdaptedDelivery(
                ALICE_DELIVERY.getId(),
                ALICE_DELIVERY.getClient().getName().fullName,
                INVALID_DATE, // Invalid date format
                VALID_FUTURE_TIME,
                ALICE_DELIVERY.getRemarks(),
                ALICE_DELIVERY.getCost(),
                ALICE_DELIVERY.getStatus(),
                ALICE_DELIVERY.getTag());

        FoodBook foodBook = new FoodBook();
        foodBook.addPerson(TypicalPersons.ALICE);
        String expectedMessage = DateTime.MESSAGE_CONSTRAINTS;

        IllegalValueException exception = assertThrows(IllegalValueException.class, ()
                -> adapted.toModelType(foodBook));
        assertEquals(expectedMessage, exception.getMessage());
    }
}
