package org.example;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    private static volatile boolean evenTurn = true;

    public static void main( String[] args ) {
        Thread evenThread = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i += 2) {
                    System.out.println(i);

                    try {
                        notifyAll();
                        wait();
                    } catch (InterruptedException ex) {
                        log.error("exception when interrupting a thread - evenThread");
                        ex.printStackTrace();
                    }
                }
            }
        };

        Thread oddThread = new Thread() {
            @Override
            public void run() {
                for (int i = 1; i < 100; i += 2) {
                    System.out.println(i);

                    try {
                        notifyAll();
                        wait();
                    } catch (InterruptedException ex) {
                        log.error("exception when interrupting a thread - oddThread");
                        ex.printStackTrace();
                    }
                }
            }
        };

        evenThread.start();
        log.info("even working...");
        oddThread.start();
        log.info("odd working...");

        try {
            evenThread.join();
            oddThread.join();
            log.info("work is over");
        } catch (InterruptedException ex) {
            log.error("exception when interrupting a thread - main");
            ex.printStackTrace();
        }
    }

}