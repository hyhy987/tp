package seedu.foodbook.model;

import static java.util.Objects.requireNonNull;
import static seedu.foodbook.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.foodbook.commons.core.GuiSettings;
import seedu.foodbook.commons.core.LogsCenter;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;

/**
 * Represents the in-memory model of the food book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final FoodBook foodBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Delivery> filteredDeliveries;

    /**
     * Initializes a ModelManager with the given foodBook and userPrefs.
     */
    public ModelManager(ReadOnlyFoodBook foodBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(foodBook, userPrefs);

        logger.fine("Initializing with food book: " + foodBook + " and user prefs " + userPrefs);

        this.foodBook = new FoodBook(foodBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.foodBook.getPersonList());
        filteredDeliveries = new FilteredList<>(this.foodBook.getDeliveryList());
    }

    public ModelManager() {
        this(new FoodBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getFoodBookFilePath() {
        return userPrefs.getFoodBookFilePath();
    }

    @Override
    public void setFoodBookFilePath(Path foodBookFilePath) {
        requireNonNull(foodBookFilePath);
        userPrefs.setFoodBookFilePath(foodBookFilePath);
    }

    //=========== FoodBook ================================================================================

    @Override
    public void setFoodBook(ReadOnlyFoodBook foodBook) {
        this.foodBook.resetData(foodBook);
    }

    @Override
    public ReadOnlyFoodBook getFoodBook() {
        return foodBook;
    }

    //=========== Persons ================================================================================

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return foodBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        foodBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        foodBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        foodBook.setPerson(target, editedPerson);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedFoodBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //=========== Delivery ================================================================================

    @Override
    public boolean hasDelivery(Delivery delivery) {
        requireNonNull(delivery);
        return foodBook.hasDelivery(delivery);
    }

    @Override
    public void deleteDelivery(Delivery delivery) {
        foodBook.removeDelivery(delivery);
    }

    @Override
    public void addDelivery(Delivery delivery) {
        foodBook.addDelivery(delivery);
        updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES);
    }

    @Override
    public void setDelivery(Delivery target, Delivery editedDelivery) {
        requireAllNonNull(target, editedDelivery);

        foodBook.setDelivery(target, editedDelivery);
    }

    //=========== Filtered Delivery List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Delivery} backed by the internal list of
     * {@code versionedFoodBook}
     */
    @Override
    public ObservableList<Delivery> getFilteredDeliveryList() {
        return filteredDeliveries;
    }

    @Override
    public void updateFilteredDeliveryList(Predicate<Delivery> predicate) {
        requireNonNull(predicate);
        filteredDeliveries.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return foodBook.equals(otherModelManager.foodBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && filteredDeliveries.equals(otherModelManager.filteredDeliveries);
    }

}
