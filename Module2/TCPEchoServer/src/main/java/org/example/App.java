package org.example;

public class App {
    public static void main( String[] args ) throws InterruptedException {
        String ip = "127.0.0.1";
        int port = 8000;

        EchoServer server = new EchoServer(port);

        for (int i = 0; i < 10; i++) {
            Client client = new Client(ip, port, "Romario" + i);
        }

        Thread.sleep(2_000);    // 2 sec
        server.stop();
    }
}
