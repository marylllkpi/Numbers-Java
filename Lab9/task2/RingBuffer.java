public class RingBuffer<T> {

    private static class Node<T> {
        T value;
        Node<T> next;
    }

    private Node<T> head;
    private Node<T> tail;

    public RingBuffer(int capacity) {
        Node<T> first = new Node<>();
        Node<T> last = first;
        for (int i = 0; i < capacity; i++) {
            last.next = new Node<>();
            last = last.next;
        }
        last.next = first; 
        head = first;
        tail = first;
    }

    public synchronized void put(T value) throws InterruptedException {
        while (tail.next == head) {   
            wait();
        }
        tail.value = value;
        tail = tail.next;
        notifyAll();                  
    }

    public synchronized T take() throws InterruptedException {
        while (head == tail) {        
            wait();
        }
        T value = head.value;
        head.value = null;
        head = head.next;
        notifyAll();              
        return value;
    }
}
