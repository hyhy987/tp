package seedu.foodbook.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.foodbook.model.FoodBook;
import seedu.foodbook.model.ReadOnlyFoodBook;
import seedu.foodbook.model.delivery.DateTime;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Address;
import seedu.foodbook.model.person.Email;
import seedu.foodbook.model.person.Name;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.model.person.Phone;
import seedu.foodbook.model.tag.Tag;

/**
 * Contains utility methods for populating {@code FoodBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alice Tan"), new Phone("91234567"), new Email("alice.tan@example.com"),
                    new Address("Bukit Timah Rd 21"), getTagSet("vip")),
            new Person(new Name("Ben Lim"), new Phone("97881234"), new Email("ben.lim@example.com"),
                    new Address("Clementi Ave 3 Blk 456"), getTagSet("friend")),
            new Person(new Name("Chloe Ng"), new Phone("82004567"), new Email("chloe.ng@example.com"),
                    new Address("Tiong Bahru Rd 12"), getTagSet("corporate")),
            new Person(new Name("Darren Koh"), new Phone("88990011"), new Email("darren.koh@example.com"),
                    new Address("Jurong West St 91"), getTagSet("cool")),
            new Person(new Name("Ethan Lee"), new Phone("96543210"), new Email("ethan.lee@example.com"),
                    new Address("Hougang Ave 8"), getTagSet("family")),
            new Person(new Name("Faith Goh"), new Phone("83337744"), new Email("faith.goh@example.com"),
                    new Address("Serangoon North Ave 4"), getTagSet("regular")),
            new Person(new Name("Gavin Ong"), new Phone("90112233"), new Email("gavin.ong@example.com"),
                    new Address("Pasir Ris Dr 6"), getTagSet("corporate")),
            new Person(new Name("Hazel Chua"), new Phone("98445566"), new Email("hazel.chua@example.com"),
                    new Address("Marine Parade Rd"), getTagSet("friend")),
            new Person(new Name("Ivan Teo"), new Phone("87441234"), new Email("ivan.teo@example.com"),
                    new Address("Yishun Ave 2"), getTagSet("vip")),
            new Person(new Name("Jasmine Low"), new Phone("91880077"), new Email("jasmine.low@example.com"),
                    new Address("Bedok North Rd"), getTagSet("regular")),
            new Person(new Name("Ken Yap"), new Phone("93660055"), new Email("ken.yap@example.com"),
                    new Address("Ang Mo Kio Ave 5"), getTagSet("cool")),
            new Person(new Name("Leah Wong"), new Phone("88221199"), new Email("leah.wong@example.com"),
                    new Address("Bukit Merah View"), getTagSet("corporate"))
        };
    }

    public static Delivery[] getSampleDeliveries() {
        Person[] persons = getSamplePersons(); // your existing sample persons

        return new Delivery[] {
            new Delivery(1, findByName(persons, "Alice Tan"), new DateTime("5/6/2024", "0930"),
                    "Cupcakes x12", 36.0, null, false),
            new Delivery(2, findByName(persons, "Ben Lim"), new DateTime("6/6/2024", "1400"),
                    "Brownies box", 24.0, null, false),
            new Delivery(3, findByName(persons, "Chloe Ng"), new DateTime("7/6/2024", "1100"),
                    "Corporate snack set", 120.0, null, false),
            new Delivery(4, findByName(persons, "Darren Koh"), new DateTime("8/6/2024", "1600"),
                    "Birthday cake 8", 58.0, null, false),
            new Delivery(5, findByName(persons, "Ethan Lee"), new DateTime("9/6/2024", "1015"),
                    "Cookies x24", 30.0, null, false),
            new Delivery(6, findByName(persons, "Faith Goh"), new DateTime("10/6/2024", "1500"),
                    "Macarons assorted", 42.0, null, false),
            new Delivery(7, findByName(persons, "Gavin Ong"), new DateTime("11/6/2024", "1200"),
                    "Meeting refreshments", 85.0, null, false),
            new Delivery(8, findByName(persons, "Hazel Chua"), new DateTime("12/6/2024", "1830"),
                    "Vegan tart", 28.0, null, false),
            new Delivery(9, findByName(persons, "Ivan Teo"), new DateTime("13/6/2024", "0900"),
                    "Coffee & pastry set", 22.0, null, false),
            new Delivery(10, findByName(persons, "Jasmine Low"), new DateTime("14/6/2024", "1345"),
                    "Cheesecake slice x4", 32.0, null, false),
            new Delivery(11, findByName(persons, "Ken Yap"), new DateTime("15/6/2024", "1700"),
                    "Fruit platter (small)", 26.0, null, false),
            new Delivery(12, findByName(persons, "Leah Wong"), new DateTime("16/6/2024", "1000"),
                    "Corporate tea break", 150.0, null, true),
            new Delivery(13, findByName(persons, "Alice Tan"), new DateTime("17/6/2024", "1130"),
                    "Gluten-free muffins", 34.0, null, false),
            new Delivery(14, findByName(persons, "Ben Lim"), new DateTime("18/6/2024", "1530"),
                    "Snack cones x20", 40.0, null, false),
            new Delivery(15, findByName(persons, "Chloe Ng"), new DateTime("19/6/2024", "1215"),
                    "Office pantry restock", 95.0, null, false)
        };
    }


    /** Helper: find a Person by display name from your sample array. */
    private static Person findByName(Person[] persons, String fullName) {
        for (Person p : persons) {
            if (p.getName().fullName.equals(fullName)) {
                return p;
            }
        }
        throw new IllegalArgumentException("No sample Person named: " + fullName);
    }

    public static ReadOnlyFoodBook getSampleFoodBook() {
        FoodBook sampleFoodBook = new FoodBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleFoodBook.addPerson(samplePerson);
        }
        for (Delivery sampleDelivery : getSampleDeliveries()) {
            sampleFoodBook.addDelivery(sampleDelivery);
        }
        return sampleFoodBook;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
