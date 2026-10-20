package model;

import java.time.LocalDate;

/** Abstract travel card. Data is stored on the card itself. */
public abstract class Card {
    private final String id;
    private final PassengerType passengerType;

    protected Card(String id, PassengerType passengerType) {
        this.id = id;
        this.passengerType = passengerType;
    }

    public String getId() {
        return id;
    }

    public PassengerType getPassengerType() {
        return passengerType;
    }

    /** Checks whether the passenger can pass (the card is not changed). */
    public abstract AccessResult check(LocalDate today);

    /** Deducts one trip (or the fare). */
    public abstract void deductRide();

    /** Card kind: "1 month", "5 trips", etc. */
    public abstract String getKindName();

    /** Current card state (expiry date, trips left, balance). */
    public abstract String getDetails();

    /** Full type name, for example "Student, 1 month". */
    public String getTypeName() {
        return passengerType.getTitle() + ", " + getKindName();
    }

    @Override
    public String toString() {
        return id + " | " + getTypeName() + " | " + getDetails();
    }
}
