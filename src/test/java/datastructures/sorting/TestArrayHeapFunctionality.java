package datastructures.sorting;

import static org.junit.Assert.assertTrue;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import org.junit.Test;
import static org.junit.Assert.fail;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }

    @Test (timeout=SECOND)
    public void testRemoveMinThrowsEmptyContainerException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.removeMin();
            fail("expected EmptyContainerException");
        } catch (EmptyContainerException e) {
            // do nothing - passed test
        }
    }

    @Test (timeout=SECOND)
    public void testRemoveMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 8; i >= 1; i--) {
            heap.insert(i);
        }
        assertEquals(heap.removeMin(), 1);
        assertEquals(heap.removeMin(), 2);
        assertEquals(heap.removeMin(), 3);
    }

    @Test (timeout=SECOND)
    public void testRemoveMinAdvanced() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 200; i >= 1; i--) {
            heap.insert(i);
        }
        for (int i = 1; i <= 200; i++) {
            assertEquals(heap.removeMin(), i);
        }
    }

    @Test (timeout=SECOND)
    public void testPeekMinThrowsEmptyContainerException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.peekMin();
            fail("expected EmptyContainerException");
        } catch (EmptyContainerException e) {
            // do nothing - passed test
        }
    }

    @Test (timeout=SECOND)
    public void testPeekMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 8; i >= 1; i--) {
            heap.insert(i);
        }
        assertEquals(heap.peekMin(), 1);
    }


    /* Tests for insert()
     * @throws IllegalArgumentException  if the item is null
     * */

    @Test (timeout=SECOND)
    public void testInsertNullElement() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.insert(null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // do nothing - passed test
        }
    }

    @Test (timeout=SECOND)
    public void testInsertSmallest() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 8; i >= 2; i--) {
            heap.insert(i);
        }
        heap.insert(1);
        assertEquals(heap.removeMin(), 1);
    }

    @Test (timeout=SECOND)
    public void testInsertBiggest() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 8; i >= 2; i--) {
            heap.insert(i);
        }
        heap.insert(10);

        int val = -1;
        while (!heap.isEmpty()) {
            val = heap.removeMin();
        }
        assertEquals(val, 10);

    }

    @Test (timeout=SECOND)
    public void testInsertMiddleElement() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 1; i <= 3; i++) {
            heap.insert(i);
        }
        heap.insert(5);
        heap.insert(6);

        heap.insert(4);
        heap.removeMin();
        heap.removeMin();
        heap.removeMin();
        assertEquals(heap.removeMin(), 4);
    }

    @Test (timeout=SECOND)
    public void testInsertCanResizeArray() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 200; i >= 1; i--) {
            heap.insert(i);
        }
        assertEquals(heap.peekMin(), 1);
    }
}

