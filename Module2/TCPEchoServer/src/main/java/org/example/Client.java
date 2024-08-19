package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.ClientException;

import java.io.*;
import java.net.Socket;

@Slf4j
public class Client implements Runnable {
    private final String name;
    private final int port;
    private final String host;

    private Socket clientSocket;
    private Thread thread;

    public Client(String host, int port, String name) {
        this.name = name;
        this.port = port;
        this.host = host;

        try {
            start();
        } catch (ClientException exc) {
            log.error("Client hasn't been started. msg={}", exc.getMessage());
            throw new ClientException(exc);
        }
    }

    // Запуск клиента
    private void start() throws ClientException {
        try {
            clientSocket = new Socket(host, port);

            thread = new Thread(this);
            thread.start();
        } catch (IOException exc) {
            log.error("Client error with socket in start() method");

            throw new ClientException(exc);
        }
    }

    // Отдельный метод для закрытия сокета чтобы не пришлось везде использовать try-catch блоки
    private void closeSocket() throws ClientException {
        try {
            clientSocket.close();
        } catch (IOException exc) {
            log.error("Client error during closing its socket");

            throw new ClientException(exc);
        }
    }

    @Override
    public void run() throws ClientException {
        try (
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String request = "Very interesting string from " + name + "!";

            writer.write(request);
            writer.newLine();
            writer.flush();

            String response = reader.readLine();

            log.info("Response from the server - {}. Client is processed <-------------------------------------------", response);
        } catch (IOException exc) {
            log.error("Something bad happened with client IO...");

            closeSocket();
            throw new ClientException(exc);
        }
    }
}