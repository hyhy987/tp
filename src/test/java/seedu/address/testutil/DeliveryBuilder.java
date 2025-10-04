package seedu.address.testutil;

import seedu.address.model.delivery.DateTime;
import seedu.address.model.delivery.Delivery;
import seedu.address.model.person.Person;

/**
 * A utility class to help with building Delivery objects.
 */
public class DeliveryBuilder {

    public static final int DEFAULT_ID = 0;

    public static final String DEFAULT_CLIENT = PersonBuilder.DEFAULT_NAME;
    public static final String DEFAULT_DATESTRING = "21/10/2003";
    public static final String DEFAULT_TIMESTRING = "2359";

    public static final String DEFAULT_REMARKS = "NIL";
    public static final Double DEFAULT_COST = 0.00;

    private int id;
    private Person client;
    private DateTime datetime;
    private String remarks;
    private Double cost;

    /**
     * Creates a {@code DeliveryBuilder} with the default details.
     */
    public DeliveryBuilder() {
        id = DEFAULT_ID;
        client = new PersonBuilder().withName(DEFAULT_CLIENT).build();
        datetime = new DateTime(DEFAULT_DATESTRING, DEFAULT_TIMESTRING);
        remarks = DEFAULT_REMARKS;
        cost = DEFAULT_COST;
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
    public DeliveryBuilder withCliet(Person client) {
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

    public Delivery build() {
        return new Delivery(id, client, datetime, remarks, cost);
    }

}
