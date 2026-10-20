import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.LongSupplier;

public class LruCache<K, V> implements Cache<K, V> {
    private static class Node<K, V> {
        final K key;
        V value;
        long expiresAt; // Long.MAX_VALUE — без обмеження часу
        Node<K, V> prev;
        Node<K, V> next;

        Node(K key, V value, long expiresAt) {
            this.key = key;
            this.value = value;
            this.expiresAt = expiresAt;
        }
    }

    private final int maxSize;
    private final long ttlMillis;
    private final LongSupplier clock;
    private final Map<K, Node<K, V>> index = new HashMap<>();
    private Node<K, V> head; 
    private Node<K, V> tail; 

    private long hits;
    private long misses;
    private long evictions;
    private long expirations;

    public LruCache(int maxSize) {
        this(maxSize, 0);
    }

    /** @param ttlMillis час життя запису в мс; 0 — записи не спливають. */
    public LruCache(int maxSize, long ttlMillis) {
        this(maxSize, ttlMillis, System::currentTimeMillis);
    }

    /** Конструктор із підміною годинника (для тестів). */
    public LruCache(int maxSize, long ttlMillis, LongSupplier clock) {
        if (maxSize <= 0) throw new IllegalArgumentException("maxSize must be > 0");
        if (ttlMillis < 0) throw new IllegalArgumentException("ttlMillis must be >= 0");
        this.maxSize = maxSize;
        this.ttlMillis = ttlMillis;
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public synchronized V get(K key) {
        Objects.requireNonNull(key, "key is null");
        Node<K, V> n = index.get(key);
        if (n == null) {
            misses++;
            return null;
        }
        if (isExpired(n)) {
            removeNode(n);
            expirations++;
            misses++;
            return null;
        }
        moveToTail(n);
        hits++;
        return n.value;
    }

    @Override
    public synchronized void put(K key, V value) {
        Objects.requireNonNull(key, "key is null");
        Objects.requireNonNull(value, "value is null");
        long expiresAt = ttlMillis == 0 ? Long.MAX_VALUE : clock.getAsLong() + ttlMillis;
        Node<K, V> n = index.get(key);
        if (n != null) {
            n.value = value;
            n.expiresAt = expiresAt;
            moveToTail(n);
            return;
        }
        n = new Node<>(key, value, expiresAt);
        index.put(key, n);
        linkLast(n);
        if (index.size() > maxSize) {
            purgeExpired();              
            while (index.size() > maxSize) {
                removeNode(head);     
                evictions++;
            }
        }
    }

    @Override
    public synchronized V remove(K key) {
        Objects.requireNonNull(key, "key is null");
        Node<K, V> n = index.get(key);
        if (n == null) return null;
        removeNode(n);
        return isExpired(n) ? null : n.value;
    }

    @Override
    public synchronized boolean containsKey(K key) {
        Objects.requireNonNull(key, "key is null");
        Node<K, V> n = index.get(key);
        return n != null && !isExpired(n);
    }

    @Override
    public synchronized int size() {
        purgeExpired();
        return index.size();
    }

    @Override
    public synchronized void clear() {
        index.clear();
        head = null;
        tail = null;
    }

    public synchronized long getHits() { return hits; }
    public synchronized long getMisses() { return misses; }
    public synchronized long getEvictions() { return evictions; }
    public synchronized long getExpirations() { return expirations; }

    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Node<K, V> x = head; x != null; x = x.next) {
            if (x != head) sb.append(", ");
            sb.append(x.key).append('=').append(x.value);
        }
        return sb.append('}').toString(); 
    }

    private boolean isExpired(Node<K, V> n) {
        return n.expiresAt != Long.MAX_VALUE && clock.getAsLong() >= n.expiresAt;
    }

    private void purgeExpired() {
        if (ttlMillis == 0) return;
        Node<K, V> x = head;
        while (x != null) {
            Node<K, V> next = x.next;
            if (isExpired(x)) {
                removeNode(x);
                expirations++;
            }
            x = next;
        }
    }

    private void linkLast(Node<K, V> n) {
        n.prev = tail;
        n.next = null;
        if (tail == null) head = n; else tail.next = n;
        tail = n;
    }

    private void unlink(Node<K, V> n) {
        if (n.prev == null) head = n.next; else n.prev.next = n.next;
        if (n.next == null) tail = n.prev; else n.next.prev = n.prev;
        n.prev = null;
        n.next = null;
    }

    private void moveToTail(Node<K, V> n) {
        if (n != tail) {
            unlink(n);
            linkLast(n);
        }
    }

    private void removeNode(Node<K, V> n) {
        unlink(n);
        index.remove(n.key);
    }
}
