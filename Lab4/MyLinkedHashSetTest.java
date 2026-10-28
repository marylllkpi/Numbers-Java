import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MyLinkedHashSetTest {

    @Test
    void duplicatesAreIgnoredAndOrderIsKept() {
        MyLinkedHashSet<String> set = new MyLinkedHashSet<>();
        assertTrue(set.add("b"));
        assertTrue(set.add("a"));
        assertFalse(set.add("b"));
        assertEquals("[b, a]", set.toString());
        assertEquals(2, set.size());
    }

    @Test
    void createFromCollection() {
        MyLinkedHashSet<String> set = new MyLinkedHashSet<>(Arrays.asList("b", "a", "b", "c"));
        assertEquals("[b, a, c]", set.toString());
    }

    @Test
    void addAllReturnsTrueOnlyIfSomethingChanged() {
        MyLinkedHashSet<String> set = new MyLinkedHashSet<>();
        assertTrue(set.addAll(Arrays.asList("a", "b")));
        assertFalse(set.addAll(Arrays.asList("a", "b")));
    }

    @Test
    void containsAll() {
        MyLinkedHashSet<String> set = new MyLinkedHashSet<>(Arrays.asList("a", "b", "c"));
        assertTrue(set.containsAll(Arrays.asList("a", "c")));
        assertFalse(set.containsAll(Arrays.asList("a", "z")));
    }

    @Test
    void removeAndContains() {
        MyLinkedHashSet<String> set = new MyLinkedHashSet<>(Arrays.asList("a", "b"));
        assertTrue(set.remove("a"));
        assertFalse(set.remove("a"));
        assertFalse(set.contains("a"));
        assertEquals("[b]", set.toString());
    }

    @Test
    void copyToAcceptsSuperTypes() {
        MyLinkedHashSet<String> set = new MyLinkedHashSet<>(Arrays.asList("a", "b"));
        List<Object> target = new ArrayList<>();
        set.copyTo(target);
        assertEquals(2, target.size());
    }

    @Test
    void manyElementsKeepOrder() {
        MyLinkedHashSet<Integer> set = new MyLinkedHashSet<>();
        for (int i = 500; i > 0; i--) {
            set.add(i);
        }
        Object[] array = set.toArray();
        assertEquals(500, set.size());
        assertEquals(500, array[0]);
        assertEquals(1, array[499]);
    }
}
