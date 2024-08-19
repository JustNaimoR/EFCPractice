package org.example;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.IntStream;

public class EvenOddThreadsTest {
    EvenOddThreads evenOddThreads = new EvenOddThreads();

    @Test
    void evenNumberOfN_50Attempts() {
        int N = 100;
        int attempts = 50;
        Queue<Integer> expected = new LinkedList<>(IntStream.range(0, N).boxed().toList());

        for (int i = 0; i < attempts; i++) {
            Queue<Integer> result = evenOddThreads.doEvenOdd(N);

            Assertions.assertEquals(expected, result);
        }
    }

    @Test
    void oddNumberOfN_50Attempts() {
        int N = 101;
        int attempts = 50;
        Queue<Integer> expected = new LinkedList<>(IntStream.range(0, N).boxed().toList());

        for (int i = 0; i < attempts; i++) {
            Queue<Integer> result = evenOddThreads.doEvenOdd(N);

            Assertions.assertEquals(expected, result);
        }
    }
}
