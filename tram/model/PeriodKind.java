package model;

import java.time.LocalDate;

/** Card kind by validity period. */
public enum PeriodKind {
    MONTH("1 month"),
    TEN_DAYS("10 days");

    private final String title;

    PeriodKind(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    /** Calculates the last valid date of the card. */
    public LocalDate calcEnd(LocalDate start) {
        if (this == MONTH) {
            return start.plusMonths(1);
        }
        return start.plusDays(10);
    }
}
