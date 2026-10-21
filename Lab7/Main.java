import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.print("Enter n (1-10000, 0 to exit): ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String text = scanner.nextLine().trim();
            try {
                int n = Integer.parseInt(text);
                if (n == 0) {
                    running = false;
                    System.out.println("Program finished.");
                } else if (n < 1 || n > 10000) {
                    System.out.println("The number must be from 1 to 10000.");
                } else {
                    List<Integer> result = PerfectNumbers.findPerfect(n);
                    if (result.isEmpty()) {
                        System.out.println("No perfect numbers from 1 to " + n + ".");
                    } else {
                        System.out.println("Perfect numbers from 1 to " + n + ": " + result);
                        result.forEach(number -> System.out.println(PerfectNumbers.describe(number)));
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Wrong input: enter a whole number.");
            }
        }
    }
}
