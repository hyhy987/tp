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
    private final Boolean isDelivered;
    private final Person client;
    private final DateTime datetime;
    private final String remarks;
    private final Double cost;

    //Optional delivery tag
    private final String tag;

    /**
     * Every field must be present and not null.
     */
    public Delivery(Integer id, Person client, DateTime datetime,
                  String remarks, Double cost, String tag, Boolean isDelivered) {
        requireAllNonNull(id, client, datetime, remarks, cost, isDelivered);
        this.id = id;
        this.client = client;
        this.datetime = datetime;
        this.remarks = remarks;
        this.cost = cost;
        if (tag == null || tag.isBlank()) {
            this.tag = null;
        } else {
            this.tag = tag;
        }

        this.isDelivered = isDelivered;
    }

    public Delivery(Integer id, Person client, DateTime datetime,
                    String remarks, Double cost, String tag) {
        this(id, client, datetime, remarks, cost, tag, false);
    }

    public Integer getId() {
        return this.id;
    }

    public boolean getStatus() {
        return this.isDelivered;
    }

    public Delivery markAsDelivered() {
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

    public Delivery unmarkAsDelivered() {
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

    public String getTag() {
        return this.tag;
    }

    /**
     * Classifies the current {@link #getTag()} value into a {@link TagKind} for UI coloring and logic.
     * <ul>
     *   <li>Returns {@link TagKind#PERSONAL} when tag equals "personal" (case-insensitive).</li>
     *   <li>Returns {@link TagKind#CORPORATE} when tag equals "corporate" (case-insensitive).</li>
     *   <li>Returns {@link TagKind#OTHER} for all other values, including {@code null}.</li>
     * </ul>
     *
     * @return corresponding {@link TagKind}.
     */
    public TagKind getTagKind() {
        if (tag == null) {
            return TagKind.OTHER;
        }
        String s = tag.trim().toLowerCase();
        if (s.equals("personal")) {
            return TagKind.PERSONAL;
        }
        if (s.equals("corporate")) {
            return TagKind.CORPORATE;
        }
        return TagKind.OTHER;
    }

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

        Delivery otherDelivery= (Delivery) other;
        return id.equals(otherDelivery.id)
                && isDelivered.equals(otherDelivery.isDelivered)
                && client.equals(otherDelivery.client)
                && datetime.equals(otherDelivery.datetime)
                && remarks.equals(otherDelivery.remarks)
                && cost.equals(otherDelivery.cost)
                && Objects.equals(tag, otherDelivery.tag);
    }

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
                .add("tag", tag == null ? "(none)" : tag)
                .toString();
    }


}
