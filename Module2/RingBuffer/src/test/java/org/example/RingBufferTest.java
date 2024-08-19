package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RingBufferTest {
    private final RingBuffer buffer = new RingBuffer(5);

    @AfterEach
    public void clearBuffer() {
        buffer.clear();
    }



    @Test
    public void simplePutTest() {
        int[] values = {1, 2, 3, 4, 5};

        buffer.putAll(values);

        assertEquals(buffer.getCounter(), values.length);   // полностью заполнен
        assertEquals(buffer.remove(), values[0]);           // Удаляется oldest элемент
    }

    @Test
    public void simpleRemoveTest() {
        int[] values = {1, 2, 3};

        buffer.putAll(values);

        for (int i = 0; i < values.length; i++) {
            assertEquals(buffer.remove(), values[i]);
        }
        assertThrows(RingBufferIsEmptyException.class, buffer::remove);     // Исключение, т.к. пустой
    }

    // Заполнить буфер большим числом значений, чем его размер и проверить функциональность
    @Test
    public void putTooMuchTest() {
        int[] values = {1, 2, 3, 4, 5, 6, 7};     // values.length > buffer.size

        buffer.putAll(values);

        assertEquals(buffer.getCounter(), buffer.getSize());        // counter == buffer.size
        assertEquals(buffer.remove(), values[2]);                   // Удаляется oldest элемент  [6 7 X 4 5]

        buffer.put(8);                                          // [6 7 8 (4) 5]   start = end = 3

        assertEquals(buffer.remove(), 4);
        assertEquals(buffer.remove(), 5);
        assertEquals(buffer.remove(), 6);
        assertEquals(buffer.remove(), 7);
        assertEquals(buffer.remove(), 8);
        assertThrows(RingBufferIsEmptyException.class, buffer::remove);
    }
}