package seedu.foodbook.testutil;

import seedu.foodbook.model.FoodBook;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;

/**
 * A utility class to help with building Foodbook objects.
 * Example usage: <br>
 *     {@code FoodBook ab = new FoodBookBuilder().withPerson("John", "Doe").build();}
 */
public class FoodBookBuilder {

    private FoodBook foodBook;

    public FoodBookBuilder() {
        foodBook = new FoodBook();
    }

    public FoodBookBuilder(FoodBook foodBook) {
        this.foodBook = foodBook;
    }

    /**
     * Adds a new {@code Person} to the {@code FoodBook} that we are building.
     */
    public FoodBookBuilder withPerson(Person person) {
        foodBook.addPerson(person);
        return this;
    }

    /**
     * Adds a new {@code Delivery} to the {@code FoodBook} that we are building.
     */
    public FoodBookBuilder withDelivery(Delivery delivery) {
        foodBook.addDelivery(delivery);
        return this;
    }

    public FoodBook build() {
        return foodBook;
    }
}
