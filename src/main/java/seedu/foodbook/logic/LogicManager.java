package seedu.foodbook.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.foodbook.commons.core.GuiSettings;
import seedu.foodbook.commons.core.LogsCenter;
import seedu.foodbook.logic.commands.Command;
import seedu.foodbook.logic.commands.CommandResult;
import seedu.foodbook.logic.commands.exceptions.CommandException;
import seedu.foodbook.logic.parser.FoodBookParser;
import seedu.foodbook.logic.parser.exceptions.ParseException;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.ReadOnlyFoodBook;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final FoodBookParser foodBookParser;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        foodBookParser = new FoodBookParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = foodBookParser.parseCommand(commandText);
        commandResult = command.execute(model);

        try {
            storage.saveFoodBook(model.getFoodBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyFoodBook getFoodBook() {
        return model.getFoodBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public ObservableList<Delivery> getFilteredDeliveryList() {
        return model.getFilteredDeliveryList();
    }

    @Override
    public Path getFoodBookFilePath() {
        return model.getFoodBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
