package model;

import java.time.LocalDate;

/** Stored-value card (regular passengers only), no expiry date. */
public class StoredValueCard extends Card {
    public static final double FARE = 8.00; // price of one ride, UAH

    private double balance;

    public StoredValueCard(String id, double balance) {
        super(id, PassengerType.REGULAR);
        this.balance = balance;
    }

    /** Tops up the card with the given amount. */
    public void topUp(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Top-up amount must be greater than 0");
        }
        balance += amount;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public AccessResult check(LocalDate today) {
        if (balance < FARE) {
            return AccessResult.NO_FUNDS;
        }
        return AccessResult.ALLOWED;
    }

    @Override
    public void deductRide() {
        balance -= FARE;
    }

    @Override
    public String getKindName() {
        return "stored value";
    }

    @Override
    public String getDetails() {
        return String.format("balance: %.2f UAH", balance);
    }
}
