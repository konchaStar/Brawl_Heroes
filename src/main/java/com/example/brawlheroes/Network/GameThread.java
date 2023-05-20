package com.example.brawlheroes.Network;

import java.io.IOException;

public class GameThread extends Thread {
    private Connection connection1;
    private Connection connection2;
    private boolean isRunning;

    public GameThread(Connection connection1, Connection connection2) {
        this.connection1 = connection1;
        this.connection2 = connection2;
        isRunning = true;
    }
    private void handleConnection1() {
        while(isRunning) {
            try {
                Message message = connection1.receive();
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
            switch (message.getType()) {
                default -> connection1.send(message);
            }
        } catch (Exception e) {
            isRunning = false;
        }
    }
    @Override
    public void run() {
        new Thread(() -> {
            handleConnection1();
        }).start();
        new Thread(() -> {
            handleConnection2();
        }).start();
        while (isRunning);

    }
}
