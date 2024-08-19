package org.example;


import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.ServerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

@Slf4j
public class ServerTests {
    final String host = "127.0.0.1";
    final int port = 8000;

    @Test
    void simpleIntegrationTest() throws InterruptedException {      // По планам тут тестируется просто работа всего вместе :)
        try {
            EchoServer echoServer = new EchoServer(port);

            for (int i = 0; i < 10; i++) {
                Client client = new Client(host, port, "Romario" + i);
            }

            Thread.sleep(2_000);    // 2 sec
            echoServer.stop();
        } catch (Exception exc) {
            log.error("The test was not completed correctly due to an error - {}", exc.getMessage());

            Assertions.fail();
        }
    }

    @Test
    void requestToServer_1Client() {
        EchoServer echoServer = null;

        try {
            echoServer = new EchoServer(port);
        } catch (ServerException exc) {
            log.error("The test was not completed correctly due to a server start error - {}", exc.getMessage());

            Assertions.fail();
        }

        String request = "Hello world!!!";

        try (
                Socket clientSocket = new Socket(host, port);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            writer.write(request);
            writer.newLine();
            writer.flush();

            String response = reader.readLine();

            Assertions.assertEquals(request, response);
        } catch (IOException exc) {
            log.error("The test was not completed correctly due to an error - {}", exc.getMessage());

            Assertions.fail();
        }

        try {
            echoServer.stop();
        } catch (ServerException exc) {
            log.error("The test was not completed correctly due to a server close error - {}", exc.getMessage());

            Assertions.fail();
        }
    }
}
