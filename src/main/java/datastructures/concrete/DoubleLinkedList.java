package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods:
 * @see datastructures.interfaces.IList
 * (You should be able to control/command+click "IList" above to open the file from IntelliJ.)
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }


    @Override
    public void add(T item) {
        Node<T> itemNode = new Node<>(this.back, item, null);
        if (back != null) {
            this.back.next = itemNode;
        }
        this.back = itemNode;
        if (this.front == null) {
            front = itemNode;
        }
        size++;
    }

    @Override
    public T remove() {
        if (this.size == 0) {
            throw new EmptyContainerException();
        }
        Node<T> temp = this.back;
        T value = temp.data;
        if (this.size == 1) {
            front = null;
            back = null;
        } else {
            back = back.prev;
            back.next = null;
        }

        this.size--;

        return value;
    }

    @Override
    public T get(int index) {
        checkBounds(index);
        return getNode(index).data;
    }

    @Override
    public void set(int index, T item) {
        checkBounds(index);
        Node<T> temp = getNode(index);
        Node<T> itemNode = new Node<>(item);

        if (size > 1) {
            itemNode.next = temp.next;
            itemNode.prev = temp.prev;
            if (index > 0) {
                temp.prev.next = itemNode;
            }
            if (index < size - 1) {
                temp.next.prev = itemNode;
            }
        }
        if (index == 0) {
            front = itemNode;
        }
        if (index == size - 1) {
            back = itemNode;
        }
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || index >= this.size + 1) {
            throw new IndexOutOfBoundsException();
        }
        if (index == size) {
            add(item);
        } else {
            Node<T> itemNode = new Node<>(item);
            if (index == 0) {
                itemNode.next = front;
                front.prev = itemNode;
                front = itemNode;
            } else {
                Node<T> temp = getNode(index);
                if (temp.prev != null) {
                    temp.prev.next = itemNode;
                    itemNode.prev = temp.prev;
                    itemNode.next = temp;
                    temp.prev = itemNode;
                } else {
                    front = itemNode;
                    itemNode.next = temp;
                    temp.prev = front;
                }
            }
            size++;
        }
    }

    @Override
    public T delete(int index) {
        checkBounds(index);
        T value;
        if (index == size -1) {
            value = back.data;
            remove();
        } else {
            if (index == 0) {
                value = front.data;
                front = front.next;
                front.prev = null;
            } else {
                Node<T> temp = getNode(index);
                value = temp.data;
                temp.next.prev = temp.prev;
                temp.prev.next = temp.next;
            }
            size--;
        }
        return value;
    }

    @Override
    public int indexOf(T item) {
        int index = -1;
        Node<T> temp = this.back;
        for (int i = this.size - 1; i > index; i--) {
            if ((item == null && temp.data == null) || temp.data.equals(item)) {
                index = i;
            }
            temp = temp.prev;
        }
        return index;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(T other) {
        if (front != null) {
            Node<T> temp = this.front;
            for (int i = 0; i < this.size; i++) {
                if ((other == null && temp.data == null) || (temp.data != null && temp.data.equals(other))) {
                    return true;
                }
                temp = temp.next;
            }
        }
        return false;
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }
    private void checkBounds(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
    }

    private boolean startFromZero(int index) {
        return index < this.size / 2;
    }

    private Node<T> getNode(int index) {


        Node<T> temp;
        if (startFromZero(index)) {
            temp = this.front;
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
        } else {
            temp = this.back;
            for (int i = this.size - 1; i > index; i--) {
                temp = temp.prev;
            }
        }

        return temp;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            T value = current.data;
            current = current.next;
            return value;
        }
    }
}
