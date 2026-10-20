public class PersonJson {

    public static String toJson(Person p) {
        return "{\"lastName\":\"" + escape(p.getLastName()) + "\","
                + "\"firstName\":\"" + escape(p.getFirstName()) + "\","
                + "\"age\":" + p.getAge() + "}";
    }

    public static Person fromJson(String json) {
        String lastName = readString(json, "lastName");
        String firstName = readString(json, "firstName");
        int age = readInt(json, "age");
        return new Person(lastName, firstName, age);
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String readString(String json, String key) {
        String marker = "\"" + key + "\":\"";
        int start = json.indexOf(marker);
        if (start < 0) {
            throw new IllegalArgumentException("No such field in JSON: " + key);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = start + marker.length(); i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '\\') {
                sb.append(json.charAt(++i)); 
            } else if (c == '"') {
                return sb.toString();          
            } else {
                sb.append(c);
            }
        }
        throw new IllegalArgumentException("Unclosed string in field " + key);
    }

    private static int readInt(String json, String key) {
        String marker = "\"" + key + "\":";
        int start = json.indexOf(marker);
        if (start < 0) {
            throw new IllegalArgumentException("No such field in JSON: " + key);
        }
        start += marker.length();
        int end = start;
        while (end < json.length() && json.charAt(end) != ',' && json.charAt(end) != '}') {
            end++;
        }
        return Integer.parseInt(json.substring(start, end).trim());
    }
}
