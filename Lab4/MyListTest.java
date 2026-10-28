import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;

class MyListTest {

    private List<MyList<String>> bothLists() {
        List<MyList<String>> result = new ArrayList<>();
        result.add(new MyArrayList<>());
        result.add(new MyLinkedList<>());
        return result;
    }

    @Test
    void addAndGet() {
        for (MyList<String> list : bothLists()) {
            list.add("A");
            list.add("B");
            assertEquals("B", list.get(1));   
            assertEquals(2, list.size());
        }
    }

    @Test
    void addByIndex() {
        for (MyList<String> list : bothLists()) {
            list.add("A");
            list.add("C");
            list.add(1, "B");
            assertEquals("[A, B, C]", list.toString());
        }
    }

    @Test
    void addAllFromArray() {
        for (MyList<String> list : bothLists()) {
            list.addAll(new String[]{"A", "B"});
            list.addAll(1, new String[]{"X"});
            assertEquals("[A, X, B]", list.toString());
        }
    }

    @Test
    void addAllAcceptsSubtypes() {
        // ? extends E: у список Number можна додати списки Integer і Double
        checkSubtypes(new MyArrayList<Number>());
        checkSubtypes(new MyLinkedList<Number>());
    }

    private void checkSubtypes(MyList<Number> numbers) {
        numbers.addAll(Arrays.asList(1, 2));
        numbers.addAll(Arrays.asList(1.5, 2.5));
        assertEquals(4, numbers.size());
        assertEquals("[1, 2, 1.5, 2.5]", numbers.toString());
    }

    @Test
    void setAndRemove() {
        for (MyList<String> list : bothLists()) {
            list.add("A");
            list.add("B");
            list.add("C");
            list.set(0, "Z");
            assertEquals("B", list.remove(1));
            assertEquals("[Z, C]", list.toString());
        }
    }

    @Test
    void indexOf() {
        for (MyList<String> list : bothLists()) {
            list.add("A");
            list.add("B");
            assertEquals(1, list.indexOf("B"));
            assertEquals(-1, list.indexOf("?"));
        }
    }

    @Test
    void wrongIndexThrowsException() {
        for (MyList<String> list : bothLists()) {
            list.add("A");
            assertThrows(IndexOutOfBoundsException.class, () -> list.get(5));
            assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        }
    }

    @Test
    void forEachVisitsAllElements() {
        for (MyList<String> list : bothLists()) {
            list.add("A");
            list.add("B");
            String all = "";
            for (String s : list) {
                all = all + s;
            }
            assertEquals("AB", all);
        }
    }

    @Test
    void copyToAcceptsSuperTypes() {
        // ? super E: рядки можна скопіювати в List<Object>
        for (MyList<String> list : bothLists()) {
            list.add("A");
            list.add("B");
            List<Object> target = new ArrayList<>();
            list.copyTo(target);
            assertEquals(2, target.size());
        }
    }

    @Test
    void onlyArrayListIsRandomAccess() {
        assertTrue(new MyArrayList<String>() instanceof RandomAccess);
        assertFalse(((Object) new MyLinkedList<String>()) instanceof RandomAccess);
    }

    @Test
    void manyElements() {
        checkThousand(new MyArrayList<Integer>());
        checkThousand(new MyLinkedList<Integer>());
    }

    private void checkThousand(MyList<Integer> list) {
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }
        assertEquals(1000, list.size());
        assertEquals(999, list.get(999));
    }
}
