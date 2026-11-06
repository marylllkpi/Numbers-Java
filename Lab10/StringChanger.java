import java.lang.reflect.Field;

// Changes the content of an existing String object using reflection
public class StringChanger {

    public static String valueFieldType() throws NoSuchFieldException {
        Field valueField = String.class.getDeclaredField("value");
        return valueField.getType().getSimpleName();
    }

    public static void change(String target, String newValue) throws Exception {
        Field valueField = String.class.getDeclaredField("value");
        valueField.setAccessible(true); // open the private field

        if (valueField.getType() == char[].class) {
            // Java 8: the text is stored in char[] value
            valueField.set(target, newValue.toCharArray());
        } else {
            // Java 9+: the text is stored in byte[] value and the field "coder" says how
            Field coderField = String.class.getDeclaredField("coder");
            coderField.setAccessible(true);
            valueField.set(target, valueField.get(newValue));
            coderField.setByte(target, coderField.getByte(newValue));
        }

        // String remembers its hash code, so reset it
        Field hashField = String.class.getDeclaredField("hash");
        hashField.setAccessible(true);
        hashField.setInt(target, 0);
        try {
            Field hashIsZero = String.class.getDeclaredField("hashIsZero");
            hashIsZero.setAccessible(true);
            hashIsZero.setBoolean(target, false);
        } catch (NoSuchFieldException e) {
            // old Java versions do not have this field
        }
        AppLogger.LOG.info("String value changed using reflection");
    }
}
