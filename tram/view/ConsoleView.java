package view;

import model.AccessResult;
import model.Card;
import model.Statistics;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/** View: all console input and output is here. */
public class ConsoleView {
    private final Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        System.out.println();
        System.out.println("===== RAPID TRAM TURNSTILE =====");
        System.out.println("1. Issue a card");
        System.out.println("2. Show all issued cards");
        System.out.println("3. Touch a card to the turnstile");
        System.out.println("4. Touch a damaged card");
        System.out.println("5. Top up a stored-value card");
        System.out.println("6. Total statistics");
        System.out.println("7. Statistics by card type");
        System.out.println("0. Exit");
    }

    public void showIssueMenu() {
        System.out.println("Choose card type:");
        System.out.println("1. 1 month");
        System.out.println("2. 10 days");
        System.out.println("3. 5 trips");
        System.out.println("4. 10 trips");
        System.out.println("5. Stored value");
    }

    public void showPassengerMenu() {
        System.out.println("Choose passenger category:");
        System.out.println("1. School");
        System.out.println("2. Student");
        System.out.println("3. Regular");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showCards(List<Card> cards) {
        System.out.println("--- Register of issued cards ---");
        if (cards.isEmpty()) {
            System.out.println("No cards yet.");
            return;
        }
        for (Card card : cards) {
            System.out.println(card);
        }
    }

    public void showResult(AccessResult result) {
        System.out.println(result.isAllowed() ? "[OK] " + result.getMessage()
                : "[DENIED] " + result.getMessage());
    }

    public void showTotalStatistics(Statistics stats) {
        System.out.println("--- Total statistics ---");
        System.out.println("Allowed passes: " + stats.getAllowed());
        System.out.println("Denied passes:  " + stats.getDenied());
        System.out.println("Total attempts: " + (stats.getAllowed() + stats.getDenied()));
    }

    public void showStatisticsByType(Statistics stats) {
        System.out.println("--- Statistics by card type ---");
        if (stats.getByType().isEmpty()) {
            System.out.println("No passes yet.");
            return;
        }
        for (Map.Entry<String, int[]> entry : stats.getByType().entrySet()) {
            System.out.println(entry.getKey() + ": allowed - " + entry.getValue()[0]
                    + ", denied - " + entry.getValue()[1]);
        }
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return "0";
        }
        return scanner.nextLine().trim();
    }

    /** Reads an integer; returns -1 if the input is not a number. */
    public int readInt(String prompt) {
        try {
            return Integer.parseInt(readLine(prompt));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /** Reads a decimal number; returns -1 if the input is not a number. */
    public double readDouble(String prompt) {
        try {
            return Double.parseDouble(readLine(prompt).replace(',', '.'));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
