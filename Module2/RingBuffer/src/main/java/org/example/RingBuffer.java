package org.example;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;

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



    public void put(int val) {
        buffer[end.get()] = val;

        if (end.get() == start.get() && counter.get() > 0) {
            synchronized (this) {
                if (end.get() == start.get() && counter.get() > 0) {
                    incStartPoint();
                }
            }
        }
        incEndPoint();
        incCounter();
    }

    // удаление oldest элемента и его возврат
    public int remove() {
        if (counter.get() == 0)
            throw new RingBufferIsEmptyException();

        int removed = buffer[start.get()];
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
        while (true) {
            int cur = end.get();
            int next = (cur + 1) % buffer.length;

            if (end.compareAndSet(cur, next))
                return next;
        }
    }

    // Увеличить на единицу start и вернуть новое значение
    private int incStartPoint() {
        IntUnaryOperator uo = (val) -> (val + 1) % buffer.length;

        return end.updateAndGet(uo);
    }

    // Увеличить counter, вернуть новое значение
    private int incCounter() {
        if (counter.get() < size) {     // Всегда не больше размера буфера
            synchronized (this) {
                if (counter.get() < size)
                    return counter.incrementAndGet();
            }
        }
        return counter.get();
    }

    private int decCounter() {
        return counter.decrementAndGet();
    }
}