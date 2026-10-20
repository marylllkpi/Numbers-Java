import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Task 1: finds the line with the maximum number of words in a file. */
public class LongestLineFinder {

    /** Counts words in a line (words are separated by whitespace). */
    public static int countWords(String line) {
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return 0;
        }
        return trimmed.split("\\s+").length;
    }

    /** Returns the line with the most words, or null if the file has no words. */
    public static String findLine(Path file) throws IOException {
        String best = null;
        int max = 0;
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                int words = countWords(line);
                if (words > max) {
                    max = words;
                    best = line;
                }
            }
        }
        return best;
    }
}
