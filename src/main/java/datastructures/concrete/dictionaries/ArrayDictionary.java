package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import java.util.NoSuchElementException;
import java.util.Iterator;

public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field.
    // We will be inspecting it in our private tests.
    private Pair<K, V>[] pairs;
    private int size;

    // You may add extra fields or helper methods though!

    public ArrayDictionary() {
        this.pairs = makeArrayOfPairs(10);
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    public V get(K key) {
        int index = indexAt(key);
        if (index == -1) {
            throw new NoSuchKeyException();
        } else {
            return pairs[index].value;
        }
    }

    private int indexAt(K key) {
        for (int i = 0; i < size; i++) {
            if (pairs[i].key != null && pairs[i].key.equals(key)) {
                return i;
            } else if (pairs[i].key == null && key == null) {
                return i;
            }
        } return -1;
    }

    @Override
    public void put(K key, V value) {
        int index = indexAt(key);
        if (index == -1) {
            if (size == pairs.length) {
                Pair<K, V>[] newArray = makeArrayOfPairs(pairs.length * 2);
                System.arraycopy(this.pairs, 0, newArray, 0, pairs.length);
                this.pairs = newArray;
            }
            this.pairs[this.size] = new Pair<>(key, value);
            size++;
        } else {
            pairs[index].value = value;
        }
    }

    @Override
    public V remove(K key) {
        int index = indexAt(key);
        if (index == -1) {
            throw new NoSuchKeyException();
        } else { /* 1. store the value that will be removed to return later
                    2. move the last key to a current key */
            V removedValue = pairs[index].value;
            pairs[index].key = pairs[size - 1].key;
            pairs[index].value = pairs[size -1].value;
            pairs[size] = null;
            size--;
            return removedValue;
        }
    }

    @Override
    public boolean containsKey(K key) {
        return indexAt(key) != -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<>(this.pairs, this.size);
    }


    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private Pair<K, V>[] iterPairs;
        private int index;
        private int size;

        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int size) {
            this.iterPairs = pairs;
            this.index = 0;
            this.size = size;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return index < size;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public KVPair<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            KVPair<K, V> current = new KVPair<>(iterPairs[index].key, iterPairs[index].value);
            index++;
            return current;

        }
    }
}
