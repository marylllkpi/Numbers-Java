import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

class MyListsTest {

    @Test
    void sumWorksForIntegersAndDoubles() {
        MyList<Integer> ints = new MyArrayList<>(Arrays.asList(1, 2, 3));
        MyList<Double> doubles = new MyLinkedList<>(Arrays.asList(1.5, 2.5));
        assertEquals(6.0, MyLists.sum(ints));
        assertEquals(4.0, MyLists.sum(doubles));
    }

    @Test
    void copyPutsDifferentNumbersIntoOneList() {
        MyList<Integer> ints = new MyArrayList<>(Arrays.asList(1, 2, 3));
        MyList<Double> doubles = new MyArrayList<>(Arrays.asList(1.5, 2.5));
        MyList<Number> target = new MyArrayList<>();
        MyLists.copy(target, ints);
        MyLists.copy(target, doubles);
        assertEquals(5, target.size());
    }

    @Test
    void maxOfNumbersAndWords() {
        MyList<Integer> ints = new MyArrayList<>(Arrays.asList(5, 9, 2));
        MyList<String> words = new MyLinkedList<>(Arrays.asList("apple", "pear", "fig"));
        int biggest = MyLists.max(ints);
        String last = MyLists.max(words);
        assertEquals(9, biggest);
        assertEquals("pear", last);
    }

    @Test
    void maxOfEmptyListThrowsException() {
        MyList<Integer> empty = new MyArrayList<>();
        assertThrows(NoSuchElementException.class, () -> MyLists.max(empty));
    }
}
