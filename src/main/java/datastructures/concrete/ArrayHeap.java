package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;


/**
 * @see IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int size;
    // Feel free to add more fields and constants.

    public ArrayHeap() {
        heap = makeArrayOfT(10);
        size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arraySize) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[arraySize]);
    }

    private boolean heapIsFull() {
        return this.size == heap.length;
    }

    private int parentIndex(int index) {
        return (index - 1)/NUM_CHILDREN;
    }

    private int nthChildIndex(int index, int n) {
        return index * NUM_CHILDREN + n;
    }


    private void resize() {
        T[] newHeap = this.makeArrayOfT(this.size * 2);
        System.arraycopy(this.heap, 0, newHeap, 0, this.size);
        this.heap = newHeap;
    }

    private void percolateUp(int index) {
        T element = heap[index];
        while (index > 0 && element.compareTo(heap[parentIndex(index)]) < 0) {
            heap[index] = heap[parentIndex(index)];
            index = parentIndex(index);
        }
        heap[index] = element;
    }

    private int indexOfSmallestChild(int index) {
        int smallestIndex = nthChildIndex(index, 1);
        int n = 2;
        int current = nthChildIndex(index, n);
        while (current < this.size && n <= NUM_CHILDREN) {
            // if heap at current is less than heap at smallestIndex,
            // set smallestIndex to current
            if (heap[current].compareTo(heap[smallestIndex]) < 0) {
                smallestIndex = current;
            }
            n++;
            current = nthChildIndex(index, n);
        }
        return smallestIndex;
    }

    private void percolateDown() {
        int childIndex;
        int index = 0;
        T temp = heap[index];
        boolean keepPercolating = true;
        while (keepPercolating && nthChildIndex(index, 1) < this.size){
            childIndex = indexOfSmallestChild(index);
            if (heap[childIndex].compareTo(temp) < 0) {
                heap[index] = heap[childIndex];
            } else {
                keepPercolating = false;
            }
            if (keepPercolating){
                index = childIndex;
            }
        }
        heap[index] = temp;
    }

    @Override
    public T removeMin() {
        if (size == 0) {
            throw new EmptyContainerException();
        }
        T temp = heap[0];
        heap[0] = heap[size - 1];
        size--;
        percolateDown();
        return temp;
    }

    @Override
    public T peekMin() {
        if (size == 0) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (heapIsFull()) {
            resize();
        }
        heap[size] = item;
        size++;
        percolateUp(size - 1);
    }

    @Override
    public int size() {
        return this.size;
    }
}
