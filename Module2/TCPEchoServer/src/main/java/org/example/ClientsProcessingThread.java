package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.ClientProcessingException;

import java.io.*;
import java.net.Socket;

@Slf4j
public class ClientsProcessingThread implements Runnable {
    private final String name;
    private final Socket clientSocket;

    public ClientsProcessingThread(String name, Socket clientSocket) {
        this.name = name;
        this.clientSocket = clientSocket;

        startThread();
    }

    private void startThread() {
        log.info("Client {} start processing", name);

        Thread thread = new Thread(this);
        thread.start();
    }

    private void closeSocket() throws ClientProcessingException {
        try {
            clientSocket.close();
        } catch (IOException exc) {
            log.error("Failed to close client socket - {}", name);

            throw new ClientProcessingException(exc);
        }
    }

    @Override
    public void run() throws ClientProcessingException {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
        ) {
            String request = reader.readLine();

            log.info("Request from client - '{}'", request);

            writer.write(request);  // echo
            writer.newLine();
            writer.flush();

            log.info("Request from client {} has been processed!", name);
        } catch (IOException exc) {
            log.error(ClientProcessingException.DEFAULT_MESSAGE);

            throw new ClientProcessingException(exc);
        } finally {
            closeSocket();
        }
    }
}