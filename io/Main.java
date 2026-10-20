import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("===== I/O STREAMS =====");
            System.out.println("1. Find the line with the most words in a file");
            System.out.println("2. Encrypt a file");
            System.out.println("3. Decrypt a file");
            System.out.println("4. Count HTML tags on a web page");
            System.out.println("5. Show file content");
            System.out.println("0. Exit");
            String choice = ask("Your choice: ");
            switch (choice) {
                case "1":
                    findLongestLine();
                    break;
                case "2":
                    cipher(true);
                    break;
                case "3":
                    cipher(false);
                    break;
                case "4":
                    countTags();
                    break;
                case "5":
                    showFile();
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

    /** Reads a line from the keyboard; returns "0" if the input has ended. */
    private static String ask(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return "0";
        }
        return scanner.nextLine().trim();
    }

    private static Path askPath(String prompt) {
        String text = ask(prompt);
        if (text.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be empty");
        }
        return Paths.get(text); // throws InvalidPathException for a bad path
    }

    // Task 1
    private static void findLongestLine() {
        try {
            Path file = askPath("File location and name: ");
            String line = LongestLineFinder.findLine(file);
            if (line == null) {
                System.out.println("The file has no words.");
            } else {
                System.out.println("Line with the most words (" + LongestLineFinder.countWords(line) + " words):");
                System.out.println(line);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong path: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Cannot read the file: " + e);
        }
    }

    // Task 3
    private static void cipher(boolean encrypt) {
        try {
            Path source = askPath("Source file (location and name): ");
            Path target = askPath("Result file (location and name): ");
            if (source.toAbsolutePath().normalize().equals(target.toAbsolutePath().normalize())) {
                System.out.println("Source and result files must be different.");
                return;
            }
            String keyText = ask("Key character (one symbol): ");
            if (keyText.length() != 1) {
                System.out.println("The key must be exactly one character.");
                return;
            }
            char key = keyText.charAt(0);
            if (encrypt) {
                FileCipher.encryptFile(source, target, key);
                System.out.println("File encrypted: " + target);
            } else {
                FileCipher.decryptFile(source, target, key);
                System.out.println("File decrypted: " + target);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong path: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("File error: " + e);
        }
    }

    // Task 4
    private static void countTags() {
        String url = ask("Page URL (Enter for https://example.com): ");
        if (url.isEmpty()) {
            url = "https://example.com";
        }
        try {
            String html = TagCounter.download(url);
            Map<String, Integer> tags = TagCounter.count(html);
            if (tags.isEmpty()) {
                System.out.println("No tags found on the page.");
                return;
            }
            System.out.println("--- Tags in lexicographic order ---");
            print(TagCounter.sortByName(tags));
            System.out.println("--- Tags by frequency (ascending) ---");
            print(TagCounter.sortByFrequency(tags));
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong URL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Cannot load the page: " + e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Loading was interrupted.");
        }
    }

    private static void print(List<Map.Entry<String, Integer>> list) {
        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }

    private static void showFile() {
        try {
            Path file = askPath("File location and name: ");
            System.out.println("--- Content of " + file + " ---");
            System.out.println(Files.readString(file, StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong path: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Cannot read the file: " + e);
        }
    }
}
