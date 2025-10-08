package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.delivery.Delivery;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_DELIVERY = "Deliveries list contains duplicate delivery(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedDelivery> deliveries = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons and deliveries.
     *
     * @param persons    List of adapted persons.
     * @param deliveries List of adapted deliveries.
     */
    @JsonCreator
    public JsonSerializableAddressBook(
            @JsonProperty("persons") List<JsonAdaptedPerson> persons,
            @JsonProperty("deliveries") List<JsonAdaptedDelivery> deliveries) {
        if (persons != null) {
            this.persons.addAll(persons);
        }
        if (deliveries != null) {
            this.deliveries.addAll(deliveries);
        }
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
    }

    /** Creates an empty {@code JsonSerializableAddressBook}. */
    public JsonSerializableAddressBook() {
        // persons and deliveries are already initialized to empty lists
    }

    /**
     * Converts this JSON-friendly address book into the model's {@code AddressBook} object.
     *
     * @return The populated AddressBook.
     * @throws IllegalValueException If there are duplicate persons or deliveries,
     *                               or any data constraints are violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedPerson jsonPerson : persons) {
            Person person = jsonPerson.toModelType();
            try {
                addressBook.addPerson(person);
            } catch (DuplicatePersonException e) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
        }
        for (JsonAdaptedDelivery jsonDelivery : deliveries) {
            Delivery delivery = jsonDelivery.toModelType(addressBook);
            if (addressBook.hasDelivery(delivery)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_DELIVERY);
            }
            addressBook.addDelivery(delivery);
        }
        return addressBook;
    }

}
