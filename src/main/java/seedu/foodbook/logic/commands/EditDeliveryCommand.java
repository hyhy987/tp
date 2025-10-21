package seedu.foodbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.foodbook.logic.commands.AddDeliveryCommand.MESSAGE_CLIENT_NOT_FOUND;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_REMARKS;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.foodbook.model.Model.PREDICATE_SHOW_ALL_DELIVERIES;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.foodbook.commons.util.CollectionUtil;
import seedu.foodbook.commons.util.ToStringBuilder;
import seedu.foodbook.logic.Messages;
import seedu.foodbook.logic.commands.exceptions.CommandException;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.delivery.DateTime;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Name;
import seedu.foodbook.model.person.Person;

/**
 * Edits the details of an existing delivery in the address book.
 */
public class EditDeliveryCommand extends Command {

    public static final String COMMAND_WORD = "edit_delivery";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the delivery identified "
            + "by the delivery ID shown in the delivery list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: DELIVERY_ID (must be a positive integer) "
            + "[" + PREFIX_NAME + "CLIENT_NAME] "
            + "[" + PREFIX_DATE + "DATE] "
            + "[" + PREFIX_TIME + "TIME] "
            + "[" + PREFIX_REMARKS + "REMARKS] "
            + "[" + PREFIX_COST + "COST]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "Jane Doe "
            + PREFIX_DATE + "26/12/2024 "
            + PREFIX_TIME + "1500 "
            + PREFIX_REMARKS + "Updated delivery "
            + PREFIX_COST + "60.00";

    public static final String MESSAGE_EDIT_DELIVERY_SUCCESS = "Edited Delivery: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_DELIVERY = "No change was made to delivery.";
    public static final String MESSAGE_DELIVERY_NOT_FOUND = "No delivery found with ID: %1$s";
    public static final String MESSAGE_INVALID_COST = "Cost must be non-negative";

    private final Integer deliveryId;
    private final EditDeliveryDescriptor editDeliveryDescriptor;

    /**
     * @param deliveryId of the delivery to edit
     * @param editDeliveryDescriptor details to edit the delivery with
     */
    public EditDeliveryCommand(Integer deliveryId, EditDeliveryDescriptor editDeliveryDescriptor) {
        requireNonNull(deliveryId);
        requireNonNull(editDeliveryDescriptor);

        this.deliveryId = deliveryId;
        this.editDeliveryDescriptor = new EditDeliveryDescriptor(editDeliveryDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!editDeliveryDescriptor.isAnyFieldEdited()) {
            throw new CommandException(MESSAGE_NOT_EDITED);
        }

        Optional<Delivery> maybeDelivery = model.getDeliveryById(deliveryId);

        if (maybeDelivery.isEmpty()) {
            throw new CommandException(
                    String.format(MESSAGE_DELIVERY_NOT_FOUND, deliveryId));
        }

        Delivery oldDelivery = maybeDelivery.get();

        Delivery newDelivery = createEditedDelivery(oldDelivery, editDeliveryDescriptor, model);

        if (!oldDelivery.equals(newDelivery) && model.hasDelivery(newDelivery )) {
            throw new CommandException(MESSAGE_DUPLICATE_DELIVERY);
        }

        this.checkpoint(model, CommandResult.UiPanel.DELIVERIES);
        model.setDelivery(oldDelivery, newDelivery);

        return new CommandResult(String.format(MESSAGE_EDIT_DELIVERY_SUCCESS, Messages.format(newDelivery)),
                CommandResult.UiPanel.DELIVERIES);
    }

    /**
     * Creates and returns a {@code Delivery} with the details of {@code deliveryToEdit}
     * edited with {@code editDeliveryDescriptor}.
     */
    private static Delivery createEditedDelivery(Delivery deliveryToEdit,
            EditDeliveryDescriptor editDeliveryDescriptor, Model model) throws CommandException {
        assert deliveryToEdit != null;

        // Get new client if name is being changed
        Person updatedClient = deliveryToEdit.getClient();
        if (editDeliveryDescriptor.getClientName().isPresent()) {
            Name newClientName = new Name(editDeliveryDescriptor.getClientName().get());
            Optional<Person> maybePerson = model.getPersonByName(newClientName);

            if (maybePerson.isEmpty()) {
                throw new CommandException(
                        String.format(MESSAGE_CLIENT_NOT_FOUND, newClientName));
            }

            updatedClient = maybePerson.get();
        }

        DateTime updatedDateTime = editDeliveryDescriptor.getDateTime().orElse(deliveryToEdit.getDeliveryDate());
        String updatedRemarks = editDeliveryDescriptor.getRemarks().orElse(deliveryToEdit.getRemarks());
        Double updatedCost = editDeliveryDescriptor.getCost().orElse(deliveryToEdit.getCost());

        if (updatedCost < 0) {
            throw new CommandException(MESSAGE_INVALID_COST);
        }

        boolean wasDelivered = deliveryToEdit.getStatus();

        Delivery editedDelivery = new Delivery(deliveryToEdit.getId(), updatedClient,
                updatedDateTime, updatedRemarks, updatedCost, deliveryToEdit.getTag(), wasDelivered);

        return editedDelivery;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditDeliveryCommand)) {
            return false;
        }

        EditDeliveryCommand otherEditCommand = (EditDeliveryCommand) other;
        return deliveryId.equals(otherEditCommand.deliveryId)
                && editDeliveryDescriptor.equals(otherEditCommand.editDeliveryDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("deliveryId", deliveryId)
                .add("editDeliveryDescriptor", editDeliveryDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the delivery with. Each non-empty field value will replace the
     * corresponding field value of the delivery.
     */
    public static class EditDeliveryDescriptor {
        private String clientName;
        private DateTime dateTime;
        private String remarks;
        private Double cost;

        public EditDeliveryDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditDeliveryDescriptor(EditDeliveryDescriptor toCopy) {
            setClientName(toCopy.clientName);
            setDateTime(toCopy.dateTime);
            setRemarks(toCopy.remarks);
            setCost(toCopy.cost);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(clientName, dateTime, remarks, cost);
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

        public Optional<String> getClientName() {
            return Optional.ofNullable(clientName);
        }

        public void setDateTime(DateTime dateTime) {
            this.dateTime = dateTime;
        }

        public Optional<DateTime> getDateTime() {
            return Optional.ofNullable(dateTime);
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public Optional<String> getRemarks() {
            return Optional.ofNullable(remarks);
        }

        public void setCost(Double cost) {
            this.cost = cost;
        }

        public Optional<Double> getCost() {
            return Optional.ofNullable(cost);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditDeliveryDescriptor)) {
                return false;
            }

            EditDeliveryDescriptor otherEditDeliveryDescriptor = (EditDeliveryDescriptor) other;
            return Objects.equals(clientName, otherEditDeliveryDescriptor.clientName)
                    && Objects.equals(dateTime, otherEditDeliveryDescriptor.dateTime)
                    && Objects.equals(remarks, otherEditDeliveryDescriptor.remarks)
                    && Objects.equals(cost, otherEditDeliveryDescriptor.cost);
        }

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
}
