package org.example;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    private static final Object lock = new Object();

    public static void main( String[] args ) {
        Thread evenThread = new Thread(new EvenThread());
        Thread oddThread = new Thread(new OddThread());

        try {
            evenThread.start();
            synchronized (lock) {   //todo неужели для использования notify() wait() все время приходится их оборачивать в синхр-блоки?
                lock.wait();    // чтобы вначале стартовал именно evenThread
            }
            oddThread.start();

            evenThread.join();
            oddThread.join();
        } catch (InterruptedException ignored) {
            log.error("main() interrupted");

            evenThread.interrupt();
            oddThread.interrupt();
        }

        log.info("work is over");
    }

    private static class EvenThread implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 100; i += 2) {
                System.out.println(i);

                try {
                    synchronized (lock) {
                        lock.notify();

                        if (i < 98)             // последняя итерация цикла
                            lock.wait();
                    }
                } catch (InterruptedException ignored) {
                    log.error("Thread interrupted - " + this.getClass().getName());
                    return;
                }
            }
        }
    }

    private static class OddThread implements Runnable {
        @Override
        public void run() {
            for (int i = 1; i < 100; i += 2) {
                System.out.println(i);

                try {
                    synchronized (lock) {
                        lock.notify();

                        if (i < 99)        // последняя итерация цикла
                            lock.wait();
                    }
                } catch (InterruptedException ignored) {
                    log.error("Thread interrupted - " + this.getClass().getName());
                    return;
                }
            }
        }
    }
}