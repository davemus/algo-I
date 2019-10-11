import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] bag;
    private int capacity;
    private int tail;

    @SuppressWarnings("unchecked")
    public RandomizedQueue() {
        bag = (Item[]) new Object[2];
        capacity = 2;
        tail = -1;
    }

    private void resizeIfNeeded() {
        if (tail == capacity) {
            resize(2);
        }
        else if (tail == capacity / 4 && capacity > 2) {
            resize(0.5);
        }
    }

    @SuppressWarnings("unchecked")
    private void resize(double sizeMultiplier) {
        int oldCapacity = capacity;
        capacity = (int) Math.ceil(capacity * sizeMultiplier);
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < tail; i++) {
            copy[i] = bag[i];
        }
        bag = copy;
    }

    public int size() {
        return tail + 1;
    }

    public boolean isEmpty() {
        return tail == -1;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        tail++;
        resizeIfNeeded();
        bag[this.tail] = item;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int itemNumberToRemove = StdRandom.uniform(tail + 1);
        Item item = bag[itemNumberToRemove];
        bag[itemNumberToRemove] = bag[tail];
        tail--;
        resizeIfNeeded();
        return item;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return bag[StdRandom.uniform(this.tail + 1)];
    }

    public Iterator<Item> iterator() {
        return new RandQueueIterator(this);
    }

    private class RandQueueIterator implements Iterator<Item> {
        private final Item[] items;
        private int ptr;

        @SuppressWarnings("unchecked")
        RandQueueIterator(RandomizedQueue<Item> randDeque) {
            items = (Item[]) new Object[randDeque.size()];
            for (int i = 0; i < randDeque.size(); i++) {
                items[i] = randDeque.bag[i];
            }
            StdRandom.shuffle(items);
            ptr = -1;
        }

        public Item next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            ptr++;
            return items[ptr];
        }

        public boolean hasNext() {
            return ptr < items.length - 1;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> randQueue = new RandomizedQueue<>();
        for (int i = 1; i <= 1000; i++) {
            randQueue.enqueue(i);
        }
        for (int i = 1; i <= 1000; i++) {
            randQueue.dequeue();
        }
        assert randQueue.isEmpty();
    }
}
