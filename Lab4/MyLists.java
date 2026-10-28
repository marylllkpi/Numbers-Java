import java.util.NoSuchElementException;

public final class MyLists {
    private MyLists() { }

    public static double sum(MyList<? extends Number> list) {
        double result = 0;
        for (Number n : list) {
            result += n.doubleValue();
        }
        return result;
    }

    public static <T> void copy(MyList<? super T> dest, MyList<? extends T> src) {
        for (T item : src) {
            dest.add(item);
        }
    }

    public static void printAll(MyList<?> list) {
        int i = 0;
        for (Object o : list) {
            System.out.println("  [" + i++ + "] " + o);
        }
    }

    public static <T extends Comparable<? super T>> T max(MyList<? extends T> list) {
        if (list.size() == 0) {
            throw new NoSuchElementException("The list is empty");
        }
        T best = list.get(0);
        for (T item : list) {
            if (item.compareTo(best) > 0) {
                best = item;
            }
        }
        return best;
    }
}
