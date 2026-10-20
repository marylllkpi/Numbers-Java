import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;

public class JournalApp {
    private final Scanner in;
    private final PrintStream out;
    private final Journal journal = new Journal();

    public JournalApp(InputStream input, PrintStream output) {
        this.in = new Scanner(input, StandardCharsets.UTF_8);
        this.out = output;
    }

    public static void main(String[] args) {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        new JournalApp(System.in, out).run();
    }

    public void run() {
        try {
            while (true) {
                out.println();
                out.println("===== CURATOR'S JOURNAL =====");
                out.println("1. Add an entry");
                out.println("2. Show all entries");
                out.println("0. Exit");
                out.print("Your choice: ");
                switch (in.nextLine().trim()) {
                    case "1" -> addEntry();
                    case "2" -> showAll();
                    case "0" -> { out.println("Program finished."); return; }
                    default -> out.println("Wrong menu item, try again.");
                }
            }
        } catch (NoSuchElementException e) {
            out.println();
            out.println("Input finished.");
        }
    }

    private void addEntry() {

        String lastName = read("Last name: ", InputValidator::name);
        String firstName = read("First name: ", InputValidator::name);
        var birth = read("Date of birth (dd.mm.yyyy): ", InputValidator::birthDate);
        String phone = read("Phone: ", InputValidator::phone);
        String street = read("Street: ", InputValidator::street);
        String house = read("House: ", InputValidator::house);
        String apartment = read("Apartment: ", InputValidator::apartment);

        // Усі дані коректні — передаємо в модель.
        journal.add(new JournalEntry(lastName, firstName, birth, phone, street, house, apartment));
        out.println("Entry added.");
    }

    private <T> T read(String prompt, Function<String, T> parser) {
        while (true) {
            out.print(prompt);
            try {
                return parser.apply(in.nextLine());
            } catch (IllegalArgumentException e) {
                out.println("  Error: " + e.getMessage() + " Try again.");
            }
        }
    }

    private void showAll() {
        if (journal.size() == 0) {
            out.println("The journal is empty.");
            return;
        }
        for (int i = 0; i < journal.size(); i++) {
            out.println((i + 1) + ". " + journal.get(i));
        }
    }
}
