import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int size;

    public Deque() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.first == null;
    }

    public int size() {
        return this.size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        this.first = new Node(null, item, this.first);
        if (this.first.next != null) {
            this.first.next.prev = this.first;
        }
        if (this.last == null) {
            this.last = this.first;
        }
        this.size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        this.last = new Node(this.last, item, null);
        if (this.last.prev != null) {
            this.last.prev.next = this.last;
        }
        if (this.first == null) {
            this.first = this.last;
        }
        this.size++;
    }

    public Item removeFirst() {
        if (this.first == null) {
            throw new NoSuchElementException();
        }
        Item firstValue = this.first.val;
        this.first = this.first.next;
        this.size--;
        if (this.first == null) {
            this.last = null;
        } else {
            this.first.prev = null;
        }
        return firstValue;
    }

    public Item removeLast() {
        if (this.last == null) {
            throw new NoSuchElementException();
        }
        Item lastValue = this.last.val;
        this.last = this.last.prev;
        this.size--;
        if (this.last == null) {
            this.first = null;
        } else {
            this.last.next = null;
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
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            Item value = this.curr.val;
            this.curr = this.curr.next;
            return value;
        }

        public boolean hasNext() {
            return this.curr != null;
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
