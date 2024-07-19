package org.example;

import java.util.concurrent.atomic.AtomicInteger;

public class RingBuffer {
    private final int[] buffer;
    private final int size;
    private AtomicInteger start = new AtomicInteger(0);
    private AtomicInteger end = new AtomicInteger(0);
    private AtomicInteger counter = new AtomicInteger(0);

    public RingBuffer(int size) {
        if (size < 1)
            throw new IllegalArgumentException("Size can't be lower than 1");

        buffer = new int[size];
        this.size = size;
    }



    public synchronized void put(int val) {
        buffer[end.get()] = val;

        if (end.get() == start.get() && counter.get() > 0) {
            incStartPoint();
        }
        incEndPoint();
        incCounter();
    }

    // удаление oldest элемента и его возврат
    public synchronized int remove() {
        if (counter.get() == 0)
            throw new RingBufferIsEmptyException();

        int removed = buffer[start.get()];    // !!!
        incStartPoint();
        decCounter();
        return removed;
    }

    public void clear() {
        end.set(0);
        start.set(0);
        counter.set(0);
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
        return counter.get();
    }



    // Увеличить на единицу end и вернуть новое значение
    private int incEndPoint() {
        end.set((end.get() + 1) % buffer.length);
        return end.get();
    }

    // Увеличить на единицу start и вернуть новое значение
    private int incStartPoint() {
        start.set((start.get() + 1) % buffer.length);
        return start.get();
    }

    // Увеличить counter, вернуть новое значение
    private int incCounter() {
        if (counter.get() == size)    // Всегда не больше размера буффера
            return counter.get();
        return counter.incrementAndGet();
    }

    private int decCounter() {
        return counter.decrementAndGet();
    }
}