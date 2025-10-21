package seedu.foodbook.model.undo;

import java.util.List;
import java.util.function.Predicate;

import seedu.foodbook.logic.commands.CommandResult;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;

public record FoodBookRecord(
        List<Person> personList,
        List<Delivery> deliveryList
) {}
