package traffic.dsa;

public class MyQueue<T> {
    private Object[] data;
    private int front;
    private int rear;
    private int size;

    public MyQueue() {
        data = new Object[10];
        front = 0;
        rear = 0;
        size = 0;

    }
    int a=0;

    public void enqueue(T value) {
        if (size == data.length) {
            resize();
        }
        data[rear] = value;
        rear = (rear + 1) % data.length;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }
        T value = (T) data[front];
        data[front] = null;
        front = (front + 1) % data.length;
        size--;
        return value;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return (T) data[front];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void resize() {
        Object[] newData = new Object[data.length * 2];
        for (int i = 0; i < size; i++) {
            newData[i] = data[(front + i) % data.length];
        }
        data = newData;
        front = 0;
        rear = size;
    }
}
