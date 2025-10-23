package seedu.foodbook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.foodbook.model.Model.PREDICATE_SHOW_ALL_DELIVERIES;
import static seedu.foodbook.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.foodbook.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.foodbook.commons.core.GuiSettings;
import seedu.foodbook.logic.commands.CommandResult.UiPanel;
import seedu.foodbook.logic.commands.exceptions.CommandException;
import seedu.foodbook.model.FoodBook;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.ReadOnlyFoodBook;
import seedu.foodbook.model.ReadOnlyUserPrefs;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Name;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.model.undo.ModelRecord;
import seedu.foodbook.model.undo.exceptions.NoMoreUndoException;

/**
 * Tests for UndoCommand covering which commands are undoable and which are not.
 */
public class UndoCommandTest {

    // =========================
    // No history -> error
    // =========================
    @Test
    public void execute_noHistory_throwsCommandException() {
        ModelStubWithUndoStack model = new ModelStubWithUndoStack();
        assertThrows(CommandException.class, () -> new UndoCommand().execute(model));
    }

    // =========================
    // Undoable commands
    // =========================
    @Test
    public void execute_afterAddClient_undoesAddClient() throws Exception {
        assertUndoSuccessFor("add_client", UiPanel.PERSONS);
    }

    @Test
    public void execute_afterAddDelivery_undoesAddDelivery() throws Exception {
        assertUndoSuccessFor("add_delivery", UiPanel.DELIVERIES);
    }

    @Test
    public void execute_afterClear_undoesClear() throws Exception {
        // Clear affects both lists; we just assert the label we saved.
        assertUndoSuccessFor("clear", UiPanel.PERSONS);
    }

    @Test
    public void execute_afterDeleteClient_undoesDeleteClient() throws Exception {
        assertUndoSuccessFor("delete_client", UiPanel.PERSONS);
    }

    @Test
    public void execute_afterDeleteDelivery_undoesDeleteDelivery() throws Exception {
        assertUndoSuccessFor("delete_delivery", UiPanel.DELIVERIES);
    }

    @Test
    public void execute_afterEditClient_undoesEditClient() throws Exception {
        assertUndoSuccessFor("edit_client", UiPanel.PERSONS);
    }

    @Test
    public void execute_afterEditDelivery_undoesEditDelivery() throws Exception {
        assertUndoSuccessFor("edit_delivery", UiPanel.DELIVERIES);
    }

    @Test
    public void execute_afterMarkCommand_undoesMark() throws Exception {
        assertUndoSuccessFor("mark_command", UiPanel.DELIVERIES);
    }

    @Test
    public void execute_afterUnmarkCommand_undoesUnmark() throws Exception {
        assertUndoSuccessFor("unmark_command", UiPanel.DELIVERIES);
    }

    // =========================
    // Non-undoable commands do NOT affect history
    // =========================
    @Test
    public void execute_afterNonUndoableCommands_keepsLastUndoable() throws Exception {
        ModelStubWithUndoStack model = new ModelStubWithUndoStack();

        // Put an undoable command in history
        model.checkpoint("add_client", UiPanel.PERSONS);

        // Simulate non-undoable commands (they do NOT call checkpoint)
        simulateExit(model);
        simulateFindClient(model);
        simulateFindDelivery(model);
        simulateHelp(model);
        simulateList(model);
        simulateListDelivery(model);

        // Undo should still undo "add_client"
        CommandResult result = new UndoCommand().execute(model);
        assertEquals(String.format(UndoCommand.MESSAGE_SUCCESS, "add_client"), result.getFeedbackToUser());
        assertEquals(UiPanel.PERSONS, result.getPanelToShow());
    }

