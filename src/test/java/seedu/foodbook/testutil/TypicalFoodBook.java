package seedu.foodbook.testutil;

import static seedu.foodbook.testutil.TypicalDeliveries.getTypicalDeliveries;
import static seedu.foodbook.testutil.TypicalPersons.getTypicalPersons;

import seedu.foodbook.model.FoodBook;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;

/**
 * A utility class containing a list of {@code Person} and {@code Delivery} objects to be used in tests.
 */
public class TypicalFoodBook {
    public static FoodBook getTypicalFoodBook() {
        FoodBook foodBook = new FoodBook();
        for (Person p : getTypicalPersons()) {
            foodBook.addPerson(p);
        }
        for (Delivery d : getTypicalDeliveries()) {
            foodBook.addDelivery(d);
        }
        return foodBook;
    }
}
