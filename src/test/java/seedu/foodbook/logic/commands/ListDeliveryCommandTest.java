package seedu.foodbook.logic.commands;

import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.foodbook.testutil.TypicalFoodBook.getTypicalFoodBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.foodbook.model.Model;
import seedu.foodbook.model.ModelManager;
import seedu.foodbook.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListDeliveryCommand.
 */
public class ListDeliveryCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalFoodBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getFoodBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListDeliveryCommand(), model,
                ListDeliveryCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
