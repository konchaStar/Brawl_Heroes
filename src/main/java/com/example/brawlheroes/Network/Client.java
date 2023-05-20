package com.example.brawlheroes.Network;

import com.example.brawlheroes.engine.Engine;
import com.example.brawlheroes.engine.Hero;
import javafx.application.Platform;

import java.net.Socket;

public class Client extends Thread {
    private boolean isRunning;
    private Connection connection;
    public Client() {
        isRunning = true;
    }
    @Override
    public void run() {
        while(isRunning) {
            boolean isNotConnected = true;
            while(isNotConnected) {
                try {
                    Socket socket = new Socket("localhost", 1234);
                    connection = new Connection(socket);
                    isNotConnected = false;
                } catch (Exception e) {
                    isNotConnected = true;
                }
            }
            Engine engine = new Engine(this);
            Platform.runLater(() -> {
                engine.start();
            });
            try {
                Message message = connection.receive();
                switch (message.getType()) {
                    case MOVE -> {
                        engine.getWorld().setEnemy((Hero) message.getData());
                    }
                }
            } catch (Exception e) {
                isRunning = false;
            }
        }
    }
    public void sendMove(Object data) {
        if(connection.isConnected()) {
            try {
                connection.send(new Message(data, Message.MessageType.MOVE));
            } catch (Exception e) {

            }
        } else {
            isRunning = false;
        }
    }
    public void setRunning(boolean running) {
        isRunning = running;
    }
}
