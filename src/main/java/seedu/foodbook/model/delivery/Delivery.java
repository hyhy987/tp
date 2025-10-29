package seedu.foodbook.model.delivery;

import static seedu.foodbook.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.Optional;

import seedu.foodbook.commons.util.ToStringBuilder;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.model.tag.DeliveryTag;

/**
 * Represents a Delivery in the food book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Delivery {

    // Identity fields
    private final Integer id;
    private final Boolean isDelivered;
    private final Person client;
    private final DateTime datetime;
    private final String remarks;
    private final Double cost;

    //Optional delivery tag
    private final Optional<DeliveryTag> tag;

    /**
     * Every field must be present and not null.
     */
    public Delivery(Integer id, Person client, DateTime datetime,
                  String remarks, Double cost, Optional<DeliveryTag> tag, Boolean isDelivered) {
        requireAllNonNull(id, client, datetime, remarks, cost, isDelivered);
        this.id = id;
        this.client = client;
        this.datetime = datetime;
        this.remarks = remarks;
        this.cost = cost;
        if (tag == null) {
            this.tag = Optional.empty();
        } else {
            this.tag = tag;
        }
        this.isDelivered = isDelivered;
    }

    public Delivery(Integer id, Person client, DateTime datetime,
                    String remarks, Double cost, Optional<DeliveryTag> tag) {
        this(id, client, datetime, remarks, cost, tag, false);
    }

    public Integer getId() {
        return this.id;
    }

    public boolean getStatus() {
        return this.isDelivered;
    }

    /**
     * Returns a copy of this delivery, marked as delivered
     * @return A copy of this delivery, marked as delivered
     */
    public Delivery copyAsDelivered() {
        return new Delivery(
                this.getId(),
                this.getClient(),
                this.getDeliveryDate(),
                this.getRemarks(),
                this.getCost(),
                this.getTag(),
                true
        );
    }

    /**
     * Returns a copy of this delivery, unmarked as delivered
     * @return A copy of this delivery, unmarked as delivered
     */
    public Delivery copyAsUndelivered() {
        return new Delivery(
                this.getId(),
                this.getClient(),
                this.getDeliveryDate(),
                this.getRemarks(),
                this.getCost(),
                this.getTag()
        );
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

    public Optional<DeliveryTag> getTag() {
        return this.tag;
    }

    /**
     * Returns true if both deliveries have the same id.
     * This defines a weaker notion of equality between two deliveries.
     */
    public boolean isSameDelivery(Delivery otherDelivery) {
        if (otherDelivery == this) {
            return true;
        }

        return otherDelivery != null
                && otherDelivery.getId().equals(getId());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Delivery)) {
            return false;
        }

        Delivery otherDelivery = (Delivery) other;
        return id.equals(otherDelivery.id)
                && isDelivered.equals(otherDelivery.isDelivered)
                && client.equals(otherDelivery.client)
                && datetime.equals(otherDelivery.datetime)
                && remarks.equals(otherDelivery.remarks)
                && cost.equals(otherDelivery.cost)
                && Objects.equals(tag, otherDelivery.tag);
    }

    /**
     * Returns copy of this delivery with the new client
     * @param newClient The new client to replace the current client
     * @return A copy of this delivery with the new client
     */
    public Delivery copyWithNewClient(Person newClient) {
        return new Delivery(
                this.getId(),
                newClient,
                this.getDeliveryDate(),
                this.getRemarks(),
                this.getCost(),
                this.getTag(),
                this.getStatus());
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
                .add("tag", tag.map(DeliveryTag::getName).orElse("(none)"))
                .toString();
    }


}