    @Test
    public void executeOnlyNonUndoableCommands_noHistory_throws() {
        ModelStubWithUndoStack model = new ModelStubWithUndoStack();

        // Only non-undoable commands occurred (no checkpoint calls)
        simulateFindClient(model);
        simulateHelp(model);

        assertThrows(CommandException.class, () -> new UndoCommand().execute(model));
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------
    private void assertUndoSuccessFor(String commandWord, UiPanel panel) throws Exception {
        ModelStubWithUndoStack model = new ModelStubWithUndoStack();
        model.checkpoint(commandWord, panel);

        CommandResult result = new UndoCommand().execute(model);
        assertEquals(String.format(UndoCommand.MESSAGE_SUCCESS, commandWord), result.getFeedbackToUser());
        assertEquals(panel, result.getPanelToShow());
    }

    private void simulateExit(ModelStubWithUndoStack model) {
        // exit does not checkpoint
    }

    private void simulateFindClient(ModelStubWithUndoStack model) {
        // find_client does not checkpoint
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS); // harmless, no checkpoint
    }

    private void simulateFindDelivery(ModelStubWithUndoStack model) {
        // find_delivery does not checkpoint
        model.updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES); // harmless, no checkpoint
    }

    private void simulateHelp(ModelStubWithUndoStack model) {
        // help does not checkpoint
    }

    private void simulateList(ModelStubWithUndoStack model) {
        // list does not checkpoint
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    private void simulateListDelivery(ModelStubWithUndoStack model) {
        // list-delivery does not checkpoint
        model.updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES);
    }

    // ---------------------------------------------------------------------
    // Minimal Model stub with an in-memory undo stack of ModelRecord
    // ---------------------------------------------------------------------
    private static class ModelStubWithUndoStack implements Model {
        private final java.util.Deque<ModelRecord> stack = new java.util.ArrayDeque<>();

        @Override
        public void checkpoint(String commandString, UiPanel uiPanel) {
            // Save the command label + current filters (use show-all by default)
            stack.push(new ModelRecord(
                    commandString,
                    uiPanel,
                    PREDICATE_SHOW_ALL_PERSONS,
                    PREDICATE_SHOW_ALL_DELIVERIES
            ));
        }

        @Override
        public ModelRecord undo() throws NoMoreUndoException {
            if (stack.isEmpty()) {
                throw new NoMoreUndoException();
            }
            return stack.pop();
        }

        // ---- The rest are unused in these tests; keep them minimal ----
        @Override public void setUserPrefs(ReadOnlyUserPrefs userPrefs) { }
        @Override public ReadOnlyUserPrefs getUserPrefs() {
            return null;
        }
        @Override public GuiSettings getGuiSettings() {
            return null;
        }
        @Override public void setGuiSettings(GuiSettings guiSettings) { }
        @Override public Path getFoodBookFilePath() {
            return null;
        }
        @Override public void setFoodBookFilePath(Path foodBookFilePath) { }
        @Override public void setFoodBook(ReadOnlyFoodBook foodBook) { }
        @Override public ReadOnlyFoodBook getFoodBook() {
            return new FoodBook();
        }
        @Override public boolean hasPerson(Person person) {
            return false;
        }
        @Override public Optional<Person> getPersonByName(Name clientName) {
            return Optional.empty();
        }
        @Override public void deletePerson(Person target) { }
        @Override public void addPerson(Person person) { }
        @Override public void setPerson(Person target, Person editedPerson) { }
        @Override public ObservableList<Person> getFilteredPersonList() {
            return null;
        }
        @Override public void updateFilteredPersonList(Predicate<Person> predicate) { }
        @Override public boolean hasDelivery(Delivery delivery) {
            return false;
        }
        @Override public Optional<Delivery> getDeliveryById(Integer deliveryId) {
            return Optional.empty();
        }
        @Override public List<Delivery> getDeliveriesByClientName(Name clientName) {
            return List.of();
        }
        @Override public void deleteDelivery(Delivery target) { }
        @Override public void addDelivery(Delivery delivery) { }
        @Override public void setDelivery(Delivery target, Delivery editedDelivery) { }
        @Override public ObservableList<Delivery> getFilteredDeliveryList() {
            return null;
        }
        @Override public void updateFilteredDeliveryList(Predicate<Delivery> predicate) { }
    }
}
