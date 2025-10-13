package seedu.foodbook.logic;

import java.nio.file.Path;

import javafx.collections.ObservableList;
import seedu.foodbook.commons.core.GuiSettings;
import seedu.foodbook.logic.commands.CommandResult;
import seedu.foodbook.logic.commands.exceptions.CommandException;
import seedu.foodbook.logic.parser.exceptions.ParseException;
import seedu.foodbook.model.ReadOnlyFoodBook;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the FoodBook.
     *
     * @see seedu.foodbook.model.Model#getFoodBook()
     */
    ReadOnlyFoodBook getFoodBook();

    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<Person> getFilteredPersonList();

    /** Returns an unmodifiable view of the filtered list of deliveries */
    ObservableList<Delivery> getFilteredDeliveryList();

    /**
     * Returns the user prefs' food book file path.
     */
    Path getFoodBookFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);
}
