package seedu.foodbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.foodbook.testutil.Assert.assertThrows;
import static seedu.foodbook.testutil.TypicalDeliveries.ALICE_DELIVERY;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.foodbook.commons.core.GuiSettings;
import seedu.foodbook.logic.Messages;
import seedu.foodbook.logic.commands.exceptions.CommandException;
import seedu.foodbook.model.FoodBook;
import seedu.foodbook.model.Model;
import seedu.foodbook.model.ReadOnlyFoodBook;
import seedu.foodbook.model.ReadOnlyUserPrefs;
import seedu.foodbook.model.delivery.DateTime;
import seedu.foodbook.model.delivery.Delivery;
import seedu.foodbook.model.person.Person;
import seedu.foodbook.testutil.DeliveryBuilder;

public class AddDeliveryCommandTest {

    @Test
    public void constructor_nullDelivery_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddDeliveryCommand(null));
    }

    @Test
    public void execute_deliveryAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingDeliveryAdded modelStub = new ModelStubAcceptingDeliveryAdded();
        Delivery validDelivery = new DeliveryBuilder().build();

        CommandResult commandResult = new AddDeliveryCommand(validDelivery).execute(modelStub);

        assertEquals(String.format(AddDeliveryCommand.MESSAGE_SUCCESS, Messages.format(validDelivery)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validDelivery), modelStub.deliveriesAdded);
    }

    @Test
    public void execute_duplicateDelivery_throwsCommandException() {
        Delivery validDelivery = new DeliveryBuilder().build();
        AddDeliveryCommand addCommand = new AddDeliveryCommand(validDelivery);
        ModelStub modelStub = new ModelStubWithDelivery(validDelivery);

        assertThrows(CommandException.class,
                AddDeliveryCommand.MESSAGE_DUPLICATE_DELIVERY, () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Delivery delivery1 = new DeliveryBuilder().withId(1).build();
        Delivery delivery2 = new DeliveryBuilder().withId(2).withCost(10340.00).build();
        AddDeliveryCommand addCommand1 = new AddDeliveryCommand(delivery1);
        AddDeliveryCommand addCommand2 = new AddDeliveryCommand(delivery2);

        // same object -> returns true
        assertTrue(addCommand1.equals(addCommand1));

        // same values -> returns true
        AddDeliveryCommand addCommand1Copy = new AddDeliveryCommand(delivery1);
        assertTrue(addCommand1.equals(addCommand1Copy));

        // different types -> returns false
        assertFalse(addCommand1.equals(1));

        // null -> returns false
        assertFalse(addCommand1.equals(null));

        // different person -> returns false
        assertFalse(addCommand1.equals(addCommand2));
    }

    @Test
    public void constructor_validParameters_success() {
        String clientName = "Alice Yeoh";
        DateTime dateTime = new DateTime("01/01/2025", "1000");
        String remarks = "Pizza delivery";
        Double cost = 25.50;

        AddDeliveryCommand command = new AddDeliveryCommand(clientName, dateTime, remarks, cost);
        // Constructor should not throw exception
    }

    @Test
    public void constructor_nullClientName_throwsNullPointerException() {
        DateTime dateTime = new DateTime("01/01/2025", "1000");
        String remarks = "Pizza delivery";
        Double cost = 25.50;

        assertThrows(NullPointerException.class, () ->
                new AddDeliveryCommand(null, dateTime, remarks, cost));
    }

    @Test
    public void constructor_nullDateTime_throwsNullPointerException() {
        String clientName = "Alice Yeoh";
        String remarks = "Pizza delivery";
        Double cost = 25.50;

        assertThrows(NullPointerException.class, () ->
                new AddDeliveryCommand(clientName, null, remarks, cost));
    }

    @Test
    public void constructor_nullRemarks_throwsNullPointerException() {
        String clientName = "Alice Yeoh";
        DateTime dateTime = new DateTime("01/01/2025", "1000");
        Double cost = 25.50;

        assertThrows(NullPointerException.class, () ->
                new AddDeliveryCommand(clientName, dateTime, null, cost));
    }

    @Test
    public void constructor_nullCost_throwsNullPointerException() {
        String clientName = "Alice Yeoh";
        DateTime dateTime = new DateTime("01/01/2025", "1000");
        String remarks = "Pizza delivery";

        assertThrows(NullPointerException.class, () ->
                new AddDeliveryCommand(clientName, dateTime, remarks, null));
    }

    @Test
    public void constructor_withTag_success() {
        String clientName = "Alice Yeoh";
        DateTime dateTime = new DateTime("01/01/2025", "1000");
        String remarks = "Pizza delivery";
        Double cost = 25.50;
        String tag = "Personal";

        AddDeliveryCommand command = new AddDeliveryCommand(clientName, dateTime, remarks, cost, tag);
        // Constructor should not throw exception
    }

    @Test
    public void constructor_withTag_negativeCost() {
        String clientName = "Alice Yeoh";
        DateTime dateTime = new DateTime("01/01/2025", "1000");
        String remarks = "Pizza delivery";
        Double cost = -25.50;
        String tag = "Personal";

        assertThrows(IllegalArgumentException.class, () ->
                new AddDeliveryCommand(clientName, dateTime, remarks, cost, tag));
    }

    @Test
    public void constructor_withTag_nullTag() {
        String clientName = "Alice Yeoh";
        DateTime dateTime = new DateTime("01/01/2025", "1000");
        String remarks = "Pizza delivery";
        Double cost = 25.50;

        AddDeliveryCommand command = new AddDeliveryCommand(clientName, dateTime, remarks, cost, null);
        // Constructor should not throw exception
    }

    @Test
    public void constructor_withTag_blankTag() {
        String clientName = "Alice Yeoh";
        DateTime dateTime = new DateTime("01/01/2025", "1000");
        String remarks = "Pizza delivery";
        Double cost = 25.50;

        AddDeliveryCommand command = new AddDeliveryCommand(clientName, dateTime, remarks, cost, "   ");
        // Constructor should not throw exception
    }

    @Test
    public void toStringMethod() {
        AddDeliveryCommand addCommand = new AddDeliveryCommand(ALICE_DELIVERY);
        String expected = AddDeliveryCommand.class.getCanonicalName()
                + "{clientName=" + ALICE_DELIVERY.getClient().getName() + ", "
                + "dateTime=" + ALICE_DELIVERY.getDeliveryDate() + ", "
                + "remarks=" + ALICE_DELIVERY.getRemarks() + ", "
                + "cost=" + ALICE_DELIVERY.getCost() + ", "
                + "tag=" + ALICE_DELIVERY.getTag() + "}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getFoodBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setFoodBookFilePath(Path foodBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setFoodBook(ReadOnlyFoodBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyFoodBook getFoodBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addDelivery(Delivery delivery) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasDelivery(Delivery delivery) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteDelivery(Delivery target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setDelivery(Delivery target, Delivery editedDelivery) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Delivery> getFilteredDeliveryList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredDeliveryList(Predicate<Delivery> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithDelivery extends ModelStub {
        private final Delivery delivery;

        ModelStubWithDelivery(Delivery delivery) {
            requireNonNull(delivery);
            this.delivery = delivery;
        }

        @Override
        public boolean hasDelivery(Delivery delivery) {
            requireNonNull(delivery);
            return this.delivery.equals(delivery);
        }
    }

    /**
     * A Model stub that always accept the delivery being added.
     */
    private class ModelStubAcceptingDeliveryAdded extends ModelStub {
        final ArrayList<Delivery> deliveriesAdded = new ArrayList<>();

        @Override
        public boolean hasDelivery(Delivery delivery) {
            requireNonNull(delivery);
            return deliveriesAdded.stream().anyMatch(delivery::equals);
        }

        @Override
        public void addDelivery(Delivery delivery) {
            requireNonNull(delivery);
            deliveriesAdded.add(delivery);
        }

        @Override
        public ReadOnlyFoodBook getFoodBook() {
            return new FoodBook();
        }
    }

}
