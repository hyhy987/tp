package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.delivery.Delivery;

/**
 * A utility class containing a list of {@code Delivery} objects to be used in tests.
 */
public class TypicalDeliveries {

    public static final Delivery ALICE_DELIVERY = new DeliveryBuilder().withId(0)
            .withClient(TypicalPersons.ALICE).withDateTime("12/12/2023", "1200")
            .withRemarks("NIL").withCost(10.00).build();
    public static final Delivery BENSON_DELIVERY = new DeliveryBuilder().withId(1)
            .withClient(TypicalPersons.BENSON).withDateTime("13/12/2023", "1300")
            .withRemarks("NIL").withCost(20.00).build();

    public static final Delivery CARL_DELIVERY = new DeliveryBuilder().withId(2)
            .withClient(TypicalPersons.CARL).withDateTime("14/12/2023", "1400")
            .withRemarks("NIL").withCost(30.00).build();
    public static final Delivery DANIEL_DELIVERY = new DeliveryBuilder().withId(3)
            .withClient(TypicalPersons.DANIEL).withDateTime("15/12/2023", "1500")
            .withRemarks("NIL").withCost(40.00).build();

    public static final Delivery ELLE_DELIVERY = new DeliveryBuilder().withId(4)
            .withClient(TypicalPersons.ELLE).withDateTime("16/12/2023", "1600")
            .withRemarks("NIL").withCost(50.00).build();

    public static final Delivery FIONA_DELIVERY = new DeliveryBuilder().withId(5)
            .withClient(TypicalPersons.FIONA).withDateTime("17/12/2023", "1700")
            .withRemarks("NIL").withCost(60.00).build();

    public static final Delivery GEORGE_DELIVERY = new DeliveryBuilder().withId(6)
            .withClient(TypicalPersons.GEORGE).withDateTime("18/12/2023", "1800")
            .withRemarks("NIL").withCost(70.00).build();

    private TypicalDeliveries() {} // prevents instantiation

    public static List<Delivery> getTypicalDeliveries() {
        return new ArrayList<>(Arrays.asList(ALICE_DELIVERY, BENSON_DELIVERY, CARL_DELIVERY,
                DANIEL_DELIVERY, ELLE_DELIVERY, FIONA_DELIVERY, GEORGE_DELIVERY));
    }
}
