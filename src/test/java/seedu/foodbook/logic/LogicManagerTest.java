package seedu.foodbook.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.foodbook.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.foodbook.logic.commands.AddDeliveryCommand.MESSAGE_CLIENT_NOT_FOUND;
import static seedu.foodbook.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.foodbook.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.foodbook.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.foodbook.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.foodbook.testutil.Assert.assertThrows;
import static seedu.foodbook.testutil.TypicalPersons.AMY;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.foodbook.commons.core.GuiSettings;
import seedu.foodbook.logic.commands.AddClientCommand;
import seedu.foodbook.logic.commands.CommandResult;
import seedu.foodbook.logic.commands.ListCommand;
import seedu.foodbook.logic.commands.exceptions.CommandException;
import seedu.foodbook.logic.parser.exceptions.ParseException;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.ModelManager;
import seedu.foodbook.model.ReadOnlyFoodBook;
import seedu.foodbook.model.UserPrefs;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.storage.JsonFoodBookStorage;
import seedu.foodbook.storage.JsonUserPrefsStorage;
import seedu.foodbook.storage.StorageManager;
import seedu.foodbook.testutil.PersonBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonFoodBookStorage foodBookStorage =
                new JsonFoodBookStorage(temporaryFolder.resolve("foodBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(foodBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete_client Alex Chan";
        assertCommandException(deleteCommand, String.format(MESSAGE_CLIENT_NOT_FOUND, "Alex Chan"));
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION, String.format(
                LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredPersonList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getFoodBook(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        // Inject LogicManager with an FoodBookStorage that throws the IOException e when saving
        JsonFoodBookStorage foodBookStorage = new JsonFoodBookStorage(prefPath) {
            @Override
            public void saveFoodBook(ReadOnlyFoodBook foodBook, Path filePath)
                    throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(foodBookStorage, userPrefsStorage);

        logic = new LogicManager(model, storage);

        // Triggers the saveFoodBook method by executing an add command
        String addCommand = AddClientCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addPerson(expectedPerson);
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }

    /**
     * Verifies that {@link LogicManager#getFoodBook()} returns the exact
     * {@link ReadOnlyFoodBook} instance held by the {@link Model}. This ensures that
     * {@code LogicManager} correctly delegates the call to the model and does not keep a
     * separate copy or cache.
     *
     * <p>Contract: The food book instance retrieved via {@code logic} must be
     * {@code equals}-to the one in {@code model}.</p>
     */
    @Test
    public void getFoodBook_returnsModelFoodBook() {
        assertEquals(model.getFoodBook(), logic.getFoodBook());
    }

    /**
     * Verifies that {@link LogicManager#getFoodBookFilePath()} is non-null and exactly
     * matches the file path maintained by the {@link Model}. This ensures correct delegation
     * and guards against regressions that return {@code null} or an unexpected path.
     *
     * <p>Contract: {@code logic.getFoodBookFilePath()} is non-null and equals
     * {@code model.getFoodBookFilePath()}.</p>
     */
    @Test
    public void getFoodBookFilePath_notNullAndDelegated() {
        Path logicPath = logic.getFoodBookFilePath();
        assertNotNull(logicPath);
        assertEquals(model.getFoodBookFilePath(), logicPath);
    }

    /**
     * Verifies that the delivery list returned by {@link LogicManager#getFilteredDeliveryList()}
     * is unmodifiable. Attempting to mutate it (e.g., {@code remove(0)}) must throw
     * {@link UnsupportedOperationException}.
     *
     * <p>Rationale: The logic layer must not expose internal model collections that callers
     * can mutate directly.</p>
     *
     * @see LogicManagerTest#getFilteredPersonList_modifyList_throwsUnsupportedOperationException()
     */
    @Test
    public void getFilteredDeliveryList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredDeliveryList().remove(0));
    }

    /**
     * Verifies round-trip delegation of GUI settings:
     * <ol>
     *   <li>{@link LogicManager#setGuiSettings(GuiSettings)} forwards the new settings to the {@link Model}.</li>
     *   <li>{@link LogicManager#getGuiSettings()} returns the updated settings.</li>
     * </ol>
     *
     * <p>Contract: After calling {@code logic.setGuiSettings(newSettings)}, both
     * {@code model.getGuiSettings()} and {@code logic.getGuiSettings()} must equal
     * {@code newSettings}.</p>
     */
    @Test
    public void guiSettings_roundTrip_delegatesToModel() {
        GuiSettings newSettings = new GuiSettings(1024, 768, 150, 200);
        logic.setGuiSettings(newSettings);
        assertEquals(newSettings, model.getGuiSettings());
        assertEquals(newSettings, logic.getGuiSettings());
    }

    /**
     * Sanity test that {@link LogicManager#getGuiSettings()} never returns {@code null} under
     * normal initialization (i.e., before any explicit {@code setGuiSettings} call).
     *
     * <p>Rationale: Guards against regressions where GUI settings are accidentally
     * uninitialized or cleared.</p>
     */
    @Test
    public void getGuiSettings_notNullByDefault() {
        assertNotNull(logic.getGuiSettings());
    }
}
