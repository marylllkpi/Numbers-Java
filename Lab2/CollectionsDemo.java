import java.util.Arrays;
import java.util.RandomAccess;

public class CollectionsDemo {
    public static void main(String[] args) {
        System.out.println("MyArrayList instanceof RandomAccess: " + (new MyArrayList() instanceof RandomAccess));
        System.out.println("MyLinkedList instanceof RandomAccess: " + ((Object) new MyLinkedList() instanceof RandomAccess));
        System.out.println();
        demoList("MyArrayList", new MyArrayList());
        demoList("MyLinkedList", new MyLinkedList());

        System.out.println("=== MyLinkedHashSet ===");
        MyLinkedHashSet<String> set = new MyLinkedHashSet<>();
        for (String s : new String[]{"banana", "apple", "cherry", "apple", "date"}) {
            System.out.println("add(" + s + ") -> " + set.add(s));
        }
        System.out.println("set = " + set + ", size = " + set.size());
        System.out.println("contains(cherry) = " + set.contains("cherry"));
        System.out.println("remove(apple) -> " + set.remove("apple"));
        set.add("apple");
        System.out.println("after remove and add(apple) again: " + set);

        MyLinkedHashSet<Integer> big = new MyLinkedHashSet<>();
        for (int i = 100; i > 0; i--) big.add(i * 7);
        System.out.println("100 elements (table resized), first five: "
                + Arrays.toString(Arrays.copyOf(big.toArray(), 5)) + ", size = " + big.size());
    }

    private static void demoList(String title, MyList list) {
        System.out.println("=== " + title + " ===");
        list.add("A");
        list.add("B");
        list.add("C");
        System.out.println("after add A, B, C:    " + list);
        list.add(1, "X");
        System.out.println("add(1, \"X\"):          " + list);
        list.addAll(new Object[]{"D", "E"});
        System.out.println("addAll([D, E]):       " + list);
        list.addAll(2, new Object[]{"P", "Q"});
        System.out.println("addAll(2, [P, Q]):    " + list);
        System.out.println("get(3) = " + list.get(3));
        list.set(0, "Z");
        System.out.println("set(0, \"Z\"):          " + list);
        System.out.println("remove(1) = " + list.remove(1) + " -> " + list);
        System.out.println("indexOf(\"E\") = " + list.indexOf("E") + ", indexOf(\"?\") = " + list.indexOf("?"));
        System.out.println("size = " + list.size() + ", toArray = " + Arrays.toString(list.toArray()));
        try {
            list.get(100);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("get(100) -> IndexOutOfBoundsException: " + e.getMessage());
        }
        System.out.println();
    }
}
