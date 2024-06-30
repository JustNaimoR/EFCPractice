package org.example;

public class RingBuffer {
    private final int[] buffer;     // volatile ???
    private final int size;
    private int start = 0;          // volatile ???
    private int end = 0;            // volatile ???
    private int counter = 0;        // volatile ???

    public RingBuffer(int size) {
        if (size < 1)
            throw new IllegalArgumentException("Size can't be lower than 1");

        buffer = new int[size];
        this.size = size;
    }



    public synchronized void put(int val) {
        buffer[end] = val;      // !!!

        if (end == start && counter > 0) {
            incStartPoint();
        }
        incEndPoint();
        incCounter();
    }

    // удаление oldest элемента и его возврат
    public synchronized int remove() {
        if (counter == 0)
            throw new RingBufferIsEmptyException();

        int removed = buffer[start];    // !!!
        incStartPoint();
        decCounter();
        return removed;
    }

    public void clear() {
        end = start = counter = 0;
    }

    public void putAll(int[] arr) {
        for (int val: arr) {
            put(val);
        }
    }



    public int getSize() {
        return size;
    }

    public int getCounter() {
        return counter;
    }



    // Увеличить на единицу end и вернуть новое значение
    private int incEndPoint() {
        return end = (end + 1) % buffer.length;
    }

    // Увеличить на единицу start и вернуть новое значение
    private int incStartPoint() {
        return start = (start + 1) % buffer.length;
    }

    // Увеличить counter, вернуть новое значение
    private int incCounter() {
        if (counter == size)    // Всегда не больше размера буффера
            return counter;
        return ++counter;
    }

    private int decCounter() {
        return --counter;
    }
}