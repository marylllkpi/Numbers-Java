import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Task 4: counts how often HTML tags occur on a web page. */
public class TagCounter {
    private static final Pattern TAG = Pattern.compile("<([a-zA-Z][a-zA-Z0-9:-]*)");

    /** Downloads the page text by URL. */
    public static String download(String url) throws IOException, InterruptedException {
        URI uri = URI.create(url); // throws IllegalArgumentException for a bad URL
        String scheme = uri.getScheme();
        if (scheme == null || !(scheme.equals("http") || scheme.equals("https"))) {
            throw new IllegalArgumentException("URL must start with http:// or https://");
        }
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("User-Agent", "Mozilla/5.0 (Java lab work)")
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Server returned HTTP status " + response.statusCode());
        }
        return response.body();
    }

    /** Counts opening tags (names in lower case). Comments, scripts and styles content is ignored. */
    public static Map<String, Integer> count(String html) {
        String clean = html.replaceAll("(?s)<!--.*?-->", "")
                .replaceAll("(?is)<script(\\s[^>]*)?>.*?</script>", "<script></script>")
                .replaceAll("(?is)<style(\\s[^>]*)?>.*?</style>", "<style></style>");
        Map<String, Integer> result = new TreeMap<>();
        Matcher matcher = TAG.matcher(clean);
        while (matcher.find()) {
            result.merge(matcher.group(1).toLowerCase(), 1, Integer::sum);
        }
        return result;
    }

    /** Tags in lexicographic order (ascending). */
    public static List<Map.Entry<String, Integer>> sortByName(Map<String, Integer> tags) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(tags.entrySet());
        list.sort(Map.Entry.comparingByKey());
        return list;
    }

    /** Tags by frequency (ascending); tags with equal frequency go in alphabetical order. */
    public static List<Map.Entry<String, Integer>> sortByFrequency(Map<String, Integer> tags) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(tags.entrySet());
        list.sort(Map.Entry.<String, Integer>comparingByValue().thenComparing(Map.Entry.comparingByKey()));
        return list;
    }
}
