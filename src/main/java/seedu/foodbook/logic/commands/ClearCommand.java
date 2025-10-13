package seedu.foodbook.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.foodbook.model.FoodBook;
import seedu.foodbook.model.Model;

/**
 * Clears the food book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Food book has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setFoodBook(new FoodBook());
        return new CommandResult(MESSAGE_SUCCESS, CommandResult.UiPanel.PERSONS);
    }
}
