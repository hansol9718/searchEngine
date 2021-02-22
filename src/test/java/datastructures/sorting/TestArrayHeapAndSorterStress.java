package datastructures.sorting;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import misc.BaseTest;
import misc.Sorter;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 *
 * As a general rule of thumb,
 * any tests in this file should be manipulating heaps
 * or lists containing roughly several hundred thousand items.
 *
 *
 * As a reminder, you may use classes from java.util.* when writing unit tests.
 * You may find Collections.sort(...) to be particularly helpful
 */
public class TestArrayHeapAndSorterStress extends BaseTest {

    @Test(timeout=2*SECOND)
    public void testRemoveMinStress() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        for (int i = 499999; i >= 0; i--) {
            heap.insert(i);
        }
        for (int i = 0; i < 500000; i++) {
            assertEquals(heap.removeMin(), i);
        }
    }

    @Test(timeout=2*SECOND)
    public void testPeekMinStress() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        for (int i = 499999; i >= 0; i--) {
            heap.insert(i);
        }
        for (int i = 0; i < 500000; i++) {
            assertEquals(heap.peekMin(), 0);
        }
    }

    @Test(timeout=2*SECOND)
    public void testInsertStress() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        for (int i = 499999; i >= 0; i--) {
            heap.insert(i);
        }
        for (int i = 0; i < 500000; i++) {
            assertEquals(heap.removeMin(), i);
        }
        for (int i = 499999; i >= 0; i--) {
            heap.insert(i);
        }
    }

    @Test(timeout=SECOND)
    public void testBigInputSmallK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 500000; i++) {
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(499995 + i, top.get(i));
        }
    }

    @Test(timeout=10*SECOND)
    public void testBigInputBigK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 200000; i++) {
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(100000, list);
        assertEquals(100000, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(100000 + i, top.get(i));
        }
    }


}
