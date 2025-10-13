package seedu.foodbook.logic.commands;

import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.foodbook.testutil.TypicalFoodBook.getTypicalFoodBook;

import org.junit.jupiter.api.Test;

import seedu.foodbook.model.FoodBook;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.ModelManager;
import seedu.foodbook.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyFoodBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyFoodBook_success() {
        Model model = new ModelManager(getTypicalFoodBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalFoodBook(), new UserPrefs());
        expectedModel.setFoodBook(new FoodBook());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
