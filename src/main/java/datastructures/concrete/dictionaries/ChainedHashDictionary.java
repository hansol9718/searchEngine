package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IDictionary and the assignment page for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int totalPairs; //keeps track of total number of key-value pairs
    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this.chains = makeArrayOfChains(10);
        this.totalPairs = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    @Override
    public V get(K key) {
        int keyHash = chainIndex(key);
        if (!containsKey(key)) {
            throw new NoSuchKeyException();
        }
        return chains[keyHash].get(key);
    }

    @Override
    public void put(K key, V value) {
        if (resize()) { //resize the array of chains if the load factor is too big
            IDictionary<K, V>[] newChains = makeArrayOfChains(chains.length * 2);
            for (int i = 0; i < chains.length; i++) {
                if (chains[i] != null) {
                    for (KVPair<K, V> pair : chains[i]) {
                        int newIndex = Math.abs(pair.getKey().hashCode() % newChains.length);
                        if (newChains[newIndex] == null) {
                            newChains[newIndex] = new ArrayDictionary<>();
                        }
                        newChains[newIndex].put(pair.getKey(), pair.getValue());
                    }
                }
            }
            chains = newChains;
        }
        int keyHash = chainIndex(key);
        if (!containsKey(key)) {
            totalPairs++;
        }
        if (chains[keyHash] == null) {
            chains[keyHash] = new ArrayDictionary<>();
        }
        chains[keyHash].put(key, value);
    }

    private boolean resize() { //calculate the load factor
        return (double) totalPairs / chains.length > 1;
    }

    private int chainIndex(K key) { //resolve null keys and set the chain index using hash codes and mod strategy.
        if (key == null) {
            return 0;
        } else {
            return Math.abs(key.hashCode() % chains.length);
        }
    }

    @Override
    public V remove(K key) {
        int keyHash = chainIndex(key);
        if (!this.containsKey(key)) {
            throw new NoSuchKeyException();
        }
        totalPairs--;
        return chains[keyHash].remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        int keyHash = chainIndex(key);
        if (chains[keyHash] == null) {
            return false;
        }
        return chains[keyHash].containsKey(key);
    }

    @Override
    public int size() {
        return totalPairs;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be
     *    true once the constructor is done setting up the class AND
     *    must *always* be true both before and after you call any
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private Iterator<KVPair<K, V>> iterator;
        private int currentIndex;


        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.currentIndex = 0;
            if (this.chains[currentIndex] != null) {
                this.iterator = chains[currentIndex].iterator();
            }

        }

        @Override
        public boolean hasNext() {
            for (int i = currentIndex; i < chains.length; i++) {
                if (iterator != null && iterator.hasNext()) {
                    return true;
                }
                if (currentIndex != chains.length - 1) {
                    currentIndex++;
                    if (chains[currentIndex] == null) {
                        iterator = null;
                    } else {
                        iterator = chains[currentIndex].iterator();
                    }
                }
            }
            return false;
        }

        @Override
        public KVPair<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                return iterator.next();
            }
        }
    }
}
