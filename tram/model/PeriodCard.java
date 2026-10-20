package model;

import java.time.LocalDate;

/** Card limited by validity period (1 month or 10 days). */
public class PeriodCard extends Card {
    private final PeriodKind kind;
    private final LocalDate validUntil;

    public PeriodCard(String id, PassengerType passengerType, PeriodKind kind, LocalDate issueDate) {
        super(id, passengerType);
        this.kind = kind;
        this.validUntil = kind.calcEnd(issueDate);
    }

    @Override
    public AccessResult check(LocalDate today) {
        if (today.isAfter(validUntil)) {
            return AccessResult.EXPIRED;
        }
        return AccessResult.ALLOWED;
    }

    @Override
    public void deductRide() {
        // a period card deducts nothing
    }

    @Override
    public String getKindName() {
        return kind.getTitle();
    }

    @Override
    public String getDetails() {
        return "valid until " + validUntil;
    }
}
