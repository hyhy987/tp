package seedu.foodbook.model.undo;

import java.util.function.Predicate;

import seedu.foodbook.logic.commands.CommandResult;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;

public record ModelRecord(
        String commandString,
        CommandResult.UiPanel uiPanel,
        Predicate<? super Person> personListPredicate,
        Predicate<? super Delivery> deliveryListPredicate
) {}
