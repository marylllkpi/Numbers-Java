import java.util.concurrent.atomic.AtomicLong;

public class CacheDemo {
    public static void main(String[] args) {
        AtomicLong time = new AtomicLong(0); 
        LruCache<String, String> cache = new LruCache<>(3, 1000, time::get);

        System.out.println("--- null is forbidden ---");
        try {
            cache.put("key", null);
        } catch (NullPointerException e) {
            System.out.println("put(\"key\", null) -> NullPointerException: " + e.getMessage());
        }
        try {
            cache.get(null);
        } catch (NullPointerException e) {
            System.out.println("get(null) -> NullPointerException: " + e.getMessage());
        }

        System.out.println("--- eviction, maxSize = 3 ---");
        cache.put("a", "1");
        cache.put("b", "2");
        cache.put("c", "3");
        System.out.println("cache: " + cache);
        System.out.println("get(a) = " + cache.get("a") + "  (a is now the most recent)");
        cache.put("d", "4");
        System.out.println("after put(d): " + cache + "  <- b was evicted");
        System.out.println("containsKey(b) = " + cache.containsKey("b") + ", evictions = " + cache.getEvictions());

        System.out.println("--- expiry, TTL = 1000 ms ---");
        time.set(500);
        System.out.println("t = 500 ms:  get(c) = " + cache.get("c"));
        time.set(1500);
        System.out.println("t = 1500 ms: get(c) = " + cache.get("c") + ", size = " + cache.size());

        System.out.println("--- statistics ---");
        System.out.println("hits = " + cache.getHits() + ", misses = " + cache.getMisses()
                + ", evictions = " + cache.getEvictions() + ", expirations = " + cache.getExpirations());
    }
}
