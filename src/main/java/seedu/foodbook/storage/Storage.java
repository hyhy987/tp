package seedu.foodbook.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.foodbook.commons.exceptions.DataLoadingException;
import seedu.foodbook.model.ReadOnlyFoodBook;
import seedu.foodbook.model.ReadOnlyUserPrefs;
import seedu.foodbook.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends FoodBookStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataLoadingException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getFoodBookFilePath();

    @Override
    Optional<ReadOnlyFoodBook> readFoodBook() throws DataLoadingException;

    @Override
    void saveFoodBook(ReadOnlyFoodBook foodBook) throws IOException;

}
