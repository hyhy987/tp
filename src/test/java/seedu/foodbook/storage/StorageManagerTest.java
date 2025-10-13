package seedu.foodbook.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.foodbook.testutil.TypicalFoodBook.getTypicalFoodBook;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.foodbook.commons.core.GuiSettings;
import seedu.foodbook.model.FoodBook;
import seedu.foodbook.model.ReadOnlyFoodBook;
import seedu.foodbook.model.UserPrefs;

public class StorageManagerTest {

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonFoodBookStorage foodBookStorage = new JsonFoodBookStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(foodBookStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void foodBookReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonFoodBookStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonFoodBookStorageTest} class.
         */
        FoodBook original = getTypicalFoodBook();
        storageManager.saveFoodBook(original);
        ReadOnlyFoodBook retrieved = storageManager.readFoodBook().get();
        assertEquals(original, new FoodBook(retrieved));
    }

    @Test
    public void getFoodBookFilePath() {
        assertNotNull(storageManager.getFoodBookFilePath());
    }

}
