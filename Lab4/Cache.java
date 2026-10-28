import java.util.Map;

public interface Cache<K, V> {
    V get(K key);
    void put(K key, V value);
    void putAll(Map<? extends K, ? extends V> map);
    V remove(K key);
    boolean containsKey(K key);
    int size();
    void clear();
}
