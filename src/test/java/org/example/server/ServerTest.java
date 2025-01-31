package org.example.server;

import org.example.stats.StatsUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerTest {

    private static Server server;

    @BeforeAll
    static void setUp() {
        int port = 8887;
        server = new Server(port);
        CompletableFuture.runAsync(() -> {
            try {
                server.run();
            } catch (InterruptedException ignored) {
            }
        });
    }

    @AfterAll
    static void tearDown() {
        server.stop();
    }

    @Test
    void run() throws InterruptedException {
        Client client = new Client(4);
        client.sendMessages(1);
        Thread.sleep(1000);
        assertEquals(4, StatsUtils.getTotalSize());
        StatsUtils.clear();
    }

    @Test
    void checkSizeWithParameters() {
        Client client = new Client(8);
        client.sendMessages(800);
        while (StatsUtils.getTotalSize() != 6400) {
            Thread.onSpinWait();
        }
        assertTrue(true);
        StatsUtils.clear();
    }

}