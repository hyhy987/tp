package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARKS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.delivery.DateTime;
import seedu.address.model.delivery.Delivery;
import seedu.address.model.person.Person;

/**
 * Adds a delivery to the food book.
 */
public class AddDeliveryCommand extends Command {

    /** The command word that users type to execute this command. */
    public static final String COMMAND_WORD = "add_delivery";

    /** Usage message showing the correct format for this command. */
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a delivery to the food book. "
            + "Parameters: "
            + PREFIX_NAME + "CLIENT_NAME "
            + PREFIX_DATE + "DATE "
            + PREFIX_TIME + "TIME "
            + PREFIX_REMARKS + "REMARKS "
            + PREFIX_COST + "COST\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_DATE + "25/12/2024 "
            + PREFIX_TIME + "1430 "
            + PREFIX_REMARKS + "Birthday cake delivery "
            + PREFIX_COST + "50.00";

    /** Success message template when a delivery is successfully added. */
    public static final String MESSAGE_SUCCESS = "New delivery added: %1$s";
    public static final String MESSAGE_DUPLICATE_DELIVERY = "This delivery already exists in the address book";

    /** Error message template when the specified client is not found. */
    public static final String MESSAGE_CLIENT_NOT_FOUND = "Client not found: %1$s";

    /** The name of the client for this delivery. */
    private final String clientName;

    /** The date and time for this delivery. */
    private final DateTime dateTime;

    /** Additional remarks or notes for this delivery. */
    private final String remarks;

    /** The cost/price of this delivery. */
    private final Double cost;

    // If delivery is provided directed via constructor
    // Mainly used for testing
    private Optional<Delivery> toAdd = Optional.empty();

    /**
     * Creates an AddDeliveryCommand to add a delivery with the specified details.
     *
     * @param clientName The name of the client for this delivery.
     * @param dateTime The date and time for this delivery.
     * @param remarks Additional remarks or notes for this delivery.
     * @param cost The cost/price of this delivery.
     * @throws NullPointerException If any parameter is null.
     */
    public AddDeliveryCommand(String clientName, DateTime dateTime, String remarks, Double cost) {
        requireNonNull(clientName);
        requireNonNull(dateTime);
        requireNonNull(remarks);
        requireNonNull(cost);

        this.clientName = clientName;
        this.dateTime = dateTime;
        this.remarks = remarks;
        this.cost = cost;
    }

    /**
     * Alternate constructor for AddDeliveryCommand to add a secific delivery.
     * Mainly used for testing
     *
     * @param toAdd The delivery to be added.
     * @throws NullPointerException If the delivery is null.
     */
    public AddDeliveryCommand(Delivery toAdd) {
        requireNonNull(toAdd);

        this.toAdd = Optional.of(toAdd);

        this.clientName = toAdd.getClient().getName().fullName;
        this.dateTime = toAdd.getDeliveryDate();
        this.remarks = toAdd.getRemarks();
        this.cost = toAdd.getCost();
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Delivery delivery;
        if (this.toAdd.isPresent()) {
            delivery = this.toAdd.get();
        } else {
            //Find the client by name
            Person client = model.getFilteredPersonList().stream()
                    .filter(person -> person.getName().fullName.equals(clientName))
                    .findFirst()
                    .orElseThrow(() -> new CommandException(
                            String.format(MESSAGE_CLIENT_NOT_FOUND, clientName)));

            //Generate unique ID
            Integer nextId = generateNextId(model);

            //Create delivery with the found client
            delivery = new Delivery(nextId, client, dateTime, remarks, cost);
        }

        if (model.hasDelivery(delivery)) {
            throw new CommandException(MESSAGE_DUPLICATE_DELIVERY);
        }

        model.addDelivery(delivery);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(delivery)),
                CommandResult.UiPanel.DELIVERIES);
    }

    /**
     * Generates the next unique ID for a new delivery.
     * The ID is calculated as the maximum existing delivery ID plus 1.
     * If no deliveries exist, returns 1.
     *
     * @param model The model containing existing deliveries.
     * @return The next unique delivery ID.
     */
    private Integer generateNextId(Model model) {
        return model.getFilteredDeliveryList().stream()
                .mapToInt(Delivery::getId)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Returns true if both commands have the same client name, date/time, remarks, and cost.
     * This defines a stronger notion of equality between two AddDeliveryCommands.
     *
     * @param other The object to compare with.
     * @return True if both commands are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddDeliveryCommand)) {
            return false;
        }

        AddDeliveryCommand otherCommand = (AddDeliveryCommand) other;
        return clientName.equals(otherCommand.clientName)
                && dateTime.equals(otherCommand.dateTime)
                && remarks.equals(otherCommand.remarks)
                && cost.equals(otherCommand.cost);
    }

    /**
     * Returns a string representation of this command for debugging purposes.
     *
     * @return A string containing all the command parameters.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("clientName", clientName)
                .add("dateTime", dateTime)
                .add("remarks", remarks)
                .add("cost", cost)
                .toString();
    }
}
