package com.example.brawlheroes.Network;


import com.example.brawlheroes.Consts;
import javafx.geometry.Point2D;

import java.io.IOException;
import java.util.Random;

public class GameThread extends Thread {
    private Connection connection1;
    private Connection connection2;
    private boolean isRunning;
    private final StartPosition firstSpawn = new StartPosition(60, 60);
    private final StartPosition secondSpawn = new StartPosition(1160, 820);

    public GameThread(Connection connection1, Connection connection2) {
        this.connection1 = connection1;
        this.connection2 = connection2;
        System.out.println("Thread started");
        isRunning = true;
    }
    @Override
    public void run() {
        try {
                connection2.send(new Message(firstSpawn, Message.MessageType.STARTED));
                connection1.send(new Message(secondSpawn, Message.MessageType.STARTED));
        } catch (Exception e) {
            isRunning = false;
        }
        while (isRunning) {
            try {
                int kills1 = 0;
                int kills2 = 0;
                Message message1 = connection1.receive();
                Message message2 = connection2.receive();
                if(message1.getType() == Message.MessageType.DEATH) {
                    kills2++;
                }
                if(message2.getType() == Message.MessageType.DEATH) {
                    kills1++;
                }
                if(kills1 == Consts.KILLS) {
                    connection1.send(new Message(null, Message.MessageType.VICTORY));
                    connection2.send(new Message(null, Message.MessageType.DEFEAT));
                    isRunning = false;
                } else if (kills2 == Consts.KILLS) {
                    connection2.send(new Message(null, Message.MessageType.VICTORY));
                    connection1.send(new Message(null, Message.MessageType.DEFEAT));
                    isRunning = false;
                } else {
                    connection2.send(message1);
                    connection1.send(message2);
                }
            } catch (IOException e) {
                isRunning = false;
            } catch (ClassNotFoundException e) {
                isRunning = false;
            }
        }
        try {
            connection2.close();
            connection1.close();
        } catch (IOException e) {

        }
        System.out.println("Thread end");
    }
}
