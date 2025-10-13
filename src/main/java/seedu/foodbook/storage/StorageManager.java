package seedu.foodbook.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.foodbook.commons.core.LogsCenter;
import seedu.foodbook.commons.exceptions.DataLoadingException;
import seedu.foodbook.model.ReadOnlyFoodBook;
import seedu.foodbook.model.ReadOnlyUserPrefs;
import seedu.foodbook.model.UserPrefs;

/**
 * Manages storage of FoodBook data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private FoodBookStorage foodBookStorage;
    private UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code FoodBookStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(FoodBookStorage foodBookStorage, UserPrefsStorage userPrefsStorage) {
        this.foodBookStorage = foodBookStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ FoodBook methods ==============================

    @Override
    public Path getFoodBookFilePath() {
        return foodBookStorage.getFoodBookFilePath();
    }

    @Override
    public Optional<ReadOnlyFoodBook> readFoodBook() throws DataLoadingException {
        return readFoodBook(foodBookStorage.getFoodBookFilePath());
    }

    @Override
    public Optional<ReadOnlyFoodBook> readFoodBook(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return foodBookStorage.readFoodBook(filePath);
    }

    @Override
    public void saveFoodBook(ReadOnlyFoodBook foodBook) throws IOException {
        saveFoodBook(foodBook, foodBookStorage.getFoodBookFilePath());
    }

    @Override
    public void saveFoodBook(ReadOnlyFoodBook foodBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        foodBookStorage.saveFoodBook(foodBook, filePath);
    }

}
