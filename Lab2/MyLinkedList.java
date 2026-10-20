import java.util.Objects;

public class MyLinkedList implements MyList {
    private static class Node {
        Object item;
        Node prev;
        Node next;

        Node(Node prev, Object item, Node next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    @Override
    public void add(Object e) {
        linkBefore(e, null);
    }

    @Override
    public void add(int index, Object element) {
        checkIndexForAdd(index);
        linkBefore(element, index == size ? null : node(index));
    }

    @Override
    public void addAll(Object[] c) {
        addAll(size, c);
    }

    @Override
    public void addAll(int index, Object[] c) {
        checkIndexForAdd(index);
        Objects.requireNonNull(c, "array is null");
        Node succ = (index == size) ? null : node(index);
        for (Object e : c) {
            linkBefore(e, succ);
        }
    }

    @Override
    public Object get(int index) {
        checkIndex(index);
        return node(index).item;
    }

    @Override
    public Object remove(int index) {
        checkIndex(index);
        Node x = node(index);
        Object old = x.item;
        if (x.prev == null) head = x.next; else x.prev.next = x.next;
        if (x.next == null) tail = x.prev; else x.next.prev = x.prev;
        x.prev = null;
        x.next = null;
        x.item = null;
        size--;
        return old;
    }

    @Override
    public void set(int index, Object element) {
        checkIndex(index);
        node(index).item = element;
    }

    @Override
    public int indexOf(Object o) {
        int i = 0;
        for (Node x = head; x != null; x = x.next, i++) {
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
        for (Node x = head; x != null; x = x.next) {
            result[i++] = x.item;
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node x = head; x != null; x = x.next) {
            if (x != head) sb.append(", ");
            sb.append(x.item);
        }
        return sb.append(']').toString();
    }

    private void linkBefore(Object e, Node succ) {
        Node pred = (succ == null) ? tail : succ.prev;
        Node newNode = new Node(pred, e, succ);
        if (pred == null) head = newNode; else pred.next = newNode;
        if (succ == null) tail = newNode; else succ.prev = newNode;
        size++;
    }

    private Node node(int index) {
        Node x;
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
