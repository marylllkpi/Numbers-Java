import controller.TramController;
import model.CardRegistry;
import model.PassengerType;
import model.PeriodCard;
import model.PeriodKind;
import model.StoredValueCard;
import model.TripCard;
import model.Turnstile;
import view.ConsoleView;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        CardRegistry registry = new CardRegistry();
        LocalDate today = LocalDate.now();

        // Initial test cards
        registry.issuePeriodCard(PassengerType.STUDENT, PeriodKind.MONTH, today);            // T-0001
        registry.issuePeriodCard(PassengerType.SCHOOL, PeriodKind.TEN_DAYS, today);          // T-0002
        registry.issueTripCard(PassengerType.REGULAR, 5, today);                             // T-0003
        registry.add(new TripCard("T-0004", PassengerType.REGULAR, 10, 0, today));           // no trips left
        registry.add(new PeriodCard("T-0005", PassengerType.REGULAR, PeriodKind.TEN_DAYS,
                today.minusDays(30)));                                                       // expired
        registry.add(new StoredValueCard("T-0006", 20.00));                                  // 2 rides
        registry.add(new StoredValueCard("T-0007", 5.00));                                   // not enough money

        Turnstile turnstile = new Turnstile(registry);
        ConsoleView view = new ConsoleView();
        new TramController(registry, turnstile, view).run();
    }
}
