import java.util.Locale;
import java.util.ResourceBundle;

// Keeps the current language and gives texts from the files in resources/location
public class Lang {
    private static final String BUNDLE_NAME = "resources.location.messages";

    private static ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.ENGLISH);

    // code: "en" or "uk"
    public static void setLanguage(String code) {
        bundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.forLanguageTag(code));
        AppLogger.LOG.info("Language changed to: " + code);
    }

    public static String get(String key) {
        return bundle.getString(key);
    }

    // text with %s / %d places
    public static String format(String key, Object... args) {
        return String.format(bundle.getString(key), args);
    }
}
