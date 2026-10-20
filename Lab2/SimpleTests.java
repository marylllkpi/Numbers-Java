public class SimpleTests {
    static int failed = 0;

    static void check(String name, boolean ok) {
        System.out.println((ok ? "OK    " : "FAIL  ") + name);
        if (!ok) failed++;
    }

    public static void main(String[] args) {
        testList("MyArrayList", new MyArrayList());
        testList("MyLinkedList", new MyLinkedList());
        testSet();
        testCache();
        testJson();
        System.out.println(failed == 0 ? "\nAll tests passed." : "\nFailed tests: " + failed);
    }

    static void testList(String n, MyList list) {
        list.add("A");
        list.add("C");
        list.add(1, "B");
        check(n + ": add / add(index)", java.util.Arrays.equals(list.toArray(), new Object[]{"A", "B", "C"}));
        list.addAll(new Object[]{"D", "E"});
        list.addAll(1, new Object[]{"X", "Y"});
        check(n + ": addAll", java.util.Arrays.equals(list.toArray(), new Object[]{"A", "X", "Y", "B", "C", "D", "E"}));
        check(n + ": get", "B".equals(list.get(3)));
        list.set(0, "Z");
        check(n + ": set", "Z".equals(list.get(0)));
        check(n + ": remove", "X".equals(list.remove(1)) && list.size() == 6);
        check(n + ": indexOf", list.indexOf("C") == 3 && list.indexOf("?") == -1);
        boolean thrown = false;
        try { list.get(100); } catch (IndexOutOfBoundsException e) { thrown = true; }
        check(n + ": bad index throws exception", thrown);
        MyList big = n.equals("MyArrayList") ? new MyArrayList() : new MyLinkedList();
        for (int i = 0; i < 1000; i++) big.add(i);
        check(n + ": 1000 elements", big.size() == 1000 && big.get(999).equals(999));
    }

    static void testSet() {
        MyLinkedHashSet<String> s = new MyLinkedHashSet<>();
        check("LinkedHashSet: add", s.add("b") && s.add("a") && !s.add("b"));
        check("LinkedHashSet: insertion order", java.util.Arrays.equals(s.toArray(), new Object[]{"b", "a"}));
        check("LinkedHashSet: contains / remove", s.contains("a") && s.remove("a") && !s.contains("a"));
        MyLinkedHashSet<Integer> big = new MyLinkedHashSet<>();
        for (int i = 500; i > 0; i--) big.add(i);
        Object[] arr = big.toArray();
        check("LinkedHashSet: 500 elements, order kept", big.size() == 500 && arr[0].equals(500) && arr[499].equals(1));
    }

    static void testCache() {
        LruCache<String, Integer> c = new LruCache<>(2);
        boolean npe = false;
        try { c.put("k", null); } catch (NullPointerException e) { npe = true; }
        check("Cache: null is forbidden", npe);
        c.put("a", 1);
        c.put("b", 2);
        c.get("a");
        c.put("c", 3);
        check("Cache: least recently used (b) evicted", c.containsKey("a") && !c.containsKey("b") && c.containsKey("c"));
        long[] time = {0};
        LruCache<String, Integer> t = new LruCache<>(5, 100, () -> time[0]);
        t.put("x", 1);
        time[0] = 99;
        boolean before = t.get("x") != null;
        time[0] = 100;
        check("Cache: entry expires after TTL", before && t.get("x") == null);
    }

    static void testJson() {
        Person p = new Person("O'Neil \"Mac\"", "Maria", 20);
        check("JSON: round trip gives equal object", p.equals(PersonJson.fromJson(PersonJson.toJson(p))));
    }
}
