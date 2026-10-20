package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** System that keeps the register of issued cards. */
public class CardRegistry {
    private final List<Card> cards = new ArrayList<>();
    private int counter = 0;

    private String nextId() {
        counter++;
        return String.format("T-%04d", counter);
    }

    public Card issuePeriodCard(PassengerType type, PeriodKind kind, LocalDate issueDate) {
        Card card = new PeriodCard(nextId(), type, kind, issueDate);
        cards.add(card);
        return card;
    }

    public Card issueTripCard(PassengerType type, int trips, LocalDate issueDate) {
        Card card = new TripCard(nextId(), type, trips, issueDate);
        cards.add(card);
        return card;
    }

    public Card issueStoredValueCard(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        Card card = new StoredValueCard(nextId(), amount);
        cards.add(card);
        return card;
    }

    /** Adds a ready card to the register (for test data). */
    public void add(Card card) {
        cards.add(card);
        counter++;
    }

    public Card find(String id) {
        for (Card card : cards) {
            if (card.getId().equalsIgnoreCase(id)) {
                return card;
            }
        }
        return null;
    }

    public List<Card> getAll() {
        return Collections.unmodifiableList(cards);
    }
}
