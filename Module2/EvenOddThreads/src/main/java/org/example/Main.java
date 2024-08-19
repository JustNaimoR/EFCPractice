package org.example;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class Main {
    static ReentrantLock locker = new ReentrantLock();
    static Condition condition = locker.newCondition();

    public static void main( String[] args ) {
        Thread evenThread = new Thread(new EvenThread());
        Thread oddThread = new Thread(new OddThread());

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
        } finally {
            locker.unlock();
        }

        log.info("work is over");
    }

    private static class EvenThread implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 100; i += 2) {
                try {
                    locker.lock();

                    System.out.println(i);

                    condition.signal();
                    condition.await();
                } catch (InterruptedException ignored) {
                    log.error("Even Thread interrupted");
                    return;
                } finally {
                    condition.signal();
                    locker.unlock();
                }
            }

            log.info("Even thread is done.");
        }
    }

    private static class OddThread implements Runnable {
        @Override
        public void run() {
            for (int i = 1; i < 100; i += 2) {
                try {
                    locker.lock();

                    System.out.println(i);

                    condition.signal();
                    condition.await();
                } catch (InterruptedException ignored) {
                    log.error("Odd Thread interrupted");
                    return;
                } finally {
                    locker.unlock();
                }
            }

            log.info("Odd thread is done.");
        }
    }
}