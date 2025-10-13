package seedu.foodbook.storage;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.foodbook.commons.exceptions.IllegalValueException;
import seedu.foodbook.commons.util.JsonUtil;

public class JsonSerializableFoodBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get(
            "src", "test", "data", "JsonSerializableFoodBookTest");
    private static final Path TYPICAL_FOODBOOK_FILE =
            TEST_DATA_FOLDER.resolve("typicalFoodBook.json");
    private static final Path INVALID_PERSON_FILE =
            TEST_DATA_FOLDER.resolve("invalidPersonFoodBook.json");
    private static final Path DUPLICATE_PERSON_FILE =
            TEST_DATA_FOLDER.resolve("duplicatePersonFoodBook.json");
    private static final Path INVALID_DELIVERY_FILE =
            TEST_DATA_FOLDER.resolve("invalidDeliveryFoodBook.json");
    private static final Path DUPLICATE_DELIVERY_FILE =
            TEST_DATA_FOLDER.resolve("duplicateDeliveryFoodBook.json");

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableFoodBook dataFromFile = JsonUtil.readJsonFile(
                INVALID_PERSON_FILE, JsonSerializableFoodBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableFoodBook dataFromFile = JsonUtil.readJsonFile(
                INVALID_DELIVERY_FILE, JsonSerializableFoodBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_invalidDeliveryFile_throwsIllegalValueException() throws Exception {
        JsonSerializableFoodBook dataFromFile = JsonUtil.readJsonFile(
                INVALID_PERSON_FILE, JsonSerializableFoodBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateDelivery_throwsIllegalValueException() throws Exception {
        JsonSerializableFoodBook dataFromFile = JsonUtil.readJsonFile(
                DUPLICATE_DELIVERY_FILE, JsonSerializableFoodBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }
}
