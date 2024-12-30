package org.example.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    private final int port;
    private final ExecutorService executorService;
    private final int threadsAmount;

    public Client(int threadsAmount) {
        if (threadsAmount > Runtime.getRuntime().availableProcessors()) {
            throw new RuntimeException("Context switching");
        }
        this.threadsAmount = threadsAmount;
        executorService = Executors.newFixedThreadPool(threadsAmount);
        port = 8887;
    }

    public void sendMessages(int messagesPerThread) {
        for (int i = 0; i < threadsAmount; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < messagesPerThread; j++) {
                    try (Socket client = new Socket(InetAddress.getLocalHost(), port)) {
                        String message = "Trying to write to a socket: " + j;
                        System.out.println(message);
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8);
                        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                        bufferedWriter.write(message);
                        bufferedWriter.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        executorService.shutdown();
    }
}