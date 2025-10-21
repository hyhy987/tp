package seedu.foodbook.ui;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
    @FXML private TextFlow deliveryId;
    @FXML private TextFlow datetime;
    @FXML private TextFlow remarks;
    @FXML private Label cost;
    @FXML private Label tagLabel;
    @FXML private Label delivered;
    @FXML private CheckBox deliveredCheckBox;

    /**
     * Creates a {@code DeliveryCard} with the given {@code Delivery} and index to display.
     */
    public DeliveryCard(Delivery delivery, int displayedIndex) {
        super(FXML);
        this.delivery = delivery;
        client.setText(delivery.getClient().getName().toString());

        deliveryId.getChildren().clear();
        datetime.getChildren().clear();
        remarks.getChildren().clear();

        // Delivery ID
        Text deliveryIdHeader = new Text("Delivery ID: \n");
        deliveryIdHeader.setStyle("-fx-font-weight: bold;");
        deliveryId.getChildren().addAll(deliveryIdHeader, new Text(delivery.getId().toString()));

        // Datetime
        Text datetimeHeader = new Text("Delivery Date: \n");
        datetimeHeader.setStyle("-fx-font-weight: bold;");

        String dateTimeString = delivery.getDeliveryDate().getDateString()
                + " "
                + delivery.getDeliveryDate().getTimeString()
                + "hrs";
        Text datetimeValue = new Text(dateTimeString);

        datetime.getChildren().addAll(datetimeHeader, datetimeValue);

        // Remarks
        Text remarksHeader = new Text("Remarks: \n");
        remarksHeader.setStyle("-fx-font-weight: bold;");
        remarks.getChildren().addAll(remarksHeader, new Text(delivery.getRemarks()));


        cost.setText("Cost: $" + delivery.getCost().toString());
        deliveredCheckBox.setSelected(delivery.getStatus());

        String tag = delivery.getTag();
        if (tag == null || tag.isBlank()) {
            tagLabel.setVisible(false);
            tagLabel.setManaged(false);
        } else {
            tagLabel.setVisible(true);
            tagLabel.setManaged(true);
            tagLabel.setText(tag);

            tagLabel.getStyleClass().removeAll("personal", "corporate", "other");

            String variant;
            switch (tag.trim().toLowerCase()) {
            case "personal":
                variant = "personal";
                break;
            case "corporate":
                variant = "corporate";
                break;
            default:
                variant = "other";
                break;
            }

            tagLabel.getStyleClass().add(variant);
        }
    }
}
