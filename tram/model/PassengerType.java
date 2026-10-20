package model;

/** Passenger category. */
public enum PassengerType {
    SCHOOL("School"),
    STUDENT("Student"),
    REGULAR("Regular");

    private final String title;

    PassengerType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
