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

public class TagCounter {
    private static final Pattern TAG = Pattern.compile("<([a-zA-Z][a-zA-Z0-9:-]*)");

    public static String download(String url) throws IOException, InterruptedException {
        AppLogger.LOG.info("Downloading page: " + url);
        URI uri = URI.create(url);
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
        AppLogger.LOG.info("Server answered with status " + response.statusCode());
        if (response.statusCode() != 200) {
            AppLogger.LOG.warning("Page was not loaded, status " + response.statusCode());
            throw new IOException("Server returned HTTP status " + response.statusCode());
        }
        return response.body();
    }

    public static Map<String, Integer> count(String html) {
        String clean = html.replaceAll("(?s)<!--.*?-->", "")
                .replaceAll("(?is)<script(\\s[^>]*)?>.*?</script>", "<script></script>")
                .replaceAll("(?is)<style(\\s[^>]*)?>.*?</style>", "<style></style>");
        Map<String, Integer> result = new TreeMap<>();
        Matcher matcher = TAG.matcher(clean);
        while (matcher.find()) {
            result.merge(matcher.group(1).toLowerCase(), 1, Integer::sum);
        }
        AppLogger.LOG.fine("Different tags found: " + result.size());
        return result;
    }

    public static List<Map.Entry<String, Integer>> sortByName(Map<String, Integer> tags) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(tags.entrySet());
        list.sort(Map.Entry.comparingByKey());
        return list;
    }

    public static List<Map.Entry<String, Integer>> sortByFrequency(Map<String, Integer> tags) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(tags.entrySet());
        list.sort(Map.Entry.<String, Integer>comparingByValue().thenComparing(Map.Entry.comparingByKey()));
        return list;
    }
}
