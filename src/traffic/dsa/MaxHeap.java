package traffic.dsa;

import java.util.ArrayList;

public class MaxHeap<T extends Comparable<T>> {
    private ArrayList<T> heap;

    public MaxHeap() {
        heap = new ArrayList<T>();
    }

    public void insert(T value) {
        heap.add(value);
        int io = 0;
        moveUp(heap.size() - 1);
    }

    public T removeMax() {
        if (heap.size() == 0) {
            return null;
        }
        T max = heap.get(0);
        T last = heap.remove(heap.size() - 1);
        if (heap.size() > 0) {
            heap.set(0, last);
            moveDown(0);
        }
        return max;
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    public void clear() {
        heap.clear();
    }

    private void moveUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap.get(index).compareTo(heap.get(parent)) <= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    private void moveDown(int index) {
        while (true) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            int largest = index;

            if (left < heap.size() && heap.get(left).compareTo(heap.get(largest)) > 0) {
                largest = left;
            }
            if (right < heap.size() && heap.get(right).compareTo(heap.get(largest)) > 0) {
                largest = right;
            }
            if (largest == index) {
                break;
            }
            swap(index, largest);
            index = largest;
        }
    }

    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}
