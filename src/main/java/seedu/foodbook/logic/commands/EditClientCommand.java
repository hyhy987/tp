package seedu.foodbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.foodbook.logic.commands.AddDeliveryCommand.MESSAGE_CLIENT_NOT_FOUND;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.foodbook.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.foodbook.commons.util.CollectionUtil;
import seedu.foodbook.commons.util.ToStringBuilder;
import seedu.foodbook.logic.Messages;
import seedu.foodbook.logic.commands.exceptions.CommandException;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Address;
import seedu.foodbook.model.person.Email;
import seedu.foodbook.model.person.Name;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.model.person.Phone;
import seedu.foodbook.model.tag.Tag;

/**
 * Edits the details of an existing client in the food book.
 */
public class EditClientCommand extends Command {

    public static final String COMMAND_WORD = "edit_client";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the name used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: OLD_NAME (must be an existing person name in our FoodBook"
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the food book.";

    private final Name currentClientName;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param currentClientName of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditClientCommand(Name currentClientName, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(currentClientName);
        requireNonNull(editPersonDescriptor);

        this.currentClientName = currentClientName;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Optional<Person> maybePerson = model.getPersonByName(currentClientName);

        if (maybePerson.isEmpty()) {
            throw new CommandException(
                    String.format(MESSAGE_CLIENT_NOT_FOUND, currentClientName)
            );
        }

        Person oldClient = maybePerson.get();

        Person newClient = createEditedPerson(oldClient, editPersonDescriptor);

        if (!newClient.isSamePerson(oldClient) && model.hasPerson(newClient)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        List<Delivery> deliveryList = model.getDeliveriesByClientName(currentClientName);

        model.checkpoint(COMMAND_WORD, CommandResult.UiPanel.PERSONS);

        model.setPerson(oldClient, newClient);
        deliveryList.forEach(oldDelivery -> {
            Delivery updatedDelivery = oldDelivery.copyWithNewClient(newClient);
            model.setDelivery(oldDelivery, updatedDelivery);
        });

        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(newClient)),
                CommandResult.UiPanel.PERSONS);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditClientCommand)) {
            return false;
        }

        EditClientCommand otherEditCommand = (EditClientCommand) other;
        return currentClientName.equals(otherEditCommand.currentClientName)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("currentClientName", currentClientName)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("tags", tags)
                    .toString();
        }
    }
}
