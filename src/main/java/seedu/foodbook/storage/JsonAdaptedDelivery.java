package seedu.foodbook.storage;

import java.time.format.DateTimeParseException;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.foodbook.commons.exceptions.IllegalValueException;
import seedu.foodbook.model.ReadOnlyFoodBook;
import seedu.foodbook.model.delivery.DateTime;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.model.tag.DeliveryTag;

/**
 * JSON representation of a {@link Delivery}.
 * <p>Backwards compatible: older saves without {@code tag} will deserialize with {@code tag == null}.</p>
 */
public class JsonAdaptedDelivery {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Delivery's %s field is missing!";

    private final Integer id;
    private final String clientName;
    private final String date;
    private final String time;
    private final String remarks;
    private final Double cost;
    private final Boolean isDelivered;
    private final String tag;

    /**
     * Constructs a {@code JsonAdaptedDelivery} with the given delivery details.
     *
     * @param id           The unique id of the delivery.
     * @param clientName   The full name of the linked client.
     * @param date         The date string in d/M/yyyy format.
     * @param time         The time string in HHmm format.
     * @param remarks      Additional notes for the delivery.
     * @param cost         The cost of the delivery.
     * @param isDelivered  Delivery completion status.
     * @param tag          Optional tag string.
     */
    @JsonCreator
    public JsonAdaptedDelivery(
            @JsonProperty("id") Integer id,
            @JsonProperty("clientName") String clientName,
            @JsonProperty("date") String date,
            @JsonProperty("time") String time,
            @JsonProperty("remarks") String remarks,
            @JsonProperty("cost") Double cost,
            @JsonProperty("isDelivered") Boolean isDelivered,
            @JsonProperty("tag") String tag) {
        this.id = id;
        this.clientName = clientName;
        this.date = date;
        this.time = time;
        this.remarks = remarks;
        this.cost = cost;
        this.isDelivered = isDelivered;
        this.tag = tag;
    }

    /**
     * Converts a given {@code Delivery} into this class for Jackson use.
     *
     * @param source The delivery to adapt.
     */
    public JsonAdaptedDelivery(Delivery source) {
        id = source.getId();
        clientName = source.getClient().getName().fullName;
        date = source.getDeliveryDate().getDateString();
        time = source.getDeliveryDate().getTimeString();
        remarks = source.getRemarks();
        cost = source.getCost();
        tag = source.getTag().map(DeliveryTag::getName).orElse(null);
        isDelivered = source.getStatus();
    }

    /**
     * Converts this Jackson-friendly adapted delivery object into the model's {@code Delivery} object.
     *
     * @param foodBook The food book providing the client list for lookup.
     * @return The model Delivery object.
     * @throws IllegalValueException If any field is missing or invalid.
     */
    public Delivery toModelType(ReadOnlyFoodBook foodBook) throws IllegalValueException {
        if (id == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "id"));
        }
        if (clientName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "clientName"));
        }
        Person client = foodBook.getPersonList().stream()
                .filter(p -> p.getName().fullName.equals(clientName))
                .findFirst()
                .orElseThrow(() -> new IllegalValueException("Client not found: " + clientName));

        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "date"));
        }
        if (time == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "time"));
        }
        DateTime dateTime;
        try {
            dateTime = new DateTime(date, time);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            throw new IllegalValueException(DateTime.MESSAGE_CONSTRAINTS);
        }

        if (remarks == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "remarks"));
        }
        if (cost == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "cost"));
        }
        if (isDelivered == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "isDelivered"));
        }

        Optional<DeliveryTag> modelTag;
        if (tag == null || tag.isBlank()) {
            modelTag = Optional.empty();
        } else {
            modelTag = Optional.of(new DeliveryTag(tag.trim()));
        }

        Delivery delivery = new Delivery(id, client, dateTime, remarks, cost, modelTag, isDelivered);

        return delivery;
    }
}
