package seedu.address.testutil;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;

/**
 * A utility class containing a typical FoodBook to be used in tests.
 */
public class TypicalFoodBook {

    private TypicalFoodBook() {} // prevents instantiation

    // TODO: Once JSON serialization is implemented for delivveries, add typical deliveries here


    /**
     * Returns an {@code FoodBook} with all the typical persons and deliveries.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : TypicalPersons.getTypicalPersons()) {
            ab.addPerson(person);
        }
        /*
        TODO: UNCOMMENT ONCE JSON SERIALIZATION IS IMPLEMENTED FOR DELIVERIES
        for (Delivery delivery : TypicalDeliveries.getTypicalDeliveries()) {
            ab.addDelivery(delivery);
        }
        */
        return ab;
    }

}
