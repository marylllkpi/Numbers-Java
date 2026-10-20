import java.util.Objects;
import java.util.RandomAccess;

public class MyArrayList implements MyList, RandomAccess {
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

    @Override
    public void add(Object e) {
        ensureCapacity(size + 1);
        data[size++] = e;
    }

    @Override
    public void add(int index, Object element) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }

    @Override
    public void addAll(Object[] c) {
        addAll(size, c);
    }

    @Override
    public void addAll(int index, Object[] c) {
        checkIndexForAdd(index);
        Objects.requireNonNull(c, "array is null");
        if (c.length == 0) {
            return;
        }
        ensureCapacity(size + c.length);
        System.arraycopy(data, index, data, index + c.length, size - index);
        System.arraycopy(c, 0, data, index, c.length);
        size += c.length;
    }

    @Override
    public Object get(int index) {
        checkIndex(index);
        return data[index];
    }

    @Override
    public Object remove(int index) {
        checkIndex(index);
        Object old = data[index];
        System.arraycopy(data, index + 1, data, index, size - index - 1);
        data[--size] = null; 
        return old;
    }

    @Override
    public void set(int index, Object element) {
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
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[i]);
        }
        return sb.append(']').toString();
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
