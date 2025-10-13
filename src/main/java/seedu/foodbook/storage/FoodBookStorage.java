package seedu.foodbook.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.foodbook.commons.exceptions.DataLoadingException;
import seedu.foodbook.model.ReadOnlyFoodBook;

/**
 * Represents a storage for {@link seedu.foodbook.model.FoodBook}.
 */
public interface FoodBookStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getFoodBookFilePath();

    /**
     * Returns FoodBook data as a {@link ReadOnlyFoodBook}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    Optional<ReadOnlyFoodBook> readFoodBook() throws DataLoadingException;

    /**
     * @see #getFoodBookFilePath()
     */
    Optional<ReadOnlyFoodBook> readFoodBook(Path filePath) throws DataLoadingException;

    /**
     * Saves the given {@link ReadOnlyFoodBook} to the storage.
     * @param foodBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveFoodBook(ReadOnlyFoodBook foodBook) throws IOException;

    /**
     * @see #saveFoodBook(ReadOnlyFoodBook)
     */
    void saveFoodBook(ReadOnlyFoodBook foodBook, Path filePath) throws IOException;

}
