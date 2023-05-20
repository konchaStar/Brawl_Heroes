package com.example.brawlheroes.Network;

public class Client extends Thread {
    private boolean isRunning;
    private Connection connection;
    public Client() {
        isRunning = true;
    }
    @Override
    public void run() {
        while(isRunning) {
            System.out.println("Try");
        }
    }
    public void setRunning(boolean running) {
        isRunning = running;
    }
}
