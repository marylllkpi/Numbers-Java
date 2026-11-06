import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

// Logger level      = INFO    -> FINE messages are thrown away by the logger itself
// Console handler   = WARNING -> only WARNING and SEVERE are printed to the console
// File handler      = INFO    -> INFO, WARNING and SEVERE are written to logs/app.log
public class AppLogger {
    public static final Logger LOG = Logger.getLogger("lab10");

    static {
        LOG.setUseParentHandlers(false); // do not use the default console handler
        LOG.setLevel(Level.INFO);

        Handler console = new ConsoleOutput();
        console.setLevel(Level.WARNING);
        LOG.addHandler(console);

        try {
            Files.createDirectories(Paths.get("logs"));
            FileHandler file = new FileHandler("logs/app.log", true);
            file.setEncoding("UTF-8");
            file.setLevel(Level.INFO);
            file.setFormatter(new LineFormatter());
            LOG.addHandler(file);
        } catch (IOException e) {
            LOG.warning("Cannot create the log file: " + e.getMessage());
        }
    }

    // Prints log lines to System.out and flushes after every message
    private static class ConsoleOutput extends StreamHandler {
        ConsoleOutput() {
            super(System.out, new LineFormatter());
        }

        @Override
        public synchronized void publish(LogRecord record) {
            super.publish(record);
            flush();
        }

        @Override
        public void close() {
            flush(); // do not close System.out
        }
    }

    private static class LineFormatter extends Formatter {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public String format(LogRecord record) {
            String line = dateFormat.format(new Date(record.getMillis()))
                    + " [" + record.getLevel() + "] " + formatMessage(record);
            if (record.getThrown() != null) {
                line += " | " + record.getThrown();
            }
            return line + System.lineSeparator();
        }
    }
}
