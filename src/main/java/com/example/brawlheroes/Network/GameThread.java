package com.example.brawlheroes.Network;

public class GameThread extends Thread {
    private Connection connection1;
    private Connection connection2;
    private boolean isRunning;

    public GameThread(Connection connection1, Connection connection2) {
        this.connection1 = connection1;
        this.connection2 = connection2;
        isRunning = true;
    }

    @Override
    public void run() {
        while(isRunning) {

        }
    }
}
