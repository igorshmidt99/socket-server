package org.example;

import org.example.server.Server;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server(8887);
        server.run();
    }

}