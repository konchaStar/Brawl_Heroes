package com.example.brawlheroes;

import com.example.brawlheroes.Network.Connection;
import com.example.brawlheroes.Network.Message;
import com.example.brawlheroes.Network.StartPosition;
import com.example.brawlheroes.engine.Engine;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MenuController {
    @FXML
    private Button exitButton;
    @FXML
    private Button play;
    @FXML
    private Button cancel;
    private Connection connection;
    public void onExitButton() {
        try {
            if(connection != null) {
                connection.close();
            }
        } catch (IOException e) {
        }
        App.getStage().close();
    }
    public void onPlay() {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("localhost", 1234), 1000);
            play.setVisible(false);
            cancel.setVisible(true);
            connection = new Connection(socket);
            new Thread(() -> {
                try {
                    Message message;
                    do {
                        message = connection.receive();
                        StartPosition position = (StartPosition) message.getData();
                        if(message.getType() == Message.MessageType.STARTED) {
                            Platform.runLater(() -> {
                                new Engine(connection, position).start();
                            });
                        }
                    } while (message.getType() != Message.MessageType.STARTED);
                } catch (IOException e) {

                } catch (ClassNotFoundException e) {

                }

            }).start();
        } catch (Exception e) {

        }
    }
    public void onCancel() {
        if(connection != null) {
            try {
                connection.close();
            } catch (IOException e) {

            }
        }
        cancel.setVisible(false);
        play.setVisible(true);
    }
}