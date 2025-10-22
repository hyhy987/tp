package seedu.foodbook.model.undo;

import java.util.function.Predicate;

import seedu.foodbook.logic.commands.CommandResult;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;

/**
 * Record representing the state of the model
 * For use with undo
 * @param commandString The most recent command word used
 * @param uiPanel The uiPanel to show after undo
 * @param personListPredicate The filters to apply to personList after undo
 * @param deliveryListPredicate The filters to apply to deliveryList after undo
 */
public record ModelRecord(
        String commandString,
        CommandResult.UiPanel uiPanel,
        Predicate<? super Person> personListPredicate,
        Predicate<? super Delivery> deliveryListPredicate
) {}
