package seedu.foodbook.logic.commands;

import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.foodbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.foodbook.testutil.TypicalFoodBook.getTypicalFoodBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.foodbook.logic.Messages;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.ModelManager;
import seedu.foodbook.model.UserPrefs;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.testutil.DeliveryBuilder;
import seedu.foodbook.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalFoodBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Person validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getFoodBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        assertCommandSuccess(new AddClientCommand(validPerson), model,
                String.format(AddClientCommand.MESSAGE_SUCCESS, Messages.format(validPerson)),
                expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getFoodBook().getPersonList().get(0);
        assertCommandFailure(new AddClientCommand(personInList), model,
                AddClientCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_newDelivery_success() {
        Delivery validDelivery = new DeliveryBuilder().build();

        Model expectedModel = new ModelManager(model.getFoodBook(), new UserPrefs());
        expectedModel.addDelivery(validDelivery);

        assertCommandSuccess(new AddDeliveryCommand(validDelivery),
                model,
                String.format(AddDeliveryCommand.MESSAGE_SUCCESS, Messages.format(validDelivery)),
                expectedModel);
    }

    @Test
    public void execute_duplicateDelivery_throwsCommandException() {
        Delivery deliveryInList = model.getFoodBook().getDeliveryList().get(0);
        assertCommandFailure(new AddDeliveryCommand(deliveryInList),
                model,
                AddDeliveryCommand.MESSAGE_DUPLICATE_DELIVERY);
    }

}
