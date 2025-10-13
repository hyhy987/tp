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

import org.junit.jupiter.api.Test;

import seedu.foodbook.commons.core.GuiSettings;
import seedu.foodbook.model.delivery.DeliveryContainsDatePredicate;
import seedu.foodbook.model.person.NameContainsKeywordsPredicate;
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
}
