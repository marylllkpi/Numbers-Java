import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedHashSet<E> implements Iterable<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private static class Entry<E> {
        final E value;
        final int hash;
        Entry<E> nextInBucket; // ланцюжок усередині кошика
        Entry<E> before;       // попередній у порядку вставки
        Entry<E> after;        // наступний у порядку вставки

        Entry(E value, int hash) {
            this.value = value;
            this.hash = hash;
        }
    }

    private Entry<E>[] table;
    private Entry<E> head;
    private Entry<E> tail;
    private int size;
    private int modCount;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Entry<E>[]) new Entry<?>[DEFAULT_CAPACITY];
    }

    public MyLinkedHashSet(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    public boolean add(E e) {
        int h = hash(e);
        int idx = indexFor(h, table.length);
        for (Entry<E> x = table[idx]; x != null; x = x.nextInBucket) {
            if (x.hash == h && equal(e, x.value)) {
                return false;
            }
        }
        Entry<E> entry = new Entry<>(e, h);
        entry.nextInBucket = table[idx];
        table[idx] = entry;
        linkLast(entry);
        size++;
        modCount++;
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    public void copyTo(Collection<? super E> target) {
        for (E e : this) {
            target.add(e);
        }
    }

    public boolean contains(Object o) {
        return find(o) != null;
    }

    public boolean remove(Object o) {
        int h = hash(o);
        int idx = indexFor(h, table.length);
        Entry<E> prev = null;
        for (Entry<E> x = table[idx]; x != null; prev = x, x = x.nextInBucket) {
            if (x.hash == h && equal(o, x.value)) {
                if (prev == null) table[idx] = x.nextInBucket; else prev.nextInBucket = x.nextInBucket;
                unlink(x);
                size--;
                modCount++;
                return true;
            }
        }
        return false;
    }

    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
        modCount++;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Entry<E> x = head; x != null; x = x.after) {
            result[i++] = x.value;
        }
        return result;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Entry<E> next = head;
            private Entry<E> last;
            private int expectedModCount = modCount;

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public E next() {
                if (expectedModCount != modCount) throw new ConcurrentModificationException();
                if (next == null) throw new NoSuchElementException();
                last = next;
                next = next.after;
                return last.value;
            }

            @Override
            public void remove() {
                if (last == null) throw new IllegalStateException();
                if (expectedModCount != modCount) throw new ConcurrentModificationException();
                MyLinkedHashSet.this.remove(last.value);
                last = null;
                expectedModCount = modCount;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Entry<E> x = head; x != null; x = x.after) {
            if (x != head) sb.append(", ");
            sb.append(x.value);
        }
        return sb.append(']').toString();
    }

    private Entry<E> find(Object o) {
        int h = hash(o);
        for (Entry<E> x = table[indexFor(h, table.length)]; x != null; x = x.nextInBucket) {
            if (x.hash == h && equal(o, x.value)) {
                return x;
            }
        }
        return null;
    }

    private void linkLast(Entry<E> e) {
        e.before = tail;
        if (tail == null) head = e; else tail.after = e;
        tail = e;
    }

    private void unlink(Entry<E> e) {
        if (e.before == null) head = e.after; else e.before.after = e.after;
        if (e.after == null) tail = e.before; else e.after.before = e.before;
        e.before = null;
        e.after = null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<E>[] newTable = (Entry<E>[]) new Entry<?>[table.length * 2];
        for (Entry<E> x = head; x != null; x = x.after) {
            int idx = indexFor(x.hash, newTable.length);
            x.nextInBucket = newTable[idx];
            newTable[idx] = x;
        }
        table = newTable;
    }

    private static int hash(Object o) {
        int h = (o == null) ? 0 : o.hashCode();
        return h ^ (h >>> 16);
    }

    private static int indexFor(int hash, int length) {
        return hash & (length - 1); // length — степінь двійки
    }

    private static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }
}
