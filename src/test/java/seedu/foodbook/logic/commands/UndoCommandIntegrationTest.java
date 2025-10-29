package seedu.foodbook.logic.commands;

import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.foodbook.model.Model.PREDICATE_SHOW_ALL_DELIVERIES;
import static seedu.foodbook.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.foodbook.testutil.TypicalFoodBook.getTypicalFoodBook;
import static seedu.foodbook.testutil.TypicalPersons.ALICE;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.foodbook.logic.Messages;
import seedu.foodbook.logic.commands.EditClientCommand.EditPersonDescriptor;
import seedu.foodbook.logic.commands.EditDeliveryCommand.EditDeliveryDescriptor;
import seedu.foodbook.model.FoodBook;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.ModelManager;
import seedu.foodbook.model.UserPrefs;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.delivery.DeliveryPredicate;
import seedu.foodbook.model.person.ClientMatchesPredicate;
import seedu.foodbook.model.person.Name;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.testutil.DeliveryBuilder;
import seedu.foodbook.testutil.EditPersonDescriptorBuilder;
import seedu.foodbook.testutil.PersonBuilder;

/**
 * Integration tests for UndoCommand with real commands and a real Model.
 * For each undoable command: execute command, then assert UndoCommand restores the exact prior state
 * and reports the correct command string.
 * For each non-undoable command: ensure no checkpoint is added (undo either undoes the last undoable
 * command earlier, or throws when no history exists).
 */
