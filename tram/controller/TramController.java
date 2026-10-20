package controller;

import model.Card;
import model.CardRegistry;
import model.PassengerType;
import model.PeriodKind;
import model.StoredValueCard;
import model.Turnstile;
import view.ConsoleView;

import java.time.LocalDate;

/** Controller: takes the user choice and calls the model and the view. */
public class TramController {
    private final CardRegistry registry;
    private final Turnstile turnstile;
    private final ConsoleView view;

    public TramController(CardRegistry registry, Turnstile turnstile, ConsoleView view) {
        this.registry = registry;
        this.turnstile = turnstile;
        this.view = view;
    }

    public void run() {
        boolean running = true;
        while (running) {
            view.showMenu();
            int choice = view.readInt("Your choice: ");
            switch (choice) {
                case 1:
                    issueCard();
                    break;
                case 2:
                    view.showCards(registry.getAll());
                    break;
                case 3:
                    touchCard();
                    break;
                case 4:
                    view.showResult(turnstile.processUnreadable());
                    break;
                case 5:
                    topUpCard();
                    break;
                case 6:
                    view.showTotalStatistics(turnstile.getStatistics());
                    break;
                case 7:
                    view.showStatisticsByType(turnstile.getStatistics());
                    break;
                case 0:
                    running = false;
                    view.showMessage("Program finished.");
                    break;
                default:
                    view.showMessage("Wrong menu item, try again.");
            }
        }
    }

    private void issueCard() {
        view.showIssueMenu();
        int type = view.readInt("Your choice: ");
        if (type < 1 || type > 5) {
            view.showMessage("Wrong card type.");
            return;
        }
        Card card;
        try {
            if (type == 5) {
                double amount = view.readDouble("Initial amount, UAH: ");
                card = registry.issueStoredValueCard(amount);
            } else {
                PassengerType passenger = askPassengerType();
                if (passenger == null) {
                    view.showMessage("Wrong passenger category.");
                    return;
                }
                LocalDate today = LocalDate.now();
                if (type == 1) {
                    card = registry.issuePeriodCard(passenger, PeriodKind.MONTH, today);
                } else if (type == 2) {
                    card = registry.issuePeriodCard(passenger, PeriodKind.TEN_DAYS, today);
                } else if (type == 3) {
                    card = registry.issueTripCard(passenger, 5, today);
                } else {
                    card = registry.issueTripCard(passenger, 10, today);
                }
            }
        } catch (IllegalArgumentException e) {
            view.showMessage("Error: " + e.getMessage());
            return;
        }
        view.showMessage("Card issued: " + card);
    }

    private PassengerType askPassengerType() {
        view.showPassengerMenu();
        int choice = view.readInt("Your choice: ");
        switch (choice) {
            case 1:
                return PassengerType.SCHOOL;
            case 2:
                return PassengerType.STUDENT;
            case 3:
                return PassengerType.REGULAR;
            default:
                return null;
        }
    }

    private void touchCard() {
        String id = view.readLine("Card ID (for example, T-0001): ");
        view.showResult(turnstile.process(id, LocalDate.now()));
        Card card = registry.find(id);
        if (card != null) {
            view.showMessage("Card state: " + card);
        }
    }

    private void topUpCard() {
        String id = view.readLine("Stored-value card ID: ");
        Card card = registry.find(id);
        if (card == null) {
            view.showMessage("Card not found.");
            return;
        }
        if (!(card instanceof StoredValueCard)) {
            view.showMessage("Only stored-value cards can be topped up.");
            return;
        }
        double amount = view.readDouble("Top-up amount, UAH: ");
        try {
            ((StoredValueCard) card).topUp(amount);
            view.showMessage("Card topped up: " + card);
        } catch (IllegalArgumentException e) {
            view.showMessage("Error: " + e.getMessage());
        }
    }
}
