package seedu.foodbook.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.foodbook.testutil.Assert.assertThrows;
import static seedu.foodbook.testutil.TypicalDeliveries.HOON_DELIVERY;
import static seedu.foodbook.testutil.TypicalDeliveries.IDA_DELIVERY;
import static seedu.foodbook.testutil.TypicalFoodBook.getTypicalFoodBook;
import static seedu.foodbook.testutil.TypicalPersons.HOON;
import static seedu.foodbook.testutil.TypicalPersons.IDA;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.foodbook.commons.exceptions.DataLoadingException;
import seedu.foodbook.model.FoodBook;
import seedu.foodbook.model.ReadOnlyFoodBook;

public class JsonFoodBookStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonFoodBookStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readFoodBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readFoodBook(null));
    }

    private java.util.Optional<ReadOnlyFoodBook> readFoodBook(String filePath) throws Exception {
        return new JsonFoodBookStorage(Paths.get(filePath)).readFoodBook(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readFoodBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readFoodBook("notJsonFormatFoodBook.json"));
    }

    @Test
    public void readFoodBook_invalidPersonFoodBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readFoodBook("invalidPersonFoodBook.json"));
    }

    @Test
    public void readFoodBook_invalidAndValidPersonFoodBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readFoodBook("invalidAndValidPersonFoodBook.json"));
    }

    @Test
    public void readFoodBook_invalidDeliveryFoodBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readFoodBook("invalidDeliveryFoodBook.json"));
    }

    @Test
    public void readFoodBook_invalidAndValidDeliveryFoodBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readFoodBook("invalidAndValidDeliveryFoodBook.json"));
    }

    @Test
    public void readAndSaveFoodBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempFoodBook.json");
        FoodBook original = getTypicalFoodBook();
        JsonFoodBookStorage jsonFoodBookStorage = new JsonFoodBookStorage(filePath);

        // Save in new file and read back
        jsonFoodBookStorage.saveFoodBook(original, filePath);
        ReadOnlyFoodBook readBack = jsonFoodBookStorage.readFoodBook(filePath).get();
        assertEquals(original, new FoodBook(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.addDelivery(HOON_DELIVERY);

        /* TODO: Currently, we cannot delete a person with deliveries,
        *   as this creates a bug when we trying parsing the deliveries (since clients cannot be found)
        *   Need to eventually ratify this into correct behaviour!
        * */
        //original.removePerson(ALICE);

        jsonFoodBookStorage.saveFoodBook(original, filePath);
        readBack = jsonFoodBookStorage.readFoodBook(filePath).get();
        assertEquals(original, new FoodBook(readBack));

        // Save and read without specifying file path
        original.addPerson(IDA);
        original.addDelivery(IDA_DELIVERY);
        jsonFoodBookStorage.saveFoodBook(original); // file path not specified
        readBack = jsonFoodBookStorage.readFoodBook().get(); // file path not specified
        assertEquals(original, new FoodBook(readBack));

        // Delete deliveries
        original.removeDelivery(IDA_DELIVERY);
        original.removeDelivery(HOON_DELIVERY);
        jsonFoodBookStorage.saveFoodBook(original); // file path not specified
        readBack = jsonFoodBookStorage.readFoodBook().get(); // file path not specified
        assertEquals(original, new FoodBook(readBack));

    }

    @Test
    public void saveFoodBook_nullFoodBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveFoodBook(null, "SomeFile.json"));
    }

    /**
     * Saves {@code foodBook} at the specified {@code filePath}.
     */
    private void saveFoodBook(ReadOnlyFoodBook foodBook, String filePath) {
        try {
            new JsonFoodBookStorage(Paths.get(filePath))
                    .saveFoodBook(foodBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveFoodBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveFoodBook(new FoodBook(), null));
    }
}
