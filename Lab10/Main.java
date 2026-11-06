import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InaccessibleObjectException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        AppLogger.LOG.info("Program started");

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println(Lang.get("menu.title"));
            System.out.println(Lang.get("menu.1"));
            System.out.println(Lang.get("menu.2"));
            System.out.println(Lang.get("menu.3"));
            System.out.println(Lang.get("menu.4"));
            System.out.println(Lang.get("menu.5"));
            System.out.println(Lang.get("menu.6"));
            System.out.println(Lang.get("menu.7"));
            System.out.println(Lang.get("menu.8"));
            System.out.println(Lang.get("menu.0"));
            String choice = ask(Lang.get("menu.choice"));
            AppLogger.LOG.fine("Menu choice: " + choice);
            switch (choice) {
                case "1":
                    AppLogger.LOG.info("Menu: longest line");
                    findLongestLine();
                    break;
                case "2":
                    AppLogger.LOG.info("Menu: encrypt");
                    cipher(true);
                    break;
                case "3":
                    AppLogger.LOG.info("Menu: decrypt");
                    cipher(false);
                    break;
                case "4":
                    AppLogger.LOG.info("Menu: count tags");
                    countTags();
                    break;
                case "5":
                    AppLogger.LOG.info("Menu: show file");
                    showFile();
                    break;
                case "6":
                    AppLogger.LOG.info("Menu: reflection");
                    reflectionDemo();
                    break;
                case "7":
                    AppLogger.LOG.info("Menu: logging demo");
                    loggingDemo();
                    break;
                case "8":
                    AppLogger.LOG.info("Menu: choose language");
                    chooseLanguage();
                    break;
                case "0":
                    running = false;
                    System.out.println(Lang.get("msg.finished"));
                    AppLogger.LOG.info("Program finished");
                    break;
                default:
                    AppLogger.LOG.warning("Wrong menu item: " + choice);
                    System.out.println(Lang.get("msg.wrongMenu"));
            }
        }
    }

    private static String ask(String prompt) {
        System.out.print(prompt + " ");
        if (!scanner.hasNextLine()) {
            return "0";
        }
        return scanner.nextLine().trim();
    }

    private static Path askPath(String prompt) {
        String text = ask(prompt);
        if (text.isEmpty()) {
            throw new IllegalArgumentException(Lang.get("err.pathEmpty"));
        }
        return Paths.get(text); // throws InvalidPathException for a bad path
    }

    private static void findLongestLine() {
        try {
            Path file = askPath(Lang.get("ask.file"));
            String line = LongestLineFinder.findLine(file);
            if (line == null) {
                System.out.println(Lang.get("longest.noWords"));
            } else {
                System.out.println(Lang.format("longest.result", LongestLineFinder.countWords(line)));
                System.out.println(line);
            }
        } catch (IllegalArgumentException e) {
            AppLogger.LOG.log(Level.WARNING, "Wrong path", e);
            System.out.println(Lang.format("err.wrongPath", e.getMessage()));
        } catch (IOException e) {
            AppLogger.LOG.log(Level.SEVERE, "Cannot read the file", e);
            System.out.println(Lang.format("err.readFile", e));
        }
    }

    private static void cipher(boolean encrypt) {
        try {
            Path source = askPath(Lang.get("ask.source"));
            Path target = askPath(Lang.get("ask.result"));
            if (source.toAbsolutePath().normalize().equals(target.toAbsolutePath().normalize())) {
                AppLogger.LOG.warning("Source and result files are the same");
                System.out.println(Lang.get("err.sameFiles"));
                return;
            }
            String keyText = ask(Lang.get("ask.key"));
            if (keyText.length() != 1) {
                AppLogger.LOG.warning("Wrong key length: " + keyText.length());
                System.out.println(Lang.get("err.keyLength"));
                return;
            }
            char key = keyText.charAt(0);
            if (encrypt) {
                FileCipher.encryptFile(source, target, key);
                System.out.println(Lang.format("cipher.encrypted", target));
            } else {
                FileCipher.decryptFile(source, target, key);
                System.out.println(Lang.format("cipher.decrypted", target));
            }
        } catch (IllegalArgumentException e) {
            AppLogger.LOG.log(Level.WARNING, "Wrong path", e);
            System.out.println(Lang.format("err.wrongPath", e.getMessage()));
        } catch (IOException e) {
            AppLogger.LOG.log(Level.SEVERE, "File error", e);
            System.out.println(Lang.format("err.fileError", e));
        }
    }

    private static void countTags() {
        String url = ask(Lang.get("ask.url"));
        if (url.isEmpty()) {
            url = "https://example.com";
        }
        try {
            String html = TagCounter.download(url);
            Map<String, Integer> tags = TagCounter.count(html);
            if (tags.isEmpty()) {
                System.out.println(Lang.get("tags.none"));
                return;
            }
            System.out.println(Lang.get("tags.byName"));
            print(TagCounter.sortByName(tags));
            System.out.println(Lang.get("tags.byFreq"));
            print(TagCounter.sortByFrequency(tags));
        } catch (IllegalArgumentException e) {
            AppLogger.LOG.log(Level.WARNING, "Wrong URL", e);
            System.out.println(Lang.format("err.wrongUrl", e.getMessage()));
        } catch (IOException e) {
            AppLogger.LOG.log(Level.SEVERE, "Cannot load the page", e);
            System.out.println(Lang.format("err.loadPage", e));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            AppLogger.LOG.warning("Loading was interrupted");
            System.out.println(Lang.get("err.interrupted"));
        }
    }

    private static void print(List<Map.Entry<String, Integer>> list) {
        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }

    private static void showFile() {
        try {
            Path file = askPath(Lang.get("ask.file"));
            System.out.println(Lang.format("show.header", file));
            System.out.println(Files.readString(file, StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            AppLogger.LOG.log(Level.WARNING, "Wrong path", e);
            System.out.println(Lang.format("err.wrongPath", e.getMessage()));
        } catch (IOException e) {
            AppLogger.LOG.log(Level.SEVERE, "Cannot read the file", e);
            System.out.println(Lang.format("err.readFile", e));
        }
    }

    // Task 1: change a String with reflection
    private static void reflectionDemo() {
        System.out.println(Lang.get("refl.title"));

        // 1) how to create the string
        System.out.println(Lang.get("refl.howCreate"));
        System.out.println(Lang.get("refl.create1"));
        System.out.println(Lang.get("refl.create2"));
        String createChoice = ask(Lang.get("menu.choice"));
        if (!createChoice.equals("1") && !createChoice.equals("2")) {
            AppLogger.LOG.warning("Wrong choice in reflection demo: " + createChoice);
            System.out.println(Lang.get("msg.wrongMenu"));
            return;
        }
        boolean fromLiteral = createChoice.equals("1");
        String text;
        String sameLiteral = null;
        if (fromLiteral) {
            text = "Java Reflection";
            sameLiteral = "Java Reflection";  // the same literal: it is the same object
        } else {
            text = ask(Lang.get("refl.enterString")); // typed from the keyboard
        }

        // 2) where to take the new value
        System.out.println(Lang.get("refl.howReplace"));
        System.out.println(Lang.get("refl.replace1"));
        System.out.println(Lang.get("refl.replace2"));
        String replaceChoice = ask(Lang.get("menu.choice"));
        String newValue;
        if (replaceChoice.equals("1")) {
            newValue = "Changed by reflection!";
        } else if (replaceChoice.equals("2")) {
            newValue = ask(Lang.get("refl.enterNew"));
        } else {
            AppLogger.LOG.warning("Wrong choice in reflection demo: " + replaceChoice);
            System.out.println(Lang.get("msg.wrongMenu"));
            return;
        }

        try {
            System.out.println(Lang.format("refl.fieldType", StringChanger.valueFieldType()));
            System.out.println(Lang.format("refl.before", text));
            StringChanger.change(text, newValue);
            System.out.println(Lang.format("refl.after", text));
            if (fromLiteral) {
                System.out.println(Lang.format("refl.literalNote", sameLiteral));
            } else {
                System.out.println(Lang.get("refl.keyboardNote"));
            }
        } catch (InaccessibleObjectException e) {
            AppLogger.LOG.log(Level.SEVERE, "No access to String fields", e);
            System.out.println(Lang.get("refl.errorAccess"));
        } catch (Exception e) {
            AppLogger.LOG.log(Level.SEVERE, "Reflection error", e);
            System.out.println(Lang.format("refl.error", e));
        }
    }

    // Task 2: one message of every level, to see where each of them goes
    private static void loggingDemo() {
        System.out.println(Lang.get("log.title"));
        System.out.println(Lang.get("log.settings"));
        AppLogger.LOG.fine("Demo message with level FINE");
        AppLogger.LOG.info("Demo message with level INFO");
        AppLogger.LOG.warning("Demo message with level WARNING");
        AppLogger.LOG.severe("Demo message with level SEVERE");
        System.out.println(Lang.get("log.sent"));
        System.out.println(Lang.get("log.check"));
    }

    // Task 3: language selection
    private static void chooseLanguage() {
        System.out.println(Lang.get("lang.title"));
        System.out.println(Lang.get("lang.en"));
        System.out.println(Lang.get("lang.uk"));
        String choice = ask(Lang.get("menu.choice"));
        if (choice.equals("1")) {
            Lang.setLanguage("en");
            System.out.println(Lang.get("lang.changed"));
        } else if (choice.equals("2")) {
            Lang.setLanguage("uk");
            System.out.println(Lang.get("lang.changed"));
        } else {
            AppLogger.LOG.warning("Wrong language choice: " + choice);
            System.out.println(Lang.get("lang.wrong"));
        }
    }
}
