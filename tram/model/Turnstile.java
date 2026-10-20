package model;

import java.time.LocalDate;

/** Turnstile of the rapid tram. */
public class Turnstile {
    private static final String UNKNOWN_TYPE = "Unknown or unreadable card";

    private final CardRegistry registry;
    private final Statistics statistics = new Statistics();

    public Turnstile(CardRegistry registry) {
        this.registry = registry;
    }

    /** Checks a card by its identifier. */
    public AccessResult process(String cardId, LocalDate today) {
        Card card = registry.find(cardId);
        if (card == null) {
            statistics.record(UNKNOWN_TYPE, false);
            return AccessResult.UNKNOWN_CARD;
        }
        AccessResult result = card.check(today);
        if (result.isAllowed()) {
            card.deductRide();
        }
        statistics.record(card.getTypeName(), result.isAllowed());
        return result;
    }

    /** Damaged card: data could not be read. */
    public AccessResult processUnreadable() {
        statistics.record(UNKNOWN_TYPE, false);
        return AccessResult.READ_ERROR;
    }

    public Statistics getStatistics() {
        return statistics;
    }
}
