package seedu.foodbook.model.undo;

import java.util.List;

import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;

/**
 * Record representing the state of foodBook
 * For use with undo
 * @param personList A copy of foodBook's personList
 * @param deliveryList A copy of foodBook's deliveryList
 */
public record FoodBookRecord(
        List<Person> personList,
        List<Delivery> deliveryList
) {}
