package model;

import java.time.LocalDate;

/** Card limited by number of trips (5 or 10). */
public class TripCard extends Card {
    private final int totalTrips;
    private int tripsLeft;
    private final LocalDate validUntil;

    public TripCard(String id, PassengerType passengerType, int totalTrips, LocalDate issueDate) {
        super(id, passengerType);
        this.totalTrips = totalTrips;
        this.tripsLeft = totalTrips;
        this.validUntil = issueDate.plusMonths(3); // such cards are valid for 3 months
    }

    /** Creates a card with a given number of trips left (for test data). */
    public TripCard(String id, PassengerType passengerType, int totalTrips, int tripsLeft, LocalDate issueDate) {
        this(id, passengerType, totalTrips, issueDate);
        this.tripsLeft = tripsLeft;
    }

    @Override
    public AccessResult check(LocalDate today) {
        if (today.isAfter(validUntil)) {
            return AccessResult.EXPIRED;
        }
        if (tripsLeft <= 0) {
            return AccessResult.NO_TRIPS;
        }
        return AccessResult.ALLOWED;
    }

    @Override
    public void deductRide() {
        tripsLeft--;
    }

    @Override
    public String getKindName() {
        return totalTrips + " trips";
    }

    @Override
    public String getDetails() {
        return "trips left: " + tripsLeft + ", valid until " + validUntil;
    }
}
