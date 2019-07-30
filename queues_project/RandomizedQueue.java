import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] bag;
    private int capacity;
    private int tail;

    public RandomizedQueue() {
        this.bag = (Item[]) new Object[2];
        this.capacity = 2;
        this.tail = -1;
    }

    private void resizeIfNeeded() {
        if (this.tail == this.capacity) {
            this.capacity = this.capacity * 2;
            Item[] copy = (Item[]) new Object[capacity];
            for (int i = 0; i < this.tail; i++) {
                copy[i] = this.bag[i];
            }
            this.bag = copy;
        }
        else if (this.tail == this.capacity / 4 && this.capacity >= 2) {
            this.capacity = this.capacity / 2;
            Item[] copy = (Item[]) new Object[capacity];
            for (int i = 0; i < capacity; i++) {
                copy[i] = this.bag[i];
            }
            this.bag = copy;
        }
    }

    public int size() {
        return tail + 1;
    }

    public boolean isEmpty() {
        return this.tail == -1;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        this.tail++;
        this.resizeIfNeeded();
        this.bag[this.tail] = item;
    }

    public Item dequeue() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        int itemNumberToRemove = StdRandom.uniform(this.tail + 1);
        Item item = this.bag[itemNumberToRemove];
        this.bag[itemNumberToRemove] = this.bag[tail];
        this.tail--;
        this.resizeIfNeeded();
        return item;
    }

    public Item sample() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.bag[StdRandom.uniform(this.tail + 1)];
    }

    public Iterator<Item> iterator() {
        return new RandQueueIterator(this);
    }

    private class RandQueueIterator implements Iterator<Item> {
        private final Item[] items;
        private int ptr;

        RandQueueIterator(RandomizedQueue<Item> randDeque) {
            this.items = (Item[]) new Object[randDeque.size()];
            for (int i = 0; i < randDeque.size(); i++) {
                this.items[i] = randDeque.bag[i];
            }
            StdRandom.shuffle(this.items);
            this.ptr = -1;
        }

        public Item next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.ptr++;
            return this.items[ptr];
        }

        public boolean hasNext() {
            return this.ptr < this.items.length - 1;
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
