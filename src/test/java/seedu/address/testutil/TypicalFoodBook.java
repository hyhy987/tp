package seedu.address.testutil;

import static seedu.address.testutil.TypicalDeliveries.getTypicalDeliveries;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;

import seedu.address.model.AddressBook;
import seedu.address.model.delivery.Delivery;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} and {@code Delivery} objects to be used in tests.
 */
public class TypicalFoodBook {
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person p : getTypicalPersons()) {
            ab.addPerson(p);
        }
        for (Delivery d : getTypicalDeliveries()) {
            ab.addDelivery(d);
        }
        return ab;
    }

}
