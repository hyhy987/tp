package seedu.foodbook.model;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.foodbook.commons.core.GuiSettings;
import seedu.foodbook.logic.commands.CommandResult;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Name;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.model.undo.ModelRecord;
import seedu.foodbook.model.undo.exceptions.NoMoreUndoException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;
    Predicate<Delivery> PREDICATE_SHOW_ALL_DELIVERIES = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' food book file path.
     */
    Path getFoodBookFilePath();

    /**
     * Sets the user prefs' food book file path.
     */
    void setFoodBookFilePath(Path foodBookFilePath);

    /**
     * Replaces food book data with the data in {@code foodBook}.
     */
    void setFoodBook(ReadOnlyFoodBook foodBook);

    /** Returns the FoodBook */
    ReadOnlyFoodBook getFoodBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the food book.
     */
    boolean hasPerson(Person person);

    Optional<Person> getPersonByName(Name name);

    /**
     * Deletes the given person.
     * The person must exist in the food book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the food book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the food book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the food book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns true if a delivery with the same identity as {@code delivery} exists in the food book.
     */
    boolean hasDelivery(Delivery delivery);

    Optional<Delivery> getDeliveryById(Integer id);

    List<Delivery> getDeliveriesByClientName(Name clientName);

    /**
     * Deletes the given delivery.
     * The delivery must exist in the food book.
     */
    void deleteDelivery(Delivery delivery);

    /**
     * Adds the given delivery.
     * {@code delivery} must not already exist in the food book.
     */
    void addDelivery(Delivery delivery);

    /**
     * Replaces the given delivery {@code target} with {@code editedDelivery}.
     * {@code target} must exist in the food book.
     * The delivery identity of {@code editedDelivery}
     * must not be the same as another existing delivery in the food book.
     */
    void setDelivery(Delivery target, Delivery editedDelivery);

    /** Returns an unmodifiable view of the filtered delivery list */
    ObservableList<Delivery> getFilteredDeliveryList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredDeliveryList(Predicate<Delivery> predicate);


    void checkpoint(String commandString);

    ModelRecord undo() throws NoMoreUndoException;

    void setCurUiPanel(CommandResult.UiPanel uiPanel);
}
