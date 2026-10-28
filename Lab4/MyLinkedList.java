import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class MyLinkedList<E> implements MyList<E> {
    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

        Node(Node<E> prev, E item, Node<E> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyLinkedList() {
    }

    public MyLinkedList(Collection<? extends E> c) {
        addAll(c);
    }

    @Override
    public void add(E e) {
        linkBefore(e, null);
    }

    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        linkBefore(element, index == size ? null : node(index));
    }

    @Override
    public void addAll(E[] c) {
        addAll(size, c);
    }

    @Override
    public void addAll(int index, E[] c) {
        checkIndexForAdd(index);
        Objects.requireNonNull(c, "array is null");
        Node<E> succ = (index == size) ? null : node(index);
        for (E e : c) {
            linkBefore(e, succ);
        }
    }

    @Override
    public void addAll(Collection<? extends E> c) {
        addAll(size, c);
    }

    @Override
    public void addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index);
        Objects.requireNonNull(c, "collection is null");
        Node<E> succ = (index == size) ? null : node(index);
        for (E e : c) {
            linkBefore(e, succ);
        }
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return node(index).item;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        Node<E> x = node(index);
        E old = x.item;
        if (x.prev == null) head = x.next; else x.prev.next = x.next;
        if (x.next == null) tail = x.prev; else x.next.prev = x.prev;
        x.prev = null;
        x.next = null;
        x.item = null;
        size--;
        return old;
    }

    @Override
    public void set(int index, E element) {
        checkIndex(index);
        node(index).item = element;
    }

    @Override
    public int indexOf(Object o) {
        int i = 0;
        for (Node<E> x = head; x != null; x = x.next, i++) {
            if (Objects.equals(o, x.item)) {
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
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = head; x != null; x = x.next) {
            result[i++] = x.item;
        }
        return result;
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
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) throw new NoSuchElementException();
                E item = current.item;
                current = current.next;
                return item;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> x = head; x != null; x = x.next) {
            if (x != head) sb.append(", ");
            sb.append(x.item);
        }
        return sb.append(']').toString();
    }

    private void linkBefore(E e, Node<E> succ) {
        Node<E> pred = (succ == null) ? tail : succ.prev;
        Node<E> newNode = new Node<>(pred, e, succ);
        if (pred == null) head = newNode; else pred.next = newNode;
        if (succ == null) tail = newNode; else succ.prev = newNode;
        size++;
    }

    private Node<E> node(int index) {
        Node<E> x;
        if (index < (size >> 1)) {
            x = head;
            for (int i = 0; i < index; i++) x = x.next;
        } else {
            x = tail;
            for (int i = size - 1; i > index; i--) x = x.prev;
        }
        return x;
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
