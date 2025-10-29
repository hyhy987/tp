package seedu.foodbook.ui;

import static java.util.Objects.requireNonNull;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart<Region> {

    private static final String FXML = "ResultDisplay.fxml";

    @FXML
    private Label resultDisplay;

    /**
     * Constructs the {@code ResultDisplay} UI part by loading {@link #FXML} and
     * configuring the result display node to take up the full available width.
     * <p>
     * Setting {@code setMaxWidth(Double.MAX_VALUE)} allows the layout to give this
     * control as much horizontal space as possible, ensuring wrapped text lays out
     * correctly and the component can expand vertically with its content.
     * </p>
     */
    public ResultDisplay() {
        super(FXML);
        resultDisplay.setMaxWidth(Double.MAX_VALUE);
    }

    public void setFeedbackToUser(String feedbackToUser) {
        requireNonNull(feedbackToUser);
        resultDisplay.setText(feedbackToUser);
    }

}
