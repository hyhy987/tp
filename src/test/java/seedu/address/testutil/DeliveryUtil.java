package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARKS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import seedu.address.logic.commands.AddDeliveryCommand;
import seedu.address.model.delivery.Delivery;

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
