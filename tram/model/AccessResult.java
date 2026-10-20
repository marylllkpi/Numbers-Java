package model;

/** Result of a card check at the turnstile. */
public enum AccessResult {
    ALLOWED(true, "Access allowed"),
    READ_ERROR(false, "Access denied: card data could not be read"),
    UNKNOWN_CARD(false, "Access denied: card not found in the system"),
    EXPIRED(false, "Access denied: card has expired"),
    NO_TRIPS(false, "Access denied: no trips left on the card"),
    NO_FUNDS(false, "Access denied: not enough money on the card");

    private final boolean allowed;
    private final String message;

    AccessResult(boolean allowed, String message) {
        this.allowed = allowed;
        this.message = message;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public String getMessage() {
        return message;
    }
}
