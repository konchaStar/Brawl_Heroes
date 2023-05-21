package com.example.brawlheroes.Network;


import java.io.IOException;

public class GameThread extends Thread {
    private Connection connection1;
    private Connection connection2;
    private boolean isRunning;

    public GameThread(Connection connection1, Connection connection2) {
        this.connection1 = connection1;
        this.connection2 = connection2;
        System.out.println("Thread started");
        isRunning = true;
    }
    private void handleConnection1() {
    }
    private void handleConnection2() {
    }
    @Override
    public void run() {
        try {
            connection2.send(new Message(null, Message.MessageType.STARTED));
            connection1.send(new Message(null, Message.MessageType.STARTED));
        } catch (Exception e) {
            isRunning = false;
        }
//        new Thread(() -> {
//            while(true) {
//                try {
//                    Message message = connection1.receive();
//                    System.out.println("Message from player 1 recieved");
//                    connection2.send(message);
//                } catch (Exception e) {
//                    isRunning = false;
//                }
//            }
//        }).start();
//        new Thread(() -> {
//            while(true) {
//                try {
//                    Message message = connection2.receive();
//                    System.out.println("Message from player 2 recieved");
//                    connection1.send(message);
//                } catch (Exception e) {
//                    isRunning = false;
//                }
//            }
//        }).start();
        while (isRunning) {
            try {
                Message message = connection1.receive();
                connection2.send(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
        System.out.println("Thread end");
    }
}
