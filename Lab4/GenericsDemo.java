import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericsDemo {
    public static void main(String[] args) {
        System.out.println("--- 1. Type safety: MyList<String> ---");
        MyList<String> words = new MyArrayList<>();
        words.add("apple");
        words.add("banana");
        String first = words.get(0);             
        System.out.println("first word = " + first + ", list = " + words);

        System.out.println();
        System.out.println("--- 2. addAll(Collection<? extends E>) ---");
        MyList<Number> numbers = new MyLinkedList<>();
        List<Integer> ints = Arrays.asList(1, 2, 3);
        List<Double> doubles = Arrays.asList(1.5, 2.5);
        numbers.addAll(ints);                     // Integer - це Number
        numbers.addAll(doubles);                  // Double - це Number
        System.out.println("MyList<Number> after adding Integers and Doubles: " + numbers);

        System.out.println();
        System.out.println("--- 3. copyTo(Collection<? super E>) ---");
        MyList<Integer> intList = new MyArrayList<>(ints);
        List<Number> numberTarget = new ArrayList<>();
        List<Object> objectTarget = new ArrayList<>();
        intList.copyTo(numberTarget);             // List<Number> підходить
        intList.copyTo(objectTarget);             // List<Object> теж підходить
        System.out.println("copied to List<Number>: " + numberTarget);
        System.out.println("copied to List<Object>: " + objectTarget);

        System.out.println();
        System.out.println("--- 4. MyLists: sum, copy, max, printAll ---");
        MyList<Double> doubleList = new MyArrayList<>(doubles);
        System.out.println("sum(MyList<Integer>) = " + MyLists.sum(intList));
        System.out.println("sum(MyList<Double>)  = " + MyLists.sum(doubleList));
        System.out.println("sum(MyList<Number>)  = " + MyLists.sum(numbers));
        MyList<Number> allNumbers = new MyArrayList<>();
        MyLists.copy(allNumbers, intList);        // Integer -> Number
        MyLists.copy(allNumbers, doubleList);     // Double -> Number
        System.out.println("copy(...) into MyList<Number>: " + allNumbers);
        System.out.println("max of ints    = " + MyLists.max(intList));
        System.out.println("max of words   = " + MyLists.max(words));
        System.out.println("printAll(MyList<?>):");
        MyLists.printAll(words);
        MyLists.printAll(intList);

        System.out.println();
        System.out.println("--- 5. for-each over MyList ---");
        int total = 0;
        for (int n : intList) {
            total += n;
        }
        System.out.println("total = " + total);

        System.out.println();
        System.out.println("--- 6. MyLinkedHashSet<E> ---");
        MyLinkedHashSet<String> set = new MyLinkedHashSet<>(Arrays.asList("b", "a", "b", "c"));
        System.out.println("set from list [b, a, b, c] = " + set);
        System.out.println("addAll([c, d]) changed the set: " + set.addAll(Arrays.asList("c", "d")));
        System.out.println("set = " + set);
        System.out.println("containsAll([a, d]) = " + set.containsAll(Arrays.asList("a", "d")));
        List<Object> copy = new ArrayList<>();
        set.copyTo(copy);
        System.out.println("copyTo(List<Object>) = " + copy);

        System.out.println();
        System.out.println("--- 7. LruCache<K, V>.putAll(Map<? extends K, ? extends V>) ---");
        LruCache<String, Number> cache = new LruCache<>(3);
        Map<String, Integer> data = new HashMap<>();
        data.put("one", 1);
        data.put("two", 2);
        cache.putAll(data);                      
        System.out.println("size = " + cache.size() + ", get(one) = " + cache.get("one"));
    }
}
