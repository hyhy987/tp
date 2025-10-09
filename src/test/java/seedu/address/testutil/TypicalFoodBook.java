package seedu.address.testutil;

import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;

import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.delivery.DateTime;
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
        return ab;
    }

    /**
     * Returns an AddressBook with typical persons and typical deliveries.
     */
    public static AddressBook getTypicalAddressBookWithDeliveries() {
        AddressBook ab = getTypicalAddressBook();
        for (Delivery d : getTypicalDeliveries(ab)) {
            ab.addDelivery(d);
        }
        return ab;
    }

    /**
     * Returns a list of typical Person objects.
     */
    public static List<Person> getTypicalPersons() {
        return List.of(ALICE, BENSON, CARL); // import from your existing TypicalPersons
    }

    /**
     * Returns a list of typical Delivery objects, each linked to a person in the provided AddressBook.
     *
     * @param ab The address book containing the clients for lookup.
     */
    public static List<Delivery> getTypicalDeliveries(AddressBook ab) {
        try {
            // Find Alice in the book
            Person alice = ab.getPersonList().stream()
                    .filter(p -> p.getName().fullName.equals("Alice Yeoh"))
                    .findFirst()
                    .orElseThrow();
            DateTime dt1 = new DateTime("15/10/2025", "0930");
            Delivery d1 = new Delivery(1, alice, dt1, "Breakfast order", 30.50);

            Person benson = ab.getPersonList().stream()
                    .filter(p -> p.getName().fullName.equals("Benson Meier"))
                    .findFirst()
                    .orElseThrow();
            DateTime dt2 = new DateTime("16/10/2025", "1200");
            Delivery d2 = new Delivery(2, benson, dt2, "Lunch catering", 100.00);

            return List.of(d1, d2);
        } catch (Exception e) {
            throw new AssertionError("Typical deliveries setup failed", e);
        }
    }

}
