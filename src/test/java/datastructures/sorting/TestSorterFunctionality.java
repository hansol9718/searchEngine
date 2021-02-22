package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Sorter;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSorterFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testKIsZero() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(0, list);
        assertEquals(top.size(), 0);
    }


    @Test (timeout=SECOND)
    public void testSorterThrowsExceptions() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        try{
            IList<Integer> top = Sorter.topKSort(5, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // do nothing
        }
        try{
            IList<Integer> top = Sorter.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // do nothing
        }
    }

    @Test (timeout=SECOND)
    public void testSorterHandlesBigK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(100, list);
        assertEquals(20, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }

    @Test (timeout=SECOND)
    public void testSorterHandlesSameElements() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(10);
        }

        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(10, top.get(i));
        }
    }

    @Test (timeout=SECOND)
    public void testSorterAdvanced() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 299; i >= 0; i--) {
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(100, list);
        assertEquals(100, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(200 + i, top.get(i));
        }

        list = new DoubleLinkedList<>();
        list.add(100);
        list.add(50);
        list.add(200);
        list.add(500);
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        top = Sorter.topKSort(5, list);
        assertEquals(top.get(0), 19);
        assertEquals(top.get(1), 50);
        assertEquals(top.get(2), 100);
        assertEquals(top.get(3), 200);
        assertEquals(top.get(4), 500);
    }

}