public class UndoCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setup() {
        model = new ModelManager(getTypicalFoodBook(), new UserPrefs());
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES);
    }

    // --------------------------------------------------------------------
    // UNDOABLE COMMANDS
    // --------------------------------------------------------------------

    @Test
    public void undo_afterAddClient_restoresPreviousStateAndReports() {
        Person toAdd = new PersonBuilder().withName("Zelda Zoom").build();

        // Snapshot BEFORE executing add_client
        Model expectedBefore = snapshotOf(model);

        // Execute add_client
        AddClientCommand add = new AddClientCommand(toAdd);
        String addMsg = String.format(AddClientCommand.MESSAGE_SUCCESS, Messages.format(toAdd));
        Model afterAddExpected = snapshotOf(model);
        afterAddExpected.addPerson(toAdd);
        assertCommandSuccess(add, model, addMsg, afterAddExpected);

        // Undo -> exact restore of expectedBefore
        String undoMsg = String.format(UndoCommand.MESSAGE_SUCCESS, "add_client");
        assertCommandSuccess(new UndoCommand(), model, undoMsg, expectedBefore);
    }

    @Test
    public void undo_afterAddDelivery_restoresPreviousStateAndReports() {
        // Build a new delivery that won't collide with typical IDs
        Delivery toAdd = new DeliveryBuilder().withId(123456).withClient(ALICE).build();

        Model expectedBefore = snapshotOf(model);

        AddDeliveryCommand add = new AddDeliveryCommand(toAdd);
        String addMsg = String.format(AddDeliveryCommand.MESSAGE_SUCCESS, Messages.format(toAdd));
        Model afterAddExpected = snapshotOf(model);
        afterAddExpected.addDelivery(toAdd);
        assertCommandSuccess(add, model, addMsg, afterAddExpected);

        String undoMsg = String.format(UndoCommand.MESSAGE_SUCCESS, "add_delivery");
        assertCommandSuccess(new UndoCommand(), model, undoMsg, expectedBefore);
    }

    @Test
    public void undo_afterClear_restoresPreviousStateAndReports() {
        Model expectedBefore = snapshotOf(model);

        ClearCommand clear = new ClearCommand();
        String clearMsg = ClearCommand.MESSAGE_SUCCESS;
        Model afterClearExpected = new ModelManager(new FoodBook(), new UserPrefs());
        assertCommandSuccess(clear, model, clearMsg, afterClearExpected);

        String undoMsg = String.format(UndoCommand.MESSAGE_SUCCESS, "clear");
        assertCommandSuccess(new UndoCommand(), model, undoMsg, expectedBefore);
    }

    @Test
    public void undo_afterDeleteClient_restoresPreviousStateAndReports() {
        // pick target
        Person target = model.getFilteredPersonList().get(0);
        Name targetName = target.getName();

        // snapshot BEFORE
        Model expectedBefore = snapshotOf(model);

        // expected AFTER delete: remove the person + all their deliveries
        Model afterDelExpected = snapshotOf(model);
        afterDelExpected.deletePerson(target);
        for (Delivery d : List.copyOf(afterDelExpected.getDeliveriesByClientName(targetName))) {
            afterDelExpected.deleteDelivery(d);
        }

        // run real command
        DeleteClientCommand del = new DeleteClientCommand(targetName);
        String delMsg = String.format(DeleteClientCommand.MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(target));
        assertCommandSuccess(del, model, delMsg, afterDelExpected);

        // undo should fully restore snapshot
        String undoMsg = String.format(UndoCommand.MESSAGE_SUCCESS, "delete_client");
        assertCommandSuccess(new UndoCommand(), model, undoMsg, expectedBefore);
    }


    @Test
    public void undo_afterDeleteDelivery_restoresPreviousStateAndReports() {
        Delivery target = model.getFilteredDeliveryList().get(0);
        DeleteDeliveryCommand del = new DeleteDeliveryCommand(target.getId());

        Model expectedBefore = snapshotOf(model);

        String delMsg = String.format(DeleteDeliveryCommand.MESSAGE_DELETE_DELIVERY_SUCCESS, Messages.format(target));
        Model afterDelExpected = snapshotOf(model);
        afterDelExpected.deleteDelivery(target);
        assertCommandSuccess(del, model, delMsg, afterDelExpected);

        String undoMsg = String.format(UndoCommand.MESSAGE_SUCCESS, "delete_delivery");
        assertCommandSuccess(new UndoCommand(), model, undoMsg, expectedBefore);
    }

    @Test
    public void undo_afterEditClient_restoresPreviousStateAndReports() {
        Person target = model.getFilteredPersonList().get(0);
        Person edited = new PersonBuilder(target).withName("New Name").build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName("New Name").build();

        Model expectedBefore = snapshotOf(model);

        EditClientCommand edit = new EditClientCommand(target.getName(), descriptor);
        String editMsg = String.format(EditClientCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(edited));
        Model afterEditExpected = snapshotOf(model);
        // Reflect cascading update to deliveries in expected model
        afterEditExpected.setPerson(target, edited);
        for (Delivery d : afterEditExpected.getDeliveriesByClientName(target.getName())) {
            afterEditExpected.setDelivery(d, d.copyWithNewClient(edited));
        }
        assertCommandSuccess(edit, model, editMsg, afterEditExpected);

        String undoMsg = String.format(UndoCommand.MESSAGE_SUCCESS, "edit_client");
        assertCommandSuccess(new UndoCommand(), model, undoMsg, expectedBefore);
    }

    @Test
    public void undo_afterEditDelivery_restoresPreviousStateAndReports() {
        Delivery target = model.getFilteredDeliveryList().get(0);
        EditDeliveryDescriptor descriptor = new EditDeliveryDescriptor();
        descriptor.setRemarks("updated-remarks");

        Model expectedBefore = snapshotOf(model);

        Delivery expectedEdited = new DeliveryBuilder(target).withRemarks("updated-remarks").build();
        EditDeliveryCommand edit = new EditDeliveryCommand(target.getId(), descriptor);
        String editMsg = String.format(
                EditDeliveryCommand.MESSAGE_EDIT_DELIVERY_SUCCESS,
                Messages.format(expectedEdited)
        );

        Model afterEditExpected = snapshotOf(model);
        afterEditExpected.setDelivery(target, expectedEdited);
        assertCommandSuccess(edit, model, editMsg, afterEditExpected);

        String undoMsg = String.format(UndoCommand.MESSAGE_SUCCESS, "edit_delivery");
        assertCommandSuccess(new UndoCommand(), model, undoMsg, expectedBefore);
    }

    @Test
    public void undo_afterMark_restoresPreviousStateAndReports() {
        Delivery target = model.getFilteredDeliveryList().get(0);

        Model expectedBefore = snapshotOf(model);

        MarkCommand mark = new MarkCommand(target.getId());
        String markMsg = String.format(
                MarkCommand.MESSAGE_MARK_DELIVERY_SUCCESS,
                Messages.format(target.copyAsDelivered())
        );
        Model afterMarkExpected = snapshotOf(model);
        afterMarkExpected.setDelivery(target, target.copyAsDelivered());
        assertCommandSuccess(mark, model, markMsg, afterMarkExpected);

        String undoMsg = String.format(UndoCommand.MESSAGE_SUCCESS, "mark");
        assertCommandSuccess(new UndoCommand(), model, undoMsg, expectedBefore);
    }

    @Test
    public void undo_afterUnmark_restoresPreviousStateAndReports() {
        // Ensure the target is first marked, then unmarked
        Delivery initially = model.getFilteredDeliveryList().get(0);
        if (!initially.getStatus()) {
            // mark it first (this will push a checkpoint)
            MarkCommand mark = new MarkCommand(initially.getId());
            Model afterMarkExpected = snapshotOf(model);
            Delivery marked = initially.copyAsDelivered();
            String markMsg = String.format(MarkCommand.MESSAGE_MARK_DELIVERY_SUCCESS, Messages.format(marked));
            afterMarkExpected.setDelivery(initially, marked);
            assertCommandSuccess(mark, model, markMsg, afterMarkExpected);
            // undo to revert the precondition? No — we want it marked now. We'll keep it marked,
            // but that leaves a checkpoint. To keep this test focused on unmark's undo, add another undoable
            // later; instead, just do a clean model for clarity:
            model = new ModelManager(getTypicalFoodBook(), new UserPrefs());
        }

        // Re-pick target on the fresh model and make sure it's delivered
        Delivery target = model.getFilteredDeliveryList().get(0).copyAsDelivered();
        model.setDelivery(model.getFilteredDeliveryList().get(0), target);

        Model expectedBefore = snapshotOf(model);

        UnmarkCommand unmark = new UnmarkCommand(target.getId());
        Delivery expectedUnmarked = target.copyAsUndelivered();
        String unmarkMsg = String.format(
                UnmarkCommand.MESSAGE_UNMARK_DELIVERY_SUCCESS,
                Messages.format(expectedUnmarked)
        );

        Model afterUnmarkExpected = snapshotOf(model);
        afterUnmarkExpected.setDelivery(target, expectedUnmarked);
        assertCommandSuccess(unmark, model, unmarkMsg, afterUnmarkExpected);

        String undoMsg = String.format(UndoCommand.MESSAGE_SUCCESS, "unmark");
        assertCommandSuccess(new UndoCommand(), model, undoMsg, expectedBefore);
    }

    // --------------------------------------------------------------------
    // NON-UNDOABLE COMMANDS — do NOT push history
    // --------------------------------------------------------------------

    @Test
    public void undo_afterOnlyExitNoHistory_throws() {
        Model local = snapshotOf(model);
        ExitCommand exit = new ExitCommand();
        // Expected state equals current (exit does not change data)
        CommandResult expectedExit = new CommandResult(ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true);
        assertCommandSuccess(exit, model, expectedExit, local);

        assertCommandFailure(new UndoCommand(), model, UndoCommand.MESSAGE_NO_MORE_UNDO);
    }

    @Test
    public void undo_afterOnlyFindClientNoHistory_throws() {
        // Build a find_client that filters to ALICE by a keyword from her name
        String keyword = ALICE.getName().fullName;
        ClientMatchesPredicate predicate =
                new ClientMatchesPredicate(Optional.of(keyword), Optional.empty(), Optional.empty());
        FindClientCommand find = new FindClientCommand(predicate);

        // Expected model with same filter applied
        Model expectedAfterFind = snapshotOf(model);
        expectedAfterFind.updateFilteredPersonList(p -> p.getName().fullName.contains(keyword));

        String expectedMsg = String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                expectedAfterFind.getFilteredPersonList().size());
        assertCommandSuccess(find, model, expectedMsg, expectedAfterFind);

        assertCommandFailure(new UndoCommand(), model, UndoCommand.MESSAGE_NO_MORE_UNDO);
    }

    @Test
    public void undo_afterOnlyFindDeliveryNoHistory_throws() {
        // Filter deliveries by a date string from a known delivery
        Delivery sample = model.getFilteredDeliveryList().get(0);
        String dateStr = sample.getDeliveryDate().getDateString();


        DeliveryPredicate predicate =
                new DeliveryPredicate(
                        Optional.of(dateStr),
                        Optional.of(dateStr),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                );
        FindDeliveryCommand find = new FindDeliveryCommand(predicate);

        Model expectedAfter = snapshotOf(model);
        expectedAfter.updateFilteredDeliveryList(d -> d.getDeliveryDate().getDateString().equals(dateStr));

        String expectedMsg = String.format(Messages.MESSAGE_DELIVERIES_LISTED_OVERVIEW,
                expectedAfter.getFilteredDeliveryList().size());
        assertCommandSuccess(find, model, expectedMsg, expectedAfter);

        assertCommandFailure(new UndoCommand(), model, UndoCommand.MESSAGE_NO_MORE_UNDO);
    }

    @Test
    public void undo_afterOnlyHelpNoHistory_throws() {
        HelpCommand help = new HelpCommand();
        // Help often returns a message and maybe opens help window (no model mutation)
        Model expectedAfter = snapshotOf(model);
        CommandResult expectedHelp = new CommandResult(HelpCommand.SHOWING_HELP_MESSAGE, true, false);
        assertCommandSuccess(help, model, expectedHelp, expectedAfter);

        assertCommandFailure(new UndoCommand(), model, UndoCommand.MESSAGE_NO_MORE_UNDO);
    }

    @Test
    public void undo_afterOnlyListNoHistory_throws() {
        // Change the filter first to demonstrate list resets it, yet still non-undoable
        model.updateFilteredPersonList(p -> false);

        ListClientCommand list = new ListClientCommand();
        Model expectedAfter = snapshotOf(model);
        expectedAfter.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        assertCommandSuccess(list, model, ListClientCommand.MESSAGE_SUCCESS, expectedAfter);
        assertCommandFailure(new UndoCommand(), model, UndoCommand.MESSAGE_NO_MORE_UNDO);
    }

    @Test
    public void undo_afterOnlyListDeliveryNoHistory_throws() {
        // Change delivery filter first
        model.updateFilteredDeliveryList(d -> false);

        ListDeliveryCommand listDelivery = new ListDeliveryCommand();
        Model expectedAfter = snapshotOf(model);
        expectedAfter.updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES);

        assertCommandSuccess(listDelivery, model, ListDeliveryCommand.MESSAGE_SUCCESS, expectedAfter);
        assertCommandFailure(new UndoCommand(), model, UndoCommand.MESSAGE_NO_MORE_UNDO);
    }

    @Test
    public void undo_skipsNonUndoables_andUndoesMostRecentUndoable() {
        // 1) Perform an UNDOABLE command
        Person toAdd = new PersonBuilder().withName("TempUser").build();
        AddClientCommand add = new AddClientCommand(toAdd);

        Model afterAddExpected = snapshotOf(model);
        afterAddExpected.addPerson(toAdd);
        String addMsg = String.format(AddClientCommand.MESSAGE_SUCCESS, Messages.format(toAdd));
        assertCommandSuccess(add, model, addMsg, afterAddExpected);

        // 2) Perform several NON-UNDOABLE commands (no checkpoints)
        HelpCommand help = new HelpCommand();

        CommandResult expectedHelp = new CommandResult(HelpCommand.SHOWING_HELP_MESSAGE, true, false);
        assertCommandSuccess(help, model, expectedHelp, snapshotOf(model));


        ListClientCommand list = new ListClientCommand();
        Model afterListExpected = snapshotOf(model);
        afterListExpected.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        assertCommandSuccess(list, model, ListClientCommand.MESSAGE_SUCCESS, afterListExpected);

        // 3) Undo should still undo the earlier add_client
        Model expectedAfterUndo = snapshotOf(model);
        expectedAfterUndo.deletePerson(toAdd);

        String undoMsg = String.format(UndoCommand.MESSAGE_SUCCESS, "add_client");
        assertCommandSuccess(new UndoCommand(), model, undoMsg, expectedAfterUndo);
    }




    // --------------------------------------------------------------------
    // Helpers
    // --------------------------------------------------------------------

    private static Model snapshotOf(Model src) {
        return new ModelManager(new FoodBook(src.getFoodBook()), new UserPrefs());
    }
}
