package com.example.brawlheroes.Network;


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
        while(isRunning) {
            try {
                Message message = connection1.receive();
                System.out.println("Message from player 1 recieved");
                switch (message.getType()) {
                    default -> connection2.send(message);
                }
            } catch (Exception e) {
                isRunning = false;
            }
        }
    }
    private void handleConnection2() {
        try {
            Message message = connection2.receive();
            System.out.println("Message from player 2 recieved");
            switch (message.getType()) {
                default -> connection1.send(message);
            }
        } catch (Exception e) {
            isRunning = false;
        }
    }
    @Override
    public void run() {
        try {
            connection2.send(new Message(null, Message.MessageType.STARTED));
            connection1.send(new Message(null, Message.MessageType.STARTED));
        } catch (Exception e) {
            isRunning = false;
        }
        new Thread(() -> {
            handleConnection1();
        }).start();
        new Thread(() -> {
            handleConnection2();
        }).start();
        while (isRunning);

    }
}
