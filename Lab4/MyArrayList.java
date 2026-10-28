import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;

public class MyArrayList<E> implements MyList<E>, RandomAccess {
    private static final int DEFAULT_CAPACITY = 10;

    private Object[] data;
    private int size;

    public MyArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public MyArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Negative capacity: " + initialCapacity);
        }
        data = new Object[Math.max(initialCapacity, 1)];
    }

    /** Створює список із елементів колекції (E або підтипів E). */
    public MyArrayList(Collection<? extends E> c) {
        this(Math.max(c.size(), 1));
        addAll(c);
    }

    @Override
    public void add(E e) {
        ensureCapacity(size + 1);
        data[size++] = e;
    }

    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }

    @Override
    public void addAll(E[] c) {
        addAll(size, c);
    }

    @Override
    public void addAll(int index, E[] c) {
        insertAll(index, c);
    }

    @Override
    public void addAll(Collection<? extends E> c) {
        addAll(size, c);
    }

    @Override
    public void addAll(int index, Collection<? extends E> c) {
        Objects.requireNonNull(c, "collection is null");
        insertAll(index, c.toArray());
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return elementAt(index);
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        E old = elementAt(index);
        System.arraycopy(data, index + 1, data, index, size - index - 1);
        data[--size] = null;
        return old;
    }

    @Override
    public void set(int index, E element) {
        checkIndex(index);
        data[index] = element;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, data[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Object[] toArray() {
        Object[] copy = new Object[size];
        System.arraycopy(data, 0, copy, 0, size);
        return copy;
    }

    @Override
    public void copyTo(Collection<? super E> target) {
        for (E e : this) {
            target.add(e);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int next = 0;

            @Override
            public boolean hasNext() {
                return next < size;
            }

            @Override
            public E next() {
                if (next >= size) throw new NoSuchElementException();
                return elementAt(next++);
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[i]);
        }
        return sb.append(']').toString();
    }

    @SuppressWarnings("unchecked")
    private E elementAt(int index) {
        return (E) data[index];
    }

    private void insertAll(int index, Object[] items) {
        checkIndexForAdd(index);
        Objects.requireNonNull(items, "array is null");
        if (items.length == 0) {
            return;
        }
        ensureCapacity(size + items.length);
        System.arraycopy(data, index, data, index + items.length, size - index);
        System.arraycopy(items, 0, data, index, items.length);
        size += items.length;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > data.length) {
            int newCapacity = Math.max(minCapacity, data.length + (data.length >> 1) + 1);
            Object[] bigger = new Object[newCapacity];
            System.arraycopy(data, 0, bigger, 0, size);
            data = bigger;
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}
