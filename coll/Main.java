import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    public static void main(String[] args) {
        RedBlackTree tree = new RedBlackTree();
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("===== RED-BLACK TREE =====");
            System.out.println("1. Fill with random numbers (unordered)");
            System.out.println("2. Fill with sorted numbers (ascending)");
            System.out.println("3. Add numbers from the keyboard");
            System.out.println("4. Delete numbers");
            System.out.println("5. Show the tree");
            System.out.println("6. Show traversals");
            System.out.println("7. Check red-black properties");
            System.out.println("8. Clear the tree");
            System.out.println("0. Exit");
            String choice = ask("Your choice: ");
            switch (choice) {
                case "1":
                    fillRandom(tree, false);
                    break;
                case "2":
                    fillRandom(tree, true);
                    break;
                case "3":
                    addNumbers(tree);
                    break;
                case "4":
                    deleteNumbers(tree);
                    break;
                case "5":
                    showTree(tree);
                    break;
                case "6":
                    showTraversals(tree);
                    break;
                case "7":
                    String problem = tree.validate();
                    System.out.println(problem == null ? "The tree is a correct red-black tree." : "Problem: " + problem);
                    break;
                case "8":
                    tree.clear();
                    System.out.println("The tree is cleared.");
                    break;
                case "0":
                    running = false;
                    System.out.println("Program finished.");
                    break;
                default:
                    System.out.println("Wrong menu item, try again.");
            }
        }
    }

    private static String ask(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return "0";
        }
        return scanner.nextLine().trim();
    }

    private static void fillRandom(RedBlackTree tree, boolean sorted) {
        try {
            String text = ask("How many numbers (Enter = 10, max 100): ");
            int n = text.isEmpty() ? 10 : Integer.parseInt(text);
            if (n < 1 || n > 100) {
                System.out.println("The count must be from 1 to 100.");
                return;
            }
            int[] array = new int[n];
            for (int i = 0; i < n; i++) {
                array[i] = random.nextInt(99) + 1;
            }
            if (sorted) {
                Arrays.sort(array);
            }
            System.out.println((sorted ? "Sorted array: " : "Array: ") + Arrays.toString(array));
            int added = 0;
            for (int value : array) {
                if (tree.insert(value)) {
                    added++;
                }
            }
            System.out.println("Added " + added + " numbers, duplicates skipped: " + (n - added));
        } catch (NumberFormatException e) {
            System.out.println("Wrong input: enter a whole number.");
        }
    }

    private static void addNumbers(RedBlackTree tree) {
        String line = ask("Numbers to add (separated by spaces): ");
        if (line.isEmpty()) {
            System.out.println("Nothing entered.");
            return;
        }
        for (String token : line.split("\\s+")) {
            try {
                int value = Integer.parseInt(token);
                if (tree.insert(value)) {
                    System.out.println("Added: " + value);
                } else {
                    System.out.println(value + " is already in the tree.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Skipped \"" + token + "\": not a whole number.");
            }
        }
    }

    private static void deleteNumbers(RedBlackTree tree) {
        String line = ask("Numbers to delete (separated by spaces): ");
        if (line.isEmpty()) {
            System.out.println("Nothing entered.");
            return;
        }
        for (String token : line.split("\\s+")) {
            try {
                int value = Integer.parseInt(token);
                if (tree.delete(value)) {
                    System.out.println("Deleted: " + value);
                } else {
                    System.out.println(value + " is not found in the tree.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Skipped \"" + token + "\": not a whole number.");
            }
        }
    }

    private static void showTree(RedBlackTree tree) {
        System.out.println("Tree (root on the left, right subtree on top, R = red, B = black):");
        System.out.print(tree.draw());
        if (!tree.isEmpty()) {
            System.out.println("Nodes: " + tree.size() + ", height: " + tree.height()
                    + ", black height: " + tree.blackHeight());
        }
    }

    private static void showTraversals(RedBlackTree tree) {
        if (tree.isEmpty()) {
            System.out.println("The tree is empty.");
            return;
        }
        print("In-order (sorted):", tree.inOrder());
        print("Pre-order:        ", tree.preOrder());
        print("Post-order:       ", tree.postOrder());
        print("Level-order:      ", tree.levelOrder());
    }

    private static void print(String title, List<String> items) {
        System.out.println(title + " " + String.join(" ", items));
    }
}
