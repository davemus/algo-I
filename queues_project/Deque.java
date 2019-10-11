import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int size;

    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return this.size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        first = new Node(null, item, first);
        if (first.next != null) {
            first.next.prev = first;
        }
        if (last == null) {
            last = first;
        }
        size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        last = new Node(last, item, null);
        if (last.prev != null) {
            last.prev.next = last;
        }
        if (first == null) {
            first = last;
        }
        size++;
    }

    public Item removeFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        Item firstValue = first.val;
        first = first.next;
        size--;
        if (first == null) {
            last = null;
        } else {
            first.prev = null;
        }
        return firstValue;
    }

    public Item removeLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        Item lastValue = last.val;
        last = last.prev;
        size--;
        if (last == null) {
            first = null;
        } else {
            last.next = null;
        }
        return lastValue;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator(this);
    }

    private class Node {
        private Node prev;
        private final Item val;
        private Node next;

        public Node(Node prev, Item val, Node next) {
            this.prev = prev;
            this.val = val;
            this.next = next;
        }
    }

    private class DequeIterator implements Iterator<Item> {
        private Node curr;

        DequeIterator(Deque<Item> deque) {
            this.curr = deque.first;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item value = curr.val;
            curr = curr.next;
            return value;
        }

        public boolean hasNext() {
            return curr != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        int tmp;
        // works as stack
        Deque<Integer> deque = new Deque<>();
        for (int i = 1; i <= 5; i++) {
            deque.addFirst(i);
        }
        for (int i = 1; i <= 5; i++) {
            tmp = deque.removeLast();
            assert tmp == i;
        }
        assert deque.isEmpty();
        // works as queue
        deque = new Deque<>();
        for (int i = 1; i <= 5; i++) {
            deque.addLast(i);
        }
        for (int i = 1; i <= 5; i++) {
            tmp = deque.removeFirst();
            assert tmp == i;
        }
        assert deque.isEmpty();
        // works as deque
        deque = new Deque<>();
        for (int i = 1; i <= 5; i++) {
            if (i % 2 == 0) {
                deque.addLast(i);
            }
            else {
                deque.addFirst(i);
            }
        }
        for (int i = 5; i >= 1; i--) {
            if (i % 2 != 0) {
                tmp = deque.removeFirst();
            }
            else {
                tmp = deque.removeLast();
            }
            assert tmp == i;
        }
        assert deque.isEmpty();
        // test i failed
        deque = new Deque<>();
        deque.addFirst(2);
        deque.removeLast();
        deque.addFirst(4);
        deque.removeLast();
        deque.addFirst(6);
        deque.removeLast();
        deque.addFirst(8);
        deque.removeLast();
        assert deque.isEmpty();
    }
}
