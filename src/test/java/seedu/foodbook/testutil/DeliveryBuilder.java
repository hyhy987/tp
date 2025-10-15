package seedu.foodbook.testutil;

import seedu.foodbook.model.delivery.DateTime;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;

/**
 * A utility class to help with building Delivery objects.
 */
public class DeliveryBuilder {

    // We do not want the base delivery built to clash with any other deliveries (other than itself)
    // Since -1 will never be used as an id in practice, we use it here instead
    public static final int DEFAULT_ID = -1;

    public static final String DEFAULT_CLIENT = PersonBuilder.DEFAULT_NAME;
    public static final String DEFAULT_DATESTRING = "21/10/2003";
    public static final String DEFAULT_TIMESTRING = "2359";

    public static final String DEFAULT_REMARKS = "NIL";
    public static final Double DEFAULT_COST = 0.00;
    public static final String DEFAULT_TAG = null;

    private int id;
    private Person client;
    private DateTime datetime;
    private String remarks;
    private Double cost;
    private String tag;

    private boolean isDelivered;

    /**
     * Creates a {@code DeliveryBuilder} with the default details.
     */
    public DeliveryBuilder() {
        id = DEFAULT_ID;
        client = new PersonBuilder().withName(DEFAULT_CLIENT).build();
        datetime = new DateTime(DEFAULT_DATESTRING, DEFAULT_TIMESTRING);
        remarks = DEFAULT_REMARKS;
        cost = DEFAULT_COST;
        tag = DEFAULT_TAG;

        isDelivered = false;
    }

    /**
     * Initializes the DeliveryBuilder with the data of {@code deliveryToCopy}.
     */
    public DeliveryBuilder(Delivery deliveryToCopy) {
        id = deliveryToCopy.getId();
        client = deliveryToCopy.getClient();
        datetime = deliveryToCopy.getDeliveryDate();
        remarks = deliveryToCopy.getRemarks();
        cost = deliveryToCopy.getCost();
        tag = deliveryToCopy.getTag();

        isDelivered = deliveryToCopy.getStatus();
    }

    /**
     * Sets the {@code id} of the {@code Delivery} that we are building.
     */
    public DeliveryBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the {@code client} of the {@code Delivery} that we are building.
     */
    public DeliveryBuilder withClient(Person client) {
        this.client = client;
        return this;
    }

    /**
     * Sets the {@code datetime} of the {@code Delivery} that we are building.
     */
    public DeliveryBuilder withDateTime(String dateString, String timeString) {
        this.datetime = new DateTime(dateString, timeString);
        return this;
    }

    /**
     * Sets the {@code remarks} of the {@code Delivery} that we are building.
     */
    public DeliveryBuilder withRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    /**
     * Sets the {@code cost} of the {@code Delivery} that we are building.
     */
    public DeliveryBuilder withCost(Double cost) {
        this.cost = cost;
        return this;
    }

    /**
     * Sets the {@code tag} of the {@code Delivery} that we are building.
     */
    public DeliveryBuilder withTag(String tag) {
        this.tag = tag;
        return this;
    }

    /**
     * Sets the {@code isDelivered} status of the {@code Delivery} that we are building to be true.
     */
    public DeliveryBuilder asDelivered() {
        this.isDelivered = true;
        return this;
    }

    /**
     * Build the delivery object with the specificed params.
     * @return The constructed delivery object.
     */
    public Delivery build() {
        Delivery delivery = new Delivery(id, client, datetime, remarks, cost, tag);

        if (this.isDelivered) {
            delivery.markAsDelivered();
        }
        return delivery;
    }

}
