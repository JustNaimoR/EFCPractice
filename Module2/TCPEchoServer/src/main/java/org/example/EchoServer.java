package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.ServerException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

@Slf4j
public class EchoServer implements Runnable {
    private final int port;

    private Thread thread;
    private ServerSocket serverSocket;

    public EchoServer(int port) {
        this.port = port;

        start();
    }

    // Запуск сервера
    private void start() throws ServerException {
        try {
            serverSocket = new ServerSocket(port);

            log.info("Started the server at port {}", port);

            thread = new Thread(this);
            thread.start();
        } catch (IOException exc) {
            log.error("Server got error during Socket connection in start() method");
            throw new ServerException(exc);
        }
    }

    // Остановка работы сервера
    public void stop() throws ServerException {
        log.info("Server stopped");

        try {
            closeSocket();
            thread.interrupt();
            thread.join();
        } catch (InterruptedException exc) {
            log.error("Server error during its stopping");

            throw new ServerException(exc);
        }
    }

    // Отдельный метод для закрытия сокета чтобы не пришлось везде использовать try-catch блоки
    private void closeSocket() throws ServerException {
        try {
            serverSocket.close();
        } catch (IOException exc) {
            log.error("Server error during closing its socket");

            throw new ServerException(exc);
        }
    }

    @Override
    public void run() throws ServerException {
        int counter = 0;

        try {
            while (!thread.isInterrupted()) {
                Socket clientSocket = serverSocket.accept();

                String name = "client" + ++counter;

                log.info("Client {} has been connected", name);

                ClientsProcessingThread forTheClient = new ClientsProcessingThread(name, clientSocket);
            }
        } catch (SocketException ignored) {
            // Выпадает в случае если мы остановили работу сервера через socket.close()
        } catch (IOException exc) {
            log.error("Server received an error after trying to connect to accept the client connection");

            closeSocket();
            throw new ServerException(exc);
        }
    }
}