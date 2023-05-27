package com.example.brawlheroes.Network;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

public class Server {
    private ServerSocket serverSocket;
    private Queue<Connection> connections;

    public static void main(String[] args) {
        new Server().start();
    }
    private Server() {
        connections = new LinkedBlockingDeque<>();
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
        try {
            System.out.println("Server started at " + Inet4Address.getLocalHost().getHostAddress() + ":" + serverSocket.getLocalPort());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                connections.add(new Connection(socket));
                new Thread(() -> {
                    Connection connection = connections.peek();
                    try {
                        Message message = connection.receive();
                        connections.remove(connection);
                    } catch (Exception ignore) {}
                }).start();
                while (connections.size() > 1) {
                    Connection connection1 = connections.poll();
                    Connection connection2 = connections.poll();
                    boolean connected = true;
                    try {
                        connection1.send(new Message(null, Message.MessageType.TEST));
                    } catch (IOException e) {
                        connected = false;
                        connection1.close();
                    }
                    try {
                        connection2.send(new Message(null, Message.MessageType.TEST));
                    } catch (IOException e) {
                        connected = false;
                        connection2.close();
                    }
                    if(!connected) {
                        if(connection1.isConnected()) {
                            connections.add(connection1);
                        }
                        if(connection2.isConnected()) {
                            connections.add(connection2);
                        }
                    } else {
                        new GameThread(connection1, connection2).start();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
