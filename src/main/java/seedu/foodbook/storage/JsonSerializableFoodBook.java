package seedu.foodbook.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.foodbook.commons.exceptions.IllegalValueException;
import seedu.foodbook.model.FoodBook;
import seedu.foodbook.model.ReadOnlyFoodBook;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.model.person.exceptions.DuplicatePersonException;

/**
 * An Immutable FoodBook that is serializable to JSON format.
 */
@JsonRootName(value = "foodbook")
class JsonSerializableFoodBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_DELIVERY = "Deliveries list contains duplicate delivery(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedDelivery> deliveries = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableFoodBook} with the given persons and deliveries.
     *
     * @param persons    List of adapted persons.
     * @param deliveries List of adapted deliveries.
     */
    @JsonCreator
    public JsonSerializableFoodBook(
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
     * Converts a given {@code ReadOnlyFoodBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableFoodBook}.
     */
    public JsonSerializableFoodBook(ReadOnlyFoodBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
        deliveries.addAll(source.getDeliveryList().stream().map(JsonAdaptedDelivery::new).collect(Collectors.toList()));
    }

    /** Creates an empty {@code JsonSerializableFoodBook}. */
    public JsonSerializableFoodBook() {
        // persons and deliveries are already initialized to empty lists
    }

    /**
     * Converts this JSON-friendly food book into the model's {@code FoodBook} object.
     *
     * @return The populated FoodBook.
     * @throws IllegalValueException If there are duplicate persons or deliveries,
     *                               or any data constraints are violated.
     */
    public FoodBook toModelType() throws IllegalValueException {
        FoodBook foodBook = new FoodBook();
        for (JsonAdaptedPerson jsonPerson : persons) {
            Person person = jsonPerson.toModelType();
            try {
                foodBook.addPerson(person);
            } catch (DuplicatePersonException e) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
        }
        for (JsonAdaptedDelivery jsonDelivery : deliveries) {
            Delivery delivery = jsonDelivery.toModelType(foodBook);
            if (foodBook.hasDelivery(delivery)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_DELIVERY);
            }
            foodBook.addDelivery(delivery);
        }
        return foodBook;
    }

}
