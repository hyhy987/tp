package seedu.foodbook.model.delivery;

import static seedu.foodbook.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.foodbook.commons.util.ToStringBuilder;
import seedu.foodbook.model.person.Person;

/**
 * Represents a Delivery in the food book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Delivery {

    // Identity fields
    private final Integer id;

    private boolean isDelivered;
    private final Person client;

    private final DateTime datetime;

    private final String remarks;

    private final Double cost;

    /**
     * Every field must be present and not null.
     */
    public Delivery(Integer id, Person client, DateTime datetime,
                  String remarks, Double cost) {
        requireAllNonNull(id, client, datetime, remarks, cost);
        this.id = id;
        this.client = client;
        this.datetime = datetime;
        this.remarks = remarks;
        this.cost = cost;

        this.isDelivered = false;
    }

    public Integer getId() {
        return this.id;
    }

    public boolean getStatus() {
        return this.isDelivered;
    }

    public void markAsDelivered() {
        this.isDelivered = true;
    }

    public void unmarkAsDelivered() {
        this.isDelivered = false;
    }

    public Person getClient() {
        return this.client;
    }

    public DateTime getDeliveryDate() {
        return this.datetime;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public Double getCost() {
        return this.cost;
    }

    /**
     * Returns true if both deliveries have the same id.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Delivery)) {
            return false;
        }

        return this.id.equals(((Delivery) other).id);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("id", id)
                .add("client", client.toString())
                .add("datetime", datetime.toString())
                .add("remarks", remarks)
                .add("cost", cost)
                .toString();
    }


}
