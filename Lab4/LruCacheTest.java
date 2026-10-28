import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class LruCacheTest {

    @Test
    void nullIsForbidden() {
        LruCache<String, Integer> cache = new LruCache<>(2);
        assertThrows(NullPointerException.class, () -> cache.put(null, 1));
        assertThrows(NullPointerException.class, () -> cache.put("a", null));
        assertThrows(NullPointerException.class, () -> cache.get(null));
    }

    @Test
    void leastRecentlyUsedIsEvicted() {
        LruCache<String, Integer> cache = new LruCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.get("a");        // a тепер найновіший
        cache.put("c", 3);     // має зникнути b
        assertTrue(cache.containsKey("a"));
        assertFalse(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));
    }

    @Test
    void putAllAcceptsMapOfSubtypes() {
        // Map<String, Integer> можна додати в кеш <String, Number>
        LruCache<String, Number> cache = new LruCache<>(5);
        Map<String, Integer> source = new HashMap<>();
        source.put("x", 1);
        source.put("y", 2);
        cache.putAll(source);
        assertEquals(2, cache.size());
        assertEquals(2, cache.get("y"));
    }

    @Test
    void entryExpiresAfterTtl() {
        long[] time = {0};                      
        LruCache<String, Integer> cache = new LruCache<>(5, 100, () -> time[0]);
        cache.put("a", 1);
        time[0] = 99;
        assertEquals(1, cache.get("a"));
        time[0] = 100;
        assertNull(cache.get("a"));
    }
}
