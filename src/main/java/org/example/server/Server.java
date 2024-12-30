package org.example.server;

import org.example.stats.StatsUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Server {

    private final int threads;
    private final ExecutorService executorService;
    private final AtomicBoolean shouldContinue;
    private final int port;

    public Server(int port) {
        this.threads = Runtime.getRuntime().availableProcessors() - 1;
        this.executorService = Executors.newFixedThreadPool(threads);
        this.shouldContinue = new AtomicBoolean(true);
        this.port = port;
    }

    public void run() throws InterruptedException {
        try (ServerSocket serverSocket = runServer()) {
            Executors.newSingleThreadScheduledExecutor()
                    .scheduleAtFixedRate(StatsUtils::print, 1, 10, TimeUnit.SECONDS);
            for (int i = 0; i < threads; i++) {
                executorService.submit(acceptConnection(serverSocket, i));
            }
            Thread.currentThread().join();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        this.shouldContinue.compareAndSet(true, false);
    }

    private ServerSocket runServer() {
        try {
            return new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Runnable acceptConnection(ServerSocket serverSocket, int i) {
        return () -> {
            try {
                StatsUtils.initCache(i);
                System.out.println("Execution of " + i + " has started");
                while (shouldContinue.get()) {
                    Socket accept = serverSocket.accept();
                    InputStreamReader inputStreamReader = new InputStreamReader(accept.getInputStream(), StandardCharsets.UTF_8);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String data = bufferedReader.lines().collect(Collectors.joining());
                    StatsUtils.add(i, data);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

}