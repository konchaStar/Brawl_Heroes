package com.example.brawlheroes.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private ServerSocket serverSocket;
    private Queue<Connection> connections;

    public static void main(String[] args) {
        new Server().start();
    }
    private Server() {
        connections = new ArrayDeque<>();
    }
    private void start() {
        Scanner scanner = new Scanner(System.in);
        boolean isIncorrect = false;
        do {
            try {
                System.out.print("Enter port: ");
                int port = scanner.nextInt();
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                System.err.println("Can't start server on this port");
                isIncorrect = true;
            }
        } while(isIncorrect);
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                connections.add(new Connection(socket));
                if(connections.size() > 1) {
                    Thread gameThread = new GameThread(connections.poll(), connections.poll());
                    gameThread.start();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
