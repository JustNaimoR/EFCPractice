package org.example;

import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class EvenOddThreads {
    private final ReentrantLock locker = new ReentrantLock();
    private final Condition condition = locker.newCondition();
    private final AtomicInteger integer = new AtomicInteger();
    private final ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {
        Queue<Integer> queue = new EvenOddThreads().doEvenOdd(5);

        System.out.println(queue);
    }

    public Queue<Integer> doEvenOdd(int N) {
        Thread evenThread = new Thread(new EvenOddThread(true));
        Thread oddThread = new Thread(new EvenOddThread(false));

        integer.set(N);
        queue.clear();

        try {
            locker.lock();
            evenThread.start();
            condition.await();
            oddThread.start();
            locker.unlock();


            evenThread.join();
            oddThread.join();
        } catch (InterruptedException ignored) {
            log.error("main() interrupted");

            evenThread.interrupt();
            oddThread.interrupt();
            locker.unlock();
        }

        log.info("work is over");

        return queue;
    }

    private class EvenOddThread implements Runnable {
        private final boolean even;   // чет/нечет поток

        public EvenOddThread(boolean even) {
            this.even = even;
        }

        @Override
        public void run() {
            for (int i = (even? 0: 1); i < integer.get(); i += 2) {
                try {
                    log.info("{} - перед locker.lock(), i = {}", getName(), i);
                    locker.lock();
                    log.info("{} - после locker.lock(), i = {}", getName(), i);

//                    queue.add(i);
                    System.out.println(i);

                    condition.signal();
                    log.info("{} - после condition.signal(), i = {}", getName(), i);
                    if (i + 1 < integer.get()) {  // Не последняя итерация
                        log.info("{} - перед condition.await(), i = {}", getName(), i);
                        condition.await();
                        log.info("{} - после condition.await(), i = {}", getName(), i);
                    }
                } catch (InterruptedException ignored) {
                    log.error("{} interrupted", getName());
                    break;
                }
            }
            locker.unlock();

            log.info("{} is done.", getName());
        }

        private String getName() {
            return even? "Even thread": "Odd thread";
        }
    }
}