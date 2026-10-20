import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Pattern;

public final class InputValidator {
    private static final Pattern NAME = Pattern.compile("\\p{L}+([ '’\\-]\\p{L}+)*");
    private static final Pattern PHONE = Pattern.compile("(\\+38)?0\\d{9}");
    private static final Pattern HOUSE = Pattern.compile("\\d{1,4}(/\\d{1,4})?\\p{L}?");
    private static final Pattern APARTMENT = Pattern.compile("\\d{1,4}");
    private static final DateTimeFormatter DATE =
            DateTimeFormatter.ofPattern("dd.MM.uuuu").withResolverStyle(ResolverStyle.STRICT);

    private InputValidator() { }

    public static String name(String s) {
        String v = s.trim();
        if (!NAME.matcher(v).matches() || v.length() > 40) {
            throw new IllegalArgumentException(
                    "Only letters are allowed (a hyphen or apostrophe is OK), up to 40 characters.");
        }
        return v;
    }

    public static LocalDate birthDate(String s) {
        LocalDate d;
        try {
            d = LocalDate.parse(s.trim(), DATE);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Date format: dd.mm.yyyy (a real date).");
        }
        if (d.isAfter(LocalDate.now()) || d.getYear() < 1900) {
            throw new IllegalArgumentException("The date must be between 1900 and today.");
        }
        return d;
    }

    public static String phone(String s) {
        String v = s.replaceAll("[\\s\\-()]", "");
        if (!PHONE.matcher(v).matches()) {
            throw new IllegalArgumentException("Phone format: +380XXXXXXXXX or 0XXXXXXXXX.");
        }
        return v.startsWith("+38") ? v : "+38" + v;
    }

    public static String street(String s) {
        String v = s.trim();
        if (v.length() < 2 || v.length() > 60 || !v.matches("[\\p{L}0-9 .'’\\-]+")) {
            throw new IllegalArgumentException("Street name: 2-60 characters (letters, digits, space, . ' -).");
        }
        return v;
    }

    public static String house(String s) {
        String v = s.trim();
        if (!HOUSE.matcher(v).matches()) {
            throw new IllegalArgumentException("House number: for example 12, 12A or 12/3.");
        }
        return v;
    }

    public static String apartment(String s) {
        String v = s.trim();
        if (!APARTMENT.matcher(v).matches()) {
            throw new IllegalArgumentException("Apartment number: a number with 1 to 4 digits.");
        }
        return v;
    }
}
