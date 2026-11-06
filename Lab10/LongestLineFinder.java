import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class LongestLineFinder {

    public static int countWords(String line) {
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return 0;
        }
        return trimmed.split("\\s+").length;
    }

    public static String findLine(Path file) throws IOException {
        AppLogger.LOG.info("Reading file: " + file);
        String best = null;
        int max = 0;
        int number = 0;
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                number++;
                int words = countWords(line);
                AppLogger.LOG.fine("Line " + number + " has " + words + " words");
                if (words > max) {
                    max = words;
                    best = line;
                }
            }
        }
        AppLogger.LOG.info("File read: " + number + " lines, max words in a line: " + max);
        return best;
    }
}
