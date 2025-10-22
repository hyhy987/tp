package seedu.foodbook.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.model.Model.PREDICATE_SHOW_ALL_DELIVERIES;
import static seedu.foodbook.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.foodbook.testutil.Assert.assertThrows;
import static seedu.foodbook.testutil.TypicalDeliveries.ALICE_DELIVERY;
import static seedu.foodbook.testutil.TypicalDeliveries.BENSON_DELIVERY;
import static seedu.foodbook.testutil.TypicalPersons.ALICE;
import static seedu.foodbook.testutil.TypicalPersons.BENSON;
import static seedu.foodbook.testutil.TypicalPersons.CARL;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.foodbook.commons.core.GuiSettings;
import seedu.foodbook.logic.commands.CommandResult.UiPanel;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.delivery.DeliveryContainsDatePredicate;
import seedu.foodbook.model.person.NameContainsKeywordsPredicate;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.model.undo.exceptions.NoMoreUndoException;
import seedu.foodbook.testutil.FoodBookBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new FoodBook(), new FoodBook(modelManager.getFoodBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setFoodBookFilePath(Paths.get("food/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setFoodBookFilePath(Paths.get("new/food/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setFoodBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setFoodBookFilePath(null));
    }

    @Test
    public void setFoodBookFilePath_validPath_setsFoodBookFilePath() {
        Path path = Paths.get("food/book/file/path");
        modelManager.setFoodBookFilePath(path);
        assertEquals(path, modelManager.getFoodBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInFoodBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInFoodBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void hasDelivery_nullDelivery_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasDelivery(null));
    }

    @Test
    public void hasDelivery_deliveryNotInFoodBook_returnsFalse() {
        assertFalse(modelManager.hasDelivery(ALICE_DELIVERY));
    }

    @Test
    public void hasDelivery_deliveryInFoodBook_returnsTrue() {
        modelManager.addDelivery(ALICE_DELIVERY);
        assertTrue(modelManager.hasDelivery(ALICE_DELIVERY));
    }

    @Test
    public void getFilteredDeliveryList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredDeliveryList().remove(0));
    }

    @Test
    public void equals() {
        FoodBook foodBook = new FoodBookBuilder()
                .withPerson(ALICE)
                .withPerson(BENSON)
                .withPerson(CARL)
                .withDelivery(ALICE_DELIVERY)
                .withDelivery(BENSON_DELIVERY)
                .build();
        FoodBook differentFoodBook = new FoodBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(foodBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(foodBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different foodBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentFoodBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(foodBook, userPrefs)));
        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different filteredList -> returns false
        String searchDate = ALICE_DELIVERY.getDeliveryDate().getDateString();
        modelManager.updateFilteredDeliveryList(new DeliveryContainsDatePredicate(searchDate));
        assertFalse(modelManager.equals(new ModelManager(foodBook, userPrefs)));
        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setFoodBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(foodBook, differentUserPrefs)));
    }

    // =========================================================================================
    // NEW TESTS: getPersonByName
    // =========================================================================================

    @Test
    public void getPersonByName_present_returnsOptionalWithPerson() {
        FoodBook fb = new FoodBookBuilder().withPerson(ALICE).build();
        ModelManager m = new ModelManager(fb, new UserPrefs());
        assertTrue(m.getPersonByName(ALICE.getName()).isPresent());
        assertEquals(ALICE, m.getPersonByName(ALICE.getName()).get());
    }

    @Test
    public void getPersonByName_absent_returnsEmptyOptional() {
        FoodBook fb = new FoodBookBuilder().withPerson(BENSON).build();
        ModelManager m = new ModelManager(fb, new UserPrefs());
        assertTrue(m.getPersonByName(ALICE.getName()).isEmpty());
    }

    @Test
    public void getPersonByName_nullName_returnsEmptyOptional() {
        FoodBook fb = new FoodBookBuilder().withPerson(ALICE).build();
        ModelManager m = new ModelManager(fb, new UserPrefs());
        assertTrue(m.getPersonByName(null).isEmpty());
    }

    // =========================================================================================
    // NEW TESTS: getDeliveryById
    // =========================================================================================

    @Test
    public void getDeliveryById_present_returnsOptionalWithDelivery() {
        FoodBook fb = new FoodBookBuilder().withDelivery(ALICE_DELIVERY).build();
        ModelManager m = new ModelManager(fb, new UserPrefs());
        Integer id = ALICE_DELIVERY.getId();
        assertTrue(m.getDeliveryById(id).isPresent());
        assertEquals(id, m.getDeliveryById(id).get().getId());
    }

    @Test
    public void getDeliveryById_absent_returnsEmptyOptional() {
        FoodBook fb = new FoodBookBuilder().withDelivery(ALICE_DELIVERY).build();
        ModelManager m = new ModelManager(fb, new UserPrefs());
        assertTrue(m.getDeliveryById(Integer.MIN_VALUE).isEmpty());
    }

    @Test
    public void getDeliveryById_null_returnsEmptyOptional() {
        FoodBook fb = new FoodBookBuilder().withDelivery(ALICE_DELIVERY).build();
        ModelManager m = new ModelManager(fb, new UserPrefs());
        assertTrue(m.getDeliveryById(null).isEmpty());
    }

    // =========================================================================================
    // NEW TESTS: getDeliveriesByClientName
    // =========================================================================================

    @Test
    public void getDeliveriesByClientName_noDeliveries_returnsEmptyList() {
        FoodBook fb = new FoodBookBuilder().build();
        ModelManager m = new ModelManager(fb, new UserPrefs());
        List<Delivery> list = m.getDeliveriesByClientName(ALICE.getName());
        assertTrue(list.isEmpty());
    }

    @Test
    public void getDeliveriesByClientName_singleMatch_returnsOnlyThatClientsDeliveries() {
        FoodBook fb = new FoodBookBuilder()
                .withDelivery(ALICE_DELIVERY)
                .withDelivery(BENSON_DELIVERY)
                .build();
        ModelManager m = new ModelManager(fb, new UserPrefs());

        List<Delivery> aliceDeliveries = m.getDeliveriesByClientName(ALICE.getName());
        assertFalse(aliceDeliveries.isEmpty());
        assertTrue(aliceDeliveries.stream().allMatch(d -> d.getClient().getName().equals(ALICE.getName())));

        List<Delivery> bensonDeliveries = m.getDeliveriesByClientName(BENSON.getName());
        assertFalse(bensonDeliveries.isEmpty());
        assertTrue(bensonDeliveries.stream().allMatch(d -> d.getClient().getName().equals(BENSON.getName())));
    }

    @Test
    public void getDeliveriesByClientName_null_returnsEmptyList() {
        FoodBook fb = new FoodBookBuilder().withDelivery(ALICE_DELIVERY).build();
        ModelManager m = new ModelManager(fb, new UserPrefs());
        assertTrue(m.getDeliveriesByClientName(null).isEmpty());
    }

    // =========================================================================================
    // NEW TESTS: checkpoint + undo
    // =========================================================================================

    @Test
    public void undo_onEmptyStack_throwsNoMoreUndoException() {
        assertThrows(NoMoreUndoException.class, () -> modelManager.undo());
    }

    @Test
    public void checkpoint_thenUndo_restoresFoodBookStateAndFilters() throws Exception {
        // Start with populated model
        FoodBook fb = new FoodBookBuilder()
                .withPerson(ALICE)
                .withPerson(BENSON)
                .withDelivery(ALICE_DELIVERY)
                .withDelivery(BENSON_DELIVERY)
                .build();
        ModelManager m = new ModelManager(fb, new UserPrefs());

        // Ensure show-all filters are active
        m.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        m.updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES);

        // Take checkpoint
        m.checkpoint("initial", UiPanel.PERSONS);

        // Mutate state: remove a person + filter lists
        Person toRemove = ALICE;
        m.deletePerson(toRemove);
        m.updateFilteredPersonList(p -> p.getName().equals(BENSON.getName())); // only BENSON
        m.updateFilteredDeliveryList(d -> false); // show none

        // Sanity checks: state changed
        assertFalse(m.getFoodBook().getPersonList().contains(toRemove));
        assertTrue(m.getFilteredPersonList().stream().allMatch(p -> p.getName().equals(BENSON.getName())));
        assertTrue(m.getFilteredDeliveryList().isEmpty());

        // Undo
        m.undo();

        // Verify: person restored and filters show-all (by content)
        assertTrue(m.getFoodBook().getPersonList().contains(toRemove));
        assertFalse(m.getFilteredPersonList().isEmpty());
        assertFalse(m.getFilteredDeliveryList().isEmpty());
    }

    @Test
    public void checkpoint_capturesCustomPredicates_undoRestoresThem() throws Exception {
        // Build model
        FoodBook fb = new FoodBookBuilder()
                .withPerson(ALICE)
                .withPerson(BENSON)
                .withDelivery(ALICE_DELIVERY)
                .withDelivery(BENSON_DELIVERY)
                .build();
        ModelManager m = new ModelManager(fb, new UserPrefs());

        // Apply custom filters: only ALICE for persons; none for deliveries
        m.updateFilteredPersonList(p -> p.getName().equals(ALICE.getName()));
        m.updateFilteredDeliveryList(d -> false);

        // Checkpoint with custom filters active
        m.checkpoint("custom-filters", UiPanel.DELIVERIES);

        // Change filters to show-all
        m.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        m.updateFilteredDeliveryList(PREDICATE_SHOW_ALL_DELIVERIES);

        // Sanity: different view now
        assertTrue(m.getFilteredPersonList().size() >= 1);
        assertFalse(m.getFilteredDeliveryList().isEmpty());

        // Undo -> filters should be restored to custom ones
        m.undo();
        assertTrue(m.getFilteredPersonList().stream().allMatch(p -> p.getName().equals(ALICE.getName())));
        assertTrue(m.getFilteredDeliveryList().isEmpty());
    }
}
