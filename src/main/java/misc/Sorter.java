package misc;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

import java.util.Iterator;

public class Sorter {
    /**
     * This method takes the input list and returns the top k elements
     * in sorted order.
     *
     * So, the first element in the output list should be the "smallest"
     * element; the last element should be the "largest".
     *
     * If the input list contains fewer than 'k' elements, return
     * a list containing all input.length elements in sorted order.
     *
     * This method must not modify the input list.
     *
     * @throws IllegalArgumentException  if k < 0
     * @throws IllegalArgumentException  if input is null
     */
    public static <T extends Comparable<T>> IList<T> topKSort(int k, IList<T> input) {
        if (k < 0) {
           throw new IllegalArgumentException("K must be greater than zero");
        }
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        if (k > input.size()) {
            k = input.size();
        }
        IList<T> output = new DoubleLinkedList<>();

        if (k == 0) {
            return output;
        }

        IPriorityQueue<T> heap = new ArrayHeap<>();

        Iterator<T> iterator = input.iterator();
        for (int i = 0; i < k; i++) {
            heap.insert(iterator.next());
        }
        // does not run if k = input.size()
        while (iterator.hasNext()) {
            T current = iterator.next();
            if (current.compareTo(heap.peekMin()) > 0) {
                heap.insert(current);
                heap.removeMin();
            }
        }
        while (!heap.isEmpty()) {
            output.add(heap.removeMin());
        }
        return output;
    }
}
