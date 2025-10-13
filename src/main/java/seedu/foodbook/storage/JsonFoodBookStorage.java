package seedu.foodbook.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.foodbook.commons.core.LogsCenter;
import seedu.foodbook.commons.exceptions.DataLoadingException;
import seedu.foodbook.commons.exceptions.IllegalValueException;
import seedu.foodbook.commons.util.FileUtil;
import seedu.foodbook.commons.util.JsonUtil;
import seedu.foodbook.model.ReadOnlyFoodBook;

/**
 * A class to access FoodBook data stored as a json file on the hard disk.
 */
public class JsonFoodBookStorage implements FoodBookStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonFoodBookStorage.class);

    private Path filePath;

    public JsonFoodBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getFoodBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyFoodBook> readFoodBook() throws DataLoadingException {
        return readFoodBook(filePath);
    }

    /**
     * Similar to {@link #readFoodBook()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ReadOnlyFoodBook> readFoodBook(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableFoodBook> jsonFoodBook = JsonUtil.readJsonFile(
                filePath, JsonSerializableFoodBook.class);
        if (!jsonFoodBook.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonFoodBook.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveFoodBook(ReadOnlyFoodBook foodBook) throws IOException {
        saveFoodBook(foodBook, filePath);
    }

    /**
     * Similar to {@link #saveFoodBook(ReadOnlyFoodBook)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveFoodBook(ReadOnlyFoodBook foodBook, Path filePath) throws IOException {
        requireNonNull(foodBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableFoodBook(foodBook), filePath);
    }

}
