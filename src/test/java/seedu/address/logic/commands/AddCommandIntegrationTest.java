package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalFoodBook.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.delivery.Delivery;
import seedu.address.model.person.Person;
import seedu.address.testutil.DeliveryBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Person validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        assertCommandSuccess(new AddClientCommand(validPerson), model,
                String.format(AddClientCommand.MESSAGE_SUCCESS, Messages.format(validPerson)),
                expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        assertCommandFailure(new AddClientCommand(personInList), model,
                AddClientCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_newDelivery_success() {
        Delivery validDelivery = new DeliveryBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addDelivery(validDelivery);

        assertCommandSuccess(new AddDeliveryCommand(validDelivery),
                model,
                String.format(AddDeliveryCommand.MESSAGE_SUCCESS, Messages.format(validDelivery)),
                expectedModel);
    }

    @Test
    public void execute_duplicateDelivery_throwsCommandException() {
        Delivery deliveryInList = model.getAddressBook().getDeliveryList().get(0);
        assertCommandFailure(new AddDeliveryCommand(deliveryInList),
                model,
                AddDeliveryCommand.MESSAGE_DUPLICATE_DELIVERY);
    }

}
