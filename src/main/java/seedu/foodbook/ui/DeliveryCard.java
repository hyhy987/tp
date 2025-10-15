package seedu.foodbook.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.foodbook.model.delivery.Delivery;

/**
 * An UI component that displays information of a {@code Delivery}.
 */
public class DeliveryCard extends UiPart<Region> {
    private static final String FXML = "DeliveryListCard.fxml";

    public final Delivery delivery;

    @FXML private HBox cardPane;
    @FXML private Label id;
    @FXML private Label client;
    @FXML private Label deliveryId;
    @FXML private Label datetime;
    @FXML private Label remarks;
    @FXML private Label cost;
    @FXML private Label tagLabel;
    @FXML private Label delivered;

    /**
     * Creates a {@code DeliveryCard} with the given {@code Delivery} and index to display.
     */
    public DeliveryCard(Delivery delivery, int displayedIndex) {
        super(FXML);
        this.delivery = delivery;
        id.setText(displayedIndex + ". ");
        client.setText(delivery.getClient().getName().toString());

        deliveryId.setText("Delivery ID: " + delivery.getId());
        datetime.setText("Delivery Date: " + delivery.getDeliveryDate().getDateString()
                + "\n" + "Delivery Time: " + delivery.getDeliveryDate().getTimeString());
        remarks.setText("Remarks: " + delivery.getRemarks());
        cost.setText("Cost: $" + delivery.getCost().toString());
        delivered.setText("Delivered: " + (delivery.getStatus() ? "True" : "False"));

        String tag = delivery.getTag();
        if (tag != null) {
            tagLabel.setText(tag);

            // Reset any previous styling before setting the new one
            tagLabel.getStyleClass().removeAll("tag-personal", "tag-corporate", "tag-generic");

            switch (delivery.getTagKind()) {
            case PERSONAL:
                tagLabel.getStyleClass().add("tag-personal");
                break;
            case CORPORATE:
                tagLabel.getStyleClass().add("tag-corporate");
                break;
            default:
                tagLabel.getStyleClass().add("tag-generic");
                break;
            }

            // Show the pill and let layout allocate space for it
            tagLabel.setManaged(true);
            tagLabel.setVisible(true);
        } else {
            // Hide and remove from layout flow when there’s no tag
            tagLabel.setManaged(false);
            tagLabel.setVisible(false);
        }

    }
}
