package seedu.foodbook.testutil;

import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_REMARKS;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_TIME;

import seedu.foodbook.logic.commands.AddDeliveryCommand;
import seedu.foodbook.model.delivery.Delivery;

/**
 * A utility class for Delivery.
 */
public class DeliveryUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Delivery delivery) {
        return AddDeliveryCommand.COMMAND_WORD + " " + getDeliveryDetails(delivery);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getDeliveryDetails(Delivery delivery) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + delivery.getClient().getName().fullName + " ");
        sb.append(PREFIX_DATE + delivery.getDeliveryDate().getDateString() + " ");
        sb.append(PREFIX_TIME + delivery.getDeliveryDate().getTimeString() + " ");
        sb.append(PREFIX_REMARKS + delivery.getRemarks() + " ");
        sb.append(PREFIX_COST + delivery.getCost().toString() + " ");
        return sb.toString();
    }

}
